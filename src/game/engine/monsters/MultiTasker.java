package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class MultiTasker extends Monster {

    private int normalSpeedTurns;

    public MultiTasker(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
        this.normalSpeedTurns = 0;
    }

    public int getNormalSpeedTurns() { return normalSpeedTurns; }
    public void setNormalSpeedTurns(int normalSpeedTurns) { this.normalSpeedTurns = normalSpeedTurns; }

    @Override
    public void move(int distance) {
        if (normalSpeedTurns > 0) {
            super.move(distance);
            normalSpeedTurns--;
        } else {
            super.move(distance / 2);
        }
    }

    @Override
    public void setEnergy(int newValue) {
        int current = this.getEnergy();
        int change = newValue - current;
        int adjusted = current + change;
        if (change != 0) {
            adjusted += Constants.MULTITASKER_BONUS;
        }
        super.setEnergy(adjusted); // super handles MIN_ENERGY clamp
    }

    @Override
    public void executePowerupEffect(Monster opponent) {
        normalSpeedTurns = 2;
    }
}