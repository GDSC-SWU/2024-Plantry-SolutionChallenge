package com.gdscplantry.plantry.domain.Pantry.domain.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UseByDateDataRepository extends JpaRepository<UseByDateData, Long> {
}
