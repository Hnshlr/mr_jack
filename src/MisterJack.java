public class MisterJack {

    CarteAlibi identite;
    int nbSabliers;

    public MisterJack() {
    }

    public MisterJack(CarteAlibi identite, int nbSabliers) {
        this.identite = identite;
        this.nbSabliers = nbSabliers;
    }

    public CarteAlibi getIdentite() {
        return identite;
    }

    public void setIdentite(CarteAlibi identite) {
        this.identite = identite;
    }

    public int getNbSabliers() {
        return nbSabliers;
    }

    public void setNbSabliers(int nbSabliers) {
        this.nbSabliers = nbSabliers;
    }
}
