package de.dhbw.ase.monopoly;

import java.util.Arrays;

import de.dhbw.ase.monopoly.spaces.BoardSpace;
import de.dhbw.ase.monopoly.spaces.PropertySpace;

public class Game {
  private GameBoard gameBoard;
  private final Player[] players;
  private int curPlayerIdx;
  private boolean canRollDice = true;
  private int consecutiveDoubles = 0;

  public Game(String[] playerNames) {
    gameBoard = new GameBoard(this);
    players = Arrays.stream(playerNames)
        .map(piece -> new Player(piece, gameBoard)).toArray(Player[]::new);
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

  public String rollDice() {
    if (!canRollDice) {
      return "You can not cast dice again.";
    }

    canRollDice = false;
    Player curPlayer = players[curPlayerIdx];
    int die1 = randomDice();
    int die2 = randomDice();

    String doublesJailMessage = "";
    if (curPlayer.isInJail()) {
      if (die1 == die2) {
        // leave jail for free after rolling doubles, use same roll to move
        curPlayer.getOutOfJail(true);
        doublesJailMessage = "You rolled doubles and got out of jail for free.";
      } else {
        curPlayer.incConsecutiveNotDoublesInJail();
        if (curPlayer.mustGetOutOfJail()) {
          // player must get out of jail after not rolling doubles for three rounds while
          // being in jail, use same roll to move
          curPlayer.getOutOfJail(false);
          doublesJailMessage = "You did not roll doubles three rounds in a row and had to get out of jail at your own expense.";
        } else {
          return "You did not roll doubles and stay in jail.";
        }
      }
    } else { // player is not in jail
      if (die1 == die2) {
        consecutiveDoubles++;
        doublesJailMessage = "You rolled doubles and can therefore roll another time.";

        if (consecutiveDoubles == 3) {
          // player must to to jail after rolling doubles for three rounds
          curPlayer.goToJail();
          return "You were speeding. Go to jail immediately.";
        } else {
          // player can roll again after rolling doubles without going to jail
          canRollDice = true;
        }
      } else {
        consecutiveDoubles = 0;
      }
    }

    int steps = die1 + die2;
    String moveMessage = curPlayer.moveForward(steps);
    return UtilService.joinMessages(doublesJailMessage, moveMessage);
  }

  public boolean canRollDice() {
    return canRollDice;
  }

  public void buyProperty() {
    Player curPlayer = players[curPlayerIdx];
    BoardSpace curSpace = gameBoard.getSpace(curPlayer.getPosition());

    if (!curSpace.isBuyable()) {
      return;
    }

    PropertySpace propertySpace = (PropertySpace) curSpace;
    int propertyPrice = propertySpace.getPrice();
    if (propertyPrice > curPlayer.getMoney()) {
      return;
    }

    curPlayer.buyProperty();
  }

  public void getOutOfJail() {
    Player curPlayer = players[curPlayerIdx];
    if (canRollDice && curPlayer.isInJail()) {
      curPlayer.getOutOfJail(false);
    }
  }

  public void endTurn() {
    if (canRollDice) {
      return;
    }
    canRollDice = true;
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
