package de.dhbw.ase.monopoly.spaces;

import java.util.Optional;
import de.dhbw.ase.monopoly.*;

public abstract class BuyableSpace extends BoardSpace {
  protected final int price;
  protected final int mortgage;
  protected Optional<Player> owner = Optional.empty();
  
  public BuyableSpace(String name, int price, int mortgage) {
    super(name);
    this.price = price;
    this.mortgage = mortgage;
  }

  @Override
  public void enterSpace(Player player, int steps) {
    // space is not yet owned by a player
    if (owner.isEmpty()) {
      // TODO buy or auction
      return;
    }

    // no action if player enters his own space
    if (owner.get().equals(player)) {
      return;
    }

    int rent = getRent(steps);
    player.transferMoney(-rent);
    owner.get().transferMoney(rent);
  }

  protected abstract int getRent(int steps);
}
