import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.FilterInputStream;
import java.lang.reflect.Array;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Partie extends Application {

    static Plateau plateau = new Plateau();
    public static Joueur joueur1 = new Joueur();
    public static Joueur joueur2 = new Joueur();

    Stage stage = new Stage();
    Pane root = new Pane();
    Pane root2 = new Pane();

    Scene scene = new Scene(root,650,650);
    Scene scene2 = new Scene(root2,650,650);

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage = stage;

        menu(primaryStage, scene, root);

        primaryStage.setTitle("Mister Jack's Pocket");
        primaryStage.getIcons().add(new Image("file:images/JetonsDetective/Holmes.png"));
        primaryStage.setResizable(false);
        primaryStage.setWidth(665);
        primaryStage.setHeight(689);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    // Jouer - Quitter
    public void menu(Stage stage,Scene scene,Pane root) throws FileNotFoundException {
        //Menu du jeu

        stage.setScene(scene); //Dans le cas ou l'on revient du menu paramètre

        /*    ----------------------------------
                Gestion du menu avec le clavier
              ---------------------------------- */

        ImageView menu1 = new ImageView(new Image(new FileInputStream("images\\Menu\\Menu1.png")));
        ImageView menu2 = new ImageView(new Image(new FileInputStream("images\\Menu\\Menu2.png")));
        ImageView menu3 = new ImageView(new Image(new FileInputStream("images\\Menu\\Menu3.png")));

        //Atomic integer nécessaire pour le passer dans lambda exp
        AtomicInteger count = new AtomicInteger(1); //compteur qui indique au programme où se trouve le curseur

        root.getChildren().add(menu1);//Le curseur est sur play par défaut
        scene.setOnKeyPressed(e -> { //scene receptive aux évenements clavier
            if(e.getCode().equals(KeyCode.DOWN)){ // si flèche du bas
                root.getChildren().remove(menu1);
                root.getChildren().add(menu2); // curseur sur Quit
                count.set(2);
            }
            if(e.getCode().equals(KeyCode.UP)){ // si flèche du haut
                root.getChildren().remove(menu2);
                root.getChildren().add(menu1); // curseur sur Play
                count.set(1);
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

        AtomicBoolean onceJ = new AtomicBoolean(false);
        AtomicBoolean onceQ = new AtomicBoolean(false);
        AtomicBoolean onceP = new AtomicBoolean(false);

        scene.setOnMouseMoved(e -> {
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 140 && e.getY() < 285 && !onceJ.get()){
                onceJ.set(true);
                onceQ.set(false);
                onceP.set(false);
                root.getChildren().remove(menu2);
                root.getChildren().remove(menu1);
                root.getChildren().add(menu1); // curseur sur Quit
                count.set(1);
            }
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 350 && e.getY() < 440 && !onceQ.get()){
                onceQ.set(true);
                onceJ.set(false);
                onceP.set(false);
                root.getChildren().remove(menu1);
                root.getChildren().add(menu2); // curseur sur Quit
                count.set(2);
            }
            if(e.getX() > 10 && e.getX() < 200 && e.getY() > 600 && e.getY() < 650 && !onceP.get()){
                onceP.set(true);
                root.getChildren().clear();
                root.getChildren().add(menu3);
            }

        });
        scene.setOnMouseClicked(e -> {
            if(e.getX() > 240 && e.getX() < 410 && e.getY() > 140 && e.getY() < 295){
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
            if(e.getX() > 10 && e.getX() < 200 && e.getY() > 600 && e.getY() < 650){
                //on lance le menu paramètre
                try {
                    menuParametre(root);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                scene.setOnMouseMoved(null);
                scene.setOnKeyPressed(null);
                scene.setOnMouseClicked(null);

                stage.setScene(scene2);
            }
        });


    }
    public void menuParametre(Pane root) throws FileNotFoundException {
        ImageView para1 = new ImageView(new Image(new FileInputStream("images\\Menu\\par1.png")));
        ImageView para2 = new ImageView(new Image(new FileInputStream("images\\Menu\\para2.png")));
        ImageView para3 = new ImageView(new Image(new FileInputStream("images\\Menu\\para3.png")));
        ImageView para4 = new ImageView(new Image(new FileInputStream("images\\Menu\\para4.png")));

        AtomicInteger count = new AtomicInteger();

        AtomicBoolean onceT = new AtomicBoolean(false);
        AtomicBoolean onceS = new AtomicBoolean(false);
        AtomicBoolean onceA = new AtomicBoolean(false);
        AtomicBoolean onceR = new AtomicBoolean(false);

        root2.getChildren().add(para1);
        scene2.setOnKeyPressed(e ->{
            if(e.getCode().equals(KeyCode.DOWN)){
                if(count.get() ==0){
                    count.getAndIncrement();
                    root2.getChildren().remove(para1);
                    root2.getChildren().add(para2);
                }
                else if(count.get() ==1){
                    count.getAndIncrement();
                    root2.getChildren().remove(para2);
                    root2.getChildren().add(para3);
                }
                else if(count.get() ==2){
                    count.getAndIncrement();
                    root2.getChildren().remove(para3);
                    root2.getChildren().add(para4);
                }
            }
            if(e.getCode().equals(KeyCode.UP)){
                if(count.get() ==3){
                    count.getAndDecrement();
                    root2.getChildren().remove(para4);
                    root2.getChildren().add(para3);
                }
                else if(count.get() ==2){
                    count.getAndDecrement();
                    root2.getChildren().remove(para3);
                    root2.getChildren().add(para2);
                }
                else if(count.get() ==1){
                    count.getAndDecrement();
                    root2.getChildren().remove(para2);
                    root2.getChildren().add(para1);
                }
            }
        });
        scene2.setOnMouseMoved(e-> {
            if(e.getX()>180 && e.getX()<450 && e.getY()>220 && e.getY()<290 && !onceT.get()){
                onceT.set(true);
                onceS.set(false);
                onceA.set(false);
                onceR.set(false);
                root2.getChildren().clear();
                root2.getChildren().add(para1);
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>290 && e.getY()<350 && !onceS.get()){
                onceT.set(false);
                onceS.set(true);
                onceA.set(false);
                onceR.set(false);
                root2.getChildren().clear();
                root2.getChildren().add(para2);
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>355 && e.getY()<415 && !onceA.get()){
                onceT.set(false);
                onceS.set(false);
                onceA.set(true);
                onceR.set(false);
                root2.getChildren().clear();
                root2.getChildren().add(para3);
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>415 && e.getY()<480 && !onceR.get()){
                onceT.set(false);
                onceS.set(false);
                onceA.set(false);
                onceR.set(true);
                root2.getChildren().clear();
                root2.getChildren().add(para4);
            }

        });
        scene2.setOnMouseClicked(e-> {
            if(e.getX()>180 && e.getX()<450 && e.getY()>220 && e.getY()<290 ){

            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>290 && e.getY()<350 ){

            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>355 && e.getY()<415 ){

            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>415 && e.getY()<480 ){   // si Retour, on revient au menu
                try {
                    menu(stage,scene,root);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }

        });
    }

    // Joueur 1 - Joueur 2 +  Button valider
    public void menuPlayers(Scene scene,Pane root) throws FileNotFoundException {
        //Récupère noms et rôles des edux joueurs

        root.getChildren().clear();

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
        valider.setOnMousePressed(e ->{
            playSound("audio\\click.wav");

            joueur1.nom = textField1.getText();
            joueur1.role = "Mr Jack";

            joueur2.nom = textField2.getText();
            joueur2.role = "Enquêteur";

            root.getChildren().remove(valider);

            //System.out.println(joueur1.nom + " " + joueur1.role);
            //System.out.println(joueur2.nom + " " + joueur2.role);

            try {
                round0(root); // on lance le jeu
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });


    }

    // Lancement de la partie

    public static int round=0;
    AtomicInteger jetonjoues = new AtomicInteger(0);


    public void round0(Pane root) throws FileNotFoundException {

        plateau.initPlateau(scene,root);

        plateau.affichagePlateau(scene,root);

        round=0;

        ImageView lancerPartie = loadImage2(root, new FileInputStream("images\\Menu\\Filtre.png"));
        root.getChildren().add(lancerPartie);
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(700));
        fade.setFromValue(0.1);
        fade.setToValue(10);
        fade.setCycleCount(1);
        fade.setNode(lancerPartie);
        fade.play();

        Button lancer = new Button("Lancer");
        lancer.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5"); lancer.setMinHeight(45); lancer.setMinWidth(100); lancer.setLayoutX(275); lancer.setLayoutY(300);

        root.getChildren().add(lancer);

        plateau.voirIdMrJack(root);

        lancer.setOnMouseClicked(e ->{
            playSound("audio\\click.wav");

            root.getChildren().remove(lancer);
            root.getChildren().remove(lancerPartie);

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
            lancer.setStyle("-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 5");
        });
        lancer.setOnMouseExited(e->{
            lancer.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
        });
    }

    public void round1(Pane root) throws FileNotFoundException {

        round+=1;
        jetonjoues.set(0);
        plateau.etatDePartie();
        System.out.println("\nDébut du round "+round+":");

        ImageView inspTurn = new ImageView(new Image(new FileInputStream("images\\Menu\\plateau_insp.png")));
        ImageView jackTurn = new ImageView(new Image(new FileInputStream("images\\Menu\\plateau_jack.png")));

        if(jetonjoues.get()==0){
            if(round%2==1){
                root.getChildren().remove(jackTurn);
                root.getChildren().add(inspTurn);
            }
            else{
                root.getChildren().remove(inspTurn);
                root.getChildren().add(jackTurn);

            }
        }


        // Jeton j: 1 à 4 générés et prêts à être joués grâce à jetonjoues:

        for (int j = 0; j < 4; j++) {
            int finalJ = j;
            plateau.jetonsAction.get(j).currentimg.setOnMousePressed(e -> {

                System.out.println("Jeton "+(finalJ+1)+" sélectionné/joué");
                jetonjoues.addAndGet(1);

                try {
                    //affichageJoueur(root,inspTurn,jackTurn);
                    validation(inspTurn,jackTurn);
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
        }
    }

    public void affichageJoueur(Pane root,ImageView inspTurn,ImageView jackTurn) throws FileNotFoundException {

        if(jetonjoues.get()==1){
            if(round%2==1){
                root.getChildren().remove(inspTurn);
                root.getChildren().add(jackTurn);
            }
            else{
                root.getChildren().remove(jackTurn);
                root.getChildren().add(inspTurn);

            }
        }
        else if(jetonjoues.get()==2){
            if(round%2==1){
                root.getChildren().remove(jackTurn);
                root.getChildren().add(jackTurn);
            }
            else{
                root.getChildren().remove(inspTurn);
                root.getChildren().add(inspTurn);
            }
        }
        else if(jetonjoues.get()==3){
            if(round%2==1){
                root.getChildren().remove(jackTurn);
                root.getChildren().add(inspTurn);
            }
            else{
                root.getChildren().remove(inspTurn);
                root.getChildren().add(jackTurn);
            }
        }
        else if (jetonjoues.get() == 4) {
            if (round % 2 == 1) {
                root.getChildren().remove(inspTurn);
            } else {
                root.getChildren().remove(jackTurn);
            }
        }

    }
    public void actionsJeton(int i) throws FileNotFoundException {


        if (plateau.jetonsAction.get(i).nom.equals((String) "Alibi - Holmes")) { // Alibi OK | Holmes OK
            if (plateau.jetonsAction.get(i).face==1) {
                CarteAlibi cartePiochee = plateau.piocheAlibi();
                if ((round%2==1 && (jetonjoues.get()==2 || jetonjoues.get()==3)) || (round%2==0 && (jetonjoues.get()==1 || jetonjoues.get()==4))) {
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

                            double x = plateau.districts.get(j).currentimg.getX();
                            double y = plateau.districts.get(j).currentimg.getY();
                            root.getChildren().remove(plateau.districts.get(j).currentimg);

                            plateau.districts.get(j).currentimg=plateau.districts.get(j).img2;
                            plateau.districts.get(j).currentimg.setX(x);
                            plateau.districts.get(j).currentimg.setY(y);
                            plateau.districts.get(j).currentimg.setFitHeight(98);
                            plateau.districts.get(j).currentimg.setFitWidth(98);
                            plateau.districts.get(j).currentimg.setRotate(plateau.districts.get(j).orientation*90-90);

                            root.getChildren().add(plateau.districts.get(j).currentimg);
                        }
                    }
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
                plateau.echangerDistrict();
            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Pivot - Joker")) { // Pivot OK |
            if (plateau.jetonsAction.get(i).face==1) {
                for(int j =1;j<10;j++){
                    plateau.rotationDistrict(root,j);
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {
                plateau.joker();
            }
        }
    }

    public void validation(ImageView inspTurn, ImageView jackTurn) throws FileNotFoundException {

        ArrayList<ImageView> waits = new ArrayList<ImageView>(4);

        for (int i = 0; i < 4; i++) {
            waits.add(i,new ImageView(new Image(new FileInputStream("images\\Divers\\wait3.png"))));
            waits.get(i).setFitHeight(48);
            waits.get(i).setFitWidth(48);
            waits.get(i).setX(21);
            waits.get(i).setY(183+80*i);
            root.getChildren().add(waits.get(i));
        }

        ImageView next = new ImageView(new Image(new FileInputStream("images\\Divers\\white_check.png")));
        next.setFitHeight(50);
        next.setFitWidth(50);
        next.setX(300);
        next.setY(575);
        next.setEffect(new DropShadow(10, Color.WHITE));
        root.getChildren().add(next);

        next.setOnMousePressed(event -> {
            try {
                for (int i = 0; i < 4; i++) {
                    root.getChildren().remove(waits.get(i));
                }
                root.getChildren().remove(next);
                affichageJoueur(root,inspTurn,jackTurn);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        next.setOnMouseReleased(event -> {
            if (jetonjoues.get() == 4) {
                try {
                    for (int i = 0; i < 4; i++) {
                        root.getChildren().remove(waits.get(i));
                    }
                    inspection();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                plateau.jetonsAction.get(3).currentimg.setOnMousePressed(null);
                plateau.jetonsAction.get(3).currentimg.setOnMouseReleased(null);
            }
        });
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
            playSound("audio\\bell.mp3");
            try {
                plateau.isJackVisible(plateau.districtsVus());

                // Distribution des Jetons Temps:
                plateau.affichageJetonsTemps(scene,root,round);
                if (!plateau.isJackVisible(plateau.districtsVus())) {
                    plateau.mrjack.nbSabliers+=1;
                    plateau.affichageSabliers(scene,root);
                }
                else {
                    plateau.enqueteur.sabliersRecuperes+=1;

                    ImageView img = new ImageView(new Image(new FileInputStream("images\\JetonsTemps\\T"+round+".png")));
                    img.setFitHeight(83);
                    img.setFitWidth(83);
                    img.setX(477-plateau.enqueteur.sabliersRecuperes*17);
                    img.setY(564);
                    root.getChildren().add(img);
                    FadeTransition fade = new FadeTransition();
                    fade.setDuration(Duration.millis(1000));
                    fade.setFromValue(0.1);
                    fade.setToValue(10);
                    fade.setCycleCount(1);
                    fade.setNode(img);
                    fade.play();
                }

                plateau.affichageDistricts(scene,root);
                ArrayList<Boolean> status = isGameOver();
                System.out.println("Inspection: isJackVisible(): "+ plateau.isJackVisible(plateau.districtsVus())+"\nFin du round "+round+" - isGameOver(): " + status.get(0) + " | doesJackWin(): " + status.get(1) + " | doesEnqueteurWin(): " + status.get(2) + "\n________________________________________________________________");

                if (!status.get(0)) {
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

        round1(root);

    }

    public static boolean doesJackWin() {
        if((plateau.mrjack.nbSabliers>=6) || (round>=8 && !plateau.isJackVisible(plateau.districtsVus()))) {
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

    public ArrayList<Boolean> isGameOver() throws FileNotFoundException {
        ArrayList<Boolean> status = new ArrayList<Boolean>(3);

        if (doesJackWin()) {
            status.add(0, true);
            status.add(1, true);
            status.add(2, false);

            ImageView filtre = new ImageView(new Image(new FileInputStream("images\\Menu\\filtre.png")));
            root.getChildren().remove(filtre);
            root.getChildren().add(filtre);
            FadeTransition fade0 = new FadeTransition(); fade0.setDuration(Duration.millis(1000)); fade0.setFromValue(0.1); fade0.setToValue(10); fade0.setCycleCount(1); fade0.setNode(filtre); fade0.play();

            ImageView win_jack = new ImageView(new Image(new FileInputStream("images\\Menu\\win_jack.png")));
            root.getChildren().remove(win_jack);
            root.getChildren().add(win_jack);
            FadeTransition fade = new FadeTransition(); fade.setDuration(Duration.millis(1000)); fade.setFromValue(0.1); fade.setToValue(10); fade.setCycleCount(1); fade.setNode(win_jack); fade.play();


            ImageView img = plateau.mrjack.identite.img ;
            img.setFitHeight(295.0);
            img.setFitWidth(180.0);
            img.setX(231.5);
            img.setY(35);
            root.getChildren().remove(img);
            root.getChildren().add(img);
            FadeTransition fade2 = new FadeTransition(); fade2.setDuration(Duration.millis(1000)); fade2.setFromValue(0.1); fade2.setToValue(10); fade2.setCycleCount(1); fade2.setNode(img); fade2.play();


            Button playAgain = new Button("Inverser les rôles");
            Button quitter = new Button("Quitter");
            playAgain.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            quitter.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            playAgain.setLayoutX(235);
            playAgain.setLayoutY(500);
            quitter.setLayoutX(285);
            quitter.setLayoutY(560);
            root.getChildren().add(playAgain);
            root.getChildren().add(quitter);

            playAgain.setOnMousePressed(event2 -> {
                try {
                    menuPlayers(scene,root);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            });

            playAgain.setOnMouseEntered(event2 -> {
                playAgain.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 7");
            });

            playAgain.setOnMouseExited(event2 -> {
                playAgain.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            });

            quitter.setOnMousePressed(event2 -> {
                stage.close();
            });

            quitter.setOnMouseEntered(event2 -> {
                quitter.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 7");
            });

            quitter.setOnMouseExited(event2 -> {
                quitter.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            });


            return status;
        }
        else if (doesEnqueteurWin()) {

            ImageView filtre = new ImageView(new Image(new FileInputStream("images\\Menu\\filtre.png")));
            root.getChildren().remove(filtre);
            root.getChildren().add(filtre);
            FadeTransition fade0 = new FadeTransition(); fade0.setDuration(Duration.millis(1000)); fade0.setFromValue(0.1); fade0.setToValue(10); fade0.setCycleCount(1); fade0.setNode(filtre); fade0.play();

            ImageView win_enqueteur = new ImageView(new Image(new FileInputStream("images\\Menu\\win_enqueteur.png")));
            root.getChildren().remove(win_enqueteur);
            root.getChildren().add(win_enqueteur);
            FadeTransition fade = new FadeTransition(); fade.setDuration(Duration.millis(1000)); fade.setFromValue(0.1); fade.setToValue(10); fade.setCycleCount(1); fade.setNode(win_enqueteur); fade.play();


            ImageView img = new ImageView(new Image(new FileInputStream("images\\JetonsDetective\\Holmes.png")));
            img.setX(245);
            img.setY(100);
            root.getChildren().remove(img);
            root.getChildren().add(img);
            FadeTransition fade2 = new FadeTransition(); fade2.setDuration(Duration.millis(1000)); fade2.setFromValue(0.1); fade2.setToValue(10); fade2.setCycleCount(1); fade2.setNode(img); fade2.play();


            Button playAgain = new Button("Inverser les rôles");
            Button quitter = new Button("Quitter");
            playAgain.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            quitter.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            playAgain.setLayoutX(235);
            playAgain.setLayoutY(500);
            quitter.setLayoutX(285);
            quitter.setLayoutY(560);
            root.getChildren().add(playAgain);
            root.getChildren().add(quitter);

            playAgain.setOnMousePressed(event2 -> {
                try {
                    menuPlayers(scene,root);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            });

            playAgain.setOnMouseEntered(event2 -> {
                playAgain.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 7");
            });

            playAgain.setOnMouseExited(event2 -> {
                playAgain.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            });

            quitter.setOnMousePressed(event2 -> {
                stage.close();
            });

            quitter.setOnMouseEntered(event2 -> {
                quitter.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 7");
            });

            quitter.setOnMouseExited(event2 -> {
                quitter.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
            });




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


    // Fonctions loadImages:
    public static void loadImage(Pane root, FileInputStream inputstream) throws FileNotFoundException {
        //Charge l'image du fichier inputstream dans la fenetre

        Image img = new Image(inputstream);
        ImageView imageView = new ImageView(img);
        root.getChildren().add(imageView);

    }
    public static ImageView loadImage2(Pane root, FileInputStream inputstream) throws FileNotFoundException {
        //Charge l'image du fichier inputstream dans la fenetre

        Image img = new Image(inputstream);
        ImageView imageView = new ImageView(img);

        return imageView;
    }

    public void playSound(String soundPath){
        Media hit = new Media(Paths.get(soundPath).toUri().toString());
        AudioClip mediaPlayer = new AudioClip(hit.getSource());
        mediaPlayer.play();
    }
}
