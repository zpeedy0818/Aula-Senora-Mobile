package co.edu.aulasenora.models;

public class ChatMessage {
    private int id;
    private int aulaId;
    private String senderEmail;
    private String message;
    private String createdAt;

    public ChatMessage(int id, int aulaId, String senderEmail, String message, String createdAt) {
        this.id = id;
        this.aulaId = aulaId;
        this.senderEmail = senderEmail;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getAulaId() { return aulaId; }
    public String getSenderEmail() { return senderEmail; }
    public String getMessage() { return message; }
    public String getCreatedAt() { return createdAt; }
}
