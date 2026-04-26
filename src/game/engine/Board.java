package game.engine; //Youssef

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.monsters.Monster;

import java.util.ArrayList;
import java.util.Collections;

public class Board {

    private Cell[][] boardCells;
    private static ArrayList<Monster> stationedMonsters;
    private static ArrayList<Card> originalCards;
    private static ArrayList<Card> cards;

    public Board(ArrayList<Card> readCards) {
        boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        stationedMonsters = new ArrayList<>();
        cards = new ArrayList<>();
        originalCards = readCards;
        setCardsByRarity();
        reloadCards();
    }

    public static ArrayList<Monster> getStationedMonsters() {
        return stationedMonsters;
    }

    public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
        Board.stationedMonsters = stationedMonsters;
    }

    public static ArrayList<Card> getCards() {
        return cards;
    }

    public static void setCards(ArrayList<Card> cards) {
        Board.cards = cards;
    }

    public static ArrayList<Card> getOriginalCards() {
        return originalCards;
    }

    private int[] indexToRowCol(int index) {
        int row = index / Constants.BOARD_COLS;
        int col = index % Constants.BOARD_COLS;
        if (row % 2 != 0) {
            col = Constants.BOARD_COLS - 1 - col;
        }
        return new int[]{row, col};
    }

    private Cell getCell(int index) {
        int[] rc = indexToRowCol(index);
        return boardCells[rc[0]][rc[1]];
    }

    private void setCell(int index, Cell cell) {
        int[] rc = indexToRowCol(index);
        boardCells[rc[0]][rc[1]] = cell;
    }

    private void setCardsByRarity() {
        ArrayList<Card> expanded = new ArrayList<>();
        for (Card c : originalCards) {
            for (int i = 0; i < c.getRarity(); i++) {
                expanded.add(c);
            }
        }
        originalCards = expanded;
    }

    public static void reloadCards() {
        cards = new ArrayList<>(originalCards);
        Collections.shuffle(cards);
    }

    public static Card drawCard() {
        if (cards == null || cards.isEmpty()) {
            reloadCards();
        }
        if (cards.isEmpty()) return null;
        return cards.remove(0);
    }

    public void initializeBoard(ArrayList<Cell> specialCells) {
        // 1. Fill with normal cells
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            setCell(i, new Cell("Normal"));
        }

        // 2. Separate by type
        ArrayList<DoorCell> doors = new ArrayList<>();
        ArrayList<CardCell> cardCells = new ArrayList<>();
        ArrayList<ConveyorBelt> conveyors = new ArrayList<>();
        ArrayList<ContaminationSock> socks = new ArrayList<>();

        for (Cell c : specialCells) {
            if (c instanceof DoorCell) doors.add((DoorCell) c);
            else if (c instanceof CardCell) cardCells.add((CardCell) c);
            else if (c instanceof ConveyorBelt) conveyors.add((ConveyorBelt) c);
            else if (c instanceof ContaminationSock) socks.add((ContaminationSock) c);
        }

        // 3. Counters
        int doorIndex = 0, cardIndex = 0, conveyorIndex = 0, sockIndex = 0;

        // 4. Place doors on odd indices (skip special indices)
        for (int i = 1; i < Constants.BOARD_SIZE; i += 2) {
            boolean isSpecial = false;
            for (int x : Constants.CARD_CELL_INDICES) if (x == i) isSpecial = true;
            for (int x : Constants.CONVEYOR_CELL_INDICES) if (x == i) isSpecial = true;
            for (int x : Constants.SOCK_CELL_INDICES) if (x == i) isSpecial = true;
            for (int x : Constants.MONSTER_CELL_INDICES) if (x == i) isSpecial = true;
            if (isSpecial) continue;
            if (doorIndex < doors.size()) {
                setCell(i, doors.get(doorIndex++));
            }
        }

        // 5. Card cells
        for (int i : Constants.CARD_CELL_INDICES) {
            if (cardIndex < cardCells.size()) setCell(i, cardCells.get(cardIndex++));
            else setCell(i, new CardCell("Card"));
        }

        // 6. Conveyor cells
        for (int i : Constants.CONVEYOR_CELL_INDICES) {
            if (conveyorIndex < conveyors.size()) setCell(i, conveyors.get(conveyorIndex++));
        }

        // 7. Sock cells
        for (int i : Constants.SOCK_CELL_INDICES) {
            if (sockIndex < socks.size()) setCell(i, socks.get(sockIndex++));
        }

        // 8. Monster cells
        for (int i = 0; i < Constants.MONSTER_CELL_INDICES.length; i++) {
            int index = Constants.MONSTER_CELL_INDICES[i];
            if (i < stationedMonsters.size()) {
                Monster m = stationedMonsters.get(i);
                MonsterCell mc = new MonsterCell(m.getName(), m);
                setCell(index, mc);
                m.setPosition(index);
            }
        }
    }

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster)
            throws game.engine.exceptions.InvalidMoveException {
        int oldPosition = currentMonster.getPosition();
        currentMonster.move(roll);
        Cell cell = getCell(currentMonster.getPosition());
        cell.onLand(currentMonster, opponentMonster);

        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(oldPosition);
            throw new game.engine.exceptions.InvalidMoveException();
        }

        if (currentMonster.isConfused()) {
            currentMonster.decrementConfusion();
            opponentMonster.decrementConfusion();
        }

        updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            getCell(i).setMonster(null);
        }
        getCell(player.getPosition()).setMonster(player);
        getCell(opponent.getPosition()).setMonster(opponent);
    }
    public Cell[][] getBoardCells() {
        return boardCells;
    }
}
