import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Plateau {

    public CarteAlibi[] pile = new CarteAlibi[9];

    public JetonAction[] jetonsActions = new JetonAction[4];

    public JetonDetective Holmes = new JetonDetective();
    public JetonDetective Watson = new JetonDetective();
    public JetonDetective Toby = new JetonDetective();

    public District[] districts = new District[9];
    public Joueur joueur1 = new Joueur();
    public Joueur joueur2 = new Joueur();

    public static void initDistricts(){
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

        }
        for (District Alibi : temp_ListeAlibis) {
            if(Alibi.position==1) {
                Alibi.orientation=2;
            }
            if(Alibi.position==3) {
                Alibi.orientation=3;
            }
            if(Alibi.position==8) {
                Alibi.orientation=1;
            }
        }

        for (District Alibi : temp_ListeAlibis) {
            System.out.println("["+Alibi.nom+","+Alibi.position+","+Alibi.orientation+","+Alibi.face+"]");
        }
    }

    public void initPlateau(){
        initDistricts();
        //toute la mise en place du jeu
    }
}
