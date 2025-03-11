package de.dhbw.ase.monopoly.spaces;

public class UtilitySpace extends BuyableSpace {
  private static final int[] rentMultipliers = new int[] { 0, 4, 10 };

  public UtilitySpace(String name) {
    super(name, 150, 75);
  }

  @Override
  protected int getRent(int steps) {
    int ownedUtilities = owner.get().getOwnedUtilities();
    return rentMultipliers[ownedUtilities] * steps;
  }
}
