package Models.POJOS;

public class User {
    private String Id, name, lastName, userName, email, cellPhone, address, profileImage;
    private boolean alerts, notifications, needHelp, isActive, timeConfiguration, isOk, picker, hotMap, viewType;

    public User() {
    }

    public User(String Id, String name, String lastName, String userName, String email, String cellPhone,
                String address, String profileImage, boolean isOk, boolean alerts, boolean notifications,
                boolean needHelp, boolean isActive, boolean timeConFiguration, boolean picker, boolean hotMap, boolean viewType) {
        this.Id = Id;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.cellPhone = cellPhone;
        this.address = address;
        this.profileImage = profileImage;
        this.alerts = alerts;
        this.notifications = notifications;
        this.needHelp = needHelp;
        this.isActive = isActive;
        this.timeConfiguration = timeConFiguration;
        this.isOk = isOk;
        this.picker = picker;
        this.hotMap = hotMap;
        this.viewType =  viewType;
    }

    public boolean isPicker() {
        return picker;
    }

    public void setPicker(boolean picker) {
        this.picker = picker;
    }

    public boolean isHotMap() {
        return hotMap;
    }

    public void setHotMap(boolean hotMap) {
        this.hotMap = hotMap;
    }

    public boolean isViewType() {
        return viewType;
    }

    public void setViewType(boolean viewType) {
        this.viewType = viewType;
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
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
