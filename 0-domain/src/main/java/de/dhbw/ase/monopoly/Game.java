package de.dhbw.ase.monopoly;

import java.util.Arrays;
import java.util.List;

import de.dhbw.ase.monopoly.spaces.BoardSpace;
import de.dhbw.ase.monopoly.spaces.PropertySpace;

public class Game {
  private final GameBoard gameBoard;
  private final Player[] players;
  private final EventReceiver eventReceiver;

  private int curPlayerIdx;
  private boolean canRollDice = true;
  private int consecutiveDoubles = 0;

  public Game(String[] playerPieces, EventReceiver eventReceiver) {
    gameBoard = new GameBoard(this, eventReceiver);
    players = Arrays.stream(playerPieces)
        .map(piece -> new Player(piece, gameBoard, eventReceiver))
        .toArray(Player[]::new);
    curPlayerIdx = (int) (Math.random() * playerPieces.length);
    this.eventReceiver = eventReceiver;
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
    if (!canRollDice) {
      eventReceiver.addEvent("You can not cast dice again.");
      return;
    }

    canRollDice = false;
    Player curPlayer = players[curPlayerIdx];
    int die1 = randomDice();
    int die2 = randomDice();

    if (curPlayer.isInJail()) {
      if (die1 == die2) {
        // leave jail for free after rolling doubles, use same roll to move
        eventReceiver.addEvent("You rolled doubles and got out of jail for free.");
        curPlayer.getOutOfJail(true);
      } else {
        curPlayer.incConsecutiveNotDoublesInJail();
        if (curPlayer.mustGetOutOfJail()) {
          // player must get out of jail after not rolling doubles for three rounds while
          // being in jail, use same roll to move
          eventReceiver.addEvent(
              "You did not roll doubles three rounds in a row and had to get out of jail at your own expense.");
          curPlayer.getOutOfJail(false);
        } else {
          eventReceiver.addEvent("You did not roll doubles and stay in jail.");
          return;
        }
      }
    } else { // player is not in jail
      if (die1 == die2) {
        consecutiveDoubles++;

        if (consecutiveDoubles == 3) {
          // player must to to jail after rolling doubles for three consecutive rounds
          eventReceiver.addEvent("You were caught speeding. Go to jail immediately.");
          curPlayer.goToJail();
          return;
        } else {
          // player can roll again after rolling doubles without going to jail
          eventReceiver.addEvent("You rolled doubles and can therefore roll another time.");
          canRollDice = true;
        }
      } else {
        consecutiveDoubles = 0;
      }
    }

    int steps = die1 + die2;
    curPlayer.moveForward(steps);
  }

  public boolean canRollDice() {
    return canRollDice;
  }

  public void buyProperty() {
    Player curPlayer = players[curPlayerIdx];
    BoardSpace curSpace = gameBoard.getSpace(curPlayer.getPosition());

    if (!curSpace.isBuyable()) {
      eventReceiver.addEvent("The property is not for sale.");
      return;
    }

    PropertySpace propertySpace = (PropertySpace) curSpace;
    int propertyPrice = propertySpace.getPrice();
    if (propertyPrice > curPlayer.getMoney()) {
      eventReceiver.addEvent("You can not afford this property");
      return;
    }

    curPlayer.buyProperty();
  }

  public void buildOnProperty(int propertyId) {
    Player curPlayer = players[curPlayerIdx];
    try {
      BuildingService.buildOnSpace(gameBoard, propertyId, curPlayer);
    } catch (InvalidMoveException exception) {
      eventReceiver.addEvent(exception.getMessage());
    }
  }

  public void unbuildOnProperty(int propertyId) {
    Player curPlayer = players[curPlayerIdx];
    try {
      BuildingService.unbuildOnSpace(gameBoard, propertyId, curPlayer);
    } catch (InvalidMoveException exception) {
      eventReceiver.addEvent(exception.getMessage());
    }
  }

  public void getOutOfJail() {
    Player curPlayer = players[curPlayerIdx];

    if (!curPlayer.isInJail()) {
      eventReceiver.addEvent("You are currently not in jail.");
      return;
    }

    if (!canRollDice) {
      eventReceiver.addEvent("You can only get out of jail at the beginning of your turn.");
      return;
    }

    if (curPlayer.getMoney() < 50 && curPlayer.getGetOutOfJailFreeCards() == 0) {
      eventReceiver.addEvent("You need at least $50 or a card to get out of jail.");
      return;
    }

    curPlayer.getOutOfJail(false);
    eventReceiver.addEvent("You got out of jail at your own expense.");
  }

  public void nextPlayer() {
    if (canRollDice) {
      eventReceiver.addEvent("You can roll the dice another time before ending your turn.");
      return;
    }

    Player curPlayer = players[curPlayerIdx];
    if (curPlayer.getMoney() < 0) {
      eventReceiver.addEvent(
          "You have to get out of debt before ending your turn. If that is not possible, you have to declare bankruptcy and leave the game.");
      return;
    }

    endTurn();
  }

  public void declareBankruptcy() {
    Player curPlayer = players[curPlayerIdx];
    if (curPlayer.getMoney() >= 0) {
      eventReceiver.addEvent("You still have money left.");
      return;
    }
    if (canRollDice) {
      eventReceiver.addEvent("You can roll the dice another time.");
      return;
    }

    curPlayer.makeBankrupt();
    endTurn();
    checkIfSinglePlayerIsLeft();
  }

  /**
   * transfer the specified sum of money from every other player in the game to
   * the current player
   */
  public void transferMoneyWithEveryPlayer(int money) {
    List<Player> activePlayers = Arrays.stream(players)
        .filter(player -> !player.isBankrupt())
        .toList();
    activePlayers.forEach(p -> p.transferMoney(-money));
    int totalMoney = money * activePlayers.size();
    players[curPlayerIdx].transferMoney(totalMoney);
  }

  private int randomDice() {
    return (int) (Math.random() * 5 + 1);
  }

  private void endTurn() {
    do {
      curPlayerIdx = (curPlayerIdx + 1) % players.length;
    } while (players[curPlayerIdx].isBankrupt());
    canRollDice = true;
  }

  private void checkIfSinglePlayerIsLeft() {
    long numberOfActivePlayers = Arrays.stream(players)
        .filter(player -> !player.isBankrupt())
        .count();
    if (numberOfActivePlayers == 1) {
      String piece = players[curPlayerIdx].getPiece();
      eventReceiver.addEvent(String.format("Player %s has won the game.", piece));
    }
  }
}
