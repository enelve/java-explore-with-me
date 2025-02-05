package ru.practicum.ewm.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.CompilationDTO;
import ru.practicum.ewm.main.dto.NewCompilationDTO;
import ru.practicum.ewm.main.dto.UpdateCompilationRequest;
import ru.practicum.ewm.main.entity.Compilation;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.mapper.CompilationMapper;
import ru.practicum.ewm.main.repository.CompilationRepository;
import ru.practicum.ewm.main.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Transactional
    public CompilationDTO add(NewCompilationDTO compilationDto) {
        Compilation compilation = compilationMapper.newCompilationDtoToCompilation(compilationDto);

        if (compilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
            compilation.setEvents(events);
        }

        compilation = compilationRepository.save(compilation);

        return compilationMapper.compilationToCompilationDto(compilation);
    }

    @Transactional
    public CompilationDTO update(Long compId, UpdateCompilationRequest compRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Calling update data: with object {}", compRequest);
            throw new NotFoundException("Compilation with id = " + compId + " doesn't exist.");
        });

        updateComp(compilation, compRequest);

        compilation = compilationRepository.save(compilation);

        return compilationMapper.compilationToCompilationDto(compilation);
    }

    public CompilationDTO get(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Calling get data: with id {}", compId);
            throw new NotFoundException("Compilation with id = " + compId + " doesn't exist.");
        });

        return compilationMapper.compilationToCompilationDto(compilation);
    }

    public List<CompilationDTO> getAll(Boolean pinned, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        if (pinned != null) {
            List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
            return compilations.stream().map(compilationMapper::compilationToCompilationDto).toList();
        } else {
            Page<Compilation> compilations = compilationRepository.findAll(pageable);
            return compilations.stream().map(compilationMapper::compilationToCompilationDto).toList();
        }
    }

    @Transactional
    public void delete(Long compId) {
        try {
            compilationRepository.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Compilation with id = " + compId + " doesn't exist.");
        }
    }

    private void updateComp(Compilation compilation, UpdateCompilationRequest compRequest) {
        if (compRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compRequest.getEvents());
            compilation.setEvents(events);
        }

        if (compRequest.getTitle() != null) {
            compilation.setTitle(compRequest.getTitle());
        }

        if (compRequest.getPinned() != null) {
            compilation.setPinned(compRequest.getPinned());
        }
    }
}
