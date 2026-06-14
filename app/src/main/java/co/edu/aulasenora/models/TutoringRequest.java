package co.edu.aulasenora.models;

public class TutoringRequest {
    private int id;
    private int aulaId;
    private String studentEmail;
    private String studentName;
    private String topic;
    private String description;
    private String preferredDate;
    private String preferredTime;
    private String preferredEndTime;
    private String status;
    private String createdAt;

    public TutoringRequest(int id, int aulaId, String studentEmail, String studentName, String topic,
                           String description, String preferredDate, String preferredTime,
                           String preferredEndTime, String status, String createdAt) {
        this.id = id;
        this.aulaId = aulaId;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.topic = topic;
        this.description = description;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.preferredEndTime = preferredEndTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getAulaId() { return aulaId; }
    public String getStudentEmail() { return studentEmail; }
    public String getStudentName() { return studentName; }
    public String getTopic() { return topic; }
    public String getDescription() { return description; }
    public String getPreferredDate() { return preferredDate; }
    public String getPreferredTime() { return preferredTime; }
    public String getPreferredEndTime() { return preferredEndTime; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
