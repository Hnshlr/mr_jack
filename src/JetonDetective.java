import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class JetonDetective {

    String nom;
    int position; // de 1 à 12 autour du plateau
    ImageView img;

    public JetonDetective() {}

    public JetonDetective(String nom, int position) {
        this.nom = nom;
        this.position = position;
    }

}
