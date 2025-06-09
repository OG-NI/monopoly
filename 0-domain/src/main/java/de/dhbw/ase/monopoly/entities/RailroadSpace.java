package de.dhbw.ase.monopoly.entities;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.services.PropertyCountService;

public class RailroadSpace extends PropertySpace {
  private static final int[] rents = new int[] { 0, 25, 50, 100, 200 };
  private final PropertyCountService propertyCountService;

  public RailroadSpace(String name, EventReceiver eventReceiver, PropertyCountService propertyCountService) {
    super(name, 200, 100, eventReceiver);
    this.propertyCountService = propertyCountService;
  }

  @Override
  public int getRent(int steps) {
    int ownedRailroads = propertyCountService.getPlayerRailroadCount(owner.get());
    return rents[ownedRailroads];
  }
}
