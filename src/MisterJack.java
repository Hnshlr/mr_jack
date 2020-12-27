public class MisterJack {

    CarteAlibi identité;
    int nbSabliers;

    public MisterJack() {
    }

    public MisterJack(CarteAlibi identité, int nbSabliers) {
        this.identité = identité;
        this.nbSabliers = nbSabliers;
    }

    public CarteAlibi getIdentité() {
        return identité;
    }

    public void setIdentité(CarteAlibi identité) {
        this.identité = identité;
    }

    public int getNbSabliers() {
        return nbSabliers;
    }

    public void setNbSabliers(int nbSabliers) {
        this.nbSabliers = nbSabliers;
    }
}
