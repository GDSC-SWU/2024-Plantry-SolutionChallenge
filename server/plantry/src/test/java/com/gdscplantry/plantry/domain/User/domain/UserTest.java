package com.gdscplantry.plantry.domain.User.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Auditing test")
    void saveUser() throws Exception {
        // given
        String email = "email@test.com";
        User user = User.builder()
                .email(email)
                .build();

        // when
        Long id = userRepository.save(user).getId();

        // then
        User findUser = userRepository.findById(id).orElseThrow(() -> new Exception("Null"));
        assertThat(findUser.getEmail()).as("User 정보가 제대로 저장되지 않음.").isEqualTo(email);
        assertThat(findUser.getCreatedAt()).as("Auditing 기능이 제대로 작동하지 않음.").isNotNull();

        log.info(findUser.getEmail());
        log.info(String.valueOf(findUser.getCreatedAt()));
    }
}