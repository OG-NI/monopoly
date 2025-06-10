package de.dhbw.ase.monopoly.repositories;

import de.dhbw.ase.monopoly.entities.Player;

public interface PlayerRepository {
  public void init(Player[] players);

  public int getNumberOfSolventPlayers();

  public Player getCurrentPlayer();

  public Player getNextSolventPlayer();

  public Player[] getAllSolventPlayers();

  public Player[] getAllPlayers();

  public void update(Player player);

  public void updateAll(Player[] players);
}
