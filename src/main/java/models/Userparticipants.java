package models;

public class Userparticipants {
    private int user_id;
    private String userName;
    private int event_id;
    private String event_name;

    // Constructor
    public Userparticipants(int user_id, String userName, int event_id, String event_name) {
        this.user_id = user_id;
        this.userName = userName;
        this.event_id = event_id;
        this.event_name = event_name;
    }

    public Userparticipants(String userName,String event_name) {
        this.event_name = event_name;
        this.userName = userName;
    }

    public Userparticipants() {

    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    @Override
    public String toString() {
        return "Userparticipants{" +
                "user_id=" + user_id +
                ", userName='" + userName + '\'' +
                ", event_id=" + event_id +
                '}';
    }
}
