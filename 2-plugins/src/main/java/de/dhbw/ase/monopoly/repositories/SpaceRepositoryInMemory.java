package de.dhbw.ase.monopoly.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dhbw.ase.monopoly.entities.*;

public class SpaceRepositoryInMemory implements SpaceRepository {
  private BoardSpace[] spaces;

  @Override
  public void init(BoardSpace[] spaces) {
    this.spaces = spaces;
  }

  @Override
  public int getNumberOfSpaces() {
    return spaces.length;
  }

  @Override
  public BoardSpace get(int position) {
    return spaces[position];
  }

  @Override
  public int getJailPosition() {
    for (int i = 0; i < getNumberOfSpaces(); i++) {
      // TODO find a better way to identify jail space
      if (spaces[i].getName().equals("Jail Just Visiting")) {
        return i;
      }
    }
    throw new IllegalStateException("Spaces must contain a jail.");
  }

  @Override
  public PropertySpace[] getPropertySpaces() {
    return Arrays.stream(spaces)
        .filter(s -> s instanceof PropertySpace)
        .toArray(PropertySpace[]::new);
  }

  @Override
  public ColoredPropertySpace[] getColoredPropertySpaces() {
    return Arrays.stream(spaces)
        .filter(s -> s instanceof ColoredPropertySpace)
        .toArray(ColoredPropertySpace[]::new);
  }

  @Override
  public RailroadSpace[] getRailroadSpaces() {
    return Arrays.stream(spaces)
        .filter(s -> s instanceof RailroadSpace)
        .toArray(RailroadSpace[]::new);
  }

  @Override
  public UtilitySpace[] getUtilitySpaces() {
    return Arrays.stream(spaces)
        .filter(s -> s instanceof UtilitySpace)
        .toArray(UtilitySpace[]::new);
  }

  @Override
  public void update(BoardSpace space) {
    // not required, since objects are passed by reference
  }

  @Override
  public List<Integer> getPositionsByType(Class<? extends BoardSpace> spaceType) {
    List<Integer> spacePositions = new ArrayList<>();
    for (int i = 0; i < getNumberOfSpaces(); i++) {
      if (spaceType.isInstance(spaces[i])) {
        spacePositions.add(i);
      }
    }
    return spacePositions;
  }

  @Override
  public int getPositionByName(String name) {
    for (int i = 0; i < getNumberOfSpaces(); i++) {
      if (spaces[i].getName().equals(name)) {
        return i;
      }
    }
    throw new IllegalStateException("No space found with name " + name);
  }
}
