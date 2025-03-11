package de.dhbw.ase.monopoly;

import de.dhbw.ase.monopoly.spaces.*;
import java.util.Arrays;

public class Game {
  private GameBoard gameBoard;
  private final Player[] players;
  private int curPlayerIdx;
  private boolean canRollAgain = true;
  private int consecutiveDoubles = 0;

  public Game(String[] playerNames) {
    ActionCard[] communityChestCards = ActionCardFactory.initCommunityChestCards(players);
    ActionCard[] chanceCards = ActionCardFactory.initChanceCards(players);
    gameBoard = new GameBoard(communityChestCards, chanceCards);

    players = Arrays.stream(playerNames)
        .map(name -> new Player(name, gameBoard)).toArray(Player[]::new);
    curPlayerIdx = (int) (Math.random() * playerNames.length);
  }

  public void rollDice() {
    if (!canRollAgain) {
      return;
    }
    canRollAgain = false;
    Player curPlayer = players[curPlayerIdx];

    int die1 = randomDice();
    int die2 = randomDice();

    if (curPlayer.isInJail()) {
      if (die1 == die2) {
        curPlayer.getOutOfJail(true);
      } else {
        curPlayer.incConsecutiveNotDoublesInJail();
        if (curPlayer.mustGetOutOfJail()) {
          curPlayer.getOutOfJail(false);
        } else {
          return;
        }
      }
    } else { // player is not in jail
      if (die1 == die2) {
        consecutiveDoubles++;

        if (consecutiveDoubles == 3) {
          curPlayer.goToJail();
          return;
        } else {
          canRollAgain = true;
        }
      } else {
        consecutiveDoubles = 0;
      }
    }

    int steps = die1 + die2;
    curPlayer.moveForward(steps);
  }

  public void getOutOfJail() {
    Player curPlayer = players[curPlayerIdx];
    if (canRollAgain && curPlayer.isInJail()) {
      curPlayer.getOutOfJail(false);
    }
  }

  public void endTurn() {
    if (canRollAgain) {
      return;
    }
    curPlayerIdx = (curPlayerIdx + 1) % players.length;
  }

  private int randomDice() {
    return (int) (Math.random() * 5 + 1);
  }
}
