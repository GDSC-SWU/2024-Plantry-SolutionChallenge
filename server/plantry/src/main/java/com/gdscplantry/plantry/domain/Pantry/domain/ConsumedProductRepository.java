package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ConsumedProductRepository extends JpaRepository<ConsumedProduct, Long> {
    ArrayList<ConsumedProduct> findAllByUser(User user);

    void deleteAllByUser(User user);
}
