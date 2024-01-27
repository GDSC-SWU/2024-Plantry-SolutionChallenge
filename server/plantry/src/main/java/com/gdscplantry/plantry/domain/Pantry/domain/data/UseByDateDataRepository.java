package com.gdscplantry.plantry.domain.Pantry.domain.data;

import com.gdscplantry.plantry.domain.Pantry.vo.RawFoodDataVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UseByDateDataRepository extends JpaRepository<UseByDateData, Long> {
    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.vo.RawFoodDataVo(u.value, u.isDay) " +
            "from UseByDateData u " +
            "where u.description like %:query% " +
            "order by case when u.description = :query then 0 " +
            "when u.description like %:query then 1 " +
            "when u.description like :query% then 2 " +
            "when u.description like %:query% then 3 " +
            "else 4 " +
            "end, length(u.description) asc")
    ArrayList<RawFoodDataVo> findAllByNameContainsQueryWithJPQL(String query);
}
