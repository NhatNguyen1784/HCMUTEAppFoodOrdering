package vn.hcmute.appfoodorder.model.dto;

public class ApiResponse<T> {
    int code;
    String message;
    T result;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiResponse(int code, String message, T result) {
        this.code = code;
        this.result = result;
        this.message = message;
    }
}
