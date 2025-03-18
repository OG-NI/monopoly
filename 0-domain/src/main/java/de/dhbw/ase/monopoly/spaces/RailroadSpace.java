package de.dhbw.ase.monopoly.spaces;

public class RailroadSpace extends BuyableSpace {
  private static final int[] rents = new int[] { 0, 25, 50, 100, 200 };

  public RailroadSpace(String name) {
    super(name, 200, 100);
  }

  @Override
  public int getRent(int steps) {
    int ownedRailroads = owner.get().getOwnedRailroads();
    return rents[ownedRailroads];
  }
}
