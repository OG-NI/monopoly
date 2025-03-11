package de.dhbw.ase.monopoly.spaces;

public class PropertySpace extends BuyableSpace {
  private final char color;
  private final int housePrice;
  private final int[] rents;
  private int numberOfHouses = 0;

  public PropertySpace(String name, int price, int mortgage, char color, int housePrice, int[] rents) {
    super(name, price, mortgage);
    this.color = color;
    this.housePrice = housePrice;
    this.rents = rents;
  }

  @Override
  protected int getRent(int steps) {
    boolean playerOwnsWholeColor = false; // TODO check if player owns whole color group
    if (numberOfHouses == 0 && playerOwnsWholeColor) {
      return 2 * rents[0];
    }

    return rents[numberOfHouses];
  }
}
