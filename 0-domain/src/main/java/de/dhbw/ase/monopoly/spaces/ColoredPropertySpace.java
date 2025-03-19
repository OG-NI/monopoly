package de.dhbw.ase.monopoly.spaces;

public class ColoredPropertySpace extends PropertySpace {
  private final char color;
  private final int buildingPrice;
  private final int[] rents;
  private int numberOfHouses = 0;

  public ColoredPropertySpace(String name, int price, int mortgage, char color, int buildingPrice, int[] rents) {
    super(name, price, mortgage);
    this.color = color;
    this.buildingPrice = buildingPrice;
    this.rents = rents;
  }

  public char getColor() {
    return color;
  }

  public int getNumberOfHouses() {
    return numberOfHouses;
  }

  public void addHouse() {
    if (numberOfHouses == 5) {
      throw new RuntimeException("House limit exceeded.");
    }

    numberOfHouses++;
  }

  @Override
  public int getRent(int steps) {
    boolean playerOwnsWholeColor = false; // TODO check if player owns whole color group
    if (numberOfHouses == 0 && playerOwnsWholeColor) {
      return 2 * rents[0];
    }

    return rents[numberOfHouses];
  }
}
