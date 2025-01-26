package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
