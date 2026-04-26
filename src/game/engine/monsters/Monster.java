package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public abstract class Monster implements Comparable<Monster> {

    private String name;
    private String description;
    private Role role;
    private Role originalRole;
    private int energy;
    private int position;
    private boolean frozen;
    private boolean shielded;
    private int confusionTurns;

    public Monster(String name, String description, Role originalRole, int energy) {
        this.name = name;
        this.description = description;
        this.originalRole = originalRole;
        this.role = originalRole;
        this.energy = energy;
        this.position = 0;
        this.frozen = false;
        this.shielded = false;
        this.confusionTurns = 0;
    }

    public abstract void executePowerupEffect(Monster opponentMonster);

    public boolean isConfused() {
        return confusionTurns > 0;
    }

    public void decrementConfusion() {
        if (confusionTurns > 0) {
            confusionTurns--;
            if (confusionTurns == 0) {
                role = originalRole;
            }
        }
    }

    public void move(int distance) {
        int newPosition = this.position + distance;
        newPosition = newPosition % Constants.BOARD_SIZE;
        if (newPosition < 0) {
            newPosition += Constants.BOARD_SIZE;
        }
        this.position = newPosition;
    }

    public final void alterEnergy(int change) {
        if (change < 0) {
            if (shielded) {
                shielded = false;
            } else {
                setEnergy(this.energy + change);
            }
        } else {
            setEnergy(this.energy + change);
        }
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Role getOriginalRole() { return originalRole; }
    public int getEnergy() { return energy; }

    // ✅ FINAL CORRECT VERSION
    public void setEnergy(int energy) {
        this.energy = Math.max(0, energy);
    }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public boolean isFrozen() { return frozen; }
    public void setFrozen(boolean frozen) { this.frozen = frozen; }
    public boolean isShielded() { return shielded; }
    public void setShielded(boolean shielded) { this.shielded = shielded; }
    public int getConfusionTurns() { return confusionTurns; }
    public void setConfusionTurns(int confusionTurns) { this.confusionTurns = confusionTurns; }

    @Override
    public int compareTo(Monster o) {
        return this.position - o.position;
    }
}