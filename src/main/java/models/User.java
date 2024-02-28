package models;

import java.util.Objects;

public class User {
    private int id_user;
    private String username;
    private String email;
    private String mdp;
    private int age;
    private String role;
    // zyeda jdida
    private String gender;
    private String image;
    private int status;


    public User() {}

    public User(int id_user, String username, String email, String mdp, int age, String role, String gender, String image, int status) {
        this.id_user = id_user;
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.role = role;
        this.gender = gender;
        this.image = image;
        this.status = status;
    }
    public User(int id_user) {
        this.id_user = id_user;
    }

    public User(int id_user, String username, String email, String mdp, int age, String role) {
        this.id_user = id_user;
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.role = role;
    }

    public User(String username, String email, String mdp, int age, String role, String gender) {
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.role = role;
        this.gender = gender;
    }

    public User( String username, String email, String mdp, int age, String role, String gender, String image) {
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.role = role;
        this.gender = gender;
        this.image = image;
    }

    public User(String username, String email, String mdp, int age, String role) {
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.role = role;
    }

    public User(int id_user, String username, String email, int age) {
        this.id_user = id_user;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public User(int id_user, String mdp) {
        this.id_user = id_user;
        this.mdp = mdp;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
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

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' +
                ", gender='" + gender + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId_user() == user.getId_user() && getAge() == user.getAge()
                && getStatus() == user.getStatus() && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getMdp(), user.getMdp())
                && Objects.equals(getRole(), user.getRole()) && Objects.equals(getGender(), user.getGender())
                && Objects.equals(getImage(), user.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_user(), getUsername(), getEmail(),
                getMdp(), getAge(), getRole(), getGender(),
                getImage(), getStatus());
    }
}
