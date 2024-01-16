package com.gdscplantry.plantry.domain.Pantry.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPantryRepository extends JpaRepository<UserPantry, Long> {
}
