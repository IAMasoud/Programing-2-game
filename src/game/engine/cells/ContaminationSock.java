package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

    public ContaminationSock(String name, int effect) {
        super(name, effect);
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }

    @Override
    public void transport(Monster monster) {
        int newPosition = monster.getPosition() + getEffect();
        if (newPosition < 0) {
            newPosition = 0;
        }
        monster.setPosition(newPosition);
        if (monster.isShielded()) {
            monster.setShielded(false);
            return;
        }
        monster.alterEnergy(-Constants.SLIP_PENALTY);
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        this.setMonster(landingMonster);
        transport(landingMonster);
    }
}