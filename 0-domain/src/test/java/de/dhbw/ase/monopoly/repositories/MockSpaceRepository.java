package de.dhbw.ase.monopoly.repositories;

import java.util.List;

import de.dhbw.ase.monopoly.MockEventReceiver;
import de.dhbw.ase.monopoly.entities.BoardSpace;
import de.dhbw.ase.monopoly.entities.EmptySpace;
import de.dhbw.ase.monopoly.entities.PropertySpace;
import de.dhbw.ase.monopoly.entities.RailroadSpace;
import de.dhbw.ase.monopoly.entities.StreetSpace;
import de.dhbw.ase.monopoly.entities.UtilitySpace;
import de.dhbw.ase.monopoly.services.MockPropertyCountService;

public class MockSpaceRepository implements SpaceRepository {
  private List<BoardSpace> spaces;

  public MockSpaceRepository() {
    int[] rents = new int[] { 1, 2, 3, 4, 5, 6 };
    MockEventReceiver eventReceiver = new MockEventReceiver();
    MockPropertyCountService propertyCountService = new MockPropertyCountService();
    spaces = List.of(
        new EmptySpace("Go", eventReceiver),
        new StreetSpace("1", 1, 2, 'a', 3, rents, eventReceiver, propertyCountService),
        new StreetSpace("2", 1, 2, 'a', 3, rents, eventReceiver, propertyCountService),
        new StreetSpace("3", 1, 2, 'a', 3, rents, eventReceiver, propertyCountService),
        new EmptySpace("Jail Just Visiting", eventReceiver));
  }

  @Override
  public void init(BoardSpace[] spaces) {
  }

  @Override
  public int getNumberOfSpaces() {
    return spaces.size();
  }

  @Override
  public BoardSpace get(int position) {
    return spaces.get(position);
  }

  @Override
  public PropertySpace[] getPropertySpaces() {
    return spaces.subList(1, 4).toArray(PropertySpace[]::new);
  }

  @Override
  public StreetSpace[] getStreetSpaces() {
    return spaces.subList(1, 4).toArray(StreetSpace[]::new);
  }

  @Override
  public RailroadSpace[] getRailroadSpaces() {
    assert false;
    return null;
  }

  @Override
  public UtilitySpace[] getUtilitySpaces() {
    assert false;
    return null;
  }

  @Override
  public void update(BoardSpace space) {
  }

  @Override
  public int getJailPosition() {
    return 4;
  }

  @Override
  public List<Integer> getPositionsByType(Class<? extends BoardSpace> spaceType) {
    return List.of(0, 4);
  }

  @Override
  public int getPositionByName(String name) {
    return 2;
  }
}
