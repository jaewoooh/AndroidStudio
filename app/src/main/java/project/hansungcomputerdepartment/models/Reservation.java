package project.hansungcomputerdepartment.models;

public class Reservation {
    private String id;
    private String name;
    private String track;
    private String phone;
    private String date;
    private String time;
    private String equipment;
    private String userId;

    public Reservation() {
        // Firebase Realtime Database 또는 Firestore를 위한 빈 생성자
    }

    public Reservation(String name, String track, String phone, String date, String time, String equipment, String userId) {
        this.name = name;
        this.track = track;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.equipment = equipment;
        this.userId = userId;
    }

    // getter와 setter 메서드
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

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
