package co.edu.aulasenora.models;

public class AdmittedStudent {
    private String email;
    private String name;
    private String admissionDate;

    public AdmittedStudent(String email, String name, String admissionDate) {
        this.email = email;
        this.name = name;
        this.admissionDate = admissionDate;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getAdmissionDate() { return admissionDate; }
}
