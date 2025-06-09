package de.dhbw.ase.monopoly.consoleui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import de.dhbw.ase.monopoly.EventReceiver;
import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.repositories.*;
import de.dhbw.ase.monopoly.services.*;

import static de.dhbw.ase.monopoly.consoleui.Color.*;

public class ConsoleUI implements EventReceiver {
  private final Scanner scanner = new Scanner(System.in);
  private final List<String> events = new ArrayList<>();

  private final StartService startService;
  private final GameStateService gameStateService;
  private final BuildingService buildingService;
  private final BuyPropertyService buyPropertyService;
  private final RollDiceService rollDiceService;
  private final TurnChangeService turnChangeService;

  public ConsoleUI() {
    ActionCardRepository actionCardRepository = new ActionCardRepositoryInMemory();
    PlayerRepository playerRepository = new PlayerRepositoryInMemory();
    SpaceRepository spaceRepository = new SpaceRepositoryInMemory();

    MovementService movementService = new MovementService(this, playerRepository, spaceRepository);
    PropertyCountService propertyCountService = new PropertyCountService(playerRepository, spaceRepository);
    ActionCardService actionCardService = new ActionCardService(this, actionCardRepository, playerRepository,
        movementService, propertyCountService);
    startService = new StartService(this, actionCardRepository, playerRepository, spaceRepository, movementService,
        propertyCountService, actionCardService);

    gameStateService = new GameStateService(playerRepository, spaceRepository);
    buildingService = new BuildingService(this, playerRepository, spaceRepository, propertyCountService);
    buyPropertyService = new BuyPropertyService(this, playerRepository, spaceRepository);
    rollDiceService = new RollDiceService(this, playerRepository, movementService);
    turnChangeService = new TurnChangeService(this, playerRepository);
  }

  public void start() {
    // String[] pieces = readPlayerPieces();
    String[] pieces = new String[] { Piece.DOG.toString(), Piece.SHIP.toString(), Piece.CAR.toString() };
    startService.start(pieces);
    while (true) {
      printGameState();
      readAndExecuteCommand();
    }
  }

  private String[] readPlayerPieces() {
    String availablePieces = Arrays.stream(Piece.values())
        .map(p -> String.format("%s \"%s\"",
            p.toString(),
            p.name().toLowerCase()))
        .collect(Collectors.joining(", "));
    System.out.print(ConsoleFormatter.CLEAR_CONSOLE);
    System.out.printf(
        "Please choose a piece for each player. The available pieces include: %s. Enter \"start\" to start the game.\n",
        availablePieces);

    List<Piece> pieces = new ArrayList<>();
    while (true) {
      String input = readInput();

      if (input.equalsIgnoreCase("start")) {
        if (pieces.size() >= 2) {
          break;
        } else {
          System.out.println("A minimum of two players is required to start a game.");
          continue;
        }
      }

      if (!Piece.includesName(input)) {
        System.out.println("Invalid piece identifier.");
        continue;
      }

      Piece piece = Piece.byName(input);
      if (pieces.contains(piece)) {
        System.out.println("This piece is already taken by another player.");
        continue;
      }

      pieces.add(piece);
    }
    return pieces.stream().map(Piece::toString).toArray(String[]::new);
  }

  private void printGameState() {
    System.out.print(ConsoleFormatter.CLEAR_CONSOLE);
    String[] boardState = ConsoleBoard.get(gameStateService.getPlayersState());
    String[] playersState = ConsoleFormatter.formatTable(getPlayersState());
    String[] propertySpacesState = ConsoleFormatter.formatTable(getPropertySpacesState());
    String[] state = ConsoleFormatter.joinTablesHorizontally(
        ConsoleFormatter.joinTablesVertically(boardState, playersState), propertySpacesState);
    System.out.println(String.join("\n", state));
  }

  private void printHelp() {
    System.out.println("""
        Help for Available Commands:

        help:\t\tdisplay this help page
        quit:\t\tleave the game
        roll:\t\troll the dice
        buy:\t\tbuy property
        build <id>:\tbuild house or hotel on property
        unbuild <id>:\tremove house or hotel from property
        leave:\t\tleave jail using a card or paying the $50 fee
        next:\t\tend the turn and pass on to the next player
        bankrupt:\tdeclare bankruptcy and give up""");
    waitForEnter();
  }

  private void readAndExecuteCommand() {
    String command = readInput();
    System.out.print(ConsoleFormatter.CLEAR_CONSOLE);
    String[] tokens = command.split(" ");

    if (tokens[0].equals("help")) {
      printHelp();
    } else if (tokens[0].equals("quit")) {
      System.exit(0);
    } else if (tokens[0].equals("roll")) {
      rollDiceService.rollDice();
    } else if (tokens[0].equals("buy")) {
      buyPropertyService.buyProperty();
    } else if (List.of("build", "unbuild").contains(tokens[0])) {
      if (tokens.length < 2) {
        addEvent("Please enter a property identifier.");
      } else {
        try {
          int propertyId = Integer.parseInt(tokens[1]);
          if (tokens[0].equals("build")) {
            buildingService.buildOnSpace(propertyId);
          } else {
            buildingService.unbuildOnSpace(propertyId);
          }
        } catch (NumberFormatException exception) {
          addEvent("Only numbers are supported as property identifiers.");
        }
      }
    } else if (tokens[0].equals("leave")) {
      turnChangeService.getOutOfJail();
    } else if (tokens[0].equals("next")) {
      turnChangeService.nextPlayer();
    } else if (tokens[0].equals("bankrupt")) {
      turnChangeService.declareBankruptcy();
    }
    printMessageAndWait(String.join("\n", events));
    events.clear();
  }

