package de.dhbw.ase.monopoly.services;

import java.util.Optional;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.repositories.*;
import de.dhbw.ase.monopoly.entities.*;

public class BuyPropertyService {
  private final EventReceiver eventReceiver;
  private final PlayerRepository playerRepository;
  private final SpaceRepository spaceRepository;

  public BuyPropertyService(EventReceiver eventReceiver, PlayerRepository playerRepository,
      SpaceRepository spaceRepository) {
    this.eventReceiver = eventReceiver;
    this.playerRepository = playerRepository;
    this.spaceRepository = spaceRepository;
  }

  public void buyProperty() {
    Player currentPlayer = playerRepository.getCurrentPlayer();
    BoardSpace curSpace = spaceRepository.get(currentPlayer.getPosition());

    if (!curSpace.isBuyable()) {
      eventReceiver.addEvent("The property is not for sale.");
      return;
    }

    PropertySpace propertySpace = (PropertySpace) curSpace;
    int propertyPrice = propertySpace.getPrice();
    if (propertyPrice > currentPlayer.getMoney()) {
      eventReceiver.addEvent("You can not afford this property");
      return;
    }

    currentPlayer.transferMoney(-propertyPrice);
    playerRepository.update(currentPlayer);
    propertySpace.setOwner(Optional.of(currentPlayer));
    spaceRepository.update(propertySpace);
  }
}
