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

  public BuildingService(EventReceiver eventReceiver, PlayerRepository playerRepository, SpaceRepository spaceRepository,
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
    Player currentPlayer = playerRepository.getCurrentPlayer();
    PropertySpace[] propertySpaces = spaceRepository.getPropertySpaces();
    int maxPropertyId = propertySpaces.length - 1;

    // id out of range
    if (propertyId < 0 || propertyId > maxPropertyId) {
      eventReceiver.addEvent(String.format("The property identifier must be between 0 and %d.", maxPropertyId));
      return;
    }
    PropertySpace propertySpace = propertySpaces[propertyId];

    // property does not belong to a color group
    if (!(propertySpace instanceof ColoredPropertySpace)) {
      eventReceiver.addEvent("You can not build on railroads and utility spaces.");
      return;
    }
    ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;

    // player is not the owner
    if (!coloredPropertySpace.isOwnedBy(currentPlayer)) {
      eventReceiver.addEvent("You can only build on your own property.");
      return;
    }

    // player does not own the whole color group
    char color = coloredPropertySpace.getColor();
    if (!propertyCountService.playerOwnsWholeColorGroup(currentPlayer, color)) {
      eventReceiver.addEvent("You have to own all properties of a color group to start building.");
      return;
    }

    // player can not afford the building
    int buildingPrice = coloredPropertySpace.getBuildingPrice();
    if (currentPlayer.getMoney() < buildingPrice) {
      eventReceiver.addEvent(String.format("You can not afford to build for $%d.", buildingPrice));
      return;
    }

    // property already has a hotel
    int numberOfBuildings = coloredPropertySpace.getNumberOfBuildings();
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

    coloredPropertySpace.addBuilding();
    currentPlayer.transferMoney(-buildingPrice);
  }

  public void unbuildOnSpace(int propertyId) {
    Player currentPlayer = playerRepository.getCurrentPlayer();
    PropertySpace[] propertySpaces = spaceRepository.getPropertySpaces();
    int maxPropertyId = propertySpaces.length - 1;

    // id out of range
    if (propertyId < 0 || propertyId > maxPropertyId) {
      eventReceiver.addEvent(String.format("The property identifier must be between 0 and %d.", maxPropertyId));
      return;
    }
    PropertySpace propertySpace = propertySpaces[propertyId];

    // property does not belong to a color group
    if (!(propertySpace instanceof ColoredPropertySpace)) {
      eventReceiver.addEvent("You can not build on railroads and utility spaces.");
      return;
    }
    ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;

    // player is not the owner
    if (!coloredPropertySpace.isOwnedBy(currentPlayer)) {
      eventReceiver.addEvent("You can only build on your own property.");
      return;
    }

    // property does not have a building
    int numberOfBuildings = coloredPropertySpace.getNumberOfBuildings();
    if (numberOfBuildings == 0) {
      eventReceiver.addEvent("Property does not have a house or hotel to unbuild.");
      return;
    }

    // color group not unbuilt evenly
    char color = coloredPropertySpace.getColor();
    int maxNumberOfBuildings = getNumbersOfBuildings(color).max().orElseThrow();
    if (maxNumberOfBuildings > numberOfBuildings) {
      eventReceiver.addEvent(
          "Houses and hotels must be built evenly across all properties of the same color group.");
      return;
    }

    int buildingPrice = coloredPropertySpace.getBuildingPrice();
    coloredPropertySpace.removeBuilding();
    currentPlayer.transferMoney(buildingPrice / 2);
  }

  public void disownPlayerOfAllProperties(Player player) {
    // TODO auction properties or give to player who is responsible for bankrupting
    // remove owner status
    Arrays.stream(spaceRepository.getPropertySpaces())
        .filter(space -> space.isOwnedBy(player))
        .forEach(space -> space.setOwner(Optional.empty()));

    // TODO sell buildings and give money to player responsible for bankrupting
    // remove remaining buildings
    Arrays.stream(spaceRepository.getColoredPropertySpaces())
        .filter(space -> space.isOwnedBy(player))
        .forEach(space -> {
          while (space.getNumberOfBuildings() > 0) {
            space.removeBuilding();
          }
        });
  }

  /**
   * get numbers of buildings on all properties in a color group
   * this is used to check if houses and hotels are built evenly
   */
  private IntStream getNumbersOfBuildings(char color) {
    ColoredPropertySpace[] coloredPropertySpaces = spaceRepository.getColoredPropertySpaces();
    return Arrays.stream(coloredPropertySpaces)
        .filter(space -> space.getColor() == color)
        .mapToInt(space -> space.getNumberOfBuildings());
  }
}
