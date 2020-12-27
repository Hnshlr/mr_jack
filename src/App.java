import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App /*extends Application*/ {

    static Plateau plateau = new Plateau();
    MisterJack misterJack = new MisterJack();
    Enqueteur enqueteur = new Enqueteur();

    /*
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root,650,650);

        primaryStage.setTitle("Mister Jack's Pocket");
        primaryStage.getIcons().add(new Image("file:images/JetonsDetective/Holmes.png"));
        primaryStage.setResizable(false);
        primaryStage.setWidth(650);
        primaryStage.setHeight(650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    */

    public static void main(String[] args) {
        /*launch(args);*/
        plateau.initPlateau();

    }
    public void menu(){
        //Menu du jeu
    }
    public void jouer(){
        //déroulement de la partie
    }
}
