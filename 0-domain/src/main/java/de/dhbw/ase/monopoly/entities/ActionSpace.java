package de.dhbw.ase.monopoly.entities;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.services.ActionCardService;

public class ActionSpace extends BoardSpace {
  private final ActionType actionType;
  private final ActionCardService actionCardService;

  public ActionSpace(ActionType actionType, EventReceiver eventReceiver, ActionCardService actionCardService) {
    super(actionType.toString(), eventReceiver);
    this.actionType = actionType;
    this.actionCardService = actionCardService;
  }

  @Override
  public void enterSpace(Player player, int steps) {
    eventReceiver.addEvent(String.format("You entered a %s space and drew a card:", name));
    actionCardService.performAction(actionType);
  }
}
