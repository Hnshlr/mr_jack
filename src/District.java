public class District {

    String nom;
    int position;      // de 1 à 9 en partant de en haut à gauche
    int orientation;   // de 1 à 4 dans le sens horaire
    int face;          // 1 ou 2 : face suspect ou chemins

    public District(String nom, int position, int orientation, int face) {
        this.nom = nom;
        this.position = position;
        this.orientation = orientation;
        this.face = face;
    }

    public void retournerDistrict(){
        //changement de face
    }

    public void rotationDistrict(){
        //changement d'orientation
    }

    public void echangerDistrict(){
        //changement de position avec un autre
    }
}
