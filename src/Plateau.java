import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Plateau {

    String urlFond = "images/Menu/plateau2.png";

    public MisterJack mrjack = new MisterJack();
    public Enqueteur enqueteur = new Enqueteur(0,0);

    public ArrayList<CarteAlibi> pile_Alibis = new ArrayList<CarteAlibi>(8);

    public ArrayList<District> districts = new ArrayList<District>(9);

    public JetonDetective Holmes;
    public JetonDetective Watson;
    public JetonDetective Toby;

    public ArrayList<JetonAction> jetonsAction = new ArrayList<JetonAction>(4);
    public ArrayList<JetonTemps> jetonsTemps = new ArrayList<JetonTemps>(8);



    public void etatDePartie() {

        // décrit l'état de chacun des éléments en console pour debbugage

        System.out.println("________________________________________________________________\n");

        System.out.println("mrjack.identité.nom="+mrjack.identite.nom+"\n");

        System.out.print("pile_Alibis=[");
        for (int i = 0; i < pile_Alibis.size()-1; i++) {
            System.out.print(pile_Alibis.get(i).nom+",");
        }
        System.out.println(pile_Alibis.get(pile_Alibis.size()-1).nom+"]");

        System.out.print("\njetonsActions=[ ");
        for (int i = 0; i < jetonsAction.size()-1; i++) {
            System.out.print(jetonsAction.get(i).nom+" ("+jetonsAction.get(i).face+")  |  ");
        }
        System.out.println(jetonsAction.get(jetonsAction.size()-1).nom+" ("+jetonsAction.get(jetonsAction.size()-1).face+") ]");

        System.out.println("\ndistricts=");
        for (int i = 0; i < 3; i++) {
            System.out.print("["+districts.get(3*i).nom+","+districts.get(3*i).position+","+districts.get(3*i).orientation+","+districts.get(3*i).face+","+districts.get(3*i).nbChemins+"]   ");
            System.out.print("["+districts.get(3*i+1).nom+","+districts.get(3*i+1).position+","+districts.get(3*i+1).orientation+","+districts.get(3*i+1).face+","+districts.get(3*i+1).nbChemins+"]   ");
            System.out.println("["+districts.get(3*i+2).nom+","+districts.get(3*i+2).position+","+districts.get(3*i+2).orientation+","+districts.get(3*i+2).face+","+districts.get(3*i+2).nbChemins+"]   ");
        }

        System.out.println("\njetonDetectives:");
        System.out.println("Le detective: Sherlock Holmes est en position: "+Holmes.position);
        System.out.println("Le detective: Dr Watson est en position: "+Watson.position);
        System.out.println("Le detective: Toby est en position: "+Toby.position);

        System.out.println("________________________________________________________________");



    }

    /* -----------------------------------------------------------------------------

                        Initialisations des éléments du plateau

   -------------------------------------------------------------------------------- */

    public void initPlateau(){

        // On init l'ensemble des éléments du plateau

        initDistricts();
        initDetectives();
        initPileAlibis();
        initJetonsAction();
        initJetonsTemps();
    }
    public void initDistricts() {

        ArrayList<Integer> temp_IndicesPos = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        District IL = new District(); IL.nom = "IL"; IL.face = 1; District MS = new District(); MS.nom = "MS"; MS.face = 1; District JB = new District(); JB.nom = "JB"; JB.face = 1; District JP = new District(); JP.nom = "JP"; JP.face = 1; District JS = new District(); JS.nom = "JS"; JS.face = 1; District JL = new District(); JL.nom = "JL"; JL.face = 1; District M = new District(); M.nom = "M"; M.face = 1; District SG = new District(); SG.nom = "SG"; SG.face = 1; District WG = new District(); WG.nom = "WG"; WG.face = 1;
        District[] temp_ListeAlibis = {IL,MS,JB,JP,JS,JL,M,SG,WG};

        for (District Alibi : temp_ListeAlibis) { // On parcours tous les district et on leur affecte une numéro au hasard dans une liste de 1 à 9 ---> position

            int randIndicePos = 10;
            while(!temp_IndicesPos.contains(randIndicePos)){
                randIndicePos = new Random().nextInt(9)+1;
            }
            Alibi.position = randIndicePos;
            temp_IndicesPos.remove((Integer) randIndicePos);

            Alibi.orientation = new Random().nextInt(4)+1;

            Alibi.face = 1;

            //Joseph Lane est le seul qui possede 4 chemins au dos
            if(Alibi==JL) {
                Alibi.nbChemins=4;
            }
            else{
                Alibi.nbChemins=3;
            }

        }

        // Les district en face des enquêteurs n'ont pas d'orientation alétoire mais le mur vers l'extérieur
        for (District Alibi : temp_ListeAlibis) {
            if(Alibi.position==1) {
                Alibi.orientation=2;
            }
            if(Alibi.position==3) {
                Alibi.orientation=4;
            }
            if(Alibi.position==8) {
                Alibi.orientation=1;
            }
        }
        // On place les districts dans la liste des districts par odre de position (il sera ainsi très simple d'accèder à un district particulier via la liste)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(temp_ListeAlibis[j].position==i+1){
                    this.districts.add(i, temp_ListeAlibis[j]);
                }
            }
        }
        // On affexte les images
        for (int i = 0; i < 9; i++) {
            districts.get(i).img = new ImageView(new Image(this.getClass().getResourceAsStream("images/Districts/"+districts.get(i).nom+".png")));
            if (districts.get(i).nbChemins==3) {
                districts.get(i).img2 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Districts/3chemins.png")));
            }
            else {
                districts.get(i).img2 = new ImageView(new Image(this.getClass().getResourceAsStream("images/Districts/4chemins.png")));
            }

        }
    }
    public void initDetectives(){

        // On affecte nom, position et images aux jetons detective

        this.Holmes = new JetonDetective("Sherlock Holmes",12);
        Holmes.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsDetective/Holmes.png")));

        this.Watson = new JetonDetective("Dr Watson",4);
        Watson.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsDetective/Watson.png")));

        this.Toby = new JetonDetective("Toby",8);
        Toby.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsDetective/Toby.png")));

    }
    public void initPileAlibis(){

        // même principe que pour l'attribution de la posotion des districts (voir plus haut)

        ArrayList<Integer> temp_IndicesPos = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
        CarteAlibi IL = new CarteAlibi("IL",0,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/IL.png")))); CarteAlibi MS = new CarteAlibi("MS",1,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/MS.png")))); CarteAlibi JB = new CarteAlibi("JB",1,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/JB.png")))); CarteAlibi JP = new CarteAlibi("JP",1,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/JP.png")))); CarteAlibi JS = new CarteAlibi("JS",1,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/JS.png")))); CarteAlibi JL = new CarteAlibi("JL",1,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/JL.png")))); CarteAlibi M = new CarteAlibi("M",2,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/M.png")))); CarteAlibi SG = new CarteAlibi("SG",0,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/SG.png")))); CarteAlibi WG = new CarteAlibi("WG",1,new ImageView(new Image(this.getClass().getResourceAsStream("images/CartesAlibi/WG.png"))));
        ArrayList<CarteAlibi> temp_ListeAlibis = new ArrayList<CarteAlibi>(Arrays.asList(IL,MS,JB,JP,JS,JL,M,SG,WG));

        int temp_randIndice = new Random().nextInt(9);
        mrjack.setIdentite(temp_ListeAlibis.get(temp_randIndice));
        mrjack.setNbSabliers(0);
        temp_ListeAlibis.remove((CarteAlibi) temp_ListeAlibis.get(temp_randIndice));
        temp_IndicesPos.remove(8);

        for (int i = 0; i < 8; i++) {
            temp_randIndice = 10;
            while(!temp_IndicesPos.contains(temp_randIndice)){
                temp_randIndice = new Random().nextInt(8);
            }
            this.pile_Alibis.add(i, temp_ListeAlibis.get(temp_randIndice));
            temp_IndicesPos.remove((Integer) temp_randIndice);
        }
    }
    public void initJetonsAction(){

        // On attribue nom, image et face (via lancerJetonsAction pour la face)

        JetonAction J1 = new JetonAction("Alibi - Holmes"); JetonAction J2 = new JetonAction("Toby - Watson"); JetonAction J3 = new JetonAction("Pivot - Echange"); JetonAction J4 = new JetonAction("Pivot - Joker");
        J1.img1= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Alibi.png"))); J1.img2= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Holmes.png"))); J2.img1= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Toby.png"))); J2.img2= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Watson.png"))); J3.img1= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Pivot.png"))); J3.img2= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Echange.png"))); J4.img1= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Pivot.png"))); J4.img2= new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsAction/Joker.png")));
        jetonsAction.add(0, J1); jetonsAction.add(1, J2); jetonsAction.add(2, J3); jetonsAction.add(3, J4);
        lancerJetonsAction();
    }
    public void initJetonsTemps(){
        // même principe que les jetons action (1 seule face)

        JetonTemps T1 = new JetonTemps(1,Partie.joueur2,1); JetonTemps T2 = new JetonTemps(2,Partie.joueur1,1); JetonTemps T3 = new JetonTemps(3,Partie.joueur2,1); JetonTemps T4 = new JetonTemps(4,Partie.joueur1,1);  JetonTemps T5 = new JetonTemps(5,Partie.joueur2,1); JetonTemps T6 = new JetonTemps(6,Partie.joueur1,1); JetonTemps T7 = new JetonTemps(7,Partie.joueur2,1); JetonTemps T8 = new JetonTemps(8,Partie.joueur1,1);
        T1.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T1.png"))); T2.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T2.png"))); T3.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T3.png"))); T4.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T4.png"))); T5.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T5.png"))); T6.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T6.png"))); T7.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T7.png"))); T8.img = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/T8.png")));
        jetonsTemps.add(0,T1); jetonsTemps.add(1,T2); jetonsTemps.add(2,T3); jetonsTemps.add(3,T4); jetonsTemps.add(4,T5); jetonsTemps.add(5,T6); jetonsTemps.add(6,T7); jetonsTemps.add(7,T8);
    }

    /* -----------------------------------------------------------------------------

                        Affichages des éléments du plateau

   -------------------------------------------------------------------------------- */

    public void affichageFondPlateau(Pane root) {
        // Ajout fond de plateau
        ImageView plateau = new ImageView(new Image(this.getClass().getResourceAsStream(urlFond)));
        root.getChildren().add(plateau);
    }
    public void affichageDistricts(Pane root) throws FileNotFoundException {
        // Ajout 9 districts
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                root.getChildren().remove(districts.get(3*i+j).currentimg);         // on retire les images de la fenêtre pour mise à jour

                if (districts.get(3*i+j).face==1) {                                 // si face 1 ==> image 1
                    districts.get(3*i+j).currentimg = districts.get(3*i+j).img;
                }
                else {                                                              // sinon image 2
                    districts.get(3*i+j).currentimg = districts.get(3*i+j).img2;

                }
                // positionnement
                districts.get(3*i+j).currentimg.setFitHeight(98);
                districts.get(3*i+j).currentimg.setFitWidth(98);
                districts.get(3*i+j).currentimg.setX(178+98*j);
                districts.get(3*i+j).currentimg.setY(178+98*i);
                districts.get(3*i+j).currentimg.setEffect(new DropShadow(10, Color.WHITE));

                // conservation de l'orientation
                switch(districts.get(3*i+j).orientation){
                    case 1:
                        districts.get(3*i+j).currentimg.setRotate(0);
                        break;
                    case 2:
                        districts.get(3*i+j).currentimg.setRotate(90);
                        break;
                    case 3:
                        districts.get(3*i+j).currentimg.setRotate(180);
                        break;
                    case 4:
                        districts.get(3*i+j).currentimg.setRotate(270);
                        break;
                }

                root.getChildren().add(districts.get(3*i+j).currentimg);   // une fois que tout est remis en place, on peut afficher les images mises à jour

            }
        }


    }
    public void affichageDetectives(Pane root) throws FileNotFoundException {
        // On affiche le jeton dans la case qui correspond à sa postion

        ArrayList<JetonDetective> temp_jetonsDetectives = new ArrayList<JetonDetective>(Arrays.asList(Holmes,Watson,Toby));

        for(JetonDetective jeton : temp_jetonsDetectives) {
            ImageView img = jeton.img;
            switch(jeton.position) {
                case 1:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(198);
                    img.setY(100);
                    root.getChildren().add(img);
                    break;
                case 2:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(296);
                    img.setY(100);
                    root.getChildren().add(img);
                    break;
                case 3:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(394);
                    img.setY(100);
                    root.getChildren().add(img);
                    break;
                case 4:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(492);
                    img.setY(198);
                    root.getChildren().add(img);
                    break;
                case 5:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(492);
                    img.setY(296);
                    root.getChildren().add(img);
                    break;
                case 6:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(492);
                    img.setY(394);
                    root.getChildren().add(img);
                    break;
                case 7:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(394);
                    img.setY(492);
                    root.getChildren().add(img);
                    break;
                case 8:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(296);
                    img.setY(492);
                    root.getChildren().add(img);
                    break;
                case 9:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(198);
                    img.setY(492);
                    root.getChildren().add(img);
                    break;
                case 10:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(100);
                    img.setY(394);
                    root.getChildren().add(img);
                    break;
                case 11:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(100);
                    img.setY(292);
                    root.getChildren().add(img);
                    break;
                case 12:
                    img.setFitHeight(58);
                    img.setFitWidth(58);
                    img.setX(100);
                    img.setY(198);
                    root.getChildren().add(img);
                    break;
            }
        }
    }
    public void affichageJetonsAction(Pane root) throws FileNotFoundException {
        // On affiche une image ou l'autre selon la face actuelle (voir Plateau/lancerJetonsActions)

        for (int i = 0; i < 4; i++) {
            switch(jetonsAction.get(i).face) {
                case 1:
                    jetonsAction.get(i).currentimg = jetonsAction.get(i).img1;
                    jetonsAction.get(i).currentimg.setFitHeight(48);
                    jetonsAction.get(i).currentimg.setFitWidth(48);
                    jetonsAction.get(i).currentimg.setX(21);
                    jetonsAction.get(i).currentimg.setY(183+80*i);

                    root.getChildren().add(jetonsAction.get(i).currentimg);
                    FadeTransition fade = new FadeTransition();
                    fade.setDuration(Duration.millis(1000));
                    fade.setFromValue(0.1);
                    fade.setToValue(10);
                    fade.setCycleCount(1);
                    fade.setNode(jetonsAction.get(i).currentimg);
                    fade.play();
                    break;

                case 2:
                    //ImageView img2 = Partie.loadImage2(root,jetonsAction.get(i).image2);
                    jetonsAction.get(i).currentimg = jetonsAction.get(i).img2;
                    jetonsAction.get(i).currentimg.setFitHeight(48);
                    jetonsAction.get(i).currentimg.setFitWidth(48);
                    jetonsAction.get(i).currentimg.setX(21);
                    jetonsAction.get(i).currentimg.setY(183+80*i);

                    root.getChildren().add(jetonsAction.get(i).currentimg);
                    FadeTransition fade2 = new FadeTransition();
                    fade2.setDuration(Duration.millis(1000));
                    fade2.setFromValue(0.1);
                    fade2.setToValue(10);
                    fade2.setCycleCount(1);
                    fade2.setNode(jetonsAction.get(i).currentimg);
                    fade2.play();
                    break;
            }
        }

    }
    public void affichageJetonsTemps(Pane root, int round) throws  FileNotFoundException {

        // si la partie commence, on affiche tous les jetons
        if(round==0){
            for (int i = round; i < 8; i++) {
                jetonsTemps.get(i).currentimg = jetonsTemps.get(i).img;
                jetonsTemps.get(i).currentimg.setFitHeight(83);
                jetonsTemps.get(i).currentimg.setFitWidth(83);
                jetonsTemps.get(i).currentimg.setX(560);
                jetonsTemps.get(i).currentimg.setY(90+56*i);
                root.getChildren().add(jetonsTemps.get(i).currentimg);
            }
        }
        // sinon le jeton du tour actuel disparait (la fonction est appelée en round 0 et en fin de tour)
        else{
            FadeTransition fade = new FadeTransition(Duration.millis(1000));
            fade.setToValue(0);
            fade.setCycleCount(1);
            fade.setNode(jetonsTemps.get(round-1).currentimg);
            fade.play();
        }
    }
    public void affichageSabliers(Pane root) throws  FileNotFoundException {
        // apparition du sablier (si Jack remporte le tour)

        for (int i = 0; i < mrjack.nbSabliers; i++) {
            ImageView sablier = new ImageView(new Image(this.getClass().getResourceAsStream("images/JetonsTemps/Compteur_sablier.png")));
            sablier.setFitHeight(50);
            sablier.setFitWidth(50);
            sablier.setX(80+50*i);
            sablier.setY(10);
            root.getChildren().remove(sablier);
            root.getChildren().add(sablier);
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(1000));
            fade.setFromValue(0.1);
            fade.setToValue(10);
            fade.setCycleCount(1);
            fade.setNode(sablier);
            fade.play();
        }
    }


    /* -----------------------------------------------------------------------------

                                        Actions

   -------------------------------------------------------------------------------- */

    public CarteAlibi piocheAlibi() {
        CarteAlibi carteAlibiPiochee = pile_Alibis.get(pile_Alibis.size()-1);    // on prend la première carte de la pile
        pile_Alibis.remove(pile_Alibis.size()-1);                          // on la retire de la pile
        return carteAlibiPiochee;                                                // on renvoie la carte
    }
    public void echangerDistrict() {

        ArrayList<District> selec = new ArrayList<District>(2);                             // liste des deux districts selectionnés
        AtomicInteger cliques = new AtomicInteger();                                            // atomic pour passer en lambda exp.

        for(District district : districts){                                                     // on parcours tous les districts
            district.currentimg.setOnMouseEntered(e-> {                                         // si la souris passe sur un district il s'entoure d'un halo orange
                district.currentimg.setEffect(new DropShadow(25, Color.ORANGE));
            });
            district.currentimg.setOnMouseExited(e-> {                                          // si elle en sort il redevient comme les autres
                district.currentimg.setEffect(new DropShadow(20, Color.WHITE));
            });
            district.currentimg.setOnMouseClicked(e-> {                                         // si on clique le halo orange est permanent...
                if(cliques.get() <= 2){
                    cliques.getAndIncrement();
                    district.currentimg.setEffect(new DropShadow(25, Color.ORANGE));
                    district.currentimg.setOnMouseEntered(null);
                    district.currentimg.setOnMouseExited(null);
                    district.currentimg.setOnMouseClicked(null);

                    selec.add(district);                                                        // ...et dans ce cas  le district est ajouté à la liste de deux

                }
                if(cliques.get() == 2){                                                         // si on cliquer deux fois sur les districts...
                    try {
                        playSound("audio/switch.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                    for(District dis : districts){                                              // ...on annule la sensibilité à la souris pour tous les districts
                        dis.currentimg.setOnMouseEntered(null);
                        dis.currentimg.setOnMouseExited(null);
                        dis.currentimg.setOnMouseClicked(null);
                    }
                    int tpos = selec.get(0).position;
                    districts.get(selec.get(0).position-1).position = selec.get(1).position;
                    districts.get(selec.get(1).position-1).position = tpos;
                    Collections.swap(districts,selec.get(0).position-1,selec.get(1).position-1);    // On échange les positions

                    double tx = selec.get(0).currentimg.getX();
                    double ty = selec.get(0).currentimg.getY();
                    districts.get(selec.get(0).position-1).currentimg.setX(selec.get(1).currentimg.getX());
                    districts.get(selec.get(0).position-1).currentimg.setY(selec.get(1).currentimg.getY());

                    districts.get(selec.get(1).position-1).currentimg.setX(tx);
                    districts.get(selec.get(1).position-1).currentimg.setY(ty);                         // On échange les positions des images dans la fenêtre

                    districts.get(selec.get(0).position-1).currentimg.setEffect(new DropShadow(20, Color.WHITE));
                    districts.get(selec.get(1).position-1).currentimg.setEffect(new DropShadow(20, Color.WHITE));

                }
            });
        }


    }
    public void rotationDistrict(Pane root,int position){

        ImageView gcheck = new ImageView(new Image(this.getClass().getResourceAsStream("images/Divers/greeCheck.png")));

        // on place le green check
        gcheck.setFitHeight(350.0/15.0);
        gcheck.setFitWidth(350.0/15.0);
        gcheck.setX(districts.get(position-1).currentimg.getX());
        gcheck.setY(districts.get(position-1).currentimg.getY());

        // on place le red cross
        ImageView rcross = new ImageView(new Image(this.getClass().getResourceAsStream("images/Divers/redCross.png")));
        rcross.setFitHeight(350.0/15.0);
        rcross.setFitWidth(350.0/15.0);
        rcross.setX(districts.get(position-1).currentimg.getX());
        rcross.setY(districts.get(position-1).currentimg.getY());

        AtomicInteger compteur = new AtomicInteger(); // atomic pour passer en lambda expression

        // Le district selec. apparait avec un halo orange
        districts.get(position-1).currentimg.setOnMousePressed(e ->{
            districts.get(position-1).currentimg.setEffect(new DropShadow(25, Color.ORANGE));

            // on desactive la sélection pour les autres deistrict des lors qu'un a été cliqué
            for(District district : districts){
                if(district != districts.get(position-1)){
                    district.currentimg.setOnMousePressed(null);
                    district.currentimg.setOnMouseEntered(null);
                    district.currentimg.setOnMouseExited(null);
                }

            }

            compteur.addAndGet(1);

            root.getChildren().remove(gcheck); // on enleve les images pour eviter la dupplicate children error
            root.getChildren().remove(rcross);
            if(e.getClickCount() == 1){        // on évite les bugs au click multiples
                try {
                    playSound("audio/rotation.mp3");
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                }
                RotateTransition rotate = new RotateTransition(); // le district tourne de 90° vers la droite
                rotate.setAxis(Rotate.Z_AXIS);
                rotate.setByAngle(90);
                rotate.setCycleCount(1);
                rotate.setDuration(Duration.millis(500));
                rotate.setNode(districts.get(position-1).currentimg);
                rotate.play();

                // on  modifie les orientations à chaque rotation ainsi une fois la rotation conservée l'état du district enregistré est le bon
                if(districts.get(position-1).orientation == 4){
                    districts.get(position-1).orientation = 1;
                }
                else{
                    districts.get(position-1).orientation += 1;
                }
                if(compteur.get() != 4){
                    root.getChildren().add(gcheck);
                }
                else{
                    root.getChildren().add(rcross);
                    compteur.set(0);
                }
            }

        });

        districts.get(position-1).currentimg.setOnMouseEntered(e ->{
            districts.get(position-1).currentimg.setEffect(new DropShadow(25, Color.ORANGE));
        });

        districts.get(position-1).currentimg.setOnMouseExited(e ->{
            districts.get(position-1).currentimg.setEffect(new DropShadow(20, Color.WHITE));
        });

        gcheck.setOnMouseClicked(e->{
            districts.get(position-1).currentimg.setOnMouseEntered(null);
            districts.get(position-1).currentimg.setOnMousePressed(null);
            districts.get(position-1).currentimg.setOnMouseExited(null);
            districts.get(position-1).currentimg.setEffect(new DropShadow(20, Color.WHITE));
            root.getChildren().remove(gcheck);
            root.getChildren().remove(rcross);
        });
    }
    public void deplacerDetective(JetonDetective jeton) {

        ArrayList<JetonDetective> jetons = new ArrayList<JetonDetective>(Arrays.asList(Holmes,Watson,Toby));
        jetons.remove((JetonDetective) jeton);
        ArrayList<Integer> positions = new ArrayList<Integer>(Arrays.asList(Holmes.position,Watson.position,Toby.position));
        positions.remove((Integer) jeton.position);

        int indiceSuperposition = 1;
        if (positions.get(0)==positions.get(1)) {
            indiceSuperposition=2;
        }
        int finalIndiceSuperposition = indiceSuperposition;

        //Coordonnées initiales du jeton
        double initX = jeton.img.getX();
        double initY = jeton.img.getY();

        // drag & drop l'objet suit les coordonnées de la souris à 20p de décalage pour un mouvement plus naturel
        jeton.img.setOnMouseDragged(e -> {
            //Le centre du jeton suit les mouvements de la souris
            jeton.img.setX(e.getX() - 20);
            jeton.img.setY(e.getY() - 20);

        });

        jeton.img.setOnMouseReleased(e -> { // une fois le jeton relaché il se place dans la case autorisée la plus proche de la zone ou il est laché, ou il revient en position de départ
            if (jeton.position == 1) {
                if ((280 < e.getX() && e.getX() < 382) && (0 < e.getY() && e.getY() < 178)) {
                    if(jetons.get(0).position == 2 || jetons.get(1).position == 2){
                        jeton.img.setX(296);
                        jeton.img.setY(100-8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }

                    }
                    else{
                        jeton.img.setX(296);
                        jeton.img.setY(100);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 2;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((384 < e.getX() && e.getX() < 650) && (0 < e.getY() && e.getY() < 178)) {
                    if(jetons.get(0).position == 3 || jetons.get(1).position == 3){
                        jeton.img.setX(394);
                        jeton.img.setY(100-8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(394);
                        jeton.img.setY(100);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 3;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 2) {
                if ((384 < e.getX() && e.getX() < 650) && (0 < e.getY() && e.getY() < 178)) {
                    if(jetons.get(0).position == 3 || jetons.get(1).position == 3){
                        jeton.img.setX(394);
                        jeton.img.setY(100-8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(394);
                        jeton.img.setY(100);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 3;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                }
                else if ((178 < e.getY() && e.getY() < 280) && (384 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 4 || jetons.get(1).position == 4){
                        jeton.img.setX(492+8*finalIndiceSuperposition);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(492);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 4;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 3) {
                if ((178 < e.getY() && e.getY() < 280) && (384 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 4 || jetons.get(1).position == 4){
                        jeton.img.setX(492+8*finalIndiceSuperposition);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(492);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 4;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((280 < e.getY() && e.getY() < 382) && (384 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 5 || jetons.get(1).position == 5){
                        jeton.img.setX(492+8*finalIndiceSuperposition);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(492);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 5;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 4) {
                if ((280 < e.getY() && e.getY() < 382) && (384 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 5 || jetons.get(1).position == 5){
                        jeton.img.setX(492+8*finalIndiceSuperposition);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(492);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 5;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((382 < e.getY() && e.getY() < 650) && (384 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 6 || jetons.get(1).position == 6){
                        jeton.img.setX(492+8*finalIndiceSuperposition);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(492);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 6;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 5) {
                if ((382 < e.getY() && e.getY() < 472) && (472 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 6 || jetons.get(1).position == 6){
                        jeton.img.setX(492+8*finalIndiceSuperposition);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(492);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 6;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((452 < e.getY() && e.getY() < 650) && (372 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 7 || jetons.get(1).position == 7){
                        jeton.img.setX(394);
                        jeton.img.setY(492+8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(394);
                        jeton.img.setY(492);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 7;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 6) {
                if ((472 < e.getY() && e.getY() < 650) && (374 < e.getX() && e.getX() < 650)) {
                    if(jetons.get(0).position == 7 || jetons.get(1).position == 7){
                        jeton.img.setX(394);
                        jeton.img.setY(492+8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(394);
                        jeton.img.setY(492);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 7;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((472 < e.getY() && e.getY() < 650) && (280 < e.getX() && e.getX() < 374)) {
                    if(jetons.get(0).position == 8 || jetons.get(1).position == 8){
                        jeton.img.setX(296);
                        jeton.img.setY(492+8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(296);
                        jeton.img.setY(492);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 8;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 7) {
                if ((472 < e.getY() && e.getY() < 650) && (280 < e.getX() && e.getX() < 374)) {
                    if(jetons.get(0).position == 8 || jetons.get(1).position == 8){
                        jeton.img.setX(296);
                        jeton.img.setY(492+8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(296);
                        jeton.img.setY(492);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 8;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((472 < e.getY() && e.getY() < 650) && (0 < e.getX() && e.getX() < 280)) {
                    if(jetons.get(0).position == 9 || jetons.get(1).position == 9){
                        jeton.img.setX(198);
                        jeton.img.setY(492+8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(198);
                        jeton.img.setY(492);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 9;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 8) {
                if ((472 < e.getY() && e.getY() < 650) && (0 < e.getX() && e.getX() < 280)) {
                    if(jetons.get(0).position == 9 || jetons.get(1).position == 9){
                        jeton.img.setX(198);
                        jeton.img.setY(492+8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(198);
                        jeton.img.setY(492);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 9;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((0 < e.getX() && e.getX() < 178) && (374 < e.getY() && e.getY() < 650)) {
                    if(jetons.get(0).position == 10 || jetons.get(1).position == 10){
                        jeton.img.setX(100-8*finalIndiceSuperposition);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(100);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 10;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 9) {
                if ((0 < e.getX() && e.getX() < 178) && (374 < e.getY() && e.getY() < 650)) {
                    if(jetons.get(0).position == 10 || jetons.get(1).position == 10){
                        jeton.img.setX(100-8*finalIndiceSuperposition);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(100);
                        jeton.img.setY(394);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 10;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((0 < e.getX() && e.getX() < 178) && (280 < e.getY() && e.getY() < 374)) {
                    if(jetons.get(0).position == 11 || jetons.get(1).position == 11){
                        jeton.img.setX(100-8*finalIndiceSuperposition);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(100);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 11;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 10) {
                if ((0 < e.getX() && e.getX() < 178) && (280 < e.getY() && e.getY() < 374)) {
                    if(jetons.get(0).position == 11 || jetons.get(1).position == 11){
                        jeton.img.setX(100-8*finalIndiceSuperposition);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(100);
                        jeton.img.setY(296);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 11;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((0 < e.getX() && e.getX() < 178) && (78 < e.getY() && e.getY() < 280)) {
                    if(jetons.get(0).position == 12 || jetons.get(1).position == 12){
                        jeton.img.setX(100-8*finalIndiceSuperposition);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(100);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 12;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 11) {
                if ((0 < e.getX() && e.getX() < 178) && (78 < e.getY() && e.getY() < 280)) {
                    if(jetons.get(0).position == 12 || jetons.get(1).position == 12){
                        jeton.img.setX(100-8*finalIndiceSuperposition);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(100);
                        jeton.img.setY(198);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 12;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((0 < e.getX() && e.getX() < 280) && (0 < e.getY() && e.getY() < 178)) {
                    if(jetons.get(0).position == 1 || jetons.get(1).position == 1){
                        jeton.img.setX(198);
                        jeton.img.setY(100-8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(198);
                        jeton.img.setY(100);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 1;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }

            else if (jeton.position == 12) {
                if ((0 < e.getX() && e.getX() < 280) && (0 < e.getY() && e.getY() < 178)) {
                    if(jetons.get(0).position == 1 || jetons.get(1).position == 1){
                        jeton.img.setX(198);
                        jeton.img.setY(100-8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(198);
                        jeton.img.setY(100);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 1;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else if ((280 < e.getX() && e.getX() < 382) && (0 < e.getY() && e.getY() < 178)) {
                    if(jetons.get(0).position == 2 || jetons.get(1).position == 2){
                        jeton.img.setX(296);
                        jeton.img.setY(100-8*finalIndiceSuperposition);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    else{
                        jeton.img.setX(296);
                        jeton.img.setY(100);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                    jeton.position = 2;
                    jeton.img.setOnMouseDragged(null);
                    jeton.img.setOnMouseReleased(null);
                } else {
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                    try {
                        playSound("audio/place.mp3");
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                    }
                }
            }
        });
    }
    public void joker(){

        // même principe que deplacerDetective appliqué à tous les jetons avec seulement une case de déplacement autorisée

        ArrayList<JetonDetective> jetonss = new ArrayList<JetonDetective>(Arrays.asList(Holmes,Watson,Toby));

        for (JetonDetective jeton : jetonss) {
            ArrayList<JetonDetective> jetons = new ArrayList<JetonDetective>(Arrays.asList(Holmes,Watson,Toby));
            jetons.remove((JetonDetective) jeton);
            ArrayList<Integer> positions = new ArrayList<Integer>(Arrays.asList(Holmes.position,Watson.position,Toby.position));
            positions.remove((Integer) jeton.position);

            int indiceSuperposition = 1;
            if (positions.get(0)==positions.get(1)) {
                indiceSuperposition=2;
            }
            int finalIndiceSuperposition = indiceSuperposition;

            //Coordonnées initiales du jeton
            double initX = jeton.img.getX();
            double initY = jeton.img.getY();

            jeton.img.setOnMouseDragged(e -> {
                //Le centre du jeton suit les mouvements de la souris
                jeton.img.setX(e.getX() - 20);
                jeton.img.setY(e.getY() - 20);

            });

            jeton.img.setOnMouseReleased(e -> {
                if (jeton.position == 1) {
                    if ((280 < e.getX() && e.getX() < 382) && (0 < e.getY() && e.getY() < 178)) {
                        if(jetons.get(0).position == 2 || jetons.get(1).position == 2){
                            jeton.img.setX(296);
                            jeton.img.setY(100-8*finalIndiceSuperposition);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(296);
                            jeton.img.setY(100);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 2;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);

                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 2) {
                    if ((384 < e.getX() && e.getX() < 650) && (0 < e.getY() && e.getY() < 178)) {
                        if(jetons.get(0).position == 3 || jetons.get(1).position == 3){
                            jeton.img.setX(394);
                            jeton.img.setY(100-8*finalIndiceSuperposition);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(394);
                            jeton.img.setY(100);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 3;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    }
                    else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 3) {
                    if ((178 < e.getY() && e.getY() < 280) && (384 < e.getX() && e.getX() < 650)) {
                        if(jetons.get(0).position == 4 || jetons.get(1).position == 4){
                            jeton.img.setX(492+8*finalIndiceSuperposition);
                            jeton.img.setY(198);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(492);
                            jeton.img.setY(198);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 4;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 4) {
                    if ((280 < e.getY() && e.getY() < 382) && (384 < e.getX() && e.getX() < 650)) {
                        if(jetons.get(0).position == 5 || jetons.get(1).position == 5){
                            jeton.img.setX(492+8*finalIndiceSuperposition);
                            jeton.img.setY(296);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(492);
                            jeton.img.setY(296);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 5;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 5) {
                    if ((382 < e.getY() && e.getY() < 472) && (472 < e.getX() && e.getX() < 650)) {
                        if(jetons.get(0).position == 6 || jetons.get(1).position == 6){
                            jeton.img.setX(492+8*finalIndiceSuperposition);
                            jeton.img.setY(394);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(492);
                            jeton.img.setY(394);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 6;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 6) {
                    if ((472 < e.getY() && e.getY() < 650) && (374 < e.getX() && e.getX() < 650)) {
                        if(jetons.get(0).position == 7 || jetons.get(1).position == 7){
                            jeton.img.setX(394);
                            jeton.img.setY(492+8*finalIndiceSuperposition);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(394);
                            jeton.img.setY(492);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 7;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 7) {
                    if ((472 < e.getY() && e.getY() < 650) && (280 < e.getX() && e.getX() < 374)) {
                        if(jetons.get(0).position == 8 || jetons.get(1).position == 8){
                            jeton.img.setX(296);
                            jeton.img.setY(492+8*finalIndiceSuperposition);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(296);
                            jeton.img.setY(492);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 8;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 8) {
                    if ((472 < e.getY() && e.getY() < 650) && (0 < e.getX() && e.getX() < 280)) {
                        if(jetons.get(0).position == 9 || jetons.get(1).position == 9){
                            jeton.img.setX(198);
                            jeton.img.setY(492+8*finalIndiceSuperposition);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(198);
                            jeton.img.setY(492);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 9;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 9) {
                    if ((0 < e.getX() && e.getX() < 178) && (374 < e.getY() && e.getY() < 650)) {
                        if(jetons.get(0).position == 10 || jetons.get(1).position == 10){
                            jeton.img.setX(100-8*finalIndiceSuperposition);
                            jeton.img.setY(394);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(100);
                            jeton.img.setY(394);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 10;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 10) {
                    if ((0 < e.getX() && e.getX() < 178) && (280 < e.getY() && e.getY() < 374)) {
                        if(jetons.get(0).position == 11 || jetons.get(1).position == 11){
                            jeton.img.setX(100-8*finalIndiceSuperposition);
                            jeton.img.setY(296);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(100);
                            jeton.img.setY(296);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 11;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 11) {
                    if ((0 < e.getX() && e.getX() < 178) && (78 < e.getY() && e.getY() < 280)) {
                        if(jetons.get(0).position == 12 || jetons.get(1).position == 12){
                            jeton.img.setX(100-8*finalIndiceSuperposition);
                            jeton.img.setY(198);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(100);
                            jeton.img.setY(198);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 12;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }

                else if (jeton.position == 12) {
                    if ((0 < e.getX() && e.getX() < 280) && (0 < e.getY() && e.getY() < 178)) {
                        if(jetons.get(0).position == 1 || jetons.get(1).position == 1){
                            jeton.img.setX(198);
                            jeton.img.setY(100-8*finalIndiceSuperposition);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        else{
                            jeton.img.setX(198);
                            jeton.img.setY(100);
                            try {
                                playSound("audio/place.mp3");
                            } catch (URISyntaxException uriSyntaxException) {
                                uriSyntaxException.printStackTrace();
                            }
                        }
                        jeton.position = 1;
                        jeton.img.setOnMouseDragged(null);
                        jeton.img.setOnMouseReleased(null);
                        jetons.get(0).img.setOnMouseDragged(null);
                        jetons.get(0).img.setOnMouseReleased(null);
                        jetons.get(1).img.setOnMouseDragged(null);
                        jetons.get(1).img.setOnMouseReleased(null);
                    } else {
                        jeton.img.setX(initX);
                        jeton.img.setY(initY);
                        try {
                            playSound("audio/place.mp3");
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }
            });
        }
    }



    /* -----------------------------------------------------------------------------

                             Différentes méthodes de jeu

   -------------------------------------------------------------------------------- */
    
    public void lancerJetonsAction() {

        //Attribution à chaque début de tour d'une face aléatoire qui correspond au lancer des jetons

        ArrayList<Integer> temp_IndicesPos = new ArrayList<>(Arrays.asList(0,1,2,3));
        ArrayList<JetonAction> temp_jetonsAction = new ArrayList<JetonAction>(4);

        // principe similaire aux fonctions d'init aléatoire (voir plus haut)
        for (int i = 0; i < 4; i++) {
            int temp_randIndice = 5;
            while(! temp_IndicesPos.contains(temp_randIndice)) {
                temp_randIndice = new Random().nextInt(4);
            }
            temp_jetonsAction.add(i, this.jetonsAction.get(temp_randIndice));
            temp_IndicesPos.remove((Integer) temp_randIndice);
        }
        // attribution de la face
        for (int i = 0; i < 4; i++) {
            jetonsAction.set(i, temp_jetonsAction.get(i));
            jetonsAction.get(i).face = new Random().nextInt(2)+1;
        }
    }
    public void voirIdMrJack(Pane root){

        // l'identité de Jack est consultable enc lqiuant sur le personnage en haut à gauche
        ImageView img = mrjack.identite.img;
        Button voir = new Button(""); // bouton quasi transparent (inutilisable si totalement transparent) pour révéler la carte
        voir.setLayoutX(10);
        voir.setLayoutY(10);
        voir.setMinSize(50,50);
        voir.setStyle( "-fx-background-color: transparent ; -fx-border-color: transparent");
        root.getChildren().add(voir);

        voir.setOnMousePressed(e ->{  // si bouton enfoncé
            img.setX(10);
            img.setY(8);
            img.setFitHeight(325.0/5.5);
            img.setFitWidth(200.0/5.5);
            root.getChildren().add(img);   // l'image apparrait en petit par dessus

        });
        voir.setOnMouseReleased(e ->{
            root.getChildren().remove(img);  // elle disparait lorqu'on relache le clique
        });

    }
    public ArrayList districtsVus() {

        /* Selon la position du district, son orientation et la position des detectives on peut savoir si le personnage
        d'un district est visible ou non en procédant par disjonction de cas. C'est le rôle de cette fonction on ne commente
        pas chacun des cas détaillés pour des raisons évidentes. */

        int[] temp_positionsDetectives = {Holmes.position,Watson.position,Toby.position};
        ArrayList<District> districtsVisibles = new ArrayList<District>();
        ArrayList<District> districtsNonVisibles = (ArrayList) districts.clone();
        for (int position : temp_positionsDetectives) {
            switch(position) {
                case 1:
                    if (districts.get(0).orientation == 1 || districts.get(0).orientation == 2 || districts.get(0).orientation == 4 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                        if(this.districts.get(0).face == 1 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(0));
                            districtsNonVisibles.remove((District) districts.get(0));
                        }
                        if (districts.get(0).orientation == 2 || districts.get(0).orientation == 4 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                            if (districts.get(3).orientation == 1 || districts.get(3).orientation == 2 || districts.get(3).orientation == 4 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                if (this.districts.get(3).face == 1 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(3));
                                    districtsNonVisibles.remove((District) districts.get(3));
                                }
                            }
                            if (districts.get(3).orientation == 2 || districts.get(3).orientation == 4 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                if (districts.get(6).orientation == 1 || districts.get(6).orientation == 2 || districts.get(6).orientation == 4 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                                    if (this.districts.get(6).face == 1 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(6));
                                        districtsNonVisibles.remove((District) districts.get(6));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    if (districts.get(1).orientation == 1 || districts.get(1).orientation == 2 || districts.get(1).orientation == 4 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                        if(this.districts.get(1).face == 1 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(1));
                            districtsNonVisibles.remove((District) districts.get(1));
                        }
                        if (districts.get(1).orientation == 2 || districts.get(1).orientation == 4 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 2 || districts.get(4).orientation == 4 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (this.districts.get(4).face == 1 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 2 || districts.get(4).orientation == 4 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (districts.get(7).orientation == 1 || districts.get(7).orientation == 2 || districts.get(7).orientation == 4 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                    if (this.districts.get(7).face == 1 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(7));
                                        districtsNonVisibles.remove((District) districts.get(7));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    if (districts.get(2).orientation == 1 || districts.get(2).orientation == 2 || districts.get(2).orientation == 4 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                        if(this.districts.get(2).face == 1 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(2));
                            districtsNonVisibles.remove((District) districts.get(2));
                        }
                        if (districts.get(2).orientation == 2 || districts.get(2).orientation == 4 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                            if (districts.get(5).orientation == 1 || districts.get(5).orientation == 2 || districts.get(5).orientation == 4 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                if (this.districts.get(5).face == 1 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(5));
                                    districtsNonVisibles.remove((District) districts.get(5));
                                }
                            }
                            if (districts.get(5).orientation == 2 || districts.get(5).orientation == 4 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                if (districts.get(8).orientation == 1 || districts.get(8).orientation == 2 || districts.get(8).orientation == 4 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                                    if (this.districts.get(8).face == 1 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(8));
                                        districtsNonVisibles.remove((District) districts.get(8));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if (districts.get(2).orientation == 1 || districts.get(2).orientation == 2 || districts.get(2).orientation == 3 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                        if(this.districts.get(2).face == 1 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(2));
                            districtsNonVisibles.remove((District) districts.get(2));
                        }
                        if (districts.get(2).orientation == 1 || districts.get(2).orientation == 3 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 2 || districts.get(1).orientation == 3 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                if (this.districts.get(1).face == 1 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(1));
                                    districtsNonVisibles.remove((District) districts.get(1));
                                }
                            }
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 3 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                if (districts.get(0).orientation == 1 || districts.get(0).orientation == 2 || districts.get(0).orientation == 3 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                                    if (this.districts.get(0).face == 1 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(0));
                                        districtsNonVisibles.remove((District) districts.get(0));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    if (districts.get(5).orientation == 1 || districts.get(5).orientation == 2 || districts.get(5).orientation == 3 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                        if(this.districts.get(5).face == 1 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(5));
                            districtsNonVisibles.remove((District) districts.get(5));
                        }
                        if (districts.get(5).orientation == 1 || districts.get(5).orientation == 3 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 2 || districts.get(4).orientation == 3 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (this.districts.get(4).face == 1 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 3 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (districts.get(3).orientation == 1 || districts.get(3).orientation == 2 || districts.get(3).orientation == 3 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                    if (this.districts.get(3).face == 1 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(3));
                                        districtsNonVisibles.remove((District) districts.get(3));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 6:
                    if (districts.get(8).orientation == 1 || districts.get(8).orientation == 2 || districts.get(8).orientation == 3 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                        if(this.districts.get(8).face == 1 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(8));
                            districtsNonVisibles.remove((District) districts.get(8));
                        }
                        if (districts.get(8).orientation == 1 || districts.get(8).orientation == 3 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 2 || districts.get(7).orientation == 3 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                if (this.districts.get(7).face == 1 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(7));
                                    districtsNonVisibles.remove((District) districts.get(7));
                                }
                            }
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 3 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                if (districts.get(6).orientation == 1 || districts.get(6).orientation == 2 || districts.get(6).orientation == 3 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                                    if (this.districts.get(6).face == 1 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(6));
                                        districtsNonVisibles.remove((District) districts.get(6));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 7:
                    if (districts.get(8).orientation == 2 || districts.get(8).orientation == 3 || districts.get(8).orientation == 4 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                        if(this.districts.get(8).face == 1 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(8));
                            districtsNonVisibles.remove((District) districts.get(8));
                        }
                        if (districts.get(8).orientation == 2 || districts.get(8).orientation == 4 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                            if (districts.get(5).orientation == 2 || districts.get(5).orientation == 3 || districts.get(5).orientation == 4 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                if (this.districts.get(5).face == 1 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(5));
                                    districtsNonVisibles.remove((District) districts.get(5));
                                }
                            }
                            if (districts.get(5).orientation == 2 || districts.get(5).orientation == 4 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                if (districts.get(2).orientation == 2 || districts.get(2).orientation == 3 || districts.get(2).orientation == 4 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                                    if (this.districts.get(2).face == 1 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(2));
                                        districtsNonVisibles.remove((District) districts.get(2));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 8:
                    if (districts.get(7).orientation == 2 || districts.get(7).orientation == 3 || districts.get(7).orientation == 4 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                        if(this.districts.get(7).face == 1 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(7));
                            districtsNonVisibles.remove((District) districts.get(7));
                        }
                        if (districts.get(7).orientation == 2 || districts.get(7).orientation == 4 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                            if (districts.get(4).orientation == 2 || districts.get(4).orientation == 3 || districts.get(4).orientation == 4 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (this.districts.get(4).face == 1 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 2 || districts.get(4).orientation == 4 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (districts.get(1).orientation == 2 || districts.get(1).orientation == 3 || districts.get(1).orientation == 4 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                    if (this.districts.get(1).face == 1 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(1));
                                        districtsNonVisibles.remove((District) districts.get(1));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 9:
                    if (districts.get(6).orientation == 2 || districts.get(6).orientation == 3 || districts.get(6).orientation == 4 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                        if(this.districts.get(6).face == 1 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(6));
                            districtsNonVisibles.remove((District) districts.get(6));
                        }
                        if (districts.get(6).orientation == 2 || districts.get(6).orientation == 4 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                            if (districts.get(3).orientation == 2 || districts.get(3).orientation == 3 || districts.get(3).orientation == 4 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                if (this.districts.get(3).face == 1 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(3));
                                    districtsNonVisibles.remove((District) districts.get(3));
                                }
                            }
                            if (districts.get(3).orientation == 2 || districts.get(3).orientation == 4 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                                if (districts.get(0).orientation == 2 || districts.get(0).orientation == 3 || districts.get(0).orientation == 4 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                                    if (this.districts.get(0).face == 1 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(0));
                                        districtsNonVisibles.remove((District) districts.get(0));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 10:
                    if (districts.get(6).orientation == 1 || districts.get(6).orientation == 3 || districts.get(6).orientation == 4 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                        if(this.districts.get(6).face == 1 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(6));
                            districtsNonVisibles.remove((District) districts.get(6));
                        }
                        if (districts.get(6).orientation == 1 || districts.get(6).orientation == 3 || (districts.get(6).face==2 && districts.get(6).nbChemins==4)) {
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 3 || districts.get(7).orientation == 4 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                if (this.districts.get(7).face == 1 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(7));
                                    districtsNonVisibles.remove((District) districts.get(7));
                                }
                            }
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 3 || (districts.get(7).face==2 && districts.get(7).nbChemins==4)) {
                                if (districts.get(8).orientation == 1 || districts.get(8).orientation == 3 || districts.get(8).orientation == 4 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                                    if (this.districts.get(8).face == 1 || (districts.get(8).face==2 && districts.get(8).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(8));
                                        districtsNonVisibles.remove((District) districts.get(8));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 11:
                    if (districts.get(3).orientation == 1 || districts.get(3).orientation == 3 || districts.get(3).orientation == 4 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                        if(this.districts.get(3).face == 1 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(3));
                            districtsNonVisibles.remove((District) districts.get(3));
                        }
                        if (districts.get(3).orientation == 1 || districts.get(3).orientation == 3 || (districts.get(3).face==2 && districts.get(3).nbChemins==4)) {
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 3 || districts.get(4).orientation == 4 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (this.districts.get(4).face == 1 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 3 || (districts.get(4).face==2 && districts.get(4).nbChemins==4)) {
                                if (districts.get(5).orientation == 1 || districts.get(5).orientation == 3 || districts.get(5).orientation == 4 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                    if (this.districts.get(5).face == 1 || (districts.get(5).face==2 && districts.get(5).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(5));
                                        districtsNonVisibles.remove((District) districts.get(5));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 12:
                    if (districts.get(0).orientation == 1 || districts.get(0).orientation == 3 || districts.get(0).orientation == 4 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                        if(this.districts.get(0).face == 1 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                            districtsVisibles.add((District) districts.get(0));
                            districtsNonVisibles.remove((District) districts.get(0));
                        }
                        if (districts.get(0).orientation == 1 || districts.get(0).orientation == 3 || (districts.get(0).face==2 && districts.get(0).nbChemins==4)) {
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 3 || districts.get(1).orientation == 4 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                if (this.districts.get(1).face == 1 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                    districtsVisibles.add((District) districts.get(1));
                                    districtsNonVisibles.remove((District) districts.get(1));
                                }
                            }
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 3 || (districts.get(1).face==2 && districts.get(1).nbChemins==4)) {
                                if (districts.get(2).orientation == 1 || districts.get(2).orientation == 3 || districts.get(2).orientation == 4 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                                    if (this.districts.get(2).face == 1 || (districts.get(2).face==2 && districts.get(2).nbChemins==4)) {
                                        districtsVisibles.add((District) districts.get(2));
                                        districtsNonVisibles.remove((District) districts.get(2));
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }

        ArrayList<ArrayList<District>> districtsVus = new ArrayList<ArrayList<District>>(2);

        districtsVus.add(districtsVisibles);
        districtsVus.add(districtsNonVisibles);

        return districtsVus;

    }
    public boolean isJackVisible(ArrayList<ArrayList<District>> districtsVus) {

        ArrayList<District> districtsVisibles = districtsVus.get(0);
        ArrayList<District> districtsNonVisibles = districtsVus.get(1);

        int indiceVisibilite=0;
        for (int i = 0; i < districtsVisibles.size(); i++) {                   // pour l'ensemble des personnages qui sont visibles
            if (mrjack.identite.nom.equals(districtsVisibles.get(i).nom)) {    // si Jack y figure, on le note
                indiceVisibilite = 1;
                break;
            }
        }

        switch(indiceVisibilite) {                                            // selon le résultat du test précédent, on retourne ou non des districts
            case 0:
                for(District district : districtsVisibles) {
                    district.face=2;
                }
                return false;
            case 1:
                for(District district : districtsNonVisibles) {
                    district.face=2;
                }
                return true;
        }
        return false;
    }

    public void playSound(String soundPath) throws URISyntaxException {
        Media hit = new Media(this.getClass().getResource(soundPath).toURI().toString());
        AudioClip mediaPlayer = new AudioClip(hit.getSource());
        mediaPlayer.play();
    }

}