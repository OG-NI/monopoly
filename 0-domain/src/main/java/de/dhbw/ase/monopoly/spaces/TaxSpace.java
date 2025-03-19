package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class TaxSpace extends BoardSpace {
  private final int tax;

  public TaxSpace(String name, int tax) {
    super(name);
    this.tax = tax;
  }

  @Override
  public String enterSpace(Player player, int steps) {
    player.transferMoney(-tax);
    return String.format("You were charged %d in %s.", tax, name);
  }
}
