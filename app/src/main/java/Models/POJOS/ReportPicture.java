package Models.POJOS;

import android.net.Uri;

public class ReportPicture {
    private String reportId;
    private Uri imageUri;

    public ReportPicture(String reportId, Uri imageUri) {
        this.imageUri = imageUri;
        this.reportId = reportId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
