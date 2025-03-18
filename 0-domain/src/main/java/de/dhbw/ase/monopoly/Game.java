package de.dhbw.ase.monopoly;

import java.util.Arrays;

public class Game {
  private GameBoard gameBoard;
  private final Player[] players;
  private int curPlayerIdx;
  private boolean canRollAgain = true;
  private int consecutiveDoubles = 0;

  public Game(String[] playerNames) {
    gameBoard = new GameBoard(this);
    players = Arrays.stream(playerNames)
        .map(name -> new Player(name, gameBoard)).toArray(Player[]::new);
    curPlayerIdx = (int) (Math.random() * playerNames.length);
  }

  public GameBoard getGameBoard() {
    return gameBoard;
  }

  public Player[] getPlayers() {
    return players;
  }

  public int getCurPlayerIdx() {
    return curPlayerIdx;
  }

  public void rollDice() {
    if (!canRollAgain) {
      return;
    }
    canRollAgain = false;
    Player curPlayer = players[curPlayerIdx];

    int die1 = randomDice();
    int die2 = randomDice();

    if (curPlayer.isInJail()) {
      if (die1 == die2) {
        // leave jail for free after rolling doubles
        curPlayer.getOutOfJail(true);
      } else {
        curPlayer.incConsecutiveNotDoublesInJail();
        if (curPlayer.mustGetOutOfJail()) {
          // player must get out of jail after not rolling doubles for three rounds while
          // being in jail
          curPlayer.getOutOfJail(false);
        } else {
          return;
        }
      }
    } else { // player is not in jail
      if (die1 == die2) {
        consecutiveDoubles++;

        if (consecutiveDoubles == 3) {
          // player must to to jail after rolling doubles for three rounds
          curPlayer.goToJail();
          return;
        } else {
          // player can roll again after rolling doubles without going to jail
          canRollAgain = true;
        }
      } else {
        consecutiveDoubles = 0;
      }
    }

    int steps = die1 + die2;
    curPlayer.moveForward(steps);
  }

  public void getOutOfJail() {
    Player curPlayer = players[curPlayerIdx];
    if (canRollAgain && curPlayer.isInJail()) {
      curPlayer.getOutOfJail(false);
    }
  }

  public void endTurn() {
    if (canRollAgain) {
      return;
    }
    curPlayerIdx = (curPlayerIdx + 1) % players.length;
  }

  public void transferMoneyWithEveryPlayer(int money) {
    Arrays.stream(players).forEach(p -> p.transferMoney(-money));
    int totalMoney = money * players.length;
    players[curPlayerIdx].transferMoney(totalMoney);
  }

  private int randomDice() {
    return (int) (Math.random() * 5 + 1);
  }
}
