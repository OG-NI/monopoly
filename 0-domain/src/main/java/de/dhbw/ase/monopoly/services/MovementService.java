package de.dhbw.ase.monopoly.services;

import java.util.List;

import de.dhbw.ase.monopoly.EventReceiver;
import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.repositories.*;

public class MovementService {
  private final EventReceiver eventReceiver;
  private final PlayerRepository playerRepository;
  private final SpaceRepository spaceRepository;

  public MovementService(EventReceiver eventReceiver, PlayerRepository playerRepository,
      SpaceRepository spaceRepository) {
    this.playerRepository = playerRepository;
    this.eventReceiver = eventReceiver;
    this.spaceRepository = spaceRepository;
  }

  public void moveSteps(int steps) {
    int boardSize = spaceRepository.getNumberOfSpaces();
    Player currentPlayer = playerRepository.getCurrentPlayer();
    int position = currentPlayer.getPosition();

    position += steps;
    if (position >= boardSize) {
      eventReceiver.addEvent("You passed go and collected $200.");
      position -= boardSize;
      currentPlayer.transferMoney(200);
    }
    currentPlayer.setPosition(position);
    playerRepository.update(currentPlayer);

    BoardSpace space = spaceRepository.get(position);
    space.enterSpace(currentPlayer, steps);
  }

  private void moveToPosition(int position) {
    int boardSize = spaceRepository.getNumberOfSpaces();
    Player currentPlayer = playerRepository.getCurrentPlayer();
    int playerPosition = currentPlayer.getPosition();

    if (position > playerPosition) {
      moveSteps(position - playerPosition);
    } else {
      moveSteps(boardSize + position - playerPosition);
    }
  }

  public void moveToNearestSpaceOfType(Class<? extends BoardSpace> spaceType) {
    List<Integer> positions = spaceRepository.getPositionsByType(spaceType);
    Player currentPlayer = playerRepository.getCurrentPlayer();
    int playerPosition = currentPlayer.getPosition();
    int nearestPosition = positions.stream()
        .filter(p -> p > playerPosition).findFirst()
        .orElse(positions.get(0));
    moveToPosition(nearestPosition);
  }

  public void moveToSpaceByName(String name) {
    int position = spaceRepository.getPositionByName(name);
    moveToPosition(position);
  }

  public void moveToJail() {
    int jailPosition = spaceRepository.getJailPosition();
    Player currentPlayer = playerRepository.getCurrentPlayer();
    currentPlayer.setPosition(jailPosition);
    currentPlayer.goToJail();
    playerRepository.update(currentPlayer);
  }
}
