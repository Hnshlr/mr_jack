import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class JetonTemps {

    int tour;     // de 1 à 8
    Joueur meneur;
    int face;       // 1 ou 2
    ImageView img;
    ImageView currentimg;

    public JetonTemps() {
    }

    public JetonTemps(int tour, Joueur meneur, int face) {
        this.tour = tour;
        this.meneur = meneur;
        this.face = face;
    }
}
