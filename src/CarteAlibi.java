import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class CarteAlibi {

    String nom;
    int nbSabliers;
    ImageView img;

    public CarteAlibi(String nom, int nbSabliers,ImageView img) {
        this.nom = nom;
        this.nbSabliers = nbSabliers;
        this.img = img;
    }
}
