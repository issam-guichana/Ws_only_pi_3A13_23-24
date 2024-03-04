package models;


public class Badge {
    private int idBadge;
    private String nomBadge;
    private String type;


    // Constructors
    public Badge() {
    }

    public Badge(String nomBadge, String type) {
        this.nomBadge = nomBadge;
        this.type = type;
    }

    public Badge(int idBadge, String nomBadge, String type, int formationId) {
        this.idBadge = idBadge;
        this.nomBadge = nomBadge;
        this.type = type;

    }

    // Getters and setters
    public int getIdBadge() {
        return idBadge;
    }

    public void setIdBadge(int idBadge) {
        this.idBadge = idBadge;
    }

    public String getNomBadge() {
        return nomBadge;
    }

    public void setNomBadge(String nomBadge) {
        this.nomBadge = nomBadge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




    @Override
    public String toString() {
        return "Badge{" +
                "idBadge=" + idBadge +
                ", nomBadge='" + nomBadge + '\'' +
                ", type='" + type + '\'' +

                '}';
    }


}
