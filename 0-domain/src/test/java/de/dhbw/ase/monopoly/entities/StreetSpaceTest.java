package de.dhbw.ase.monopoly.entities;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import de.dhbw.ase.monopoly.MockEventReceiver;
import de.dhbw.ase.monopoly.services.MockPropertyCountService;

public class StreetSpaceTest {
  private MockPropertyCountService propertyCountService;
  private StreetSpace streetSpace;

  @BeforeEach
  public void beforeEach() {
    propertyCountService = new MockPropertyCountService();
    int[] rents = new int[] { 1, 2, 3, 4, 5, 6 };
    MockEventReceiver eventReceiver = new MockEventReceiver();
    streetSpace = new StreetSpace("Name", 12, 34, 'r', 56, rents, eventReceiver, propertyCountService);
    Player owner = new Player("x");
    streetSpace.setOwner(Optional.of(owner));
  }

  @Test
  public void getRentWithWholeColorGroup() {
    propertyCountService.mockPlayerOwnsWholeColorGroup(true);
    assertEquals(streetSpace.getRent(-1), 2);
  }

  @Test
  public void getRentWithThreeBuildings() {
    for (int i = 0; i < 3; i++) {
      streetSpace.addBuilding();
    }
    assertEquals(streetSpace.getNumberOfBuildings(), 3);
    assertEquals(streetSpace.getRent(-1), 4);
  }

  @Test
  public void hasHotelWithFiveBuildings() {
    for (int i = 0; i < 4; i++) {
      streetSpace.addBuilding();
      assertFalse(streetSpace.hasHotel());
    }
    streetSpace.addBuilding();
    assertTrue(streetSpace.hasHotel());
  }
}
