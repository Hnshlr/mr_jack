import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jdk.jfr.events.ExceptionThrownEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Partie extends Application {

    static Plateau plateau = new Plateau();
    public static Joueur joueur1 = new Joueur();
    public static Joueur joueur2 = new Joueur();

    Pane root = new Pane();
    Scene scene = new Scene(root,650,650);

    @Override
    public void start(Stage primaryStage) throws Exception {

        menu(primaryStage, scene, root);

        primaryStage.setTitle("Mister Jack's Pocket");
        primaryStage.getIcons().add(new Image("file:images/JetonsDetective/Holmes.png"));
        primaryStage.setResizable(false);
        primaryStage.setWidth(665);
        primaryStage.setHeight(680);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    // Jouer - Quitter
    public void menu(Stage stage,Scene scene,Pane root) throws FileNotFoundException {
        //Menu du jeu

        /*    ----------------------------------
                Gestion du menu avec le clavier
              ---------------------------------- */

        //Atomic integer nécessaire pour le passer dans lambda exp
        AtomicInteger count = new AtomicInteger(1); //compteur qui indique au programme où se trouve le curseur

        loadImage(root,new FileInputStream("images\\Menu\\Menu1.png"));//Le curseur est sur play par défaut
        scene.setOnKeyPressed(e -> { //scene receptives aux évenements clavier
            if(e.getCode().equals(KeyCode.DOWN)){ // si flèche du bas
                try {
                    loadImage(root,new FileInputStream("images\\Menu\\Menu2.png")); // curseur sur Quit
                    count.set(2);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
            if(e.getCode().equals(KeyCode.UP)){ // si flèche du haut
                try {
                    loadImage(root,new FileInputStream("images\\Menu\\Menu1.png")); // curseur sur play
                    count.set(1);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
            if(e.getCode().equals(KeyCode.ENTER) && count.get()==1){ // si Entrer sur play
                    //on lance la partie
                scene.setOnMouseMoved(null); //On desactive le mouvement souris du menu
                scene.setOnKeyPressed(null);
                scene.setOnMouseClicked(null);
                //root.getChildren().remove()
                try {
                    menuPlayers(scene, root);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
            if(e.getCode().equals(KeyCode.ENTER) && count.get()==2){ // si Entrer sur Quit
                stage.close();
            }
        });

       /*     ----------------------------------
                Gestion du menu avec la souris
              ---------------------------------- */

        scene.setOnMouseMoved(e -> {
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 140 && e.getY() < 285){
                try {
                    loadImage(root,new FileInputStream("images\\Menu\\Menu1.png")); // curseur sur play
                    count.set(1);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 350 && e.getY() < 440){
                try {
                    loadImage(root,new FileInputStream("images\\Menu\\Menu2.png")); // curseur sur Quit
                    count.set(2);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        scene.setOnMouseClicked(e -> {
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 140 && e.getY() < 285){
                //on lance la partie
                try {
                    menuPlayers(scene, root);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                scene.setOnMouseMoved(null); //On desactive le mouvement souris du menu
                scene.setOnKeyPressed(null);
                scene.setOnMouseClicked(null);
            }
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 350 && e.getY() < 440){
                stage.close();
            }
        });
    }

    // Joueur 1 - Joueur 2 +  Button valider
    public void menuPlayers(Scene scene,Pane root) throws FileNotFoundException {
        //Récupère noms et rôles des edux joueurs

        loadImage(root,new FileInputStream("images\\Menu\\MenuRoles.png"));

        TextField textField1 = new TextField("");
        textField1.setFont(Font.font("Harrington", FontWeight.BOLD, 15));
        textField1.setStyle("-fx-text-inner-color: white;-fx-background-color: #6d532f;");
        textField1.setMinWidth(120);
        textField1.setLayoutX(235);
        textField1.setLayoutY(255);
        root.getChildren().add(textField1);

        TextField textField2 = new TextField("");
        textField2.setFont(Font.font("Harrington", FontWeight.BOLD, 15));
        textField2.setStyle("-fx-text-inner-color: white;-fx-background-color: #6d532f;");
        textField2.setMinWidth(120);
        textField2.setLayoutX(235);
        textField2.setLayoutY(410);
        root.getChildren().add(textField2);

        Button valider = new Button("Valider");
        valider.setStyle( "-fx-background-color: #6d532f; -fx-font-family : Harrington; -fx-text-fill: black; -fx-font-size : 15;-fx-border-color: grey; -fx-border-radius: 5;" );
        valider.setMinWidth(100);
        valider.setLayoutX(460);
        valider.setLayoutY(335);
        root.getChildren().add(valider);

        // Gestion des évenements sur le bouton valider

        valider.setOnMouseEntered(e ->{
            valider.setStyle( "-fx-background-color: #6d532f; -fx-font-family : Harrington; -fx-text-fill: white; -fx-font-size : 15;-fx-border-color: white; -fx-border-radius: 5;" );
        });
        valider.setOnMouseExited(e ->{
            valider.setStyle( "-fx-background-color: #6d532f; -fx-font-family : Harrington; -fx-text-fill: black; -fx-font-size : 15;-fx-border-color: grey; -fx-border-radius: 5;" );
        });
        valider.setOnMouseReleased(e ->{
            joueur1.nom = textField1.getText();
            joueur1.role = "Mr Jack";

            joueur2.nom = textField2.getText();
            joueur2.role = "Enquêteur";

            root.getChildren().remove(valider);

            //System.out.println(joueur1.nom + " " + joueur1.role);
            //System.out.println(joueur2.nom + " " + joueur2.role);

            try {
                jouer(root); // on lance le jeu
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });


    }

    // Lancement de la partie
    public void jouer(Pane root) throws FileNotFoundException {

        round0(root);

        //plateau.deplacerDetective(plateau.Holmes);

        // modifications de districts
        /*
        plateau.echangerDistrict(1,3);
        plateau.rotationDistrict(6,1);
        plateau.retournerDistrict(8);
        plateau.etatDePartie();
         */

        // test de visibilité
        /*
        plateau.districts.get(0).orientation=1;
        plateau.districts.get(2).orientation=1;
        plateau.districts.get(7).orientation=2;
        plateau.Holmes.position=1;
        plateau.Watson.position=2;
        plateau.Toby.position=3;
        System.out.println(plateau.isJackVisible());
        plateau.etatDePartie();
        */

        //plateau.deplacerDetective(plateau.Holmes);

    }

    public void round0(Pane root) throws FileNotFoundException {

        plateau.initPlateau(scene,root);

        plateau.affichagePlateau(scene,root);

        ImageView lancerPartie = loadImage2(root, new FileInputStream("images\\Menu\\lancer_la_partie.png"));
        root.getChildren().addAll(lancerPartie);

        Button lancer = new Button("");
        lancer.setStyle( "-fx-background-color: transparent ; -fx-border-color: transparent"); lancer.setMinHeight(45); lancer.setMinWidth(238); lancer.setLayoutX(207); lancer.setLayoutY(302);

        root.getChildren().add(lancer);

        plateau.voirIdMrJack(root);

        lancer.setOnMouseClicked(e ->{
            root.getChildren().remove(lancer);
            root.getChildren().remove(lancerPartie);
            try {
                plateau.affichageDistricts(scene,root);
                plateau.affichageJetonsTemps(scene,root);
                plateau.affichageJetonsAction(scene,root);
                plateau.affichageDetectives(scene,root);
                round1(root);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }

    public void round1(Pane root) throws FileNotFoundException {
        plateau.etatDePartie();

        for(int i =1;i<10;i++){
            plateau.rotationDistrict(root,i);
        }



    }

    public static boolean doesJackWin() {
        if(plateau.mrjack.nbSabliers==6) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean doesEnqueteurWin() {
        int compteur=0;
        for (int i = 0; i < 9; i++) {
            compteur+=plateau.districts.get(i).face;
        }
        if(compteur==1 & plateau.isJackVisible(plateau.districtsVus())) {
            return true;
        }
        else {
            return false;
        }
    }


    // Fonctions loadImages:
    public static void loadImage(Pane root, FileInputStream inputstream) throws FileNotFoundException {
        //Charge l'image du fichier inputstream dans la fenetre

        Image img = new Image(inputstream);
        ImageView imageView = new ImageView(img);
        root.getChildren().addAll(imageView);

    }
    public static ImageView loadImage2(Pane root, FileInputStream inputstream) throws FileNotFoundException {
        //Charge l'image du fichier inputstream dans la fenetre

        Image img = new Image(inputstream);
        ImageView imageView = new ImageView(img);

        return imageView;
    }
}
