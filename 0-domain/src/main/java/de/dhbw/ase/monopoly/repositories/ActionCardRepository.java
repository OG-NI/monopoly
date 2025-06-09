package de.dhbw.ase.monopoly.repositories;

import de.dhbw.ase.monopoly.ActionType;
import de.dhbw.ase.monopoly.valueobjects.ActionCard;

public interface ActionCardRepository {
  public void init(ActionCard[] actionCards);
  public ActionCard getRandomCardOfType(ActionType actionType);
}
