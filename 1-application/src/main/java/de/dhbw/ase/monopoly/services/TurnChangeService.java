package de.dhbw.ase.monopoly.services;

import de.dhbw.ase.monopoly.EventReceiver;
import de.dhbw.ase.monopoly.entities.Player;
import de.dhbw.ase.monopoly.repositories.PlayerRepository;

public class TurnChangeService {
  private final EventReceiver eventReceiver;
  private final PlayerRepository playerRepository;

  public TurnChangeService(EventReceiver eventReceiver, PlayerRepository playerRepository) {
    this.eventReceiver = eventReceiver;
    this.playerRepository = playerRepository;
  }

  public void getOutOfJail() {
    Player currentPlayer = playerRepository.getCurrentPlayer();

    if (!currentPlayer.isInJail()) {
      eventReceiver.addEvent("You are currently not in jail.");
      return;
    }

    if (!currentPlayer.canRollDice()) {
      eventReceiver.addEvent("You can only get out of jail at the beginning of your turn.");
      return;
    }

    if (currentPlayer.getMoney() < 50 && currentPlayer.getGetOutOfJailFreeCards() == 0) {
      eventReceiver.addEvent("You need at least $50 or a card to get out of jail.");
      return;
    }

    currentPlayer.leaveJailByCardOrFee();
    eventReceiver.addEvent("You got out of jail at your own expense.");
  }

  public void nextPlayer() {
    Player currentPlayer = playerRepository.getCurrentPlayer();

    if (currentPlayer.canRollDice()) {
      eventReceiver.addEvent("You can roll the dice another time before ending your turn.");
      return;
    }

    if (currentPlayer.getMoney() < 0) {
      eventReceiver.addEvent(
          "You have to get out of debt before ending your turn. If that is not possible, you have to declare bankruptcy and leave the game.");
      return;
    }
    endTurn();
  }

  public void declareBankruptcy() {
    Player currentPlayer = playerRepository.getCurrentPlayer();

    if (currentPlayer.getMoney() >= 0) {
      eventReceiver.addEvent("You still have money left.");
      return;
    }
    if (currentPlayer.canRollDice()) {
      eventReceiver.addEvent("You can roll the dice another time.");
      return;
    }

    currentPlayer.makeBankrupt();
    endTurn();
    checkIfSinglePlayerIsLeft();
  }

  private void endTurn() {
    Player currentPlayer = playerRepository.getCurrentPlayer();
    Player nextPlayer = playerRepository.getNextSolventPlayer();

    currentPlayer.setActive(false);
    playerRepository.update(currentPlayer);
    nextPlayer.setActive(true);
    nextPlayer.setCanRollDice(true);
    playerRepository.update(nextPlayer);
  }

  private void checkIfSinglePlayerIsLeft() {
    int numberOfSolventPlayers = playerRepository.getNumberOfSolventPlayers();

    if (numberOfSolventPlayers == 1) {
      String piece = playerRepository.getCurrentPlayer().getPiece();
      eventReceiver.addEvent(String.format("Player %s has won the game.", piece));
    }
  }
}
