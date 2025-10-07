package app.model.enums;

public enum NotificationStatus {

    SUCCEEDED,
    FAILED,
    PENDING;

    public static NotificationStatus fromString(String status) {
        return NotificationStatus.valueOf(status.toUpperCase());
    }
}
