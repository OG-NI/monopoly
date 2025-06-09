package de.dhbw.ase.monopoly.repositories;

import java.util.Arrays;

import de.dhbw.ase.monopoly.entities.Player;

public class PlayerRepositoryInMemory implements PlayerRepository {
  private Player[] players;

  @Override
  public void init(Player[] players) {
    this.players = players;
  }

  @Override
  public int getNumberOfPlayers() {
    return players.length;
  }

  @Override
  public int getNumberOfSolventPlayers() {
    return (int) Arrays.stream(players)
        .filter(player -> !player.isBankrupt())
        .count();
  }

  @Override
  public Player getCurrentPlayer() {
    return Arrays.stream(players).filter(Player::isActive).findAny().get();
  }

  @Override
  public Player getNextSolventPlayer() {
    int playerIndex = -1;
    for (int i = 0; i < players.length; i++) {
      if (players[i].isActive()) {
        playerIndex = i;
        break;
      }
    }

    do {
      playerIndex = (playerIndex + 1) % players.length;
    } while (players[playerIndex].isBankrupt());

    return players[playerIndex];
  }

  @Override
  public Player[] getAllSolventPlayers() {
    return Arrays.stream(players)
        .filter(p -> !p.isBankrupt())
        .toArray(Player[]::new);
  }

  @Override
  public Player[] getAllPlayers() {
    return players;
  }

  @Override
  public void update(Player player) {
    // not required, since objects are passed by reference
  }

  @Override
  public void updateAll(Player[] players) {
    // not required, since objects are passed by reference
  }
}
