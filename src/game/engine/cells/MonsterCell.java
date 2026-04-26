package game.engine.cells;

import game.engine.monsters.Monster;

public class MonsterCell extends Cell {

    private Monster cellMonster;

    public MonsterCell(String name, Monster cellMonster) {
        super(name);
        this.cellMonster = cellMonster;
    }

    public Monster getCellMonster() {
        return cellMonster;
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster); // always first

        if (cellMonster == null) return;

       
        if (landingMonster.getRole() == cellMonster.getRole()) {
            landingMonster.executePowerupEffect(opponentMonster);
        }

        
        else {
            if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
                int difference = landingMonster.getEnergy() - cellMonster.getEnergy();
                
                landingMonster.alterEnergy(-difference);
               
                cellMonster.setEnergy(cellMonster.getEnergy() + difference);
            }
            
        }
    }
}