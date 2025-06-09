package de.dhbw.ase.monopoly.valueobjects;

import java.util.Objects;

import de.dhbw.ase.monopoly.ActionType;
import de.dhbw.ase.monopoly.entities.BoardSpace;

public final class ActionCard {
  public final ActionType actionType;
  public final String text;

  public final int moneyTransferWithBank;
  public final int moneyTransferWithAllPlayers;
  public final int moneyPerHouse;
  public final int moneyPerHotel;

  public final Class<? extends BoardSpace> moveToSpaceByType;
  public final String moveToSpaceByName;
  public final int moveSteps;
  public final boolean moveToJail;

  public final boolean getOutOfJailFreeCard;

  private ActionCard(ActionType actionType, String text, int moneyTransferWithBank, int moneyTransferWithAllPlayers,
      int moneyPerHouse, int moneyPerHotel, Class<? extends BoardSpace> moveToSpaceByType, String moveToSpaceByName,
      int moveSteps, boolean moveToJail, boolean getOutOfJailFreeCard) {
    this.actionType = actionType;
    this.text = text;
    this.moneyTransferWithBank = moneyTransferWithBank;
    this.moneyTransferWithAllPlayers = moneyTransferWithAllPlayers;
    this.moneyPerHouse = moneyPerHouse;
    this.moneyPerHotel = moneyPerHotel;
    this.moveToSpaceByType = moveToSpaceByType;
    this.moveToSpaceByName = moveToSpaceByName;
    this.moveSteps = moveSteps;
    this.moveToJail = moveToJail;
    this.getOutOfJailFreeCard = getOutOfJailFreeCard;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (!(object instanceof ActionCard)) {
      return false;
    }

    ActionCard actionCard = (ActionCard) object;

    return actionType == actionCard.actionType
        && text.equals(actionCard.text)
        && moneyTransferWithBank == actionCard.moneyTransferWithBank
        && moneyTransferWithAllPlayers == actionCard.moneyTransferWithAllPlayers
        && moneyPerHouse == actionCard.moneyPerHouse
        && moneyPerHotel == actionCard.moneyPerHotel
        && moveToSpaceByType.equals(actionCard.moveToSpaceByType)
        && moveSteps == actionCard.moveSteps
        && moveToJail == actionCard.moveToJail
        && getOutOfJailFreeCard == actionCard.getOutOfJailFreeCard;
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionType, text, moneyTransferWithBank, moneyTransferWithAllPlayers, moneyPerHouse,
        moneyPerHotel, moveToSpaceByType, moveToSpaceByName, moveSteps, moveToJail, getOutOfJailFreeCard);
  }

  public static final class Builder {
    private final ActionType actionType;
    private final String text;

    private int moneyTransferWithBank;
    private int moneyTransferWithAllPlayers;
    private int moneyPerHouse;
    private int moneyPerHotel;

    private Class<? extends BoardSpace> moveToSpaceByType;
    private String moveToSpaceByName;
    private int moveSteps;
    private boolean moveToJail;

    private boolean getOutOfJailFreeCard;

    public Builder(ActionType actionType, String text) {
      this.actionType = actionType;
      this.text = text;
    }

    public Builder moneyTransferWithBank(int moneyTransferWithBank) {
      this.moneyTransferWithBank = moneyTransferWithBank;
      return this;
    }

    public Builder moneyTransferWithAllPlayers(int moneyTransferWithAllPlayers) {
      this.moneyTransferWithAllPlayers = moneyTransferWithAllPlayers;
      return this;
    }

    public Builder moneyPerHouse(int moneyPerHouse) {
      this.moneyPerHouse = moneyPerHouse;
      return this;
    }

    public Builder moneyPerHotel(int moneyPerHotel) {
      this.moneyPerHotel = moneyPerHotel;
      return this;
    }

    public Builder moveToSpaceByType(Class<? extends BoardSpace> moveToSpaceByType) {
      this.moveToSpaceByType = moveToSpaceByType;
      return this;
    }

    public Builder moveToSpaceByName(String moveToSpaceByName) {
      this.moveToSpaceByName = moveToSpaceByName;
      return this;
    }

    public Builder moveSteps(int moveSteps) {
      this.moveSteps = moveSteps;
      return this;
    }

    public Builder moveToJail() {
      this.moveToJail = true;
      return this;
    }

    public Builder getOutOfJailFreeCard() {
      this.getOutOfJailFreeCard = true;
      return this;
    }

    public ActionCard build() {
      return new ActionCard(actionType, text, moneyTransferWithBank, moneyTransferWithAllPlayers, moneyPerHouse,
          moneyPerHotel, moveToSpaceByType, moveToSpaceByName, moveSteps, moveToJail, getOutOfJailFreeCard);
    }
  }
}
