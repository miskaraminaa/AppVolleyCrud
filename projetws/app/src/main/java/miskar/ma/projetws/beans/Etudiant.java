package miskar.ma.projetws.beans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private String ville;
    private String sexe;
    private String image; // Base64 image string

    // Constructor
    public Etudiant(int id, String nom, String prenom, String ville, String sexe, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.sexe = sexe;
        this.image = image;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Method to decode the Base64 string into a Bitmap
    public Bitmap getImageBitmap() {
        if (image != null && !image.isEmpty()) {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null; // or a default image
    }

    // Methods to get formatted strings
    public String getFormattedNom() {
        return "Nom : " + nom; // Formats the name
    }

    public String getFormattedPrenom() {
        return "Prénom : " + prenom; // Formats the first name
    }

    public String getFormattedVille() {
        return "Ville : " + ville; // Formats the city
    }

    public String getFormattedSexe() {
        return "Sexe : " + sexe; // Formats the gender
    }
}