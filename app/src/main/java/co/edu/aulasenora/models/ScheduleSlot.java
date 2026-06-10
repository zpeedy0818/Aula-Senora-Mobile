package co.edu.aulasenora.models;

public class ScheduleSlot {
    private int id;
    private int aulaId;
    private String volunteerEmail;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String type;
    private String topic;
    private String targetStudentEmail;
    private String targetStudentName;
    private String createdAt;

    public ScheduleSlot(int id, int aulaId, String volunteerEmail, String slotDate, String startTime,
                        String endTime, String type, String topic, String targetStudentEmail,
                        String targetStudentName, String createdAt) {
        this.id = id;
        this.aulaId = aulaId;
        this.volunteerEmail = volunteerEmail;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.topic = topic;
        this.targetStudentEmail = targetStudentEmail;
        this.targetStudentName = targetStudentName;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getAulaId() { return aulaId; }
    public String getVolunteerEmail() { return volunteerEmail; }
    public String getSlotDate() { return slotDate; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getType() { return type; }
    public String getTopic() { return topic; }
    public String getTargetStudentEmail() { return targetStudentEmail; }
    public String getTargetStudentName() { return targetStudentName; }
    public String getCreatedAt() { return createdAt; }
}
