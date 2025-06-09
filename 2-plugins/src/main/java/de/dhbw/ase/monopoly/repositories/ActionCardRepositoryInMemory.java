package de.dhbw.ase.monopoly.repositories;

import java.util.Arrays;

import de.dhbw.ase.monopoly.ActionType;
import de.dhbw.ase.monopoly.valueobjects.ActionCard;

public class ActionCardRepositoryInMemory implements ActionCardRepository {
  private ActionCard[] actionCards;

  public void init(ActionCard[] actionCards) {
    this.actionCards = actionCards;
  }

  @Override
  public ActionCard getRandomCardOfType(ActionType actionType) {
    ActionCard[] actionCardsOfType = Arrays.stream(actionCards)
        .filter(c -> c.actionType == actionType)
        .toArray(ActionCard[]::new);
    int randomIndex = (int) (Math.random() * actionCardsOfType.length);
    return actionCardsOfType[randomIndex];
  }
}