  @Override
  public void addEvent(String message) {
    events.add("• " + message);
  }

  private String[][] getPropertySpacesState() {
    PropertySpace[] propertySpaces = gameStateService.getPropertySpacesState();
    String[][] state = new String[propertySpaces.length + 1][6];
    state[0] = new String[] { "", "Id", "Name", "Buildings", "Owner", "Rent" };

    for (int i = 0; i < propertySpaces.length; i++) {
      state[i + 1][0] = getIconForPropertySpace(propertySpaces[i]);
      state[i + 1][1] = String.valueOf(i);
      state[i + 1][2] = propertySpaces[i].getName();
      state[i + 1][3] = getIconForBuildings(propertySpaces[i]);
      Optional<Player> owner = propertySpaces[i].getOwner();
      if (owner.isPresent()) {
        state[i + 1][4] = owner.get().getPiece();
        if (propertySpaces[i] instanceof UtilitySpace) {
          state[i + 1][5] = "";
        } else {
          state[i + 1][5] = "$" + propertySpaces[i].getRent(1);
        }
      } else {
        state[i + 1][4] = "";
        state[i + 1][5] = "";
      }
    }
    return state;
  }

  private String[][] getPlayersState() {
    Player[] players = gameStateService.getPlayersState();
    Player currentPlayer = gameStateService.getCurrentPlayerState();
    String[][] playersState = new String[players.length + 1][4];
    playersState[0] = new String[] { "", "Player", "Money", "Get out of Jail Free Cards" };

    for (int i = 0; i < players.length; i++) {
      // can roll dice, current player
      boolean isPlayerActive = players[i] == currentPlayer;
      boolean isBankrupt = players[i].isBankrupt();
      if (isPlayerActive) {
        playersState[i + 1][0] = players[i].canRollDice() ? " " : " ";
      } else if (isBankrupt) {
        playersState[i + 1][0] = ConsoleFormatter.colorText("󱙖 ", GRAY);
      } else {
        playersState[i + 1][0] = "";
      }

      // player piece, is player in jail
      playersState[i + 1][1] = players[i].getPiece();
      if (players[i].isInJail()) {
        if (players[i].isBankrupt()) {
          playersState[i + 1][1] += ConsoleFormatter.colorText(" 󱨮 ", GRAY);
        } else {
          playersState[i + 1][1] += ConsoleFormatter.colorText(" 󱨮 ", BLACK, ORANGE);
        }
      } else {
        playersState[i + 1][1] += "   ";
      }

      // money
      int money = players[i].getMoney();
      String moneyFormatted = "$" + money;
      if (isBankrupt) {
        playersState[i + 1][2] = ConsoleFormatter.colorText(moneyFormatted, GRAY);
      } else if (money < 0) {
        playersState[i + 1][2] = ConsoleFormatter.colorText(moneyFormatted, RED);
      } else {
        playersState[i + 1][2] = moneyFormatted;
      }

      // get out of jail free cards
      int jailCards = players[i].getGetOutOfJailFreeCards();
      playersState[i + 1][3] = jailCards == 0 ? "" : String.valueOf(jailCards);

      // bankruptcy
      if (isBankrupt) {
        playersState[i + 1][1] = ConsoleFormatter.colorText(playersState[i + 1][1], GRAY);
        playersState[i + 1][3] = ConsoleFormatter.colorText(playersState[i + 1][3], GRAY);
      }
    }
    return playersState;
  }

  private String getIconForPropertySpace(PropertySpace propertySpace) {
    if (propertySpace instanceof ColoredPropertySpace) {
      ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;
      Color color = Color.byChar(coloredPropertySpace.getColor());
      return ConsoleFormatter.colorText("▔▔▔", color, Color.BACKGROUND);
    } else if (propertySpace instanceof RailroadSpace) {
      return ConsoleFormatter.colorText("  ", BLACK, BACKGROUND);
    } else if (propertySpace.getName().equals("Electric Company")) {
      return ConsoleFormatter.colorText(" 󱠃 ", BLACK, BACKGROUND);
    } else if (propertySpace.getName().equals("Water Works")) {
      return ConsoleFormatter.colorText(" 󰖏 ", BLACK, BACKGROUND);
    }
    throw new RuntimeException("Invalid property space.");
  }

  private String getIconForBuildings(PropertySpace propertySpace) {
    if (!(propertySpace instanceof ColoredPropertySpace)) {
      return "";
    }

    ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;
    int numberOfHouses = coloredPropertySpace.getNumberOfBuildings();
    if (numberOfHouses <= 4) {
      return ConsoleFormatter.colorText(" ".repeat(numberOfHouses), GREEN);
    } else {
      return ConsoleFormatter.colorText(" ", RED);
    }
  }

  private String readInput() {
    System.out.print("==> ");
    String input = scanner.nextLine().strip();
    return input;
  }

  private void waitForEnter() {
    System.out.println("\nPress <return> to continue.");
    try {
      System.in.read();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void printMessageAndWait(String message) {
    if (message.length() == 0) {
      return;
    }
    System.out.print(ConsoleFormatter.CLEAR_CONSOLE);
    System.out.println(message);
    waitForEnter();
  }
}
