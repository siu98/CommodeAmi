package com.siuuuuu.commodeami.customticket.query.repository;

import com.siuuuuu.commodeami.customticket.query.aggregate.CustomTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomTicketMapper {

    List<CustomTicket> selectAllCustomTickets();

    List<CustomTicket> selectCustomTicketsByUserId(@Param("userId") Long userId);
}
