import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
            valider.setStyle( "-fx-background-color: #6d532f; -fx-font-family : Harrington; -fx-text-fill: white; -fx-font-size : 18;-fx-border-color: white; -fx-border-radius: 5;" );
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

    public static int round=0;
    AtomicInteger jetonjoues = new AtomicInteger(0);


    public void round0(Pane root) throws FileNotFoundException {

        plateau.initPlateau(scene,root);

        plateau.affichagePlateau(scene,root);

        ImageView lancerPartie = loadImage2(root, new FileInputStream("images\\Menu\\Filtre.png"));
        root.getChildren().addAll(lancerPartie);

        Button lancer = new Button("Jouer");
        lancer.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5"); lancer.setMinHeight(45); lancer.setMinWidth(100); lancer.setLayoutX(280); lancer.setLayoutY(300);

        root.getChildren().add(lancer);

        plateau.voirIdMrJack(root);

        lancer.setOnMouseClicked(e ->{
            root.getChildren().remove(lancer);
            root.getChildren().remove(lancerPartie);
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(2000));
            fade.setFromValue(0.8);
            fade.setToValue(10);
            fade.setCycleCount(1);
            fade.setNode(root);
            fade.play();
            try {
                plateau.affichageDistricts(scene,root);
                plateau.affichageJetonsTemps(scene,root,0);
                plateau.affichageJetonsAction(scene,root);
                plateau.affichageDetectives(scene,root);
                round1(root);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        lancer.setOnMouseEntered(e->{
            lancer.setStyle("-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 25; -fx-border-radius: 5");
        });
        lancer.setOnMouseExited(e->{
            lancer.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
        });
    }

    public void round1(Pane root) throws FileNotFoundException {
        round+=1;
        plateau.etatDePartie();

        System.out.println("\nDébut du round "+round+":");

        jetonjoues.set(0);


        // Jeton j: 1 à 4 générés et prêts à être joués grâce à jetonjoues:

        for (int j = 0; j < 4; j++) {
            int finalJ = j;
            plateau.jetonsAction.get(j).currentimg.setOnMousePressed(e -> {
                System.out.println("Jeton "+(finalJ+1)+" sélectionné/joué");
                jetonjoues.addAndGet(1);
                try {
                    actionsJeton(finalJ);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                FadeTransition hide = new FadeTransition(Duration.millis(500));
                hide.setToValue(0.4);
                hide.setCycleCount(1);
                hide.setNode(plateau.jetonsAction.get(finalJ).currentimg);
                hide.play();
                plateau.jetonsAction.get(finalJ).currentimg.setOnMousePressed(null);
            });

            plateau.jetonsAction.get(j).currentimg.setOnMouseReleased(event -> {
                if (jetonjoues.get() == 4) {
                    try {
                        inspection();
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
                plateau.jetonsAction.get(finalJ).currentimg.setOnMousePressed(null);
                plateau.jetonsAction.get(finalJ).currentimg.setOnMouseReleased(null);
            });
        }

    }

    public void actionsJeton(int i) throws FileNotFoundException {

        if (plateau.jetonsAction.get(i).nom.equals((String) "Alibi - Holmes")) { // Alibi OK | Holmes OK
            if (plateau.jetonsAction.get(i).face==1) {
                CarteAlibi cartePiochee = plateau.piocheAlibi();
                if ((round%2==1 && (jetonjoues.get()==2 || jetonjoues.get()==2)) || (round%2==0 && (jetonjoues.get()==1 || jetonjoues.get()==4))) {
                    plateau.mrjack.nbSabliers+=cartePiochee.nbSabliers;
                    plateau.affichageSabliers(scene,root);
                }
                else {
                    ImageView img = cartePiochee.img;
                    img.setX(550-plateau.enqueteur.cartePiochees*7);
                    img.setY(580-plateau.enqueteur.cartePiochees*7); plateau.enqueteur.cartePiochees+=1;
                    img.setFitHeight(50);
                    img.setFitWidth(30);
                    root.getChildren().add(img);
                    FadeTransition fade = new FadeTransition();
                    fade.setDuration(Duration.millis(1000));
                    fade.setFromValue(0.1);
                    fade.setToValue(10);
                    fade.setCycleCount(1);
                    fade.setNode(img);
                    fade.play();
                    for (int j = 0; j < 9; j++) {
                        if (plateau.districts.get(j).nom.equals(cartePiochee.nom)) {
                            plateau.districts.get(j).face=2;
                        }
                    }
                    plateau.affichageDistricts(scene,root);
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {
                plateau.deplacerDetective(plateau.Holmes);
            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Toby - Watson")) { // Toby OK | Watson OK
            if (plateau.jetonsAction.get(i).face==1) {
                plateau.deplacerDetective(plateau.Toby);
            }
            if (plateau.jetonsAction.get(i).face==2) {
                plateau.deplacerDetective(plateau.Watson);
            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Pivot - Echange")) { // Pivot OK |
            if (plateau.jetonsAction.get(i).face==1) {
                for (int j =1;j<10;j++) {
                    plateau.rotationDistrict(root,j);
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {

            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Pivot - Joker")) { // Pivot OK |
            if (plateau.jetonsAction.get(i).face==1) {
                for(int j =1;j<10;j++){
                    plateau.rotationDistrict(root,j);
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {
                //plateau.joker();
            }
        }
        plateau.isJackVisible(plateau.districtsVus());

    }

    public void inspection() throws FileNotFoundException {

        ImageView loupe = new ImageView(new Image(new FileInputStream("images\\Divers\\loupe.png")));
        loupe.setFitHeight(50);
        loupe.setFitWidth(50);
        loupe.setX(307);
        loupe.setY(575);
        loupe.setEffect(new DropShadow(10, Color.WHITE));
        root.getChildren().add(loupe);

        loupe.setOnMousePressed(event -> {
            try {
                ArrayList<Boolean> status = isGameOver();
                System.out.println("Fin du round "+round+" - isGameOver(): " + status.get(0) + " | doesJackWin(): " + status.get(1) + " | doesEnqueteurWin(): " + status.get(2) + "\n________________________________________________________________");

                if (doesJackWin()) {
                    ImageView win_jack = new ImageView(new Image(new FileInputStream("images\\Menu\\win_jack.png")));

                    win_jack.setOnMousePressed(event2 -> {
                        try {
                            menuPlayers(scene,root);
                            win_jack.setOnMousePressed(null);
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                        win_jack.setOnMousePressed(null);
                    });

                    root.getChildren().add(win_jack);

                    ImageView img = plateau.mrjack.identite.img ;
                    img.setFitHeight(325.0);
                    img.setFitWidth(200.0);
                    img.setX(225);
                    img.setY(50);
                    root.getChildren().add(img);
                }
                if (doesEnqueteurWin()) {
                    ImageView win_enqueteur = new ImageView(new Image(new FileInputStream("images\\Menu\\win_enqueteur.png")));

                    win_enqueteur.setOnMousePressed(event3 -> {
                        try {
                            menuPlayers(scene,root);
                            win_enqueteur.setOnMousePressed(null);
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                        win_enqueteur.setOnMousePressed(null);
                    });

                    root.getChildren().add(win_enqueteur);

                    ImageView img = new ImageView(new Image(new FileInputStream("images\\JetonsDetective\\Holmes.png")));
                    img.setFitHeight(325.0);
                    img.setFitWidth(325.0);
                    img.setX(162.5);
                    img.setY(50);
                    root.getChildren().add(img);
                }
                else {
                    plateau.isJackVisible(plateau.districtsVus());
                    prochainRound();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            root.getChildren().remove(loupe);
        });
    }

    public void prochainRound() throws FileNotFoundException {

        // Relancement des jetons
        for (int i = 0; i < 4; i++) {
            root.getChildren().remove(plateau.jetonsAction.get(i).currentimg);
        }
        if (round%2!=0) {
            for (int i = 0; i < 4; i++) {
                //plateau.jetonsAction.get(i).face=(plateau.jetonsAction.get(i).face+1)%2;
                if(plateau.jetonsAction.get(i).face==1) {
                    plateau.jetonsAction.get(i).face=2;
                }
                else {
                    plateau.jetonsAction.get(i).face=1;
                }
            }
        }
        else {
            plateau.lancerJetonsAction();
        }
        plateau.affichageJetonsAction(scene, root);


        // Distribution des sabliers
        plateau.affichageJetonsTemps(scene,root,round);

        if (!plateau.isJackVisible(plateau.districtsVus())) {
            plateau.mrjack.nbSabliers+=1;
            plateau.affichageSabliers(scene,root);
        }

        round1(root);

    }


    public static boolean doesJackWin() {
        if((plateau.mrjack.nbSabliers==6) || (round>=8 && !plateau.isJackVisible(plateau.districtsVus()))) {
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean doesEnqueteurWin() {
        int compteur=0;
        for (int i = 0; i < 9; i++) {
            if (plateau.districts.get(i).face==1) {
                compteur+=1;
            }
        }
        if(compteur==1 & plateau.isJackVisible(plateau.districtsVus())) {
            return true;
        }
        else {
            return false;
        }
    }
    public ArrayList<Boolean> isGameOver() {
        ArrayList<Boolean> status = new ArrayList<Boolean>(3);

        if (doesJackWin()) {
            status.add(0, true);
            status.add(1, true);
            status.add(2, false);
            return status;
        }
        else if (doesEnqueteurWin()) {
            status.add(0, true);
            status.add(1, false);
            status.add(2, true);
            return status;
        }
        else {
            status.add(0, false);
            status.add(1, false);
            status.add(2, false);
            return status;
        }
    }
    public void gameOver(int indiceGagnant) {
        if (indiceGagnant==0) {
            System.out.println("Mr Jack gagne.");
        }
        else {
            System.out.println("L'enqueteur gagne");
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
