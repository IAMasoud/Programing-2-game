package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {

    public Schemer(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
    }

    private int stealEnergyFrom(Monster target) {
        int stealAmount = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
        target.setEnergy(target.getEnergy() - stealAmount);
        return stealAmount;
    }

    @Override
    public void setEnergy(int newValue) {
        int diff = newValue - this.getEnergy();
        diff = diff + 10;
        int finalEnergy = this.getEnergy() + diff;
        if (finalEnergy < Constants.MIN_ENERGY) {
            finalEnergy = Constants.MIN_ENERGY;
        }
        super.setEnergy(finalEnergy);
    }

    @Override
    public void executePowerupEffect(Monster opponent) {
        int totalSteal = 0;
        totalSteal += stealEnergyFrom(opponent);
        for (Monster m : Board.getStationedMonsters()) {
            totalSteal += stealEnergyFrom(m);
        }
        setEnergy(this.getEnergy() + totalSteal);
    }
}