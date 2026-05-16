package co.edu.aulasenora.models;

public class Aula {
    private int id;
    private String name;
    private String description;
    private String subject;
    private String volunteerEmail;
    private String createdAt;

    public Aula(int id, String name, String description, String subject, String volunteerEmail, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.volunteerEmail = volunteerEmail;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSubject() { return subject; }
    public String getVolunteerEmail() { return volunteerEmail; }
    public String getCreatedAt() { return createdAt; }
}
