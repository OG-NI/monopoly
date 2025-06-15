package de.dhbw.ase.monopoly.services;

import de.dhbw.ase.monopoly.entities.Player;

public class MockPropertyCountService extends PropertyCountService {
  private boolean playerOwnsWholeColorGroup;

  public MockPropertyCountService() {
    super(null, null);
  }

  public void mockPlayerOwnsWholeColorGroup(boolean playerOwnsWholeColorGroup) {
    this.playerOwnsWholeColorGroup = playerOwnsWholeColorGroup;
  }

  @Override
  public boolean playerOwnsWholeColorGroup(Player player, char color) {
    return playerOwnsWholeColorGroup;
  }

  @Override
  public int getCurrentPlayerHouseCount() {
    return 2;
  }
}
