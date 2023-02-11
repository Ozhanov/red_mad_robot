package kz.test.tz.dto;

import kz.test.tz.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BulletinBoardDTO {
    private String name;
    private long minPrice;
    private String description;
    private byte[] image;
    private Board.Status status;
    private LocalDateTime closedDateTime;
    private Long clientId;
    private UserDto user;

    public static BulletinBoardDTO from(Board bulletinBoard) {
        return BulletinBoardDTO.builder()
                .name(bulletinBoard.getName())
                .minPrice(bulletinBoard.getMinPrice())
                .description(bulletinBoard.getDescription())
                //  .image(bulletinBoard.getImage())
                .status(bulletinBoard.getStatus())
                .closedDateTime(bulletinBoard.getClosedDateTime())
                .clientId(bulletinBoard.getClientId())
                .user(UserDto.from(bulletinBoard.getUser()))
                .build();
    }
}


