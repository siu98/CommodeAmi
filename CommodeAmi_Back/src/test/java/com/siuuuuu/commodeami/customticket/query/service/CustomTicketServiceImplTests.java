package com.siuuuuu.commodeami.customticket.query.service;

import com.siuuuuu.commodeami.customticket.query.aggregate.CustomTicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CustomTicketServiceImplTests {

    @Autowired
    private CustomTicketService customTicketService;

    @DisplayName("모든 커스텀티켓 조회")
    @Test
    void findAllCustomTickets() {
        // given

        // when
        List<CustomTicketDTO> customTicketDTOList = customTicketService.getAllCustomTickets();

        // then
        assertNotNull(customTicketDTOList, "커스텀티켓 목록이 null이 아닙니다.");
        assertFalse(customTicketDTOList.isEmpty(), "커스텀티켓 목록이 비어있지 않아야 합니다.");
        customTicketDTOList.forEach(customTicketDTO -> {
            log.info("customTicketDTO: {}", customTicketDTO);
        });
    }

    @DisplayName("특정 유저의 커스텀 티켓 조회")
    @Test
    void findCustomTicketByUserId() {
        // given
        Long userId = 2L;
        // when
        List<CustomTicketDTO> customTicketDTOList = customTicketService.getCustomTicketByUserId(userId);

        // then
        assertNotNull(customTicketDTOList, "커스텀티켓 목록이 null이 아닙니다.");
        assertFalse(customTicketDTOList.isEmpty(), "커스텀티켓 목록이 비어있지 않아야 합니다.");
        customTicketDTOList.forEach(customTicketDTO -> {
            log.info("customTicketDTO: {}", customTicketDTO);
        });
    }
}