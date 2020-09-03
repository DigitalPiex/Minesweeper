package com.javarush.games.minesweeper;

import com.javarush.engine.cell.*;

import java.util.*;

public class MinesweeperGame extends Game {
    private static final int SIDE = 7;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {

        if (isGameStopped) {
            restart();
        } else {
            openTile(x, y);
        }

    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);

    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(x, y, "");
                boolean isMine = getRandomNumber(10) < 3;
                if (isMine) {
                    countMinesOnField++;
                    gameField[y][x] = new GameObject(x, y, true);
                    setCellColor(x, y, Color.RED);
                } else {
                    gameField[y][x] = new GameObject(x, y, false);
                    setCellColor(x, y, Color.AQUA);
                }
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Well Done!", Color.BLACK, 24);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Game Over", Color.WHITE, 24);
    }

    private void restart() {
        isGameStopped = false;
        score = 0;
        setScore(score);
        countClosedTiles = SIDE * SIDE;
        countMinesOnField = 0;
        countFlags = 0;
        createGame();
    }

    private void countMineNeighbors() {
        int count = 0;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                count = 0;
                if (!(gameField[y][x].isMine)) {
                    for (GameObject go : getNeighbors(gameField[y][x])) {
                        if (go.isMine) {
                            ++count;
                        }
                        gameField[y][x].countMineNeighbors = count;
                    }
                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> NeighborList = new ArrayList<>();
        for (int y = gameObject.y - 1; y < gameObject.y + 2; y++) {
            for (int x = gameObject.x - 1; x < gameObject.x + 2; x++) {
                if (y < 0 || y > SIDE - 1) {
                    continue;
                }
                if (x < 0 || x > SIDE - 1) {
                    continue;
                }
                if (gameField[y][x].equals(gameObject)) {
                    continue;
                }
                NeighborList.add(gameField[y][x]);
            }
        }
        return NeighborList;
    }

    private void markTile(int x, int y) {

        if (gameField[y][x].isOpen) {
            int j = 0;
        }

        if ((countFlags == 0) && !(gameField[y][x].isFlag)) {
            int j = 0;
        }
        if (isGameStopped) {
            int j = 0;
        } else if (!(gameField[y][x].isOpen) && !(gameField[y][x].isFlag) && !(countFlags == 0)) {
            gameField[y][x].isFlag = true;
            countFlags--;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.YELLOW);
        } else if (!(gameField[y][x].isOpen) && (gameField[y][x].isFlag)) {
            gameField[y][x].isFlag = false;
            countFlags++;
            setCellValue(x, y, "");
            if (!(gameField[y][x].isMine)) {
                setCellColor(x, y, Color.AQUA);
            }
            if ((gameField[y][x].isMine)) {
                setCellColor(x, y, Color.ORANGE);
            }
        }
    }

    private void openTile(int x, int y) {
        if (isGameStopped) {
            int j = 0;

        } else if ((countClosedTiles == SIDE * SIDE) && (gameField[y][x].isMine)) {
            restart();
            openTile(x, y);

        } else if ((gameField[y][x].isOpen) || (gameField[y][x].isFlag)) {
            int j = 0;

        } else if (!(gameField[y][x].isMine) && (gameField[y][x].countMineNeighbors == 0) && !(gameField[y][x].isOpen)) {
            countClosedTiles--;
            setCellColor(x, y, Color.GREEN);
            setCellValue(x, y, "");
            gameField[y][x].isOpen = true;
            score += 5;

            for (GameObject go : getNeighbors(gameField[y][x])) {
                if (!(go.isMine) && (go.countMineNeighbors == 0) && !(go.isOpen)) {
                    openTile(go.x, go.y);
                }

                if (!(go.isMine) && (go.countMineNeighbors != 0) && !(go.isOpen)) {
                    countClosedTiles--;
                    setCellNumber(go.x, go.y, go.countMineNeighbors);
                    setCellColor(go.x, go.y, Color.GREEN);
                    go.isOpen = true;
                    score += 5;
                }
            }
            if (countClosedTiles == countMinesOnField) {
                win();
            }

        } else if ((gameField[y][x].isMine) && !(gameField[y][x].isOpen)) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameField[y][x].isOpen = true;
            gameOver();


        } else {
            countClosedTiles--;
            setCellNumber(x, y, gameField[y][x].countMineNeighbors);
            gameField[y][x].isOpen = true;
            setCellColor(x, y, Color.GREEN);
            score += 5;
            if (countClosedTiles == countMinesOnField) {
                win();
            }
        }
        setScore(score);
    }


}

