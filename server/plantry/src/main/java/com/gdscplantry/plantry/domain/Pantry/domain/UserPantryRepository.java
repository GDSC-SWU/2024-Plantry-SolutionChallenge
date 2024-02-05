package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.Pantry.dto.pantry.PantryListItemDto;
import com.gdscplantry.plantry.domain.Pantry.vo.PantryWithCodeVo;
import com.gdscplantry.plantry.domain.Pantry.vo.UserPantryWithCodeVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserPantryRepository extends JpaRepository<UserPantry, Long> {
    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.pantry.PantryListItemDto(up.pantryId, up.title, up.color, up.isMarked) " +
            "from UserPantry up " +
            "where up.user = :user " +
            "order by up.isMarked desc, up.title asc")
    ArrayList<PantryListItemDto> findAllByUserWithJPQL(User user);

    Optional<UserPantry> findByPantryIdAndUser(Long pantryId, User user);

    Boolean existsByPantryId(Long pantryId);

    Boolean existsByPantryIdAndUser(Long pantryId, User user);

    ArrayList<UserPantry> findAllByUser(User user);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.vo.UserPantryWithCodeVo(u.user, u.pantryId, p.code) " +
            "from UserPantry u join Pantry p on u.pantryId = p.id " +
            "where u.user = :user and u.pantryId = :pantryId")
    Optional<UserPantryWithCodeVo> findByUserAndPantryIdWithJPQL(User user, Long pantryId);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.vo.PantryWithCodeVo(u.isOwner, p) " +
            "from UserPantry u join Pantry p on u.pantryId = p.id " +
            "where u.user = :user and u.pantryId = :pantryId")
    Optional<PantryWithCodeVo> findPantryByUserAndPantryIdWithJPQL(User user, Long pantryId);

    Optional<UserPantry> findByPantryIdAndIsOwner(Long pantryId, Boolean isOwner);
}
