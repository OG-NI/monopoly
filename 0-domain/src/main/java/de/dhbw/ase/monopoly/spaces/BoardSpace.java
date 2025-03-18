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

  public abstract void enterSpace(Player player, int steps);
}
