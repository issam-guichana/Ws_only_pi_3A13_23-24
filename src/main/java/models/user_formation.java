package models;

public class user_formation {
    private int user_u_f;
    private int user_id;
    private  int form_id;
    private int room_id ;

    public user_formation() {
    }

    public user_formation(int user_id, int form_id , int room_id) {
        this.user_id=user_id;
        this.form_id=form_id;

        this.room_id = room_id;
    }

    public user_formation(int idForm, int idRoom) {
        this.form_id = idForm;
        this.room_id = idRoom;
    }

    public int getForm_id() {
        return form_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public void setForm_id(int form_id) {
        this.form_id = form_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
