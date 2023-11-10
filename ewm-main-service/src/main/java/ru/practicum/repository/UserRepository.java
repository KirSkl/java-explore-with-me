package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> getUsersByIdIn(Iterable<Integer> ids);

    Page<User> findAllUser(Pageable page);

    @Transactional
    @Modifying
    @Query(value = "DELETE from User u where u.id = ?1")
    int deleteByIdAndReturnCount(Long userId);
}
