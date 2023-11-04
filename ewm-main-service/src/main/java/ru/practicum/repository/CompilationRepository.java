package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE from Compilation c where c.id = ?1")
    int deleteByIdAndReturnCount(Integer compId);
}
