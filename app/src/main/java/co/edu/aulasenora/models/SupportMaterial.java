package co.edu.aulasenora.models;

public class SupportMaterial {
    private int id;
    private int aulaId;
    private String volunteerEmail;
    private String customName;
    private String fileName;
    private String filePath;
    private String mimeType;
    private long fileSize;
    private String createdAt;
    private int viewCount;

    public SupportMaterial(int id, int aulaId, String volunteerEmail, String customName,
                           String fileName, String filePath, String mimeType,
                           long fileSize, String createdAt) {
        this(id, aulaId, volunteerEmail, customName, fileName, filePath, mimeType, fileSize, createdAt, 0);
    }

    public SupportMaterial(int id, int aulaId, String volunteerEmail, String customName,
                           String fileName, String filePath, String mimeType,
                           long fileSize, String createdAt, int viewCount) {
        this.id = id;
        this.aulaId = aulaId;
        this.volunteerEmail = volunteerEmail;
        this.customName = customName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }

    public int getId() { return id; }
    public int getAulaId() { return aulaId; }
    public String getVolunteerEmail() { return volunteerEmail; }
    public String getCustomName() { return customName; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getMimeType() { return mimeType; }
    public long getFileSize() { return fileSize; }
    public String getCreatedAt() { return createdAt; }
    public int getViewCount() { return viewCount; }
}
