package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.model.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByCategoryId(Long categoryId);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findAllByIdIn(Set<Long> events);

    Optional<Event> findByIdAndStateIs(Long eventId, EventState published);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Page<Event> findAll(Specification<Event> specification, Pageable pageable);
}
