package com.gdscplantry.plantry.domain.Pantry.domain.data;

import com.gdscplantry.plantry.domain.Pantry.vo.RawFoodDataVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, Long> {
    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.vo.RawFoodDataVo(f.id, g.emoji, u.value, u.isDay) " +
            "from FoodData f " +
            "left join FoodGroup g on f.group = g " +
            "left join UseByDateData u on f = u.foodData " +
            "where f.name like %:query% " +
            "order by case when f.name = :query then 0 " +
            "when u != null then 1 " +
            "when f.name like %:query then 2 " +
            "when f.name like :query% then 3 " +
            "when f.name like %:query% then 4 " +
            "else 5 " +
            "end, length(f.name) asc")
    ArrayList<RawFoodDataVo> findAllByNameContainsQueryWithJPQL(String query);
}
