package de.dhbw.ase.monopoly.services;

import java.util.Arrays;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.entities.Player;
import de.dhbw.ase.monopoly.repositories.*;
import de.dhbw.ase.monopoly.valueobjects.ActionCard;

public class ActionCardService {
  private final EventReceiver eventReceiver;
  private final ActionCardRepository actionCardRepository;
  private final PlayerRepository playerRepository;
  private final MovementService movementService;
  private final PropertyCountService propertyCountService;

  public ActionCardService(EventReceiver eventReceiver, ActionCardRepository actionCardRepository,
      PlayerRepository playerRepository, MovementService movementService, PropertyCountService propertyCountService) {
    this.eventReceiver = eventReceiver;
    this.actionCardRepository = actionCardRepository;
    this.playerRepository = playerRepository;
    this.movementService = movementService;
    this.propertyCountService = propertyCountService;
  }

  public void performAction(ActionType actionType) {
    ActionCard randomActionCard = actionCardRepository.getRandomCardOfType(actionType);
    eventReceiver.addEvent(randomActionCard.text);
    moneyTransferWithBank(randomActionCard);
    moneyTransferWithAllPlayers(randomActionCard);
    moneyPerHouse(randomActionCard);
    moneyPerHotel(randomActionCard);
    moveToSpaceByType(randomActionCard);
    moveToSpaceByName(randomActionCard);
    moveSteps(randomActionCard);
    moveToJail(randomActionCard);
    getOutOfJailFreeCard(randomActionCard);
  }

  private void moneyTransferWithBank(ActionCard actionCard) {
    if (actionCard.moneyTransferWithBank == 0) {
      return;
    }

    Player currentPlayer = playerRepository.getCurrentPlayer();
    currentPlayer.transferMoney(actionCard.moneyTransferWithBank);
    playerRepository.update(currentPlayer);
  }

  private void moneyTransferWithAllPlayers(ActionCard actionCard) {
    if (actionCard.moneyTransferWithAllPlayers == 0) {
      return;
    }

    Player[] solventPlayers = playerRepository.getAllSolventPlayers();
    Player currentPlayer = playerRepository.getCurrentPlayer();

    Arrays.stream(solventPlayers).forEach(p -> p.transferMoney(-actionCard.moneyTransferWithAllPlayers));
    int totalMoney = actionCard.moneyTransferWithAllPlayers * solventPlayers.length;
    currentPlayer.transferMoney(totalMoney);

    playerRepository.updateAll(solventPlayers);
  }

  private void moneyPerHouse(ActionCard actionCard) {
    if (actionCard.moneyPerHouse == 0) {
      return;
    }

    int houseCount = propertyCountService.getCurrentPlayerHouseCount();
    Player currentPlayer = playerRepository.getCurrentPlayer();
    currentPlayer.transferMoney(actionCard.moneyPerHouse * houseCount);
    playerRepository.update(currentPlayer);
  }

  private void moneyPerHotel(ActionCard actionCard) {
    if (actionCard.moneyPerHotel == 0) {
      return;
    }

    int hotelCount = propertyCountService.getCurrentPlayerHotelCount();
    Player currentPlayer = playerRepository.getCurrentPlayer();
    currentPlayer.transferMoney(actionCard.moneyPerHotel * hotelCount);
    playerRepository.update(currentPlayer);
  }

  private void moveToSpaceByType(ActionCard actionCard) {
    if (actionCard.moveToSpaceByType == null) {
      return;
    }

    movementService.moveToNearestSpaceOfType(actionCard.moveToSpaceByType);
  }

  private void moveToSpaceByName(ActionCard actionCard) {
    if (actionCard.moveToSpaceByName == null) {
      return;
    }

    movementService.moveToSpaceByName(actionCard.moveToSpaceByName);
  }

  private void moveSteps(ActionCard actionCard) {
    if (actionCard.moveSteps == 0) {
      return;
    }

    movementService.moveSteps(actionCard.moveSteps);
  }

  private void moveToJail(ActionCard actionCard) {
    if (!actionCard.moveToJail) {
      return;
    }

    movementService.moveToJail();
  }

  private void getOutOfJailFreeCard(ActionCard actionCard) {
    if (!actionCard.getOutOfJailFreeCard) {
      return;
    }

    Player currentPlayer = playerRepository.getCurrentPlayer();
    currentPlayer.incGetOutOfJailFreeCards();
    playerRepository.update(currentPlayer);
  }
}
