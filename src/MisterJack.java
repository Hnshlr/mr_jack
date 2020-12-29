public class MisterJack {

    CarteAlibi identite;
    int nbSabliers;

    public MisterJack() {
    }

    public MisterJack(CarteAlibi identité, int nbSabliers) {
        this.identite = identité;
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
