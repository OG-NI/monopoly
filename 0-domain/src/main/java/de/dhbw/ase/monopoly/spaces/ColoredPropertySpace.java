package de.dhbw.ase.monopoly.spaces;

public class ColoredPropertySpace extends PropertySpace {
  private final char color;
  private final int buildingPrice;
  private final int[] rents;
  private int numberOfBuildings = 0;

  public ColoredPropertySpace(String name, int price, int mortgage, char color, int buildingPrice, int[] rents) {
    super(name, price, mortgage);
    this.color = color;
    this.buildingPrice = buildingPrice;
    this.rents = rents;
  }

  public char getColor() {
    return color;
  }

  public int getBuildingPrice() {
    return buildingPrice;
  }

  @Override
  public int getRent(int steps) {
    boolean playerOwnsWholeColor = false; // TODO check if player owns whole color group
    if (numberOfBuildings == 0 && playerOwnsWholeColor) {
      return 2 * rents[0];
    }

    return rents[numberOfBuildings];
  }

  public int getNumberOfBuildings() {
    return numberOfBuildings;
  }

  public void addBuilding() {
    numberOfBuildings++;
  }

  public void removeBuilding() {
    numberOfBuildings--;
  }
}
