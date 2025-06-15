package de.dhbw.ase.monopoly.repositories;

import de.dhbw.ase.monopoly.ActionType;
import de.dhbw.ase.monopoly.valueobjects.ActionCard;

public class MockActionCardRepository implements ActionCardRepository {
  private ActionCard actionCard;

  @Override
  public void init(ActionCard[] actionCards) {
  }

  @Override
  public ActionCard getRandomCardOfType(ActionType actionType) {
    return actionCard;
  }

  public void mockActionCard(ActionCard actionCard) {
    this.actionCard = actionCard;
  }
}
