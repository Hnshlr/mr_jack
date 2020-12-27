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

    public JetonAction[] jetonsActions = new JetonAction[4];


    public void initPlateau(){
        initDistricts();
        initDetectives();
        initPileAlibis();
        //toute la mise en place du jeu
    }

    public void etatDePartie(){
        System.out.println("mrjack.identité.nom="+mrjack.identité.nom+"\n");

        System.out.print("pile_Alibis=[");
        for (int i = 0; i < pile_Alibis.size()-1; i++) {
            System.out.print(pile_Alibis.get(i).nom+",");
        }
        System.out.println(pile_Alibis.get(pile_Alibis.size()-1).nom+"]");

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

    public void initDistricts(){
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
    }
    public void initDetectives() {
        this.Holmes = new JetonDetective("Sherlock Holmes",12);
        this.Watson = new JetonDetective("Dr Watson",4);
        this.Toby = new JetonDetective("Toby",8);
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

    public void echangerDistrict(int position1, int position2) {
        this.districts.get(position1-1).position=(position2);
        this.districts.get(position2-1).position=(position1);
        Collections.swap(this.districts,position1-1,position2-1);
    }

    public void retournerDistrict(int position){
        this.districts.get(position-1).face=2;
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

}
