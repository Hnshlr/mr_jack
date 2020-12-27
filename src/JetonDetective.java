public class JetonDetective {

    String nom;
    int position; // de 1 à 12 autour du plateau

    public JetonDetective() {}

    public JetonDetective(String nom, int position) {
        this.nom = nom;
        this.position = position;
    }

    public void actionDetective(){
        //Déplace le jetons de deux espaces dans le sens horaire

        if(position <= 10){
            position += 2;
        }
        else{
            // si position = 11 ---> nouvelle position = 1
            // si position = 12 ---> nouvelle position = 2
            position -= 10;
        }
    }

    public void joker(){
        //Déplace le jetons de un espaces dans le sens horaire

        if(position <= 11){
            position += 1;
        }
        else{
            // si position = 12 ---> nouvelle position = 1
            position = 1;
        }
    }
}
