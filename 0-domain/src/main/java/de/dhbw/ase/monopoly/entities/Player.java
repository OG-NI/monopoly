package de.dhbw.ase.monopoly.entities;

public class Player {
  private final String piece;

  private int money = 1500;
  private int position = 0;
  private int getOutOfJailFreeCards = 0;
  private int consecutiveDoubles = 0;
  private int consecutiveNotDoublesInJail = 0;
  private boolean isActive = false;
  private boolean isInJail = false;
  private boolean isBankrupt = false;
  private boolean canRollDice = false;

  public Player(String piece) {
    this.piece = piece;
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

  public void setPosition(int position) {
    this.position = position;
  }

  public int getGetOutOfJailFreeCards() {
    return getOutOfJailFreeCards;
  }

  public void incGetOutOfJailFreeCards() {
    getOutOfJailFreeCards++;
  }

  public boolean mustGoToJailForRollingConsecutiveDoubles() {
    return consecutiveDoubles == 3;
  }

  public void incConsecutiveDoubles() {
    consecutiveDoubles++;
  }

  public void resetConsecutiveDoubles() {
    consecutiveDoubles = 0;
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

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public void goToJail() {
    isInJail = true;
    setCanRollDice(false);
  }

  public boolean isInJail() {
    return isInJail;
  }

  public void leaveJailForFree() {
    isInJail = false;
    consecutiveNotDoublesInJail = 0;
  }

  public void leaveJailByCardOrFee() {
    leaveJailForFree();
    if (getOutOfJailFreeCards > 0) {
      getOutOfJailFreeCards--;
    } else {
      transferMoney(-50);
    }
  }

  public boolean isBankrupt() {
    return isBankrupt;
  }

  public void makeBankrupt() {
    isBankrupt = true;
  }

  public boolean canRollDice() {
    return canRollDice;
  }

  public void setCanRollDice(boolean canRollDice) {
    this.canRollDice = canRollDice;
  }
}
