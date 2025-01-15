package com.siuuuuu.commodeami.customticket.query.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import lombok.Data;

@Data
public class CustomTicketDTO {

    @JsonProperty("custom_ticket_id")
    Long customTicketId;

    @JsonProperty("ticket_image")
    String ticketImage;

    @JsonProperty("hologram_color1")
    String hologramColor1;

    @JsonProperty("hologram_color2")
    String hologramColor2;

    @JsonProperty("comment")
    String comment;

    Long userId;
}
