package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long orderDetailId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt; // luc test o postman thi khong can truyen
    private String userEmail;
}
