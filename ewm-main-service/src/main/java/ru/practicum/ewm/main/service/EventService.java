package ru.practicum.ewm.main.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.EventFullDto;
import ru.practicum.ewm.main.dto.EventShortDto;
import ru.practicum.ewm.main.dto.EventUpdateDto;
import ru.practicum.ewm.main.dto.NewEventDto;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.exception.IncorrectRequestException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.UserRequestException;
import ru.practicum.ewm.main.mapper.EventMapper;
import ru.practicum.ewm.main.model.EventSort;
import ru.practicum.ewm.main.model.EventState;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.RequestRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.ewm.stats.client.StatisticClient;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.ewm.main.model.EventState.PUBLISHED;
import static ru.practicum.ewm.main.model.RequestStatus.CONFIRMED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticClient statisticClient;
    private final RequestRepository requestRepository;
    @Value("${spring.application.name}")
    private String applicationName;

    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
       if ((rangeStart != null && rangeEnd != null) && (rangeStart.isAfter(rangeEnd) || rangeStart.isEqual(rangeEnd))) {
            throw new IncorrectRequestException("Start time must not after or equal to end time.");
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.unsorted());

        if (users != null || states != null || categories != null || rangeStart != null || rangeEnd != null) {

            Specification<Event> specification = Specification.where(null);
            if (categories != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        root.get("category").get("id").in(categories));
            }
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDateTime = Objects.requireNonNullElseGet(rangeStart, () -> now);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));
            if (rangeEnd != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
            }
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("state"), PUBLISHED));

            List<Event> eventsPage = eventRepository.findAll(specification, pageable).getContent();

            return eventsPage.stream()
                    .map(eventMapper::eventToEventFullDto)
                    .toList();
        } else {
            return eventRepository.findAll(pageable).stream()
                    .map(eventMapper::eventToEventFullDto)
                    .toList();
        }
    }

    public EventFullDto updateAdminEvent(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling updateAdminEvent data: with object {}", eventUpdateDto);
            throw new NotFoundException("Event with id = " + eventId + " is not found.");
        });
        updateEvent(event, eventUpdateDto);

        if (eventUpdateDto.getStateAction() != null) {
            switch (eventUpdateDto.getStateAction()) {
                case REJECT_EVENT -> {
                    if (event.getState().equals(PUBLISHED)) {
                        throw new UserRequestException("Event with id = " + eventId + " is published and can't be cancelled.");
                    }
                    event.setState(EventState.CANCELED);
                }
                case PUBLISH_EVENT -> {
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new UserRequestException("Event with id = " + eventId + " is not pending and can't be published.");
                    }
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            }
        }

        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    @Transactional
    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                                            EventSort sort, HttpServletRequest request) {

        if ((rangeStart != null && rangeEnd != null) && (rangeStart.isAfter(rangeEnd) || rangeStart.isEqual(rangeEnd))) {
            log.error("Calling getAll data: with text {}, categories {}, paid {}", text, categories, paid);
            throw new IncorrectRequestException("Start time must not after or equal to end time.");
        }

        Pageable pageable = sort.equals(EventSort.VIEWS)
                ? PageRequest.of(from / size, size, Sort.by("views"))
                : PageRequest.of(from / size, size, Sort.by("eventDate"));

        Specification<Event> specification = Specification.where(null);
        if (text != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                    ));
        }
        if (categories != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        if (paid != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paid"), paid));
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = Objects.requireNonNullElseGet(rangeStart, () -> now);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));
        if (rangeEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
        }
        if (onlyAvailable != null && onlyAvailable) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), PUBLISHED));

        List<Event> eventsPage = eventRepository.findAll(specification, pageable).getContent();

        updateViews(eventsPage, request);

        return eventsPage.stream()
                .filter(event -> event.getPublishedOn() != null)
                .map(eventMapper::eventToShortDto)
                .toList();
    }

    @Transactional
    public EventFullDto getById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("Event must be published.");
        }
        StatDto[] statsDto = statisticClient.getStatistic(event.getCreatedOn(), LocalDateTime.now(),
                List.of(request.getRequestURI()), true).getBody();

        EventFullDto result;
        event.setConfirmedRequests(requestRepository.countByEventIdAndStatus(eventId, CONFIRMED));
        if (statsDto != null && statsDto.length != 0) {
            event.setViews(statsDto[0].getHits() + 1);
        } else {
            event.setViews(1L);
        }
        result = eventMapper.eventToEventFullDto(event);
        HitDto hitDto = new HitDto();
        String ip = request.getRemoteAddr();
        hitDto.setIp(ip);
        hitDto.setUri(request.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        hitDto.setApp(applicationName);
        statisticClient.save(hitDto);
        return result;
    }

    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        return eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(eventMapper::eventToShortDto)
                .toList();
    }

    public EventFullDto addUserEvent(Long userId, NewEventDto eventDto) {
        Event event = eventMapper.newEventDtoToEvent(eventDto);

        updateEvent(event, userId, eventDto);

        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Calling getUserEventById data: with id {}", userId);
            throw new NotFoundException("Event with id = " + eventId + " and user id = " + userId + " is not found.");
        });
        event.setViews(event.getViews() + 1);

        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    public EventFullDto updateUserEventById(Long userId, Long eventId, EventUpdateDto eventDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Calling updateUserEventById data: with id {}, object {}", userId, eventDto);
            throw new NotFoundException("Event with id = " + eventId + " and user id = " + userId + " is not found.");
        });

        if (event.getState().equals(PUBLISHED)) {
            log.error("Calling updateUserEventById data: with id {}, object {}", userId, eventDto);
            throw new UserRequestException("Event must not be published.");
        }
        updateEvent(event, eventDto);

        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
            }
        }

        event = eventRepository.save(event);

        return eventMapper.eventToEventFullDto(event);
    }

    private void updateViews(List<Event> events, HttpServletRequest request) {
        HitDto hitDto = new HitDto();
        String ip = request.getRemoteAddr();
        hitDto.setIp(ip);
        hitDto.setUri(request.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        hitDto.setApp(applicationName);

        statisticClient.save(hitDto);

        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);

    }

    private void updateEvent(Event event, Long userId, NewEventDto eventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id = " + userId + " doesn't exist.");
        });

        event.setInitiator(initiator);

        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        }

        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        event.setCreatedOn(LocalDateTime.now());

        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> {
            throw new NotFoundException("Category with id = " + eventDto.getCategory() + " doesn't exist.");
        });
        event.setCategory(category);

        event.setState(EventState.PENDING);
    }

    private void updateEvent(Event event, EventUpdateDto eventUpdateDto) {
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }

        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }

        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }

        if (eventUpdateDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventUpdateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id = " + eventUpdateDto.getCategory() + " is not found"));
            event.setCategory(category);
        }

        if (eventUpdateDto.getLocation() != null) {
            event.setLocation(eventUpdateDto.getLocation());
        }

        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }

        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }

        if (eventUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDto.getRequestModeration());
        }

        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
    }
}
