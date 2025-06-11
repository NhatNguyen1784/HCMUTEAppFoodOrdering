package vn.hcmute.appfood.utils;

public enum OrderStatus {
    PENDING,       // Đang chờ xử lý
    SHIPPING,      // Đang giao
    DELIVERED,     // Đã giao hàng
    SUCCESSFUL,    // Đơn hàng đã hoàn thành 2 phía xác nhận
    CANCELLED     // Đã hủy
}
