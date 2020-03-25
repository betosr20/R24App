package Models.POJOS;

public class NaturalDisaster {
    private String Id;
    private String name;
    private String menaceId;

    public NaturalDisaster() {}

    public NaturalDisaster(String Id, String name, String menaceId) {
        this.Id = Id;
        this.name = name;
        this.menaceId = menaceId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenaceId() {
        return menaceId;
    }

    public void setMenaceId(String menaceId) {
        this.menaceId = menaceId;
    }
}
