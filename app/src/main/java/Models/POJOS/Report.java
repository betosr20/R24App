package Models.POJOS;

import java.util.Date;

public class Report {
    private String Id, type, detail, latitude, longitude, place, ownerId;
    private boolean pathDisabled, isActive;
    private Date startDate, endDate;
    private int affectedPeople, affectedAnimals;

    public Report() {
    }

    public Report(String Id, String type, String detail, String latitude, String longitude, String place,
                  boolean pathDisabled, boolean isActive, Date startDate, Date endDate, int affectedAnimals,
                  int affectedPeople, String ownerId) {
        this.Id = Id;
        this.place = place;
        this.type = type;
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pathDisabled = pathDisabled;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.affectedAnimals = affectedAnimals;
        this.affectedPeople = affectedPeople;
        this.ownerId = ownerId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isPathDisabled() {
        return pathDisabled;
    }

    public void setPathDisabled(boolean pathDisabled) {
        this.pathDisabled = pathDisabled;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAffectedPeople() {
        return affectedPeople;
    }

    public void setAffectedPeople(int affectedPeople) {
        this.affectedPeople = affectedPeople;
    }

    public int getAffectedAnimals() {
        return affectedAnimals;
    }

    public void setAffectedAnimals(int affectedAnimals) {
        this.affectedAnimals = affectedAnimals;
    }
}
