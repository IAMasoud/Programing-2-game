package game.engine.cells;

import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {

    private Role role;
    private int energy;
    private boolean activated;

    public DoorCell(String name, Role role, int energy) {
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }

    public Role getRole() { return role; }
    public int getEnergy() { return energy; }
    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        if (monster.getRole() == this.role) {
            // same role — gain energy
            monster.alterEnergy(canisterValue);
        } else {
            // different role — lose energy
            monster.alterEnergy(-canisterValue);
        }
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        if (activated) return;

        if (landingMonster.getRole() == this.role) {
            landingMonster.alterEnergy(energy);
            for (Monster teammate : Board.getStationedMonsters()) {
                if (teammate != null
                        && teammate != landingMonster
                        && teammate.getRole() == landingMonster.getRole()) {
                    teammate.alterEnergy(energy);
                }
            }
            activated = true;
        } else {
            if (landingMonster.isShielded()) {
                landingMonster.setShielded(false);
                return;
            }
            landingMonster.alterEnergy(-energy);
            for (Monster teammate : Board.getStationedMonsters()) {
                if (teammate != null
                        && teammate != landingMonster
                        && teammate.getRole() == landingMonster.getRole()) {
                    teammate.alterEnergy(-energy);
                }
            }
            activated = true;
        }
    }
}