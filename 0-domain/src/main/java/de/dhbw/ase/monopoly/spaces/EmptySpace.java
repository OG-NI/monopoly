package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class EmptySpace extends BoardSpace {
  public EmptySpace(String name) {
    super(name);
  }

  @Override
  public String enterSpace(Player player, int steps) {
    // do nothing
    return String.format("You entered %s.", name);
  }
}
