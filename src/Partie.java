import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

import java.lang.reflect.Array;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

        menu(primaryStage, scene, root); // toute la suite en escalier depuis le menu

        primaryStage.setTitle("Mister Jack Pocket");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("images/JetonsDetective/Holmes.png")));
        primaryStage.setResizable(false);
        primaryStage.setWidth(650);
        primaryStage.setHeight(650);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }


   /* -----------------------------------------------------------------------------

                       Différents menus et fonctions associées

   -------------------------------------------------------------------------------- */

    // Premier menu

    public void menu(Stage stage,Scene scene,Pane root) throws FileNotFoundException {
        //Menu du jeu

        stage.setScene(scene); //Dans le cas ou l'on revient du menu paramètre

        /*    ----------------------------------
                Gestion du menu avec le clavier
              ---------------------------------- */

        //menu défilé par succession d'images
        ImageView menu1 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/Menu1.png")));
        ImageView menu2 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/Menu2.png")));
        ImageView menu3 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/Menu3.png")));

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
                count.set(0);
                onceJ.set(true);
                onceQ.set(false);
                onceP.set(false);
                root.getChildren().remove(menu2);
                root.getChildren().remove(menu1);
                root.getChildren().add(menu1); // curseur sur Quit
                count.set(1);
            }
            if(e.getX() > 240 && e.getX() < 395 && e.getY() > 350 && e.getY() < 440 && !onceQ.get()){
                count.set(1);
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

            }
        });


    }

    // Menu des paramètres accessible depuis le premier menu

    public void menuParametre(Pane root) throws FileNotFoundException {
        //Nouvelle scene pour ne pas avoir les évenembts du menu précédent
        stage.setScene(scene2);
        //même procédé que pour le menu précédent
        ImageView para1 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/par1.png")));
        ImageView para2 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/para2.png")));
        ImageView para3 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/para3.png")));
        ImageView para4 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/para4.png")));

        //compteur pour se repérer (atomic pour l'inserer en lambda)
        AtomicInteger count = new AtomicInteger();

        //pour éviter les duplicate error avec la souris
        AtomicBoolean onceT = new AtomicBoolean(false);
        AtomicBoolean onceS = new AtomicBoolean(false);
        AtomicBoolean onceA = new AtomicBoolean(false);
        AtomicBoolean onceR = new AtomicBoolean(false);

        root2.getChildren().add(para1);

        /*    ----------------------------------
                Gestion du menu avec le clavier
              ---------------------------------- */

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
            if(e.getCode().equals(KeyCode.ENTER)){
                if(count.get() ==0){
                    try {
                        tutoriel(stage);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
                else if(count.get() ==1){
                    try {
                        lireLesScores(stage);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else if(count.get() ==2){
                    try {
                        changerSkin(stage);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
                else{
                    try {
                        menu(stage,scene,root);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            }
        });

         /*     ----------------------------------
                Gestion du menu avec la souris
              ---------------------------------- */

        scene2.setOnMouseMoved(e-> {
            if(e.getX()>180 && e.getX()<450 && e.getY()>220 && e.getY()<290 && !onceT.get()){
                count.set(0);
                onceT.set(true);
                onceS.set(false);
                onceA.set(false);
                onceR.set(false);
                root2.getChildren().clear();
                root2.getChildren().add(para1);
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>290 && e.getY()<350 && !onceS.get()){
                count.set(1);
                onceT.set(false);
                onceS.set(true);
                onceA.set(false);
                onceR.set(false);
                root2.getChildren().clear();
                root2.getChildren().add(para2);

            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>355 && e.getY()<415 && !onceA.get()){
                count.set(2);
                onceT.set(false);
                onceS.set(false);
                onceA.set(true);
                onceR.set(false);
                root2.getChildren().clear();
                root2.getChildren().add(para3);
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>415 && e.getY()<480 && !onceR.get()){
                count.set(3);
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
                try {
                    tutoriel(stage);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>290 && e.getY()<350 ){
                try {
                    lireLesScores(stage);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if(e.getX()>180 && e.getX()<450 && e.getY()>355 && e.getY()<415 ){
                try {
                    changerSkin(stage);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
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
    public void tutoriel(Stage stage) throws FileNotFoundException {
        ScrollPane tuto = new ScrollPane();
        tuto.setStyle(" -fx-background-color: #806237;");
        tuto.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Pane content = new Pane();
        Scene sceneTuto = new Scene(tuto,650,650);
        stage.setScene(sceneTuto);

        // Le tutoriel est une longue image que l'on defile dans un scrollpane

        ImageView tutoImg = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/Tutoriel.png")));

        // Lien vers le pdf des règles du jeu
        Hyperlink lien = new Hyperlink("juste ici");
        lien.setLayoutX(110);
        lien.setLayoutY(177);
        lien.setStyle(" -fx-border-color: transparent; -fx-font-family: Harrington; -fx-text-fill: orange; -fx-font-size: 28");
        content.getChildren().addAll(tutoImg,lien);

        Button retour = new Button("Retour");
        retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5"); retour.setMinHeight(45); retour.setMinWidth(100); retour.setLayoutX(275); retour.setLayoutY(5050);
        content.getChildren().add(retour);
        retour.setOnMouseEntered(e->{
            retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 5");
        });
        retour.setOnMouseExited(e->{
            retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
        });
        retour.setOnMousePressed(e->{
            try {
                menuParametre(root);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        tuto.setContent(content);

        lien.setOnAction(e->{
            getHostServices().showDocument("https://moodle.isep.fr/moodle/pluginfile.php/29594/mod_resource/content/1/mr-jack-pocket_rules_fr.pdf");
        });
    }
    public void lireLesScores(Stage stage) throws IOException {

        ScrollPane sco = new ScrollPane();
        sco.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Pane content = new Pane(); // contenu du scrollpane
        Scene sceneSco = new Scene(sco,650,650);
        stage.setScene(sceneSco);
        sco.setPrefSize(650, 650);
        content.setMinSize(650,650);   // ainsi le scrollPane est extensible selon le nombre de noms à afficher
        sco.setStyle("-fx-background: #806237; -fx-background-color: #806237");


        //Label de titre
        Label titre = new Label("Tableaux des scores");
        titre.setTextFill(Color.web("orange"));
        titre.setFont(new Font("Harrington",50));
        titre.setLayoutX(90);
        titre.setLayoutY(50);
        content.getChildren().add(titre);

        //lecture du fichir scores.txt (disponible uniquement via l'IDE)
        Path path = Paths.get("scores.txt");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);


        for(int i =6;i<lines.size();i++){
            long count = lines.get(i).chars().filter(ch -> ch == '1').count(); //On compte les scores
            String nom = "";
            for(int j=0;j<lines.get(i).length();j++){
                if(lines.get(i).charAt(j) == '|'){
                    break;                              // Les noms sont délimités par des '|'
                }
                nom = nom + lines.get(i).charAt(j);
            }
            //On ajoute un label pour chaque nom du fichier
            Label lab = new Label(nom+" : .....................................  " + "" + ""+count);
            lab.setTextFill(Color.web("white"));
            lab.setFont(new Font("Harrington",30));
            lab.setLayoutY((i-3)*50);
            lab.setLayoutX(100);


            content.getChildren().add(lab);
        }
        // Bouton de retour au menu des paramètres
        Button retour = new Button("Retour");
        retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5"); retour.setMinHeight(45); retour.setMinWidth(100); retour.setLayoutX(275); retour.setLayoutY(lines.size()*45 - 20);
        content.getChildren().add(retour);
        retour.setOnMouseEntered(e->{
            retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 5");
        });
        retour.setOnMouseExited(e->{
            retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
        });
        retour.setOnMousePressed(e->{
            try {
                menuParametre(root);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });


        sco.setContent(content);
    }
    public void changerSkin(Stage stage) throws FileNotFoundException {
        Pane skin = new Pane();
        skin.setStyle("-fx-background: #806237; -fx-background-color: #806237");
        Scene sceneSk = new Scene(skin,650,650);
        stage.setScene(sceneSk);

        // 3 skins différents proposés
        ImageView normal = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/plateau2.png"))); normal.setFitHeight(100);normal.setFitWidth(100);normal.setX(120);normal.setY(275);
        ImageView bois = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/plateau_bois.png"))); bois.setFitHeight(100);bois.setFitWidth(100);bois.setX(270);bois.setY(275);
        ImageView whiteChapel = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/plateau_WhiteChapel.png"))); whiteChapel.setFitHeight(100);whiteChapel.setFitWidth(100);whiteChapel.setX(420);whiteChapel.setY(275);

        skin.getChildren().addAll(normal,bois,whiteChapel);

        normal.setOnMouseEntered(e->{
            normal.setEffect(new DropShadow(10, Color.WHITE));
        });
        normal.setOnMouseExited(e->{
            normal.setEffect(new DropShadow(0, Color.WHITE));
        });
        normal.setOnMouseClicked(e->{
            normal.setEffect(new DropShadow(10, Color.ORANGE));
            plateau.urlFond = "images/Menu/plateau2.png";
        });

        bois.setOnMouseEntered(e->{
            bois.setEffect(new DropShadow(10, Color.WHITE));
        });
        bois.setOnMouseExited(e->{
            bois.setEffect(new DropShadow(0, Color.WHITE));
        });
        bois.setOnMouseClicked(e->{
            bois.setEffect(new DropShadow(10, Color.ORANGE));
            plateau.urlFond = "images/Menu/plateau_bois.png";
        });

        whiteChapel.setOnMouseEntered(e->{
            whiteChapel.setEffect(new DropShadow(10, Color.WHITE));
        });
        whiteChapel.setOnMouseExited(e->{
            whiteChapel.setEffect(new DropShadow(0, Color.WHITE));
        });
        whiteChapel.setOnMouseClicked(e->{
            whiteChapel.setEffect(new DropShadow(10, Color.ORANGE));
            plateau.urlFond = "images/Menu/plateau_WhiteChapel.png";
        });

        //Bouton retour
        Button retour = new Button("Retour");
        retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5"); retour.setMinHeight(45); retour.setMinWidth(100); retour.setLayoutX(275); retour.setLayoutY(500);
        skin.getChildren().add(retour);
        retour.setOnMouseEntered(e->{
            retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: white; -fx-font-family: Harrington; -fx-text-fill: white; -fx-font-size: 20; -fx-border-radius: 5");
        });
        retour.setOnMouseExited(e->{
            retour.setStyle( "-fx-background-color: #806237 ; -fx-border-color: grey; -fx-font-family: Harrington; -fx-text-fill: black; -fx-font-size: 20; -fx-border-radius: 5");
        });
        retour.setOnMousePressed(e->{
            try {
                menuParametre(root);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }

    // Menu de choix des rôles accessible depuis menu en cliquant sur 'jouer'

    public void menuPlayers(Scene scene,Pane root) throws FileNotFoundException {
        //Récupère noms et rôles des edux joueurs

        root.getChildren().clear();

        ImageView menuPlayers = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/MenuRoles.png")));
        root.getChildren().add(menuPlayers);

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
            try {
                playSound("audio/click.wav");
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }

            joueur1.nom = textField1.getText();
            joueur1.role = "Mr Jack";

            joueur2.nom = textField2.getText();
            joueur2.role = "Enquêteur";

            root.getChildren().remove(valider);

            System.out.println(joueur1.nom + " " + joueur1.role);
            System.out.println(joueur2.nom + " " + joueur2.role);

            try {
                round0(root); // on lance le jeu
                root.getChildren().removeAll(menuPlayers,textField1,textField2);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });


    }

    /* -----------------------------------------------------------------------------

                             Déroulemnt de la partie

   -------------------------------------------------------------------------------- */

    public static int round=0; // compte les tours

    AtomicInteger jetonjoues = new AtomicInteger(0); // compte les jetons actions sélectionnés pour activer l'inspection

    // Mise en place des différents éléments (n'intervient qu'au lancement)
    public void round0(Pane root) throws FileNotFoundException {

        plateau.initPlateau(); // On init le plateau

        plateau.affichageFondPlateau(root); // On le charge dans la fenêtre

        round=0;

        //Mini interface de lancement

        ImageView lancerPartie = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/Filtre.png")));
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

        // Mr jack peut découvrir son identité (déterminée dans initplateau l.617)
        plateau.voirIdMrJack(root);

        //lancement du jeu avec la fonction round1
        lancer.setOnMouseClicked(e ->{
            try {
                playSound("audio/click.wav");
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }

            root.getChildren().remove(lancer);
            root.getChildren().remove(lancerPartie);

            try {
                plateau.affichageDistricts(root);
                plateau.affichageJetonsTemps(root,0);
                plateau.affichageJetonsAction(root);
                plateau.affichageDetectives(root);
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

        // On incrémente les variables de comptage
        round+=1;
        jetonjoues.set(0);

        // Pour débug
        //plateau.etatDePartie();

        // Transaprents pour indiquer le joueur qui doit jouer (voir affichageJoueur l.726)
        ImageView inspTurn = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/plateau_insp.png")));
        ImageView jackTurn = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/plateau_jack.png")));

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

                jetonjoues.addAndGet(1);

                try {
                    validation(inspTurn,jackTurn);
                    actionsJeton(finalJ);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                // Modif de l'opacité en transition
                FadeTransition hide = new FadeTransition(Duration.millis(500));
                hide.setToValue(0.4);
                hide.setCycleCount(1);
                hide.setNode(plateau.jetonsAction.get(finalJ).currentimg);
                hide.play();

                // On ne peut plus jouer l'action
                plateau.jetonsAction.get(finalJ).currentimg.setOnMousePressed(null);
            });
        }
    }

    public void affichageJoueur(Pane root,ImageView inspTurn,ImageView jackTurn) throws FileNotFoundException {

        // implémente l'aternance des coups de chaque joueur et affiche le joueur qui doit jouer dans la fenêtre

        if(jetonjoues.get()==1){ // Une fois le premier jeton cliqué selon que le tour est pair ou impair Mr jack ou l'enquêteur prend la main
            if(round%2==1){
                root.getChildren().remove(inspTurn);
                root.getChildren().add(jackTurn);
            }
            else{
                root.getChildren().remove(jackTurn);
                root.getChildren().add(inspTurn);

            }
        }
        else if(jetonjoues.get()==2){ // Deux fois de suite le même joueur
            if(round%2==1){
                root.getChildren().remove(jackTurn);
                root.getChildren().add(jackTurn);
            }
            else{
                root.getChildren().remove(inspTurn);
                root.getChildren().add(inspTurn);
            }
        }
        else if(jetonjoues.get()==3){ // Celui qui n'a pas joué qu'une fois termine le tour
            if(round%2==1){
                root.getChildren().remove(jackTurn);
                root.getChildren().add(inspTurn);
            }
            else{
                root.getChildren().remove(inspTurn);
                root.getChildren().add(jackTurn);
            }
        }
        else if (jetonjoues.get() == 4) {  // Transition avec le tour suivant
            if (round % 2 == 1) {
                root.getChildren().remove(inspTurn);
            } else {
                root.getChildren().remove(jackTurn);
            }
        }

    }
    public void actionsJeton(int i) throws FileNotFoundException {
        // On définit ici les fonctions appelées selon le jeton sur lequel on clique

        if (plateau.jetonsAction.get(i).nom.equals((String) "Alibi - Holmes")) {                                            // si "Alibi/Holmes...
            if (plateau.jetonsAction.get(i).face==1) {                                                                      // si face 1...
                CarteAlibi cartePiochee = plateau.piocheAlibi();                                                            // On pioche une carte alibi

                if ((round%2==1 && (jetonjoues.get()==2 || jetonjoues.get()==3)) || (round%2==0 && (jetonjoues.get()==1 || jetonjoues.get()==4))) { // si la pioche intervient pendant un tour de Jack...
                    plateau.mrjack.nbSabliers+=cartePiochee.nbSabliers;                                                                             // on incrémente son compteur...
                    plateau.affichageSabliers(root);                                                                                                // et on affiche les sabliers de la carte à côté de lui
                }
                else {                                                                                                                              // sinon...
                    ImageView img = cartePiochee.img;                                                                                               // On affche l'image de la carte piochée à côté de l'enquêteur
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
                    for (int j = 0; j < 9; j++) {                                                          // On test tous les districts pour savoir si le personnage est toujours visible
                        if (plateau.districts.get(j).nom.equals(cartePiochee.nom)) {                       // si c'est le cas...

                            plateau.districts.get(j).face=2;                                               // on le retourne

                            //on recupère les coordonnées pour replacer la nouvelle image
                            double x = plateau.districts.get(j).currentimg.getX();
                            double y = plateau.districts.get(j).currentimg.getY();

                            //on retire l'ancienne
                            root.getChildren().remove(plateau.districts.get(j).currentimg);

                            //on ajoute la nouvelle
                            plateau.districts.get(j).currentimg=plateau.districts.get(j).img2;
                            plateau.districts.get(j).currentimg.setX(x);
                            plateau.districts.get(j).currentimg.setY(y);
                            plateau.districts.get(j).currentimg.setFitHeight(98);
                            plateau.districts.get(j).currentimg.setFitWidth(98);
                            plateau.districts.get(j).currentimg.setRotate(plateau.districts.get(j).orientation*90-90);

                            root.getChildren().add(plateau.districts.get(j).currentimg);
                            break; // pas de test inutile
                        }
                    }
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {         // si [Alibi/Holmes] sur face 2
                plateau.deplacerDetective(plateau.Holmes);     // on active le déplacement de Holmes
            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Toby - Watson")) {   //si jeton Toby/Watson sélectionné
            if (plateau.jetonsAction.get(i).face==1) {                            // si face 1...
                plateau.deplacerDetective(plateau.Toby);                          // on active déplacement Toby
            }
            if (plateau.jetonsAction.get(i).face==2) {                            // si face 2...
                plateau.deplacerDetective(plateau.Watson);                        // on active le déplacement de Watson
            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Pivot - Echange")) { // si pivot/echange sélectionné
            if (plateau.jetonsAction.get(i).face==1) {                            // si face 1...
                for (int j =1;j<10;j++) {
                    plateau.rotationDistrict(root,j);                             // On active la rotation pour tous les districts
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {                            // si face 2...
                plateau.echangerDistrict();                                       // On active l'échange pour tous les districts
            }
        }
        if (plateau.jetonsAction.get(i).nom.equals((String) "Pivot - Joker")) {   // si pivot/Joker sélectionné
            if (plateau.jetonsAction.get(i).face==1) {                            // si face 1...
                for(int j =1;j<10;j++){
                    plateau.rotationDistrict(root,j);                             // On active la rotation pour tous les districts
                }
            }
            if (plateau.jetonsAction.get(i).face==2) {                            // si face 2...
                plateau.joker();                                                  // on active le déplacement joker
            }
        }
    }

    public void validation(ImageView inspTurn, ImageView jackTurn) throws FileNotFoundException {

        ArrayList<ImageView> waits = new ArrayList<ImageView>(4);

        for (int i = 0; i < 4; i++) {
            waits.add(i,new ImageView(new Image(this.getClass().getResourceAsStream("images/Divers/wait3.png"))));  // pour empêcher le clic sur le jeton le temps de l'action en cours
            waits.get(i).setFitHeight(48);
            waits.get(i).setFitWidth(48);
            waits.get(i).setX(21);
            waits.get(i).setY(183+80*i);
            root.getChildren().add(waits.get(i));
        }

        ImageView next = new ImageView(new Image(this.getClass().getResourceAsStream("images/Divers/white_check.png")));  // bouton de validation de l'action sélectionnée
        next.setFitHeight(50);
        next.setFitWidth(50);
        next.setX(300);
        next.setY(575);
        next.setEffect(new DropShadow(10, Color.WHITE));
        root.getChildren().add(next);

        next.setOnMousePressed(event -> {                    // une fois validé...
            try {
                for (int i = 0; i < 4; i++) {
                    root.getChildren().remove(waits.get(i)); // on retire le cache
                }
                root.getChildren().remove(next);             // le bouton disparais
                affichageJoueur(root,inspTurn,jackTurn);     // affichage du joueur en cours (changement ou non selon le nb de jetons selec.)
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        next.setOnMouseReleased(event -> {                   // On configure le relachement pour empêcher les bugs au moment de l'inspection en séparant les tâches
            if (jetonjoues.get() == 4) {
                try {
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

        ImageView loupe = new ImageView(new Image(this.getClass().getResourceAsStream("images/Divers/loupe.png")));
        loupe.setFitHeight(50);
        loupe.setFitWidth(50);
        loupe.setX(307);
        loupe.setY(575);
        loupe.setEffect(new DropShadow(10, Color.WHITE));
        root.getChildren().add(loupe);

        loupe.setOnMousePressed(event -> {                                  // On clique sur la loupe pour réaliser l'inspection
            try {
                playSound("audio/bell.mp3");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            try {
                plateau.isJackVisible(plateau.districtsVus());             // le programme révèle si Jack est visible

                // Distribution des Jetons Temps:
                plateau.affichageJetonsTemps(root,round);
                if (!plateau.isJackVisible(plateau.districtsVus())) {      // si Jack n'est pas visible...
                    plateau.mrjack.nbSabliers+=1;                          // il remporte le tour
                    plateau.affichageSabliers(root);
                }
                else {
                    plateau.enqueteur.sabliersRecuperes+=1;                // sinon c'est l'enquêteur

                    // On fait disparaitre le jeton temps du tour joué :

                    ImageView img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T"+round+".png")));
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

                plateau.affichageDistricts(root);
                ArrayList<Boolean> status = isGameOver();                   // à chaque inspection on test si la partie est terminée

                if (!status.get(0)) {                                       // si la partie continue...
                    plateau.isJackVisible(plateau.districtsVus());
                    prochainRound();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            root.getChildren().remove(loupe);
        });
    }

    public void prochainRound() throws FileNotFoundException {

        // Relancement des jetons
        for (int i = 0; i < 4; i++) {
            root.getChildren().remove(plateau.jetonsAction.get(i).currentimg);       // On retire les images du tour précédent
        }
        if (round%2!=0) {                                                            // Si on est en round pair...
            for (int i = 0; i < 4; i++) {                                            // On test tous les jetons action
                if(plateau.jetonsAction.get(i).face==1) {                            // face 1 ==> face 2
                    plateau.jetonsAction.get(i).face=2;
                }
                else {                                                               // face 2 ==> face 1
                    plateau.jetonsAction.get(i).face=1;
                }
            }
        }
        else {                                                                       // Si round impair...
            plateau.lancerJetonsAction();                                            // On lance les jetons
        }
        plateau.affichageJetonsAction(root);                                         // Un e fois la face déterminée, il reste plus qu'à afficher

        round1(root);                                                                // On peut rentrer dans le round

    }

    public static boolean doesJackWin() {
        if((plateau.mrjack.nbSabliers>=6) || (round>=8 && !plateau.isJackVisible(plateau.districtsVus()))) {  // Jack gagne s'il possede 6 sabliers ou s'il est invisible après 8 tours de jeu
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean doesEnqueteurWin() {

        // L'enquêteur gagne si jack est le seul visible avant 8 tours de jeu
        int compteur=0;
        for (int i = 0; i < 9; i++) {
            if (plateau.districts.get(i).face==1) {   // on compte l'ensemble des district dont le perso est tjr visible
                compteur+=1;
            }
        }
        if(compteur==1 & plateau.isJackVisible(plateau.districtsVus())) {  // il gagne s'il y en a un et que jack est visible
            return true;
        }
        else {                                                             // sinon il n'a pas encore gagné
            return false;
        }
    }

    public ArrayList<Boolean> isGameOver() throws IOException {
        ArrayList<Boolean> status = new ArrayList<Boolean>(3);

        if (doesJackWin()) {  // Si jack gagne on affiche le menu de fin "victoire de Mr Jack"
            rajouterScore(joueur1); //On renseigne une victoire du joueur1 dans le fichier des scores

            status.add(0, true);
            status.add(1, true);
            status.add(2, false);

            ImageView filtre = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/filtre.png")));
            root.getChildren().remove(filtre);
            root.getChildren().add(filtre);
            FadeTransition fade0 = new FadeTransition(); fade0.setDuration(Duration.millis(1000)); fade0.setFromValue(0.1); fade0.setToValue(10); fade0.setCycleCount(1); fade0.setNode(filtre); fade0.play();

            ImageView win_jack = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/win_jack.png")));
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
        else if (doesEnqueteurWin()) { // Si enquêteur gagne on affiche le menu de fin "victoire de l'enquêteur"

            rajouterScore(joueur2); //On renseigne une victoire du joueur2 dans le fichier des scores

            ImageView filtre = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/Filtre.png")));
            root.getChildren().remove(filtre);
            root.getChildren().add(filtre);
            FadeTransition fade0 = new FadeTransition(); fade0.setDuration(Duration.millis(1000)); fade0.setFromValue(0.1); fade0.setToValue(10); fade0.setCycleCount(1); fade0.setNode(filtre); fade0.play();

            ImageView win_enqueteur = new ImageView(new Image(this.getClass().getResourceAsStream("images/Menu/win_enqueteur.png")));
            root.getChildren().remove(win_enqueteur);
            root.getChildren().add(win_enqueteur);
            FadeTransition fade = new FadeTransition(); fade.setDuration(Duration.millis(1000)); fade.setFromValue(0.1); fade.setToValue(10); fade.setCycleCount(1); fade.setNode(win_enqueteur); fade.play();


            ImageView img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsDetective/Holmes.png")));
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
                    round0(root);

                    //On inverse les noms ce qui inverse les rôles (plus facile comme ca pr les scores par exemple)
                    String temp = null;
                    temp = joueur1.nom;
                    joueur1.nom = joueur2.nom;
                    joueur2.nom = temp;

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
        else {  // sinon la partie n'est pas terminée

            status.add(0, false);
            status.add(1, false);
            status.add(2, false);
            return status;
        }
    }
    public void rajouterScore(Joueur joueur) throws IOException {

        Path path = Paths.get("scores.txt");  // On lis et on écris dans scores.txt (dans le directory donc pas d'écriture pour l'exécutable)
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int c = 0;
        for(int i=0;i<lines.size();i++){
            if(lines.get(i).contains(joueur.nom)){  // si le nom du joueur est déjà présent dans le nom du fichier
                lines.set(i,lines.get(i)+"1");      // On rajoute 1 après le dernier cara de la ligne
                break;
            }
            else if(c== lines.size()-1){   // sinon on rajoute son nom et le 1
                lines.add(joueur.nom+"|  1");
                break;
            }
            c++;
        }
        Files.write(path, lines, StandardCharsets.UTF_8); // On écrit les modif dans le fichier

    }


    public void playSound(String soundPath) throws URISyntaxException {
        Media hit = new Media(this.getClass().getResource(soundPath).toURI().toString());
        AudioClip mediaPlayer = new AudioClip(hit.getSource());
        mediaPlayer.play();
    }
}
