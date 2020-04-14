package Models.POJOS;

public class Image {
    private int image;

    public Image() {
    }

    public Image(int photoUrl) {
        this.image = photoUrl;
    }

    public int getPhotoUrl() {
        return image;
    }

    public void setPhotoUrl(int photoUrl) {
        this.image = photoUrl;
    }
}
