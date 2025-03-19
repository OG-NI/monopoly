package de.dhbw.ase.monopoly.spaces;

import java.util.Optional;
import de.dhbw.ase.monopoly.*;

public abstract class PropertySpace extends BoardSpace {
  protected final int price;
  protected final int mortgage;
  protected Optional<Player> owner = Optional.empty();

  public PropertySpace(String name, int price, int mortgage) {
    super(name);
    this.price = price;
    this.mortgage = mortgage;
  }

  public int getPrice() {
    return price;
  }

  public Optional<Player> getOwner() {
    return owner;
  }

  public void setOwner(Optional<Player> owner) {
    this.owner = owner;
  }

  @Override
  public boolean isBuyable() {
    return owner.isEmpty();
  }

  @Override
  public String enterSpace(Player player, int steps) {
    String spaceMessage = String.format("You entered %s.", name);

    // space is not yet owned by a player
    if (owner.isEmpty()) {
      String buyMessage = String.format("The property is owned by the bank and can be bought for %d$.", price);
      return UtilService.joinMessages(spaceMessage, buyMessage);
    }

    // no action if player enters his own space
    if (owner.get().equals(player)) {
      return UtilService.joinMessages(spaceMessage, "The property belongs to you.");
    }

    // TODO check for mortgaged property

    int rent = getRent(steps);
    player.transferMoney(-rent);
    owner.get().transferMoney(rent);
    String rentMessage = String.format("The property is owned by %s who collected %s$ in rent from you.",
        owner.get().getPiece(), rent);
    return UtilService.joinMessages(spaceMessage, rentMessage);
  }

  public abstract int getRent(int steps);
}
