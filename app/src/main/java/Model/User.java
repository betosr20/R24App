package Model;

public class User {

    String iD, name, lastName, userName, email, cellPhone, address, profileImage;
    Boolean healthStatus, alerts, notifications;

    public User(){}

    public User(String iD, String name, String lastName, String userName, String email, String cellPhone,
                String address, String profileImage, Boolean healthStatus, Boolean alerts, Boolean notifications) {
        this.iD = iD;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.cellPhone = cellPhone;
        this.address = address;
        this.healthStatus = healthStatus;
        this.profileImage = profileImage;
        this.alerts = alerts;
        this.notifications = notifications;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(Boolean healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Boolean getAlerts() {
        return alerts;
    }

    public void setAlerts(Boolean alerts) {
        this.alerts = alerts;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }
}
