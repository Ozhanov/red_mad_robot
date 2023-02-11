package kz.test.tz.pogo;

import lombok.Data;

@Data
public class NewBoardRequest {
    private String name;
    private Long minPrice;
    private String description;
    private String image;
}