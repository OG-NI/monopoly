package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.BuildingService;
import de.dhbw.ase.monopoly.GameBoard;

public class RailroadSpace extends PropertySpace {
  private static final int[] rents = new int[] { 0, 25, 50, 100, 200 };

  public RailroadSpace(String name, GameBoard gameBoard) {
    super(name, 200, 100, gameBoard);
  }

  @Override
  public int getRent(int steps) {
    int ownedRailroads = BuildingService.getPlayerRailroadCount(gameBoard, owner.get());
    return rents[ownedRailroads];
  }
}
