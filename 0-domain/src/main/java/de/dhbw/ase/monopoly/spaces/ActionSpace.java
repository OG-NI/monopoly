package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

/**
 * aggregate
 */
public class ActionSpace extends BoardSpace {
  private final ActionCard[] actionCards;

  public ActionSpace(String name, ActionCard[] actionCards) {
    super(name);
    this.actionCards = actionCards;
  }

  @Override
  public String enterSpace(Player player, int steps) {
    int cardIdx = (int) (Math.random() * actionCards.length);
    String enterMessage = String.format("You entered a %s space and drew a card:", name);
    String cardMessage = actionCards[cardIdx].getText();
    String actionMessage = actionCards[cardIdx].performAction(player);
    return UtilService.joinMessages(enterMessage, cardMessage, actionMessage);
  }
}
