package com.gdscplantry.plantry.domain.MyPage.service;

import com.gdscplantry.plantry.domain.MyPage.domain.TermsRepository;
import com.gdscplantry.plantry.domain.MyPage.dto.NotificationTimeResDto;
import com.gdscplantry.plantry.domain.MyPage.dto.TermsResDto;
import com.gdscplantry.plantry.domain.MyPage.dto.UpdateNicknameResDto;
import com.gdscplantry.plantry.domain.MyPage.dto.UserProfileResDto;
import com.gdscplantry.plantry.domain.MyPage.error.MyPageErrorCode;
import com.gdscplantry.plantry.domain.MyPage.vo.TermsItemVo;
import com.gdscplantry.plantry.domain.Notification.service.RelatedNotificationService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final RelatedNotificationService relatedNotificationService;

    public UserProfileResDto getUserProfile(User user) {
        return new UserProfileResDto(user.getEmail(), user.getNickname(), user.getProfileImagePath());
    }

    @Transactional
    public UpdateNicknameResDto updateNickname(User user, String nickname) {
        // Check if nickname exists
        if (userRepository.existsByNickname(nickname))
            throw new AppException(MyPageErrorCode.NICKNAME_ALREADY_EXISTS);

        // Update data
        user.updateNickname(nickname);

        return new UpdateNicknameResDto(nickname);
    }

    @Transactional
    public NotificationTimeResDto updateNotificationTime(User user, Integer time) {
        // Update data
        user.updateNotificationTime(time);

        // Update notification data
        relatedNotificationService.updateNotificationTime(user, time);

        return new NotificationTimeResDto(time);
    }

    @Transactional(readOnly = true)
    public TermsResDto getTerms(String type) {
        // Validate type value
        if (!type.equals("all") && !type.equals("use") && !type.equals("privacy"))
            throw new AppException(MyPageErrorCode.INVALID_TERM_TYPE);

        ArrayList<TermsItemVo> result = new ArrayList<>();

        // Find Terms of Use if type is all or use
        if (type.equals("all") || type.equals("use"))
            result.add(termsRepository.findByTitleOrderByCreatedAtDescUpdatedAtDescWithJPQL("Terms of Use"));

        // Find Privacy Policy if type is all or privacy
        if (type.equals("all") || type.equals("privacy"))
            result.add(termsRepository.findByTitleOrderByCreatedAtDescUpdatedAtDescWithJPQL("Privacy Policy"));

        return new TermsResDto(type, result);
    }
}
