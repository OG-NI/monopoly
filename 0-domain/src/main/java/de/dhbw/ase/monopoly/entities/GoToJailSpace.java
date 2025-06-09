package de.dhbw.ase.monopoly.entities;

import de.dhbw.ase.monopoly.EventReceiver;
import de.dhbw.ase.monopoly.services.MovementService;

public class GoToJailSpace extends BoardSpace {
  private final MovementService movementService;

  public GoToJailSpace(EventReceiver eventReceiver, MovementService movementService) {
    super("Go to Jail", eventReceiver);
    this.movementService = movementService;
  }

  @Override
  public void enterSpace(Player player, int steps) {
    eventReceiver.addEvent("You entered the Go To Jail space and went directly to jail.");
    movementService.moveToJail();
  }
}
