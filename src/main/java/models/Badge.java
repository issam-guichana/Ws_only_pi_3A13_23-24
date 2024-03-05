package models;

public class Badge {
    private int idBadge;
    private String nomBadge;
    private String type;
    private String imgBadge;

    // Constructors
    public Badge() {
    }

    public Badge(String nomBadge, String type, String imgBadge) {
        this.nomBadge = nomBadge;
        this.type = type;
        this.imgBadge = imgBadge;
    }

    public Badge(int idBadge, String nomBadge, String type, String imgBadge) {
        this.idBadge = idBadge;
        this.nomBadge = nomBadge;
        this.type = type;
        this.imgBadge = imgBadge;
    }

    // Getters and setters
    public int getIdBadge() {
        return idBadge;
    }

    public void setIdBadge(int idBadge) {
        this.idBadge = idBadge;
    }

    public String getImgBadge() {
        return imgBadge;
    }

    public void setImgBadge(String imgBadge) {
        this.imgBadge = imgBadge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNomBadge() {
        return nomBadge;
    }

    public void setNomBadge(String nomBadge) {
        this.nomBadge = nomBadge;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "idBadge=" + idBadge +
                ", nomBadge='" + nomBadge + '\'' +
                ", type='" + type + '\'' +
                ", imgBadge='" + imgBadge + '\'' +
                '}';
    }
}
