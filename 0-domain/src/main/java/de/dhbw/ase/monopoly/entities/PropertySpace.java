package de.dhbw.ase.monopoly.entities;

import java.util.Optional;

import de.dhbw.ase.monopoly.*;

public abstract class PropertySpace extends BoardSpace {
  protected final int price;
  protected final int mortgage;
  protected Optional<Player> owner = Optional.empty();

  public PropertySpace(String name, int price, int mortgage, EventReceiver eventReceiver) {
    super(name, eventReceiver);
    this.price = price;
    this.mortgage = mortgage;
  }

  public int getPrice() {
    return price;
  }

  public Optional<Player> getOwner() {
    return owner;
  }

  public boolean isOwnedBy(Player player) {
    return owner.isPresent() && owner.get() == player;
  }

  public void setOwner(Optional<Player> owner) {
    this.owner = owner;
  }

  @Override
  public boolean isBuyable() {
    return owner.isEmpty();
  }

  @Override
  public void enterSpace(Player player, int steps) {
    eventReceiver.addEvent(String.format("You entered %s.", name));

    if (owner.isEmpty()) {
      eventReceiver.addEvent(String.format("The property is owned by the bank and can be bought for $%d.", price));
      return;
    }

    if (owner.get().equals(player)) {
      eventReceiver.addEvent("The property belongs to you.");
      return;
    }

    // TODO check for mortgaged property

    int rent = getRent(steps);
    player.transferMoney(-rent);
    owner.get().transferMoney(rent);
    eventReceiver.addEvent(String.format("The property is owned by %s who collected $%d in rent from you.",
        owner.get().getPiece(), rent));
  }

  public abstract int getRent(int steps);
}
