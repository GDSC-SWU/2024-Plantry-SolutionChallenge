package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.Pantry.dto.PantryListItemDto;
import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserPantryRepository extends JpaRepository<UserPantry, Long> {
    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.PantryListItemDto(up.pantryId, up.title, up.color, up.isMarked) " +
            "from UserPantry up " +
            "where up.user = :user " +
            "order by up.isMarked desc, up.title asc")
    ArrayList<PantryListItemDto> findAllByUserWithJPQL(User user);

    Optional<UserPantry> findByPantryId(Long pantryId);

    Boolean existsByPantryId(Long pantryId);
}
