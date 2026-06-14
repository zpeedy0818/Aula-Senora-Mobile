package co.edu.aulasenora.models;

public class NotificationItem implements Comparable<NotificationItem> {
    private String type;
    private String title;
    private String description;
    private String timestamp;
    private int aulaId;
    private int requestId;
    private String studentEmail;
    private int unreadCount;

    public NotificationItem(String type, String title, String description, String timestamp,
                            int aulaId, int requestId, String studentEmail, int unreadCount) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.aulaId = aulaId;
        this.requestId = requestId;
        this.studentEmail = studentEmail;
        this.unreadCount = unreadCount;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTimestamp() { return timestamp; }
    public int getAulaId() { return aulaId; }
    public int getRequestId() { return requestId; }
    public String getStudentEmail() { return studentEmail; }
    public int getUnreadCount() { return unreadCount; }

    @Override
    public int compareTo(NotificationItem other) {
        if (timestamp == null && other.timestamp == null) return 0;
        if (timestamp == null) return 1;
        if (other.timestamp == null) return -1;
        return other.timestamp.compareTo(timestamp);
    }
}
