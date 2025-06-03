package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class EmptySpace extends BoardSpace {
  public EmptySpace(String name, EventReceiver eventReceiver) {
    super(name, eventReceiver);
  }

  @Override
  public void enterSpace(Player player, int steps) {
    // do nothing
    eventReceiver.addEvent(String.format("You entered %s.", name));
  }
}
