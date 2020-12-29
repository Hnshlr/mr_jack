import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Plateau {

    public static MisterJack mrjack = new MisterJack();
    public static Enqueteur enqueteur = new Enqueteur();

    public ArrayList<CarteAlibi> pile_Alibis = new ArrayList<CarteAlibi>(8);

    public ArrayList<District> districts = new ArrayList<District>(9);

    public JetonDetective Holmes;
    public JetonDetective Watson;
    public JetonDetective Toby;

    public ArrayList<JetonAction> jetonsAction = new ArrayList<JetonAction>(4);


    public void initPlateau(Scene scene, Pane root) throws FileNotFoundException {
        initDistricts();
        initDetectives();
        initPileAlibis();
        initJetonsAction();

        affichagePlateau(scene,root);
        affichageDistricts(scene,root);
        affichageDetectives(scene,root);
        affichageJetonsAction(scene,root);
        //toute la mise en place du jeu
    }

    public void etatDePartie() {
        System.out.println("mrjack.identité.nom="+mrjack.identité.nom+"\n");

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
    }

    public void affichagePlateau(Scene scene, Pane root) throws FileNotFoundException {
        // Ajout fond de plateau
        ImageView plateau = Partie.loadImage2(root,new FileInputStream("images\\Menu\\plateau.png"));
        root.getChildren().add(plateau);
    }
    public void affichageDistricts(Scene scene, Pane root) throws FileNotFoundException {
        // Ajout 9 districts
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView img = Partie.loadImage2(root,districts.get(3*i+j).image);
                img.setFitHeight(98);
                img.setFitWidth(98);
                img.setX(178+98*j);
                img.setY(178+98*i);
                switch(districts.get(3*i+j).orientation){
                    case 2:
                        img.setRotate(img.getRotate() + 90);
                        break;
                    case 3:
                        img.setRotate(img.getRotate() + 180);
                        break;
                    case 4:
                        img.setRotate(img.getRotate() + 270);
                        break;
                }
                root.getChildren().add(img);
            }
        }
    }
    public void affichageDetectives(Scene scene, Pane root) throws FileNotFoundException {
        ArrayList<JetonDetective> temp_jetonsDetectives = new ArrayList<JetonDetective>(Arrays.asList(Holmes,Watson,Toby));
        for(JetonDetective jeton : temp_jetonsDetectives) {
            //ImageView img = Partie.loadImage2(root,jeton.image);
            ImageView img =jeton.img;
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
    public void affichageJetonsAction(Scene scene, Pane root) throws FileNotFoundException {
        for (int i = 0; i < 4; i++) {
            switch(jetonsAction.get(i).face) {
                case 1:
                    ImageView img1 = Partie.loadImage2(root,jetonsAction.get(i).image1);
                    img1.setFitHeight(48);
                    img1.setFitWidth(48);
                    img1.setX(21);
                    img1.setY(120+122*i);
                    root.getChildren().add(img1);
                    break;
                case 2:
                    ImageView img2 = Partie.loadImage2(root,jetonsAction.get(i).image2);
                    img2.setFitHeight(48);
                    img2.setFitWidth(48);
                    img2.setX(21);
                    img2.setY(120+122*i);
                    root.getChildren().add(img2);
                    break;
            }
        }
    }

    public void initDistricts() throws FileNotFoundException {
        ArrayList<Integer> temp_IndicesPos = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        District IL = new District(); IL.nom = "IL"; IL.face = 1; District MS = new District(); MS.nom = "MS"; MS.face = 1; District JB = new District(); JB.nom = "JB"; JB.face = 1; District JP = new District(); JP.nom = "JP"; JP.face = 1; District JS = new District(); JS.nom = "JS"; JS.face = 1; District JL = new District(); JL.nom = "JL"; JL.face = 1; District M = new District(); M.nom = "M"; M.face = 1; District SG = new District(); SG.nom = "SG"; SG.face = 1; District WG = new District(); WG.nom = "WG"; WG.face = 1;
        District[] temp_ListeAlibis = {IL,MS,JB,JP,JS,JL,M,SG,WG};
        for (District Alibi : temp_ListeAlibis) {

            int randIndicePos = 10;
            while(!temp_IndicesPos.contains(randIndicePos)){
                randIndicePos = new Random().nextInt(9)+1;
            }
            Alibi.position = randIndicePos;
            temp_IndicesPos.remove((Integer) randIndicePos);

            Alibi.orientation = new Random().nextInt(4)+1;

            Alibi.face = 1;

            if(Alibi==IL) {
                Alibi.nbChemins=4;
            }
            else{
                Alibi.nbChemins=3;
            }

        }

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
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(temp_ListeAlibis[j].position==i+1){
                    this.districts.add(i, temp_ListeAlibis[j]);
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            districts.get(i).image = new FileInputStream("images\\Districts\\"+districts.get(i).nom+".png");
        }
    }
    public void initDetectives() throws FileNotFoundException {
        this.Holmes = new JetonDetective("Sherlock Holmes",12);
        Holmes.image = new FileInputStream("images\\JetonsDetective\\Holmes.png");
        Holmes.img = new ImageView(new Image(Holmes.image));

        this.Watson = new JetonDetective("Dr Watson",4);
        Watson.image = new FileInputStream("images\\JetonsDetective\\Watson.png");
        Watson.img = new ImageView(new Image(Watson.image));

        this.Toby = new JetonDetective("Toby",8);
        Toby.image = new FileInputStream("images\\JetonsDetective\\Toby.png");
        Toby.img = new ImageView(new Image(Toby.image));

    }
    public void deplacerDetective(JetonDetective jeton){

        //Coordonnées initiales du jeton
        double initX = jeton.img.getX();
        double initY = jeton.img.getY();

        jeton.img.setOnMouseDragged(e ->{

            //Le centre du jeton suis les mouvements de la souris

            jeton.img.setX(e.getX()-20);
            jeton.img.setY(e.getY()-20);

        });
        jeton.img.setOnMouseReleased(e ->{
            if(0 < e.getY() && e.getY()<178){          //S'il est laché en haut
                if(0 < e.getX() && e.getX()<280){
                    jeton.img.setX(198);
                    jeton.img.setY(100);
                    jeton.position = 1;
                }
                else if(280 < e.getX() && e.getX()<382){
                    jeton.img.setX(296);
                    jeton.img.setY(100);
                    jeton.position = 2;
                }
                else if(384 < e.getX() && e.getX()<650){
                    jeton.img.setX(394);
                    jeton.img.setY(100);
                    jeton.position = 3;
                }
            }
            else if(384 < e.getX() && e.getX()<650){    //S'il est laché à droite
                if(178 < e.getY() && e.getY()<280){
                    jeton.img.setX(492);
                    jeton.img.setY(198);
                    jeton.position = 4;
                }
                else if(280 < e.getY() && e.getY()<382){
                    jeton.img.setX(492);
                    jeton.img.setY(296);
                    jeton.position = 5;
                }
                else if(382 < e.getY() && e.getY()<650){
                    jeton.img.setX(492);
                    jeton.img.setY(394);
                    jeton.position = 6;
                }
            }
            else if(472 < e.getY() && e.getY()<650){    //S'il est laché en bas
                if(374 < e.getX() && e.getX()<650){
                    jeton.img.setX(394);
                    jeton.img.setY(492);
                    jeton.position = 7;
                }
                else if(280 < e.getX() && e.getX()<374){
                    jeton.img.setX(296);
                    jeton.img.setY(492);
                    jeton.position = 8;
                }
                else if(0 < e.getX() && e.getX()<280){
                    jeton.img.setX(198);
                    jeton.img.setY(492);
                    jeton.position = 9;
                }
            }
            else if(0 < e.getX() && e.getX()<178){     //S'il est laché à gauche
                if(374 < e.getY() && e.getY()<650){
                    jeton.img.setX(100);
                    jeton.img.setY(394);
                    jeton.position = 10;
                }
                else if(280 < e.getY() && e.getY()<374){
                    jeton.img.setX(100);
                    jeton.img.setY(296);
                    jeton.position = 11;
                }
                else if(78 < e.getY() && e.getY()<280){
                    jeton.img.setX(100);
                    jeton.img.setY(198);
                    jeton.position = 12;
                }
            }
            else if(178 < e.getY() && e.getY()<472){    //S'il est laché dans le carré central
                if(178 < e.getX() && e.getX()<472){
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
                }
            }
            //S'il est laché en dehors de la fenêtre

            if(0 > jeton.img.getY() || e.getY()>650){
                    jeton.img.setX(initX);
                    jeton.img.setY(initY);
            }
            if(0 > jeton.img.getX() || e.getX()>650){
                jeton.img.setX(initX);
                jeton.img.setY(initY);

            }

            //Un seul déplacement permis

            jeton.img.setOnMouseDragged(null);
            jeton.img.setOnMouseReleased(null);

        });
    }
    public void initPileAlibis() {
        ArrayList<Integer> temp_IndicesPos = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
        CarteAlibi IL = new CarteAlibi("IL",0); CarteAlibi MS = new CarteAlibi("MS",1); CarteAlibi JB = new CarteAlibi("JB",1); CarteAlibi JP = new CarteAlibi("JP",1); CarteAlibi JS = new CarteAlibi("JS",1); CarteAlibi JL = new CarteAlibi("JL",1); CarteAlibi M = new CarteAlibi("M",2); CarteAlibi SG = new CarteAlibi("SG",0); CarteAlibi WG = new CarteAlibi("WG",1);
        ArrayList<CarteAlibi> temp_ListeAlibis = new ArrayList<CarteAlibi>(Arrays.asList(IL,MS,JB,JP,JS,JL,M,SG,WG));

        int temp_randIndice = new Random().nextInt(9);
        mrjack.setIdentité(temp_ListeAlibis.get(temp_randIndice));
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
    public void initJetonsAction() throws FileNotFoundException {
        JetonAction J1 = new JetonAction("Alibi - Holmes"); JetonAction J2 = new JetonAction("Toby - Watson"); JetonAction J3 = new JetonAction("Pivot - Echange"); JetonAction J4 = new JetonAction("Pivot - Joker");
        J1.image1 = new FileInputStream("images\\JetonsAction\\Alibi.png"); J1.image2 = new FileInputStream("images\\JetonsAction\\Holmes.png"); J2.image1 = new FileInputStream("images\\JetonsAction\\Toby.png"); J2.image2 = new FileInputStream("images\\JetonsAction\\Watson.png"); J3.image1 = new FileInputStream("images\\JetonsAction\\Pivot.png"); J3.image2 = new FileInputStream("images\\JetonsAction\\Echange.png"); J4.image1 = new FileInputStream("images\\JetonsAction\\Pivot.png"); J4.image2 = new FileInputStream("images\\JetonsAction\\Joker.png");
        jetonsAction.add(0, J1); jetonsAction.add(1, J2); jetonsAction.add(2, J3); jetonsAction.add(3, J4);
        lancerJetonsAction();
    }

    public void echangerDistrict(int position1, int position2) {
        this.districts.get(position1-1).position=(position2);
        this.districts.get(position2-1).position=(position1);
        Collections.swap(this.districts,position1-1,position2-1);
    }
    public void retournerDistrict(int position){
        this.districts.get(position-1).face=2;
        // blabla
    }
    public void rotationDistrict(int position, int pivot){
        switch(pivot){
            case 0: // tourne 45deg horaire
                if(this.districts.get(position-1).orientation==4) {
                    this.districts.get(position-1).orientation=1;
                }
                else {
                    this.districts.get(position-1).orientation+=1;
                }
            case 1: // tourne 45 deg anti-horaire
                if(this.districts.get(position-1).orientation==1) {
                    this.districts.get(position-1).orientation=4;
                }
                else {
                    this.districts.get(position-1).orientation+=-1;
                }
            case 2: // tourne 90deg
                if(this.districts.get(position-1).orientation==1) {
                    this.districts.get(position-1).orientation=3;
                }
                if(this.districts.get(position-1).orientation==2) {
                    this.districts.get(position-1).orientation=4;
                }
                if(this.districts.get(position-1).orientation==3) {
                    this.districts.get(position-1).orientation=1;
                }
                if(this.districts.get(position-1).orientation==4) {
                    this.districts.get(position-1).orientation=2;
                }
        }
    }

    public void lancerJetonsAction() {

        ArrayList<Integer> temp_IndicesPos = new ArrayList<>(Arrays.asList(0,1,2,3));
        ArrayList<JetonAction> temp_jetonsAction = new ArrayList<JetonAction>(4);

        for (int i = 0; i < 4; i++) {
            int temp_randIndice = 5;
            while(! temp_IndicesPos.contains(temp_randIndice)) {
                temp_randIndice = new Random().nextInt(4);
            }
            temp_jetonsAction.add(i, this.jetonsAction.get(temp_randIndice));
            temp_IndicesPos.remove((Integer) temp_randIndice);
        }

        for (int i = 0; i < 4; i++) {
            jetonsAction.set(i, temp_jetonsAction.get(i));
            jetonsAction.get(i).face = new Random().nextInt(2)+1;
        }
    }

    public boolean isJackVisible() {
        int[] temp_positionsDetectives = {Holmes.position,Watson.position,Toby.position};
        ArrayList<District> districtsVisibles = new ArrayList<District>();
        ArrayList<District> districtsNonVisibles = (ArrayList) districts.clone();
        for (int position : temp_positionsDetectives) {
            switch(position) {
                case 1:
                    if (districts.get(0).orientation == 1 || districts.get(0).orientation == 2 || districts.get(0).orientation == 4) {
                        if(this.districts.get(0).face == 1) {
                            districtsVisibles.add((District) districts.get(0));
                            districtsNonVisibles.remove((District) districts.get(0));
                        }
                        if (districts.get(0).orientation == 2 || districts.get(0).orientation == 4) {
                            if (districts.get(3).orientation == 1 || districts.get(3).orientation == 2 || districts.get(3).orientation == 4) {
                                if (this.districts.get(3).face == 1) {
                                    districtsVisibles.add((District) districts.get(3));
                                    districtsNonVisibles.remove((District) districts.get(3));
                                }
                            }
                            if (districts.get(3).orientation == 2 || districts.get(3).orientation == 4) {
                                if (districts.get(6).orientation == 1 || districts.get(6).orientation == 2 || districts.get(6).orientation == 4) {
                                    if (this.districts.get(6).face == 1) {
                                        districtsVisibles.add((District) districts.get(6));
                                        districtsNonVisibles.remove((District) districts.get(6));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    if (districts.get(1).orientation == 1 || districts.get(1).orientation == 2 || districts.get(1).orientation == 4) {
                        if(this.districts.get(1).face == 1) {
                            districtsVisibles.add((District) districts.get(1));
                            districtsNonVisibles.remove((District) districts.get(1));
                        }
                        if (districts.get(1).orientation == 2 || districts.get(1).orientation == 4) {
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 2 || districts.get(4).orientation == 4) {
                                if (this.districts.get(4).face == 1) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 2 || districts.get(4).orientation == 4) {
                                if (districts.get(7).orientation == 1 || districts.get(7).orientation == 2 || districts.get(7).orientation == 4) {
                                    if (this.districts.get(7).face == 1) {
                                        districtsVisibles.add((District) districts.get(7));
                                        districtsNonVisibles.remove((District) districts.get(7));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    if (districts.get(2).orientation == 1 || districts.get(2).orientation == 2 || districts.get(2).orientation == 4) {
                        if(this.districts.get(2).face == 1) {
                            districtsVisibles.add((District) districts.get(2));
                            districtsNonVisibles.remove((District) districts.get(2));
                        }
                        if (districts.get(2).orientation == 2 || districts.get(2).orientation == 4) {
                            if (districts.get(5).orientation == 1 || districts.get(5).orientation == 2 || districts.get(5).orientation == 4) {
                                if (this.districts.get(5).face == 1) {
                                    districtsVisibles.add((District) districts.get(5));
                                    districtsNonVisibles.remove((District) districts.get(5));
                                }
                            }
                            if (districts.get(5).orientation == 2 || districts.get(5).orientation == 4) {
                                if (districts.get(8).orientation == 1 || districts.get(8).orientation == 2 || districts.get(8).orientation == 4) {
                                    if (this.districts.get(8).face == 1) {
                                        districtsVisibles.add((District) districts.get(8));
                                        districtsNonVisibles.remove((District) districts.get(8));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if (districts.get(2).orientation == 1 || districts.get(2).orientation == 2 || districts.get(2).orientation == 3) {
                        if(this.districts.get(2).face == 1) {
                            districtsVisibles.add((District) districts.get(2));
                            districtsNonVisibles.remove((District) districts.get(2));
                        }
                        if (districts.get(2).orientation == 1 || districts.get(2).orientation == 3) {
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 2 || districts.get(1).orientation == 3) {
                                if (this.districts.get(1).face == 1) {
                                    districtsVisibles.add((District) districts.get(1));
                                    districtsNonVisibles.remove((District) districts.get(1));
                                }
                            }
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 3) {
                                if (districts.get(0).orientation == 1 || districts.get(0).orientation == 2 || districts.get(0).orientation == 3) {
                                    if (this.districts.get(0).face == 1) {
                                        districtsVisibles.add((District) districts.get(0));
                                        districtsNonVisibles.remove((District) districts.get(0));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    if (districts.get(5).orientation == 1 || districts.get(5).orientation == 2 || districts.get(5).orientation == 3) {
                        if(this.districts.get(5).face == 1) {
                            districtsVisibles.add((District) districts.get(5));
                            districtsNonVisibles.remove((District) districts.get(5));
                        }
                        if (districts.get(5).orientation == 1 || districts.get(5).orientation == 3) {
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 2 || districts.get(4).orientation == 3) {
                                if (this.districts.get(4).face == 1) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 3) {
                                if (districts.get(3).orientation == 1 || districts.get(3).orientation == 2 || districts.get(3).orientation == 3) {
                                    if (this.districts.get(3).face == 1) {
                                        districtsVisibles.add((District) districts.get(3));
                                        districtsNonVisibles.remove((District) districts.get(3));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 6:
                    if (districts.get(8).orientation == 1 || districts.get(8).orientation == 2 || districts.get(8).orientation == 3) {
                        if(this.districts.get(8).face == 1) {
                            districtsVisibles.add((District) districts.get(8));
                            districtsNonVisibles.remove((District) districts.get(8));
                        }
                        if (districts.get(8).orientation == 1 || districts.get(8).orientation == 3) {
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 2 || districts.get(7).orientation == 3) {
                                if (this.districts.get(7).face == 1) {
                                    districtsVisibles.add((District) districts.get(7));
                                    districtsNonVisibles.remove((District) districts.get(7));
                                }
                            }
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 3) {
                                if (districts.get(6).orientation == 1 || districts.get(6).orientation == 2 || districts.get(6).orientation == 3) {
                                    if (this.districts.get(6).face == 1) {
                                        districtsVisibles.add((District) districts.get(6));
                                        districtsNonVisibles.remove((District) districts.get(6));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 7:
                    if (districts.get(8).orientation == 2 || districts.get(8).orientation == 3 || districts.get(8).orientation == 4) {
                        if(this.districts.get(8).face == 1) {
                            districtsVisibles.add((District) districts.get(8));
                            districtsNonVisibles.remove((District) districts.get(8));
                        }
                        if (districts.get(8).orientation == 2 || districts.get(8).orientation == 4) {
                            if (districts.get(5).orientation == 2 || districts.get(5).orientation == 3 || districts.get(5).orientation == 4) {
                                if (this.districts.get(5).face == 1) {
                                    districtsVisibles.add((District) districts.get(5));
                                    districtsNonVisibles.remove((District) districts.get(5));
                                }
                            }
                            if (districts.get(5).orientation == 2 || districts.get(5).orientation == 4) {
                                if (districts.get(2).orientation == 2 || districts.get(2).orientation == 3 || districts.get(2).orientation == 4) {
                                    if (this.districts.get(2).face == 1) {
                                        districtsVisibles.add((District) districts.get(2));
                                        districtsNonVisibles.remove((District) districts.get(2));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 8:
                    if (districts.get(7).orientation == 2 || districts.get(7).orientation == 3 || districts.get(7).orientation == 4) {
                        if(this.districts.get(7).face == 1) {
                            districtsVisibles.add((District) districts.get(7));
                            districtsNonVisibles.remove((District) districts.get(7));
                        }
                        if (districts.get(7).orientation == 2 || districts.get(7).orientation == 4) {
                            if (districts.get(4).orientation == 2 || districts.get(4).orientation == 3 || districts.get(4).orientation == 4) {
                                if (this.districts.get(4).face == 1) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 2 || districts.get(4).orientation == 4) {
                                if (districts.get(1).orientation == 2 || districts.get(1).orientation == 3 || districts.get(1).orientation == 4) {
                                    if (this.districts.get(1).face == 1) {
                                        districtsVisibles.add((District) districts.get(1));
                                        districtsNonVisibles.remove((District) districts.get(1));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 9:
                    if (districts.get(6).orientation == 2 || districts.get(6).orientation == 3 || districts.get(6).orientation == 4) {
                        if(this.districts.get(6).face == 1) {
                            districtsVisibles.add((District) districts.get(6));
                            districtsNonVisibles.remove((District) districts.get(6));
                        }
                        if (districts.get(6).orientation == 2 || districts.get(6).orientation == 4) {
                            if (districts.get(3).orientation == 2 || districts.get(3).orientation == 3 || districts.get(3).orientation == 4) {
                                if (this.districts.get(3).face == 1) {
                                    districtsVisibles.add((District) districts.get(3));
                                    districtsNonVisibles.remove((District) districts.get(3));
                                }
                            }
                            if (districts.get(3).orientation == 2 || districts.get(3).orientation == 4) {
                                if (districts.get(0).orientation == 2 || districts.get(0).orientation == 3 || districts.get(0).orientation == 4) {
                                    if (this.districts.get(0).face == 1) {
                                        districtsVisibles.add((District) districts.get(0));
                                        districtsNonVisibles.remove((District) districts.get(0));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 10:
                    if (districts.get(6).orientation == 1 || districts.get(6).orientation == 3 || districts.get(6).orientation == 4) {
                        if(this.districts.get(6).face == 1) {
                            districtsVisibles.add((District) districts.get(6));
                            districtsNonVisibles.remove((District) districts.get(6));
                        }
                        if (districts.get(6).orientation == 1 || districts.get(6).orientation == 3) {
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 3 || districts.get(7).orientation == 4) {
                                if (this.districts.get(7).face == 1) {
                                    districtsVisibles.add((District) districts.get(7));
                                    districtsNonVisibles.remove((District) districts.get(7));
                                }
                            }
                            if (districts.get(7).orientation == 1 || districts.get(7).orientation == 3) {
                                if (districts.get(8).orientation == 1 || districts.get(8).orientation == 3 || districts.get(8).orientation == 4) {
                                    if (this.districts.get(8).face == 1) {
                                        districtsVisibles.add((District) districts.get(8));
                                        districtsNonVisibles.remove((District) districts.get(8));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 11:
                    if (districts.get(3).orientation == 1 || districts.get(3).orientation == 3 || districts.get(3).orientation == 4) {
                        if(this.districts.get(3).face == 1) {
                            districtsVisibles.add((District) districts.get(3));
                            districtsNonVisibles.remove((District) districts.get(3));
                        }
                        if (districts.get(3).orientation == 1 || districts.get(3).orientation == 3) {
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 3 || districts.get(4).orientation == 4) {
                                if (this.districts.get(4).face == 1) {
                                    districtsVisibles.add((District) districts.get(4));
                                    districtsNonVisibles.remove((District) districts.get(4));
                                }
                            }
                            if (districts.get(4).orientation == 1 || districts.get(4).orientation == 3) {
                                if (districts.get(5).orientation == 1 || districts.get(5).orientation == 3 || districts.get(5).orientation == 4) {
                                    if (this.districts.get(5).face == 1) {
                                        districtsVisibles.add((District) districts.get(5));
                                        districtsNonVisibles.remove((District) districts.get(5));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 12:
                    if (districts.get(0).orientation == 1 || districts.get(0).orientation == 3 || districts.get(0).orientation == 4) {
                        if(this.districts.get(0).face == 1) {
                            districtsVisibles.add((District) districts.get(0));
                            districtsNonVisibles.remove((District) districts.get(0));
                        }
                        if (districts.get(0).orientation == 1 || districts.get(0).orientation == 3) {
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 3 || districts.get(1).orientation == 4) {
                                if (this.districts.get(1).face == 1) {
                                    districtsVisibles.add((District) districts.get(1));
                                    districtsNonVisibles.remove((District) districts.get(1));
                                }
                            }
                            if (districts.get(1).orientation == 1 || districts.get(1).orientation == 3) {
                                if (districts.get(2).orientation == 1 || districts.get(2).orientation == 3 || districts.get(2).orientation == 4) {
                                    if (this.districts.get(2).face == 1) {
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
        int indiceVisibilité=0;
        for (int i = 0; i < districtsVisibles.size(); i++) {
            if(mrjack.identité.nom == districtsVisibles.get(i).nom) {
                indiceVisibilité = 1;
            }
        }

        System.out.print("districtsVisibles:   ");
        for (int i = 0; i < districtsVisibles.size(); i++) {
            System.out.print(districtsVisibles.get(i).nom + "   ");
        }
        System.out.println();
        System.out.print("districtsNonVisibles:   ");
        for (int i = 0; i < districtsNonVisibles.size(); i++) {
            System.out.print(districtsNonVisibles.get(i).nom + "   ");
        }
        System.out.println();

        switch(indiceVisibilité) {
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

}