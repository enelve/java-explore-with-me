package ru.practicum.ewm.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.mapper.UserMapper;
import ru.practicum.ewm.main.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User postUser(UserDto userDto) {
        return userRepository.save(UserMapper.toUser(userDto));
    }

    public List<User> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (ids != null) {
            return userRepository.findAllByIdIn(ids, pageable);
        }
        return userRepository.findAll(pageable).getContent();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete,
                        () -> {
                            throw new NotFoundException(String.format("User with id=%d was not found", id));
                        });
    }
}
