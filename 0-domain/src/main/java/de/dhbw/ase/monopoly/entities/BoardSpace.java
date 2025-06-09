package de.dhbw.ase.monopoly.entities;

import de.dhbw.ase.monopoly.*;

public abstract class BoardSpace {
  protected final String name;
  protected final EventReceiver eventReceiver;

  protected BoardSpace(String name, EventReceiver eventReceiver) {
    this.name = name;
    this.eventReceiver = eventReceiver;
  }

  public String getName() {
    return name;
  }

  public boolean isBuyable() {
    return false;
  }

  public abstract void enterSpace(Player player, int steps);
}
