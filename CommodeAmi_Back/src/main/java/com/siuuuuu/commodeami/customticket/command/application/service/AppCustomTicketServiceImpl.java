package com.siuuuuu.commodeami.customticket.command.application.service;

import com.siuuuuu.commodeami.customticket.command.aggregate.dto.CustomTicketDTO;
import com.siuuuuu.commodeami.customticket.command.aggregate.entity.CustomTicket;
import com.siuuuuu.commodeami.customticket.command.domain.repository.CustomTicketRepository;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import com.siuuuuu.commodeami.user.command.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AppCustomTicketServiceImpl implements AppCustomTicketService {

    private final CustomTicketRepository customTicketRepository;
    private final UserRepository userRepository;

    public AppCustomTicketServiceImpl(CustomTicketRepository customTicketRepository,
                                      UserRepository userRepository) {
        this.customTicketRepository = customTicketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CustomTicketDTO createCustomTicket(CustomTicketDTO customTicketDTO, Long userId) {

        // 로그인된 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 티켓 엔티티 생성 및 사용자 정보 설정
        CustomTicket ticket = new CustomTicket();
        ticket.setTicketImage(customTicketDTO.getTicketImage());
        ticket.setHologramColor1(customTicketDTO.getHologramColor1());
        ticket.setHologramColor2(customTicketDTO.getHologramColor2());
        ticket.setComment(customTicketDTO.getComment());
        ticket.setUser(user); // 사용자 정보 연동

        // 티켓 저장
        CustomTicket savedTicket = customTicketRepository.save(ticket);

        // 엔티티 -> DTO 변환
        return new CustomTicketDTO(
                savedTicket.getCustomTicketId(),
                savedTicket.getTicketImage(),
                savedTicket.getHologramColor1(),
                savedTicket.getHologramColor2(),
                savedTicket.getComment(),
                user.getUserId()
        );
    }

    @Override
    public CustomTicketDTO updateCustomTicket(Long customTicketId, CustomTicketDTO customTicketDTO, Long userId) {

        // 엔티티 조회
        CustomTicket ticket = customTicketRepository.findById(customTicketId)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없습니다."));

        // 로그인된 사용자가 티켓의 소유자인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!ticket.getUser().equals(user)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 엔티티 업데이트
        ticket.setTicketImage(customTicketDTO.getTicketImage());
        ticket.setHologramColor1(customTicketDTO.getHologramColor1());
        ticket.setHologramColor2(customTicketDTO.getHologramColor2());
        ticket.setComment(customTicketDTO.getComment());

        // 저장
        CustomTicket updatedTicket = customTicketRepository.save(ticket);

        // 엔티티 -> DTO 변환
        return new CustomTicketDTO(
                updatedTicket.getCustomTicketId(),
                updatedTicket.getTicketImage(),
                updatedTicket.getHologramColor1(),
                updatedTicket.getHologramColor2(),
                updatedTicket.getComment(),
                updatedTicket.getUser().getUserId()
        );
    }

    @Override
    public void deleteCustomTicket(Long customTicketId, Long userId) {

        // 엔티티 조회
        CustomTicket ticket = customTicketRepository.findById(customTicketId)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없습니다."));

        // 로그인된 사용자가 티켓의 소유자인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!ticket.getUser().equals(user)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 티켓 삭제
        customTicketRepository.delete(ticket);

    }
}
