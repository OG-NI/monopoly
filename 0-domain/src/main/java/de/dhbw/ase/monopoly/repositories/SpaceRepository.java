package de.dhbw.ase.monopoly.repositories;

import java.util.List;

import de.dhbw.ase.monopoly.entities.*;

public interface SpaceRepository {
  public void init(BoardSpace[] spaces);
  public int getNumberOfSpaces();

  public BoardSpace get(int position);
  public PropertySpace[] getPropertySpaces();
  public StreetSpace[] getStreetSpaces();
  public RailroadSpace[] getRailroadSpaces();
  public UtilitySpace[] getUtilitySpaces();

  public void update(BoardSpace space);

  public int getJailPosition();
  public List<Integer> getPositionsByType(Class<? extends BoardSpace> spaceType);
  public int getPositionByName(String name);
}
