import java.io.FileInputStream;

public class JetonAction {

    String nom;
    int face;        // 1 ou 2
    FileInputStream image1;
    FileInputStream image2;

    public JetonAction() {
    }

    public JetonAction(String nom) {
        this.nom=nom;
    }

    public JetonAction(String nom, int face) {
        this.nom = nom;
        this.face = face;
    }
}
