package com.gdscplantry.plantry.domain.Pantry.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PantryRepository extends JpaRepository<Pantry, Long> {
    Boolean existsAllByCode(String code);

    Optional<Pantry> findByCode(String code);
}
