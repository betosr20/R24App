package Models.POJOS;

import java.net.URI;

public class Image{
    private int image;

    public Image() {
    }

    public Image(int fotoUrl) {
        this.image = fotoUrl;
    }

    public int getFotoUrl() {
        return image;
    }

    public void setFotoUrl(int fotoUrl) {
        this.image = fotoUrl;
    }
}
