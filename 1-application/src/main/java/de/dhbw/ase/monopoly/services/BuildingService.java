package de.dhbw.ase.monopoly.services;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.repositories.*;

public class BuildingService {
  private final EventReceiver eventReceiver;
  private final PlayerRepository playerRepository;
  private final SpaceRepository spaceRepository;
  private final PropertyCountService propertyCountService;

  public BuildingService(EventReceiver eventReceiver, PlayerRepository playerRepository,
      SpaceRepository spaceRepository,
      PropertyCountService propertyCountService) {
    this.eventReceiver = eventReceiver;
    this.playerRepository = playerRepository;
    this.spaceRepository = spaceRepository;
    this.propertyCountService = propertyCountService;
  }

  /**
   * advances the property with the given id to the next building level
   * 
   * @param propertyId index on the game board just including property spaces
   */
  public void buildOnSpace(int propertyId) {
    StreetSpace streetSpace;
    try {
      streetSpace = parsePropertyId(propertyId);
    } catch (PropertyIdException exception) {
      return;
    }

    // player does not own the whole color group
    Player currentPlayer = playerRepository.getCurrentPlayer();
    char color = streetSpace.getColor();
    if (!propertyCountService.playerOwnsWholeColorGroup(currentPlayer, color)) {
      eventReceiver.addEvent("You have to own all properties of a color group to start building.");
      return;
    }

    // player can not afford the building
    int buildingPrice = streetSpace.getBuildingPrice();
    if (currentPlayer.getMoney() < buildingPrice) {
      eventReceiver.addEvent(String.format("You can not afford to build for $%d.", buildingPrice));
      return;
    }

    // property already has a hotel
    int numberOfBuildings = streetSpace.getNumberOfBuildings();
    if (numberOfBuildings == 5) {
      eventReceiver.addEvent("You can not build more than one hotel on one property.");
      return;
    }

    // color group not built evenly
    int minNumberOfBuildings = getNumbersOfBuildings(color).min().orElseThrow();
    if (minNumberOfBuildings < numberOfBuildings) {
      eventReceiver.addEvent(
          "Houses and hotels must be built evenly across all properties of the same color group.");
      return;
    }

    streetSpace.addBuilding();
    spaceRepository.update(streetSpace);
    currentPlayer.transferMoney(-buildingPrice);
    playerRepository.update(currentPlayer);
  }

  public void unbuildOnSpace(int propertyId) {
    StreetSpace streetSpace;
    try {
      streetSpace = parsePropertyId(propertyId);
    } catch (PropertyIdException exception) {
      return;
    }

    // property does not have a building
    int numberOfBuildings = streetSpace.getNumberOfBuildings();
    if (numberOfBuildings == 0) {
      eventReceiver.addEvent("Property does not have a house or hotel to unbuild.");
      return;
    }

    // color group not unbuilt evenly
    char color = streetSpace.getColor();
    int maxNumberOfBuildings = getNumbersOfBuildings(color).max().orElseThrow();
    if (maxNumberOfBuildings > numberOfBuildings) {
      eventReceiver.addEvent(
          "Houses and hotels must be built evenly across all properties of the same color group.");
      return;
    }

    Player currentPlayer = playerRepository.getCurrentPlayer();
    int buildingPrice = streetSpace.getBuildingPrice();
    streetSpace.removeBuilding();
    spaceRepository.update(streetSpace);
    currentPlayer.transferMoney(buildingPrice / 2);
    playerRepository.update(currentPlayer);
  }

  public void disownPlayerOfAllProperties(Player player) {
    // TODO auction properties or give to player who is responsible for bankrupting
    // remove owner status
    Arrays.stream(spaceRepository.getPropertySpaces())
        .filter(space -> space.isOwnedBy(player))
        .forEach(space -> space.setOwner(Optional.empty()));

    // TODO sell buildings and give money to player responsible for bankrupting
    // remove remaining buildings
    Arrays.stream(spaceRepository.getStreetSpaces())
        .filter(space -> space.isOwnedBy(player))
        .forEach(space -> {
          while (space.getNumberOfBuildings() > 0) {
            space.removeBuilding();
          }
        });
  }

  private StreetSpace parsePropertyId(int propertyId) throws PropertyIdException {
    PropertySpace[] propertySpaces = spaceRepository.getPropertySpaces();
    int maxPropertyId = propertySpaces.length - 1;

    // id out of range
    if (propertyId < 0 || propertyId > maxPropertyId) {
      eventReceiver.addEvent(String.format("The property identifier must be between 0 and %d.", maxPropertyId));
      throw new PropertyIdException();
    }
    PropertySpace propertySpace = propertySpaces[propertyId];

    if (!(propertySpace instanceof StreetSpace)) {
      eventReceiver.addEvent("You can not build on railroads and utility spaces.");
      throw new PropertyIdException();
    }
    StreetSpace streetSpace = (StreetSpace) propertySpace;

    Player currentPlayer = playerRepository.getCurrentPlayer();
    if (!streetSpace.isOwnedBy(currentPlayer)) {
      eventReceiver.addEvent("You can only build on your own property.");
      throw new PropertyIdException();
    }

    return streetSpace;
  }

  /**
   * get numbers of buildings on all properties in a color group
   * this is used to check if houses and hotels are built evenly
   */
  private IntStream getNumbersOfBuildings(char color) {
    StreetSpace[] streetSpaces = spaceRepository.getStreetSpaces();
    return Arrays.stream(streetSpaces)
        .filter(space -> space.getColor() == color)
        .mapToInt(space -> space.getNumberOfBuildings());
  }

  private class PropertyIdException extends Exception {
  }
}
