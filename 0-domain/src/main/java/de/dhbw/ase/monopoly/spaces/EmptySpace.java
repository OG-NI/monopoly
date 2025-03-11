package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class EmptySpace extends BoardSpace {
  public EmptySpace(String name) {
    super(name);
  }

  @Override
  public void enterSpace(Player player, int steps) {
    // do nothing
  }
}
