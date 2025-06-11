package vn.hcmute.appfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    int code; //Status code
    String message; //Message
    T result; //Dữ liệu trả về .

    //Success with data
    public static <T> ApiResponse<T> success(String message, T result) {
        return new ApiResponse<>(200, message, result); // Mã trạng thái 200 cho thành công
    }

    //Success but no data
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<T>(200, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T result) {
        return new ApiResponse<>(400, message, result); // Mã trạng thái 500 cho lỗi
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null); // Mã trạng thái 404 cho không tìm thấy
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
