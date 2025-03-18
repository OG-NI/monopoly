package de.dhbw.ase.monopoly;

public abstract class ActionCard { // TODO: extends value object
  private final String text;

  public ActionCard(String text, Game game) {
    this.text = text;
  }

  public abstract void performAction(Player player);
}
