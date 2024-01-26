package com.gdscplantry.plantry.domain.Pantry.domain.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, Long> {
    
}
