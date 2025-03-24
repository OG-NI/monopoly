package de.dhbw.ase.monopoly;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import de.dhbw.ase.monopoly.spaces.ColoredPropertySpace;
import de.dhbw.ase.monopoly.spaces.PropertySpace;

public class BuildingService {
  /**
   * advances the property with the given id to the next building level
   * 
   * @param propertyId index on the game board just including property spaces
   */
  public static String buildOnSpace(GameBoard gameBoard, int propertyId, Player player) {
    PropertySpace[] propertySpaces = gameBoard.getPropertySpaces();
    int maxPropertyId = propertySpaces.length - 1;

    // id out of range
    if (propertyId < 0 || propertyId > maxPropertyId) {
      return String.format("The property identifier must be between 0 and %d.", maxPropertyId);
    }
    PropertySpace propertySpace = propertySpaces[propertyId];

    // property does not belong to a color group
    if (!(propertySpace instanceof ColoredPropertySpace)) {
      return "You can not build on railroads and utility spaces.";
    }
    ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;

    // player is not the owner
    if (!coloredPropertySpace.isOwnedBy(player)) {
      return "You can only build on your own property.";
    }

    // player does not own the whole color group
    char color = coloredPropertySpace.getColor();
    if (!playerOwnsWholeColorGroup(gameBoard, color, player)) {
      return "You have to own all properties of a color group to start building.";
    }

    // player can not afford the building
    int buildingPrice = coloredPropertySpace.getBuildingPrice();
    if (player.getMoney() < buildingPrice) {
      return String.format("You can not afford to build for %d$.", buildingPrice);
    }

    // property already has a hotel
    int numberOfBuildings = coloredPropertySpace.getNumberOfBuildings();
    if (numberOfBuildings == 5) {
      return "You can not build more than one hotel on one property.";
    }

    // color group not built evenly
    int minNumberOfBuildings = getNumbersOfBuildings(gameBoard, color).min().orElseThrow();
    if (minNumberOfBuildings < numberOfBuildings) {
      return "Houses and hotels must be built evenly across all properties of the same color group.";
    }

    coloredPropertySpace.addBuilding();
    player.transferMoney(-buildingPrice);
    return "";
  }

  public static String unbuildOnSpace(GameBoard gameBoard, int propertyId, Player player) {
    PropertySpace[] propertySpaces = gameBoard.getPropertySpaces();
    int maxPropertyId = propertySpaces.length - 1;

    // id out of range
    if (propertyId < 0 || propertyId > maxPropertyId) {
      return String.format("The property identifier must be between 0 and %d.", maxPropertyId);
    }
    PropertySpace propertySpace = propertySpaces[propertyId];

    // property does not belong to a color group
    if (!(propertySpace instanceof ColoredPropertySpace)) {
      return "You can not build on railroads and utility spaces.";
    }
    ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;

    // player is not the owner
    if (!coloredPropertySpace.isOwnedBy(player)) {
      return "You can only build on your own property.";
    }

    // property does not have a building
    int numberOfBuildings = coloredPropertySpace.getNumberOfBuildings();
    if (numberOfBuildings == 0) {
      return "Property does not have a house or hotel to unbuild.";
    }

    // color group not unbuilt evenly
    char color = coloredPropertySpace.getColor();
    int maxNumberOfBuildings = getNumbersOfBuildings(gameBoard, color).max().orElseThrow();
    if (maxNumberOfBuildings > numberOfBuildings) {
      return "Houses and hotels must be built evenly across all properties of the same color group.";
    }

    int buildingPrice = coloredPropertySpace.getBuildingPrice();
    coloredPropertySpace.removeBuilding();
    player.transferMoney(buildingPrice / 2);
    return "";
  }

  public static void disownPlayerOfAllProperties(GameBoard gameBoard, Player player) {
    // TODO auction properties or give to player who is responsible for bankrupting
    // remove owner status
    Arrays.stream(gameBoard.getPropertySpaces())
        .filter(space -> space.isOwnedBy(player))
        .forEach(space -> space.setOwner(Optional.empty()));

    // TODO sell buildings and give money to player responsible for bankrupting
    // remove remaining buildings
    Arrays.stream(gameBoard.getColoredPropertySpaces())
        .filter(space -> space.isOwnedBy(player))
        .forEach(space -> {
          while (space.getNumberOfBuildings() > 0) {
            space.removeBuilding();
          }
        });
  }

  public static boolean playerOwnsWholeColorGroup(GameBoard gameBoard, char color, Player player) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    boolean existsPropertyNotOwnedByPlayer = Arrays.stream(coloredPropertySpaces)
        .anyMatch(space -> space.isOwnedBy(player) &&
            space.getColor() == color);
    return !existsPropertyNotOwnedByPlayer;
  }

  public static int getPlayerHouseCount(GameBoard gameBoard, Player player) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    return Arrays.stream(coloredPropertySpaces)
        .filter(space -> space.isOwnedBy(player))
        .mapToInt(ColoredPropertySpace::getNumberOfBuildings)
        .sum();
  }

  public static int getPlayerHotelCount(GameBoard gameBoard, Player player) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    return (int) Arrays.stream(coloredPropertySpaces)
        .filter(space -> space.isOwnedBy(player) &&
            space.getNumberOfBuildings() == 5)
        .count();
  }

  public static int getPlayerRailroadCount(GameBoard gameBoard, Player player) {
    return (int) Arrays.stream(gameBoard.getRailroadSpaces())
        .filter(space -> space.isOwnedBy(player))
        .count();
  }

  public static int getPlayerUtilityCount(GameBoard gameBoard, Player player) {
    return (int) Arrays.stream(gameBoard.getUtilitySpaces())
        .filter(space -> space.isOwnedBy(player))
        .count();
  }

  /**
   * get numbers of buildings on all properties in a color group
   * this is used to check if houses and hotels are built evenly
   */
  private static IntStream getNumbersOfBuildings(GameBoard gameBoard, char color) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    return Arrays.stream(coloredPropertySpaces)
        .filter(space -> space.getColor() == color)
        .mapToInt(space -> space.getNumberOfBuildings());
  }
}
