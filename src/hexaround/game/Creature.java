package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.required.CreatureName;
import hexaround.required.CreatureProperty;

public class Creature {

    private boolean blueTeam;

    private CreatureDefinition creatureDef;

    public Creature(boolean b, CreatureDefinition def ) {
        this.blueTeam = b;
        this.creatureDef = def;
    }

    public boolean getTeam() {
        return this.blueTeam;
    }

    public CreatureDefinition getDef() {
        return this.creatureDef;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        Creature cCopy = new Creature(this.blueTeam, this.creatureDef);
        return cCopy;
    }
}
