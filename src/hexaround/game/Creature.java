package hexaround.game;

import hexaround.config.CreatureDefinition;

public class Creature {
    /*
    * This class keeps track of the information inside a cell including team, definition, (unimplemented) trapped creatures
    */

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

    // Overridden clone method to allow for deep copies of a board to be created
    @Override
    protected Object clone() throws CloneNotSupportedException {

        Creature cCopy = new Creature(this.blueTeam, this.creatureDef);
        return cCopy;
    }
}
