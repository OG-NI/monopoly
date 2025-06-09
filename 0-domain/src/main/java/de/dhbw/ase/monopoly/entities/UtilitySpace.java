package de.dhbw.ase.monopoly.entities;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.services.PropertyCountService;

public class UtilitySpace extends PropertySpace {
  private static final int[] rentMultipliers = new int[] { 0, 4, 10 };
  private final PropertyCountService propertyCountService;

  public UtilitySpace(String name, EventReceiver eventReceiver, PropertyCountService propertyCountService) {
    super(name, 150, 75, eventReceiver);
    this.propertyCountService = propertyCountService;
  }

  @Override
  public int getRent(int steps) {
    int ownedUtilities = propertyCountService.getPlayerUtilityCount(owner.get());
    return rentMultipliers[ownedUtilities] * steps;
  }
}
