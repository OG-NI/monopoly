package de.dhbw.ase.monopoly;

public enum ActionType {
  COMMUNITY_CHEST("Community Chest"),
  CHANCE("Chance");

  private final String name;

  private ActionType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
