package de.dhbw.ase.monopoly.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dhbw.ase.monopoly.MockEventReceiver;
import de.dhbw.ase.monopoly.entities.EmptySpace;
import de.dhbw.ase.monopoly.repositories.MockPlayerRepository;
import de.dhbw.ase.monopoly.repositories.MockSpaceRepository;
import de.dhbw.ase.monopoly.repositories.PlayerRepository;
import de.dhbw.ase.monopoly.repositories.SpaceRepository;

public class MovementServiceTest {
  private PlayerRepository playerRepository;
  private SpaceRepository spaceRepository;
  private MovementService movementService;

  @BeforeEach
  public void beforeEach() {
    MockEventReceiver eventReceiver = new MockEventReceiver();
    playerRepository = new MockPlayerRepository();
    spaceRepository = new MockSpaceRepository();
    movementService = new MovementService(eventReceiver, playerRepository, spaceRepository);
  }

  @Test
  public void moveSteps() {
    movementService.moveSteps(2);
    assertEquals(playerRepository.getCurrentPlayer().getPosition(), 2);
  }

  @Test
  public void passGoAndCollect200Dollar() {
    movementService.moveSteps(4);
    assertEquals(playerRepository.getCurrentPlayer().getMoney(), 1500);
    movementService.moveSteps(1);
    assertEquals(playerRepository.getCurrentPlayer().getMoney(), 1700);
  }

  @Test
  public void moveToNearestSpaceOfTypeEmptySpace() {
    movementService.moveToNearestSpaceOfType(EmptySpace.class);
    assertEquals(playerRepository.getCurrentPlayer().getPosition(), 4);
  }

  @Test
  public void moveToSpaceByName() {
    movementService.moveToSpaceByName("2");
    assertEquals(playerRepository.getCurrentPlayer().getPosition(), 2);
  }

  @Test
  public void moveToSpaceByNameBehindPlayer() {
    playerRepository.getCurrentPlayer().setPosition(3);
    movementService.moveToSpaceByName("2");
    assertEquals(playerRepository.getCurrentPlayer().getPosition(), 2);
  }

  @Test
  public void moveToJail() {
    movementService.moveToJail();
    assertEquals(playerRepository.getCurrentPlayer().getPosition(), 4);
    assertTrue(playerRepository.getCurrentPlayer().isInJail());
  }
}
