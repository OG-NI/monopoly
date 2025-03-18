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
  public void enterSpace(Player player, int steps) {
    int cardIdx = (int) (Math.random() * actionCards.length);
    actionCards[cardIdx].performAction(player);
  }
}
