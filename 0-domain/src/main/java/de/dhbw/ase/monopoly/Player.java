package de.dhbw.ase.monopoly;

import java.util.Optional;

import de.dhbw.ase.monopoly.spaces.PropertySpace;

public class Player {
  private final static int BOARD_SIZE = 40;
  private final static int START_MONEY = 1500;

  private final String piece;
  private final GameBoard gameBoard;
  private final EventReceiver eventReceiver;

  private int money = START_MONEY;
  private int position = 0;
  private int getOutOfJailFreeCards = 0;
  private int consecutiveNotDoublesInJail = 0;
  private boolean isInJail = false;
  private boolean isBankrupt = false;

  public Player(String piece, GameBoard gameBoard, EventReceiver eventReceiver) {
    this.piece = piece;
    this.gameBoard = gameBoard;
    this.eventReceiver = eventReceiver;
  }

  public String getPiece() {
    return piece;
  }

  public int getMoney() {
    return money;
  }

  public void transferMoney(int amount) {
    money += amount;
  }

  public int getPosition() {
    return position;
  }

  public void moveForward(int steps) {
    position += steps;
    if (position >= BOARD_SIZE) {
      eventReceiver.addEvent("You passed go and collected $200.");
      position -= BOARD_SIZE;
      transferMoney(200);
    }
    gameBoard.enterSpace(position, this, steps);
  }

  public void moveToPosition(int position) {
    if (position > this.position) {
      moveForward(position - this.position);
    } else {
      moveForward(BOARD_SIZE + position - this.position);
    }
  }

  public void moveToNearestRailroad() {
    int shiftedPos = (position + 5) % BOARD_SIZE;
    int railroadIdx = shiftedPos / 10;
    int railroadPos = railroadIdx * 10 + 5;
    // TODO pay twice the rent
    moveToPosition(railroadPos);
  }

  public void moveToNearestUtility() {
    int shiftedPos = (position + 12) % BOARD_SIZE;
    int utilityIdx = shiftedPos / 24;
    int utilityPos = utilityIdx * 16 + 12;
    // TODO throw dice and pay owner ten times amount thrown
    moveToPosition(utilityPos);
  }

  public void buyProperty() {
    PropertySpace propertySpace = (PropertySpace) gameBoard.getSpace(position);
    transferMoney(-propertySpace.getPrice());
    propertySpace.setOwner(Optional.of(this));
  }

  public int getGetOutOfJailFreeCards() {
    return getOutOfJailFreeCards;
  }

  public void incGetOutOfJailFreeCards() {
    getOutOfJailFreeCards++;
  }

  /**
   * @return returns true if player has not rolled doubles three times in a row
   *         while being in jail
   */
  public boolean mustGetOutOfJail() {
    return consecutiveNotDoublesInJail >= 3;
  }

  public void incConsecutiveNotDoublesInJail() {
    consecutiveNotDoublesInJail++;
  }

  public boolean isInJail() {
    return isInJail;
  }

  /**
   * moves the player directly to jail without passing go and without receiving
   * $200
   */
  public void goToJail() {
    position = GameBoard.JAIL_POS;
    isInJail = true;
  }

  /**
   * @param leaveForFree if set to false, the player uses a get out of jail free
   *                     card or pays the fine
   */
  public void getOutOfJail(boolean leaveForFree) {
    isInJail = false;
    consecutiveNotDoublesInJail = 0;
    if (!leaveForFree) {
      if (getOutOfJailFreeCards > 0) {
        getOutOfJailFreeCards--;
      } else {
        transferMoney(-50);
      }
    }
  }

  public boolean isBankrupt() {
    return isBankrupt;
  }

  public void makeBankrupt() {
    isBankrupt = true;
  }
}
