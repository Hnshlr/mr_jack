public class Plateau {

    public CarteAlibi[] pile = new CarteAlibi[9];
    public JetonAction[] jetonsActions = new JetonAction[4];
    public JetonDetective Holmes = new JetonDetective();
    public JetonDetective Watson = new JetonDetective();
    public JetonDetective Toby = new JetonDetective();
    public District[] districts = new District[9];
    public Joueur joueur1 = new Joueur();
    public Joueur joueur2 = new Joueur();

    public void initPlateau(){
        //toute la mise en place du jeu
    }
}
