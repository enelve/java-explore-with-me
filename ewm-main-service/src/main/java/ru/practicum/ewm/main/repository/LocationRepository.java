package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
