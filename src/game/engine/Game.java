package game.engine;

import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import game.engine.cells.Cell;
import game.engine.cards.Card;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

    private Board board;
    private ArrayList<Monster> allMonsters;
    private Monster player;
    private Monster opponent;
    private Monster current;

    public Game(Role playerRole) throws IOException {
        ArrayList<Card> cards = game.engine.dataloader.DataLoader.readCards();
        ArrayList<Cell> cells = game.engine.dataloader.DataLoader.readCells();
        allMonsters = game.engine.dataloader.DataLoader.readMonsters();

        board = new Board(cards);

        // Select player
        for (Monster m : allMonsters) {
            if (m.getRole() == playerRole) {
                player = m;
                break;
            }
        }
        if (player == null && !allMonsters.isEmpty()) {
            player = allMonsters.get(0);
        }

        // Select opponent
        Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
        for (Monster m : allMonsters) {
            if (m.getRole() == opponentRole && m != player) {
                opponent = m;
                break;
            }
        }
        if (opponent == null) {
            for (Monster m : allMonsters) {
                if (m != player) {
                    opponent = m;
                    break;
                }
            }
        }

        // Stationed monsters
        ArrayList<Monster> stationed = new ArrayList<>();
        for (Monster m : allMonsters) {
            if (m != player && m != opponent) {
                stationed.add(m);
            }
        }

        board.setStationedMonsters(stationed);
        board.initializeBoard(cells);
        current = player;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Monster> getAllMonsters() {
        return Board.getStationedMonsters();
    }

    public Monster getPlayer() {
        return player;
    }

    public Monster getOpponent() {
        return opponent;
    }

    public Monster getCurrent() {
        return current;
    }

    public void setCurrent(Monster monster) {
        this.current = monster;
    }

    private Monster getCurrentOpponent() {
        if (current == player) return opponent;
        else return player;
    }

    private int rollDice() {
        return (int)(Math.random() * 6) + 1;
    }

    public void usePowerup() throws OutOfEnergyException {
        if (current.getEnergy() < Constants.POWERUP_COST) {
            throw new OutOfEnergyException();
        }
        current.alterEnergy(-Constants.POWERUP_COST);
        current.executePowerupEffect(getCurrentOpponent());
    }

    public void playTurn() throws InvalidMoveException {
        if (current.isFrozen()) {
            current.setFrozen(false);
            switchTurn();
            return;
        }
        int roll = rollDice();
        board.moveMonster(current, roll, getCurrentOpponent());
        switchTurn();
    }

    private void switchTurn() {
        if (current == player) current = opponent;
        else current = player;
    }

    private boolean checkWinCondition(Monster monster) {
        if (monster == null) return false;
        return (monster.getPosition() == Constants.WINNING_POSITION)
                && (monster.getEnergy() >= Constants.WINNING_ENERGY);
    }

    public Monster getWinner() {
        if (checkWinCondition(player)) return player;
        if (checkWinCondition(opponent)) return opponent;
        return null;
    }

    private Monster selectRandomMonsterByRole(Role role) {
        if (allMonsters == null || allMonsters.isEmpty()) return null;
        for (Monster m : allMonsters) {
            if (m != null && m.getRole() == role) return m;
        }
        return null;
    }
}