package ru.practicum.ewm.main.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.ewm.main.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.main.dto.RequestDto;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.entity.Request;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.UserRequestException;
import ru.practicum.ewm.main.mapper.RequestMapper;
import ru.practicum.ewm.main.model.EventState;
import ru.practicum.ewm.main.model.EventStatus;
import ru.practicum.ewm.main.model.RequestStatus;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.RequestRepository;
import ru.practicum.ewm.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Transactional
    public RequestDto addRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            log.error("Calling addRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Participation request with userId = " + userId
                    + " eventId = " + eventId + " already exists.");
        }

        User requester = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Calling addRequest data: with id {}, event id {}", userId, eventId);
            throw new NotFoundException("User with id = " + userId + " was not found.");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling addRequest data: with id {}, event id {}", userId, eventId);
            throw new NotFoundException("Event with id = " + eventId + " doesn't exist.");
        });

        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Calling addRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Users are not allowed to register for unpublished events.");
        }

        if (Objects.equals(userId, event.getInitiator().getId())) {
            log.error("Calling addRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Event organizers are not allowed to request participation in their own events.");
        }

        if ((event.getParticipantLimit() != 0L) && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            log.error("Calling addRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Participant limit reached.");
        }
        Request requestToSave = new Request()
                .setRequester(requester)
                .setEvent(event)
                .setStatus(!event.getRequestModeration() || event.getParticipantLimit() == 0L ?
                        RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .setCreated(LocalDateTime.now());

        Request request = requestRepository.save(requestToSave);

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return requestMapper.requestToDto(request);
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() -> {
            log.error("Calling cancelRequest data: with id {}, request id {}", userId, requestId);
            throw new NotFoundException("Participation request with id = " + requestId + " doesn't exist.");
        });

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            log.error("Calling cancelRequest data: with id {}, request id {}", userId, requestId);
            throw new UserRequestException("Participation request with id = " + requestId + " is already confirmed.");
        }

        request.setStatus(RequestStatus.CANCELED);

        Long eventId = request.getEvent().getId();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling cancelRequest data: with id {}, request id {}", userId, requestId);
            throw new NotFoundException("Event with id = " + eventId + " doesn't exist.");
        });
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);

        request = requestRepository.save(request);
        return requestMapper.requestToDto(request);
    }

    public List<RequestDto> getUserRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);

        if (!requests.isEmpty()) {
            return requests.stream()
                    .map(requestMapper::requestToDto)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {
      List<Request> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);

        if (!requests.isEmpty()) {
            return requests.stream()
                    .map(requestMapper::requestToDto)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public EventRequestStatusUpdateResultDto updateEventRequests(Long userId, Long eventId,
                                                                 @Valid EventRequestStatusUpdateRequestDto requestsUpdate) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new NotFoundException("Event with id = " + eventId + " and user id = " + userId + " doesn't exist.");
        });

        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Access denied. User with id = " + userId + " is not an event initiator.");
        }

        List<Request> requests = requestRepository.findAllByIdInAndAndEventId(requestsUpdate.getRequestIds(), eventId);

        if (requests.size() != requestsUpdate.getRequestIds().size()) {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new NotFoundException("Incorrect request id(s) received in the request body.");
        }

        for (Request request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
                throw new UserRequestException("Only requests with status 'Pending' can be accepted or rejected.");
            }
        }

        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();

        if (requestsUpdate.getStatus() == EventStatus.REJECTED) {
            requests.forEach(request -> {
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                rejectedRequests.add(requestMapper.requestToDto(request));
            });
            return new EventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return new EventRequestStatusUpdateResultDto(
                    requests.stream().map(requestMapper::requestToDto).toList(),
                    new ArrayList<>()
            );
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Failed to accept request. Reached max participant limit for event id = " + eventId + ".");
        }

        requests.forEach(Request -> {
            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                Request.setStatus(RequestStatus.CONFIRMED);
                requestRepository.save(Request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmedRequests.add(requestMapper.requestToDto(Request));
            } else {
                Request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(Request);
                rejectedRequests.add(requestMapper.requestToDto(Request));
            }
        });

        if (!confirmedRequests.isEmpty()) {
            eventRepository.save(event);
        }
      return new EventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
    }

    @Transactional
    public RequestDto addParticipationRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Participation request with userId = " + userId
                    + " eventId = " + eventId + " already exists.");
        }

        User requester = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new NotFoundException("User with id = " + userId + " was not found.");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new NotFoundException("Event with id = " + eventId + " doesn't exist.");
        });

        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Users are not allowed to register for unpublished events.");
        }

        if (Objects.equals(userId, event.getInitiator().getId())) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Event organizers are not allowed to request participation in their own events.");
        }

        if ((event.getParticipantLimit() != 0L) && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new UserRequestException("Participant limit reached.");
        }

        Request requestToSave = new Request()
                .setRequester(requester)
                .setEvent(event)
                .setStatus(!event.getRequestModeration() || event.getParticipantLimit() == 0L ?
                        RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .setCreated(LocalDateTime.now());

        Request request = requestRepository.save(requestToSave);

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return requestMapper.requestToDto(request);
    }

    @Transactional
    public RequestDto cancelParticipationRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() -> {
            log.error("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);
            throw new NotFoundException("Participation request with id = " + requestId + " doesn't exist.");
        });

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            log.error("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);
            throw new UserRequestException("Participation request with id = " + requestId + " is already confirmed.");
        }

        request.setStatus(RequestStatus.CANCELED);

        Long eventId = request.getEvent().getId();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);
            throw new NotFoundException("Event with id = " + eventId + " doesn't exist.");
        });
       event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);

        request = requestRepository.save(request);
        return requestMapper.requestToDto(request);
    }
}

