package com.gdscplantry.plantry.domain.MyPage.vo;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TermsItemVo {
    private final String createdAt;
    private final String updatedAt;
    private final String title;
    private final String content;

    public TermsItemVo(LocalDateTime createdAt, LocalDateTime updatedAt, String title, String content) {
        this.createdAt = createdAt.format(DateTimeFormatter.ISO_DATE_TIME);
        this.updatedAt = updatedAt == null ? null : updatedAt.format(DateTimeFormatter.ISO_DATE_TIME);
        this.title = title;
        this.content = content;
    }
}
