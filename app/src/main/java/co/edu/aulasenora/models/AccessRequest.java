package co.edu.aulasenora.models;

public class AccessRequest {
    private int id;
    private int aulaId;
    private String aulaName;
    private String studentEmail;
    private String studentName;
    private String status;
    private String createdAt;

    public AccessRequest(int id, int aulaId, String aulaName, String studentEmail, String studentName, String status, String createdAt) {
        this.id = id;
        this.aulaId = aulaId;
        this.aulaName = aulaName;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getAulaId() { return aulaId; }
    public String getAulaName() { return aulaName; }
    public String getStudentEmail() { return studentEmail; }
    public String getStudentName() { return studentName; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
