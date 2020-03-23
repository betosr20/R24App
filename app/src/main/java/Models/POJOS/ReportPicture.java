package Models.POJOS;

public class ReportPicture {
    private String Id;
    private String reportId;
    private String imageName;

    public ReportPicture(String imageName, String reportId, String Id) {
        this.imageName = imageName;
        this.reportId = reportId;
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
