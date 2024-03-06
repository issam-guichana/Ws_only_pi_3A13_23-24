package models;

import java.sql.Date;

public class Certificat {
    private int id;
    private String nomCertif;
    private Date dateCertif;


    public Certificat() {}

    public Certificat(String nomCertif, Date dateCertif ) {
        this.nomCertif = nomCertif;
        this.dateCertif = dateCertif;
        //this.formationId = formationId;
    }

    public Certificat(int id, String nomCertif, Date dateCertif) {
        this.id = id;
        this.nomCertif = nomCertif;
        this.dateCertif = dateCertif;
        //this.formationId = formationId;
    }

    // Getters and setters for all fields, including id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCertif() {
        return nomCertif;
    }

    public void setNomCertif(String nomCertif) {
        this.nomCertif = nomCertif;
    }

    public Date getDateCertif() {
        return dateCertif;
    }

    public void setDateCertif(Date dateCertif) {
        this.dateCertif = dateCertif;
    }




    // toString method
    @Override
    public String toString() {
        return "Certificat{" +
                "id=" + id +
                ", nomCertif='" + nomCertif + '\'' +
                ", dateCertif=" + dateCertif +
               // ", formationId=" + formationId +
                '}';
    }
}