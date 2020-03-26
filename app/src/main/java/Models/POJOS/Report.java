package Models.POJOS;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Report implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

//         type.setText("Tipo de Evento: " + "hi there");
//         place.setText("Lugar del  Evento: " + fakeReport.getPlace());
//         affectedPeople.setText("Cantidad de personas afectadas: " + fakeReport.getAffectedPeople());
//         affectedAnimals.setText("Cantidad de Animales afectadas: " + fakeReport.getAffectedAnimals());
//         details.setText("Detalle de Evento: " + fakeReport.getDetail());
//         startDateTextView.setText("Fecha de Inicio: " + dateFormat.format(fakeReport.getStartDate()));
//         endDateTextView.setText("Fecha de Fin: " + dateFormat.format(fakeReport.getEndDate()));

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.Id);
        dest.writeString(this.type);
        dest.writeString(this.detail);
//        dest.writeString(this.latitude);
//        dest.writeString(this.longitude);
        dest.writeString(this.place);
//        dest.writeByte((byte) (this.pathDisabled ? 1 : 0));
//        dest.writeByte((byte) (this.isActive ? 1 : 0));
        dest.writeLong(this.startDate.getTime());
        dest.writeLong(this.endDate.getTime());
        dest.writeInt(this.affectedAnimals);
        dest.writeInt(this.affectedPeople);
    }

    protected Report(Parcel in) {
//        Id = in.readString();
        type = in.readString();
        detail = in.readString();
//        latitude = in.readString();
//        longitude = in.readString();
        place = in.readString();
//        pathDisabled = in.readByte() != 0;
//        isActive = in.readByte() != 0;
        affectedPeople = in.readInt();
        affectedAnimals = in.readInt();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
