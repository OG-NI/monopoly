package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

/**
 * aggregate
 */
public class ActionSpace extends BoardSpace {
  private final ActionCard[] actionCards;

  public ActionSpace(String name, ActionCard[] actionCards, EventReceiver eventReceiver) {
    super(name, eventReceiver);
    this.actionCards = actionCards;
  }

  @Override
  public void enterSpace(Player player, int steps) {
    int cardIdx = (int) (Math.random() * actionCards.length);
    eventReceiver.addEvent(String.format("You entered a %s space and drew a card:", name));
    String cardText = actionCards[cardIdx].getText();
    eventReceiver.addEvent(cardText);
    actionCards[cardIdx].performAction(player);
  }
}
