package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.BuildingService;
import de.dhbw.ase.monopoly.GameBoard;

public class ColoredPropertySpace extends PropertySpace {
  private final char color;
  private final int buildingPrice;
  private final int[] rents;
  private int numberOfBuildings = 0;

  public ColoredPropertySpace(String name, int price, int mortgage, GameBoard gameBoard,
      char color, int buildingPrice, int[] rents) {
    super(name, price, mortgage, gameBoard);
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
    // twice the rent if player owns all properties of a color group but the
    // property has no buildings
    boolean playerOwnsWholeColor = BuildingService.playerOwnsWholeColorGroup(gameBoard, color, owner.get());
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
