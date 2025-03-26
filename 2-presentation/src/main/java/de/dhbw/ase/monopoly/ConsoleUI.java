package de.dhbw.ase.monopoly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import de.dhbw.ase.monopoly.spaces.PropertySpace;
import de.dhbw.ase.monopoly.spaces.ColoredPropertySpace;
import de.dhbw.ase.monopoly.spaces.RailroadSpace;
import de.dhbw.ase.monopoly.spaces.UtilitySpace;

import static de.dhbw.ase.monopoly.Color.*;

public class ConsoleUI {
  private Game game;
  private Scanner scanner = new Scanner(System.in);

  public void start() {
    // String[] pieces = readPlayerPieces();
    String[] pieces = new String[] { Piece.DOG.toString(), Piece.SHIP.toString(), Piece.CAR.toString() };
    game = new Game(pieces);
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
    // TODO show tables next to each other
    System.out.print(ConsoleFormatter.CLEAR_CONSOLE);
    System.out.println(String.join("\n", ConsoleBoard.get(game.getPlayers())));
    System.out.println(ConsoleFormatter.formatTable(getPlayersState()));
    System.out.println(ConsoleFormatter.formatTable(getPropertySpacesState()));
  }

  private void printHelp() {
    System.out.println("""
        Help for Available Commands:

        help:\t\tdisplay this help page
        quit:\t\tleave the game
        roll:\t\troll the dice
        buy:\t\tbuy property
        build:\t\tbuild house or hotel on property
        unbuild:\tremove house or hotel from property
        leave:\t\tleave jail using a card or paying the $50 fee
        next:\t\tend the turn and pass on to the next player
        bankrupt:\tdeclare bankruptcy and give up""");
    waitForEnter();
  }

  private void readAndExecuteCommand() {
    String command = readInput();
    System.out.print(ConsoleFormatter.CLEAR_CONSOLE);
    String[] tokens = command.split(" ");

    String message = "";
    if (tokens[0].equals("help")) {
      printHelp();
    } else if (tokens[0].equals("quit")) {
      System.exit(0);
    } else if (tokens[0].equals("roll")) {
      message = game.rollDice();
    } else if (tokens[0].equals("buy")) {
      message = game.buyProperty();
    } else if (List.of("build", "unbuild").contains(tokens[0])) {
      if (tokens.length < 2) {
        message = "Please enter a property identifier.";
      } else if (!UtilService.isInteger(tokens[1])) {
        message = "Only numbers are supported as property identifiers.";
      } else {
        int propertyId = Integer.parseInt(tokens[1]);
        if (tokens[0].equals("build")) {
          message = game.buildOnProperty(propertyId);
        } else {
          message = game.unbuildOnProperty(propertyId);
        }
      }
    } else if (tokens[0].equals("leave")) {
      message = game.getOutOfJail();
    } else if (tokens[0].equals("next")) {
      message = game.nextPlayer();
    } else if (tokens[0].equals("bankrupt")) {
      message = game.declareBankruptcy();
    }
    printMessageAndWait(message);
  }

  private String[][] getPropertySpacesState() {
    PropertySpace[] propertySpaces = game.getGameBoard().getPropertySpaces();
    String[][] state = new String[propertySpaces.length][6];

    for (int i = 0; i < propertySpaces.length; i++) {
      state[i][0] = getIconForPropertySpace(propertySpaces[i]);
      state[i][1] = String.valueOf(i);
      state[i][2] = propertySpaces[i].getName();
      state[i][3] = getIconForBuildings(propertySpaces[i]);
      Optional<Player> owner = propertySpaces[i].getOwner();
      if (owner.isPresent()) {
        state[i][4] = owner.get().getPiece();
        if (propertySpaces[i] instanceof UtilitySpace) {
          state[i][5] = "";
        } else {
          state[i][5] = "$" + propertySpaces[i].getRent(1);
        }
      } else {
        state[i][4] = "";
        state[i][5] = "";
      }
    }
    String[][] splitState = ConsoleFormatter.splitTableVertically(state);
    splitState[0] = new String[] { "", "Id", "Name", "Buildings", "Owner", "Rent",
        "", "Id", "Name", "Buildings", "Owner", "Rent" };
    return splitState;
  }

  private String[][] getPlayersState() {
    Player[] players = game.getPlayers();
    String[][] playersState = new String[players.length + 1][4];
    playersState[0] = new String[] { "", "Piece", "Money", "Get out of Jail Free Cards" };

    for (int i = 0; i < players.length; i++) {
      // can roll dice, current player
      boolean isPlayerActive = game.getCurPlayerIdx() == i;
      boolean isBankrupt = players[i].isBankrupt();
      if (isPlayerActive) {
        playersState[i + 1][0] = game.canRollDice() ? " " : " ";
      } else if (isBankrupt) {
        playersState[i + 1][0] = ConsoleFormatter.colorText("󱙖 ", GRAY);
      } else {
        playersState[i + 1][0] = "";
      }

      // player piece
      playersState[i + 1][1] = players[i].getPiece();

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
