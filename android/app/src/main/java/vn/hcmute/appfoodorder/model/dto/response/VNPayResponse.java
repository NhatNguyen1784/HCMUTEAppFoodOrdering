package vn.hcmute.appfoodorder.model.dto.response;

public class VNPayResponse {
    private String code;
    private String message;
    private String paymentUrl;

    // Getter và Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPaymentUrl() { return paymentUrl; }
    public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }
}