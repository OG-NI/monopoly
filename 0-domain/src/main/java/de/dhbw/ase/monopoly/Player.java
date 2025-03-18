package de.dhbw.ase.monopoly;

public class Player {
  private final static int BOARD_SIZE = 40;
  private final static int START_MONEY = 1500;
  private final static int COLLECT_ON_GO_MONEY = 200;
  private final static int GET_OUT_OF_JAIL_MONEY = -50;

  private final String piece;
  private final GameBoard gameBoard;

  private int money = START_MONEY;
  private int position = 0;
  private int ownedRailroads = 0;
  private int ownedUtilities = 0;
  private int ownedHouses = 0;
  private int ownedHotels = 0;
  private int getOutOfJailFreeCards = 0;
  private int consecutiveNotDoublesInJail = 0;
  private boolean isInJail = false;

  public Player(String piece, GameBoard gameBoard) {
    this.piece = piece;
    this.gameBoard = gameBoard;
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
      position -= BOARD_SIZE;
      money += COLLECT_ON_GO_MONEY;
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

  public int getOwnedRailroads() {
    return ownedRailroads;
  }

  public int getOwnedUtilities() {
    return ownedUtilities;
  }

  public int getOwnedHouses() {
    return ownedHouses;
  }

  public int getOwnedHotels() {
    return ownedHotels;
  }

  public int getGetOutOfJailFreeCards() {
    return getOutOfJailFreeCards;
  }

  public void incGetOutOfJailFreeCards() {
    getOutOfJailFreeCards++;
  }

  public void incConsecutiveNotDoublesInJail() {
    consecutiveNotDoublesInJail++;
  }

  /**
   * @return returns true if player has not rolled doubles three times in a row
   *         while being in jail
   */
  public boolean mustGetOutOfJail() {
    return consecutiveNotDoublesInJail >= 3;
  }

  public boolean isInJail() {
    return isInJail;
  }

  /**
   * moves the player directly to jail without passing go and without receiving
   * 200$
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
        transferMoney(GET_OUT_OF_JAIL_MONEY);
      }
    }
  }
}
