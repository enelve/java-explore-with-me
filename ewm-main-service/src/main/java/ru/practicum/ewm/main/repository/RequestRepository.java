package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.entity.Request;
import ru.practicum.ewm.main.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByIdInAndAndEventId(Iterable<Long> ids, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    long countByEventIdAndStatus(Long eventId, RequestStatus status);
}
