package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public abstract class BoardSpace {
  protected final String name;

  protected BoardSpace(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean isBuyable() {
    return false;
  }

  public abstract String enterSpace(Player player, int steps);
}
