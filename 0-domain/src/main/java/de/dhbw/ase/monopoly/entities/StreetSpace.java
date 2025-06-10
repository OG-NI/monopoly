package de.dhbw.ase.monopoly.entities;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.services.PropertyCountService;

public class StreetSpace extends PropertySpace {
  private final char color;
  private final int buildingPrice;
  private final int[] rents;
  private int numberOfBuildings = 0;

  private final PropertyCountService propertyCountService;

  public StreetSpace(String name, int price, int mortgage, char color, int buildingPrice, int[] rents,
      EventReceiver eventReceiver, PropertyCountService propertyCountService) {
    super(name, price, mortgage, eventReceiver);
    this.color = color;
    this.buildingPrice = buildingPrice;
    this.rents = rents;
    this.propertyCountService = propertyCountService;
  }

  public char getColor() {
    return color;
  }

  public int getBuildingPrice() {
    return buildingPrice;
  }

  @Override
  public int getRent(int steps) {
    // twice the rent if player owns all properties of a color group but the
    // property has no buildings
    boolean playerOwnsWholeColor = propertyCountService.playerOwnsWholeColorGroup(owner.get(), color);
    if (numberOfBuildings == 0 && playerOwnsWholeColor) {
      return 2 * rents[numberOfBuildings];
    }

    return rents[numberOfBuildings];
  }

  public int getNumberOfBuildings() {
    return numberOfBuildings;
  }

  public boolean hasHotel() {
    return numberOfBuildings == 5;
  }

  public void addBuilding() {
    numberOfBuildings++;
  }

  public void removeBuilding() {
    numberOfBuildings--;
  }
}
