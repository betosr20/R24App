package Models.POJOS;

public class DistressCall {

    private String id, name, lastName, locationPlace, latitude, longitude;

    public DistressCall(String id, String name, String lastName, String locationPlace, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.locationPlace = locationPlace;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocationPlace() {
        return locationPlace;
    }

    public void setLocationPlace(String locationPlace) {
        this.locationPlace = locationPlace;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
