package com.gdscplantry.plantry.domain.Notification.controller;

import com.gdscplantry.plantry.domain.Notification.dto.CheckNotificationResDto;
import com.gdscplantry.plantry.domain.Notification.dto.NotificationListResDto;
import com.gdscplantry.plantry.domain.Notification.dto.ProductNotificationListResDto;
import com.gdscplantry.plantry.domain.Notification.dto.UpdateProductNotificationReqDto;
import com.gdscplantry.plantry.domain.Notification.service.NotificationService;
import com.gdscplantry.plantry.domain.Notification.vo.MessageVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import com.gdscplantry.plantry.global.util.FcmUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/notif")
public class NotificationController {
    private final NotificationService notificationService;
    private final FcmUtil fcmUtil;

    @GetMapping("/test")
    public ResponseEntity<ResponseDto> fcmTest(@RequestParam(value = "token") String deviceToken) {
        fcmUtil.sendMessage(new MessageVo(0L, deviceToken, -1));

        return ResponseEntity.ok(ResponseDto.of(200));
    }

    @GetMapping("/product")
    public ResponseEntity<ResponseDto> readProductNotificationList(@RequestParam(value = "product") Long productId, @RequestAttribute("user") User user) {
        ProductNotificationListResDto productNotificationListResDto = notificationService.readProductNotificationList(user, productId);

        return ResponseEntity.ok(DataResponseDto.of(productNotificationListResDto, 200));
    }

    @PatchMapping("/product")
    public ResponseEntity<ResponseDto> updateProductNotificationList(@RequestParam(value = "product") Long productId, @RequestBody @Valid UpdateProductNotificationReqDto dto,
                                                                     @RequestAttribute("user") User user) {
        ProductNotificationListResDto productNotificationListResDto = notificationService.updateProductNotification(user, productId, dto);

        return ResponseEntity.status(201).body(DataResponseDto.of(productNotificationListResDto, 201));
    }

    @GetMapping
    public ResponseEntity<ResponseDto> readNotificationList(@RequestAttribute("user") User user) {
        NotificationListResDto notificationListResDto = notificationService.readNotificationList(user);

        return ResponseEntity.ok(DataResponseDto.of(notificationListResDto, 200));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto> checkNotification(@RequestParam(value = "id") Long notificationId, @RequestAttribute("user") User user) {
        CheckNotificationResDto checkNotificationResDto = notificationService.checkNotification(user, notificationId);

        return ResponseEntity.ok(DataResponseDto.of(checkNotificationResDto, 200));
    }
}
