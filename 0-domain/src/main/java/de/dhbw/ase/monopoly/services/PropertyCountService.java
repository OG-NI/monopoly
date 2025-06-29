package de.dhbw.ase.monopoly.services;

import java.util.Arrays;

import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.repositories.*;

public class PropertyCountService {
  private final PlayerRepository playerRepository;
  private final SpaceRepository spaceRepository;

  public PropertyCountService(PlayerRepository playerRepository, SpaceRepository spaceRepository) {
    this.playerRepository = playerRepository;
    this.spaceRepository = spaceRepository;
  }

  public boolean playerOwnsWholeColorGroup(Player player, char color) {
    StreetSpace[] streetSpaces = spaceRepository.getStreetSpaces();

    boolean existsStreetNotOwnedByPlayer = Arrays.stream(streetSpaces)
        .anyMatch(s -> !s.isOwnedBy(player) && s.getColor() == color);
    return !existsStreetNotOwnedByPlayer;
  }

  public int getPlayerRailroadCount(Player player) {
    RailroadSpace[] railroadSpaces = spaceRepository.getRailroadSpaces();

    return (int) Arrays.stream(railroadSpaces)
        .filter(s -> s.isOwnedBy(player))
        .count();
  }

  public int getPlayerUtilityCount(Player player) {
    UtilitySpace[] utilitySpaces = spaceRepository.getUtilitySpaces();

    return (int) Arrays.stream(utilitySpaces)
        .filter(s -> s.isOwnedBy(player))
        .count();
  }

  public int getCurrentPlayerHouseCount() {
    Player currentPlayer = playerRepository.getCurrentPlayer();
    StreetSpace[] streetSpaces = spaceRepository.getStreetSpaces();

    return Arrays.stream(streetSpaces)
        .filter(s -> s.isOwnedBy(currentPlayer) && !s.hasHotel())
        .mapToInt(s -> s.getNumberOfBuildings())
        .sum();
  }

  public int getCurrentPlayerHotelCount() {
    Player currentPlayer = playerRepository.getCurrentPlayer();
    StreetSpace[] streetSpaces = spaceRepository.getStreetSpaces();

    return (int) Arrays.stream(streetSpaces)
        .filter(s -> s.isOwnedBy(currentPlayer) && s.hasHotel())
        .count();
  }

}
