package de.dhbw.ase.monopoly.services;

import de.dhbw.ase.monopoly.entities.Player;
import de.dhbw.ase.monopoly.entities.PropertySpace;
import de.dhbw.ase.monopoly.repositories.PlayerRepository;
import de.dhbw.ase.monopoly.repositories.SpaceRepository;

public class GameStateService {
  private final PlayerRepository playerRepository;
  private final SpaceRepository spaceRepository;

  public GameStateService(PlayerRepository playerRepository, SpaceRepository spaceRepository) {
    this.playerRepository = playerRepository;
    this.spaceRepository = spaceRepository;
  }

  public Player[] getPlayersState() {
    return playerRepository.getAllPlayers();
  }

  public Player getCurrentPlayerState() {
    return playerRepository.getCurrentPlayer();
  }

  public PropertySpace[] getPropertySpacesState() {
    return spaceRepository.getPropertySpaces();
  }
}
