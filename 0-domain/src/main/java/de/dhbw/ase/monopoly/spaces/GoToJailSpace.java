package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class GoToJailSpace extends BoardSpace {
  public GoToJailSpace(EventReceiver eventReceiver) {
    super("Go to Jail", eventReceiver);
  }

  @Override
  public void enterSpace(Player player, int steps) {
    eventReceiver.addEvent("You entered the Go To Jail space and went directly to jail.");
    player.goToJail();
  }
}
