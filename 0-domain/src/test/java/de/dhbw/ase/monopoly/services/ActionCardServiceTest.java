package de.dhbw.ase.monopoly.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dhbw.ase.monopoly.ActionType;
import de.dhbw.ase.monopoly.MockEventReceiver;
import de.dhbw.ase.monopoly.repositories.MockActionCardRepository;
import de.dhbw.ase.monopoly.repositories.MockPlayerRepository;
import de.dhbw.ase.monopoly.repositories.MockSpaceRepository;
import de.dhbw.ase.monopoly.valueobjects.ActionCard;

public class ActionCardServiceTest {
  private MockActionCardRepository actionCardRepository;
  private MockPlayerRepository playerRepository;
  private ActionCardService actionCardService;

  @BeforeEach
  public void beforeEach() {
    MockEventReceiver eventReceiver = new MockEventReceiver();
    actionCardRepository = new MockActionCardRepository();
    playerRepository = new MockPlayerRepository();
    MovementService movementService = new MovementService(eventReceiver, playerRepository, new MockSpaceRepository());
    MockPropertyCountService propertyCountService = new MockPropertyCountService();
    actionCardService = new ActionCardService(eventReceiver, actionCardRepository, playerRepository, movementService,
        propertyCountService);
  }

  @Test
  public void moneyTransferWithBank() {
    actionCardRepository.mockActionCard(
        new ActionCard.Builder(ActionType.CHANCE, "").moneyTransferWithBank(10).build());
    actionCardService.performAction(ActionType.CHANCE);
    assertEquals(playerRepository.getCurrentPlayer().getMoney(), 1510);
  }

  @Test
  public void moneyTransferWithAllPlayers() {
    actionCardRepository.mockActionCard(
        new ActionCard.Builder(ActionType.CHANCE, "").moneyTransferWithAllPlayers(10).build());
    actionCardService.performAction(ActionType.CHANCE);
    assertEquals(playerRepository.getCurrentPlayer().getMoney(), 1510);
    assertEquals(playerRepository.getNextSolventPlayer().getMoney(), 1490);
  }

  @Test
  public void moneyPerHouse() {
    actionCardRepository.mockActionCard(
        new ActionCard.Builder(ActionType.CHANCE, "").moneyPerHouse(-100).build());
    actionCardService.performAction(ActionType.CHANCE);
    assertEquals(playerRepository.getCurrentPlayer().getMoney(), 1300);
  }

  @Test
  public void getOutOfJailFreeCard() {
    actionCardRepository.mockActionCard(
        new ActionCard.Builder(ActionType.CHANCE, "").getOutOfJailFreeCard().build());
    actionCardService.performAction(ActionType.CHANCE);
    assertEquals(playerRepository.getCurrentPlayer().getGetOutOfJailFreeCards(), 1);
  }
}
