package de.dhbw.ase.monopoly.services;

import java.util.Arrays;

import de.dhbw.ase.monopoly.EventReceiver;
import de.dhbw.ase.monopoly.entities.Player;
import de.dhbw.ase.monopoly.generators.*;
import de.dhbw.ase.monopoly.repositories.*;

public class StartService {
  private final EventReceiver eventReceiver;

  private final ActionCardRepository actionCardRepository;
  private final PlayerRepository playerRepository;
  private final SpaceRepository spaceRepository;

  private final MovementService movementService;
  private final PropertyCountService propertyCountService;
  private final ActionCardService actionCardService;

  public StartService(EventReceiver eventReceiver, ActionCardRepository actionCardRepository,
      PlayerRepository playerRepository, SpaceRepository spaceRepository, MovementService movementService,
      PropertyCountService propertyCountService, ActionCardService actionCardService) {
    this.eventReceiver = eventReceiver;
    this.actionCardRepository = actionCardRepository;
    this.playerRepository = playerRepository;
    this.spaceRepository = spaceRepository;
    this.movementService = movementService;
    this.propertyCountService = propertyCountService;
    this.actionCardService = actionCardService;
  }

  public void start(String[] pieces) {
    actionCardRepository.init(ActionCardsGenerator.generateActionCards());
    spaceRepository.init(SpacesGenerator.generateSpaces(
        eventReceiver, movementService, propertyCountService, actionCardService));
    initPlayers(pieces);
  }

  private void initPlayers(String[] pieces) {
    Player[] players = Arrays.stream(pieces)
        .map(Player::new).toArray(Player[]::new);
    int firstPlayerIndex = (int) (Math.random() * players.length);
    players[firstPlayerIndex].setActive(true);
    players[firstPlayerIndex].setCanRollDice(true);
    playerRepository.init(players);
  }
}
