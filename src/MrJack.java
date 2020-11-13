package com.isep.project;

public class MrJack extends Player {
    public int nbSabliers;
    public PersonnagePlateau coupable;

    public MrJack(int nbSabliers, int role, PersonnagePlateau coupable) {
        this.nbSabliers = nbSabliers;
        this.coupable = coupable;
    }
}
}
