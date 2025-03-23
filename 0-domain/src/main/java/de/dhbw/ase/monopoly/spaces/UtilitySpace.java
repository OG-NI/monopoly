package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.BuildingService;
import de.dhbw.ase.monopoly.GameBoard;

public class UtilitySpace extends PropertySpace {
  private static final int[] rentMultipliers = new int[] { 0, 4, 10 };

  public UtilitySpace(String name, GameBoard gameBoard) {
    super(name, 150, 75, gameBoard);
  }

  @Override
  public int getRent(int steps) {
    int ownedUtilities = BuildingService.getPlayerUtilityCount(gameBoard, owner.get());
    return rentMultipliers[ownedUtilities] * steps;
  }
}
