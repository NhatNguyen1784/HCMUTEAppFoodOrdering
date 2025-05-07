package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private String userEmail;
}
