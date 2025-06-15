package de.dhbw.ase.monopoly.repositories;

import de.dhbw.ase.monopoly.entities.Player;

public class MockPlayerRepository implements PlayerRepository {
  private Player player1, player2;

  public MockPlayerRepository() {
    player1 = new Player("a");
    player2 = new Player("b");
  }

  @Override
  public void init(Player[] players) {
  }

  @Override
  public int getNumberOfSolventPlayers() {
    return 2;
  }

  @Override
  public Player getCurrentPlayer() {
    return player1;
  }

  @Override
  public Player getNextSolventPlayer() {
    return player2;
  }

  @Override
  public Player[] getAllSolventPlayers() {
    return new Player[] { player1, player2 };
  }

  @Override
  public Player[] getAllPlayers() {
    return new Player[] { player1, player2 };
  }

  @Override
  public void update(Player player) {
  }

  @Override
  public void updateAll(Player[] players) {
  }
}
