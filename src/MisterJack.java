public class MisterJack {

    CarteAlibi identite;
    int nbSabliers;

    public MisterJack() {
    }

    public MisterJack(CarteAlibi identite, int nbSabliers) {
        this.identite = identite;
        this.nbSabliers = nbSabliers;
    }

    public CarteAlibi getIdentité() {
        return identite;
    }

    public void setIdentité(CarteAlibi identité) {
        this.identite = identité;
    }

    public int getNbSabliers() {
        return nbSabliers;
    }

    public void setNbSabliers(int nbSabliers) {
        this.nbSabliers = nbSabliers;
    }
}
