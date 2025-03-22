package de.dhbw.ase.monopoly;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    Optional<Player> owner = coloredPropertySpace.getOwner();

    // player is not the owner
    if (owner.isEmpty() || owner.get() != player) {
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
    if (!isColorGroupEvenlyBuilt(gameBoard, color, numberOfBuildings)) {
      return "Houses and hotels must be built evenly across all properties of the same color group.";
    }

    coloredPropertySpace.addBuilding();
    player.transferMoney(-buildingPrice);
    return "";
  }

  public static boolean playerOwnsWholeColorGroup(GameBoard gameBoard, char color, Player player) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    boolean existsPropertyNotOwnedByPlayer = Arrays.stream(coloredPropertySpaces)
        .anyMatch(space -> space.getColor() == color &&
            (!space.getOwner().isPresent() || space.getOwner().get() != player));
    return !existsPropertyNotOwnedByPlayer;
  }

  public static int getPlayerHouseCount(GameBoard gameBoard, Player player) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    return Arrays.stream(coloredPropertySpaces)
        .filter(space -> space.getOwner().isPresent() &&
            space.getOwner().get() == player &&
            space.getNumberOfBuildings() <= 4)
        .mapToInt(space -> space.getNumberOfBuildings())
        .sum();
  }

  public static int getPlayerHotelCount(GameBoard gameBoard, Player player) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    return (int) Arrays.stream(coloredPropertySpaces)
        .filter(space -> space.getOwner().isPresent() &&
            space.getOwner().get() == player &&
            space.getNumberOfBuildings() == 5)
        .count();
  }

  /**
   * returns true if all properties of the given color group have at least
   * the given number of buildings
   */
  private static boolean isColorGroupEvenlyBuilt(GameBoard gameBoard, char color, int minNumberOfBuildings) {
    ColoredPropertySpace[] coloredPropertySpaces = gameBoard.getColoredPropertySpaces();
    boolean existsPropertyBelowMinimumNumberOfHouses = Arrays.stream(coloredPropertySpaces)
        .anyMatch(space -> space.getColor() == color &&
            space.getNumberOfBuildings() < minNumberOfBuildings);
    return !existsPropertyBelowMinimumNumberOfHouses;
  }
}
