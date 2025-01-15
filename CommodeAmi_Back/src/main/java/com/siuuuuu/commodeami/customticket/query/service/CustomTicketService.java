package com.siuuuuu.commodeami.customticket.query.service;

import com.siuuuuu.commodeami.customticket.query.aggregate.CustomTicketDTO;

import java.util.List;

public interface CustomTicketService {
    List<CustomTicketDTO> getAllCustomTickets();

    List<CustomTicketDTO> getCustomTicketByUserId(Long userId);
}
