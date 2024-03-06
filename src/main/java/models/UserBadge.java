package models;

public class UserBadge {
    private int id;
    private int badgeId;
    private int userId;

    // Constructors
    public UserBadge() {
    }

    public UserBadge(int id, int badgeId, int userId) {
        this.id = id;
        this.badgeId = badgeId;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(int badgeId) {
        this.badgeId = badgeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // ToString method
    @Override
    public String toString() {
        return "UserBadge{" +
                "id=" + id +
                ", badgeId=" + badgeId +
                ", userId=" + userId +
                '}';
    }
}
