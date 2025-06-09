package de.dhbw.ase.monopoly.services;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.repositories.*;

public class RollDiceService {

  private EventReceiver eventReceiver;
  private PlayerRepository playerRepository;
  private MovementService movementService;

  public RollDiceService(EventReceiver eventReceiver, PlayerRepository playerRepository,
      MovementService movementService) {
    this.eventReceiver = eventReceiver;
    this.playerRepository = playerRepository;
    this.movementService = movementService;
  }

  public void rollDice() {
    Player currentPlayer = playerRepository.getCurrentPlayer();

    if (!currentPlayer.canRollDice()) {
      eventReceiver.addEvent("You can not cast dice again.");
      return;
    }

    currentPlayer.setCanRollDice(false);
    int die1 = randomDice();
    int die2 = randomDice();
    boolean rolledDoubles = die1 == die2;

    if (currentPlayer.isInJail()) {
      if (rolledDoubles) {
        // leave jail for free after rolling doubles, use same roll to move
        eventReceiver.addEvent("You rolled doubles and got out of jail for free.");
        currentPlayer.leaveJailForFree();
      } else {
        currentPlayer.incConsecutiveNotDoublesInJail();
        if (currentPlayer.mustGetOutOfJail()) {
          // player must get out of jail after not rolling doubles for three rounds while
          // being in jail, use same roll to move
          eventReceiver.addEvent(
              "You did not roll doubles three rounds in a row and had to get out of jail at your own expense.");
          currentPlayer.leaveJailByCardOrFee();
        } else {
          eventReceiver.addEvent("You did not roll doubles and stay in jail.");
          return;
        }
      }
    } else { // player is not in jail
      if (rolledDoubles) {
        currentPlayer.incConsecutiveDoubles();

        if (currentPlayer.mustGoToJailForRollingConsecutiveDoubles()) {
          // player must to to jail after rolling doubles for three consecutive rounds
          eventReceiver.addEvent("You were caught speeding. Go to jail immediately.");
          movementService.moveToJail();
          return;
        } else {
          // player can roll again after rolling doubles without going to jail
          eventReceiver.addEvent("You rolled doubles and can therefore roll another time.");
          currentPlayer.setCanRollDice(true);
        }
    } else {
      currentPlayer.resetConsecutiveDoubles();
      }
    }

    int steps = die1 + die2;
    movementService.moveForward(steps);
  }

  private int randomDice() {
    return (int) (Math.random() * 5 + 1);
  }
}
