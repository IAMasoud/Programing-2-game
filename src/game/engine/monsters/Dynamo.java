package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class Dynamo extends Monster {

    public Dynamo(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
    }

    @Override
    public void setEnergy(int newValue) {
        int current = this.getEnergy();
        int change = newValue - current;
        int amplified = current + (change * 2);
        super.setEnergy(amplified); // super handles MIN_ENERGY clamp
    }

    @Override
    public void executePowerupEffect(Monster opponent) {
        opponent.setFrozen(true);
    }
}