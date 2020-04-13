package Models.POJOS;

public class User {
    private String id, name, lastName, username, email, cellPhone, address, profileImage;
    private boolean alerts, notifications, needHelp, isActive, timeConfiguration, ok, heatMap, picker, viewType;

    public User() {
    }

    public User(String Id, String name, String lastName, String username, String email, String cellPhone,
                String address, String profileImage, boolean isOk, boolean alerts, boolean notifications,
                boolean needHelp, boolean isActive, boolean timeConFiguration, boolean heatMap, boolean picker, boolean viewType) {
        this.id = Id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.cellPhone = cellPhone;
        this.address = address;
        this.profileImage = profileImage;
        this.alerts = alerts;
        this.notifications = notifications;
        this.needHelp = needHelp;
        this.isActive = isActive;
        this.timeConfiguration = timeConFiguration;
        this.ok = isOk;
        this.heatMap = heatMap;
        this.picker = picker;
        this.viewType = viewType;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isAlerts() {
        return alerts;
    }

    public void setAlerts(boolean alerts) {
        this.alerts = alerts;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isNeedHelp() {
        return needHelp;
    }

    public void setNeedHelp(boolean needHelp) {
        this.needHelp = needHelp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isTimeConfiguration() {
        return timeConfiguration;
    }

    public void setTimeConfiguration(boolean timeConfiguration) {
        this.timeConfiguration = timeConfiguration;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        ok = ok;
    }

    public boolean isHeatMap() {
        return heatMap;
    }

    public void setHeatMap(boolean heatMap) {
        this.heatMap = heatMap;
    }

    public boolean isPicker() {
        return picker;
    }

    public void setPicker(boolean picker) {
        this.picker = picker;
    }

    public boolean isViewType() {
        return viewType;
    }

    public void setViewType(boolean viewType) {
        this.viewType = viewType;
    }
}
