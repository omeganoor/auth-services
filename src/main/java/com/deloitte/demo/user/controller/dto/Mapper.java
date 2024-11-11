package com.deloitte.demo.user.controller.dto;

import com.deloitte.demo.user.domain.model.UserData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mapper {

    public static UserDto toUserDTO (UserData data) {
        log.info("toParticipantDTO({})", data);
        return UserDto.builder()
                .id(data.getId())
                .username(data.getUsername())
                .email(data.getEmail())
                .password(data.getPassword())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build();
    }

}