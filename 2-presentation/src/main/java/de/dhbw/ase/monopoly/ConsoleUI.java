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
    String[] pieces = new String[] { Piece.DOG.toString(), Piece.SHIP.toString() };
    game = new Game(pieces);
    while (true) {
      printGameState();
      readAndExecuteCommand();
    }
  }

  private String[] readPlayerPieces() {
    String availablePieces = Arrays.stream(Piece.values())
        .map(p -> String.format("%s \"%s\"",
            colorText(p.toString(), Color.GRAY),
            p.name().toLowerCase()))
        .collect(Collectors.joining(", "));
    clearConsole();
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
    clearConsole();
    System.out.println(String.join("\n", getBoard()));
    printTable(getPlayersState());
    printTable(getPropertySpacesState());
  }

  private void printHelp() {
    System.out.println("""
        Help for Available Commands:

        help:\tdisplay this help page
        quit:\tleave the game
        roll:\troll the dice
        buy:\tbuy property
        leave:\tleave jail using a card or paying the 50$ fee
        next:\tend the turn and pass on to the next player""");
    waitForEnter();
  }

  private void readAndExecuteCommand() {
    // TODO add auction
    String command = readInput();
    clearConsole();
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
    } else if (tokens[0].equals("leave")) {
      message = game.getOutOfJail();
    } else if (tokens[0].equals("next")) {
      message = game.endTurn();
    }
    printMessageAndWait(message);
  }

  private String colorText(String text, Color foregroundColor) {
    return String.format("\u001B[38;2;%sm%s\u001B[0m", foregroundColor, text);
  }

  private String colorText(String text, Color foregroundColor, Color backgroundColor) {
    return String.format("\u001B[38;2;%sm\u001B[48;2;%sm%s\u001B[0m", foregroundColor, backgroundColor, text);
  }

  private String[] getBoard() {
    String coloredProperty = "▔▔▔";
    String rowSeparator = "├───┤                                   ├───┤";
    String rowSpacer = "│                                   │";
    String rowDelimiter = "│";
    String gameTitle = "│         " + colorText("▐", RED) + colorText("M O N O P O L Y", WHITE, RED)
        + colorText("▌", RED) + "         │";
    String chestStack = "│               " + colorText("  󰜦  ", WHITE, BLUE) + "               │";
    String chanceStack = "│               " + colorText("    ", WHITE, ORANGE) + "               │";

    String brownProperty = colorText(coloredProperty, BROWN, BACKGROUND);
    String lightBlueProperty = colorText(coloredProperty, LIGHT_BLUE, BACKGROUND);
    String pinkProperty = colorText(coloredProperty, PINK, BACKGROUND);
    String orangeProperty = colorText(coloredProperty, ORANGE, BACKGROUND);
    String redProperty = colorText(coloredProperty, RED, BACKGROUND);
    String yellowProperty = colorText(coloredProperty, YELLOW, BACKGROUND);
    String greenProperty = colorText(coloredProperty, GREEN, BACKGROUND);
    String blueProperty = colorText(coloredProperty, DARK_BLUE, BACKGROUND);

    String chest = colorText(" 󰜦 ", BLUE, BACKGROUND);
    String pinkChance = colorText("  ", PINK, BACKGROUND);
    String blueChance = colorText("  ", BLUE, BACKGROUND);
    String orangeChance = colorText("  ", ORANGE, BACKGROUND);
    String railroad = colorText("  ", BLACK, BACKGROUND);
    String freeParking = colorText("  ", RED, BACKGROUND);
    String waterWorks = colorText(" 󰖏 ", BLACK, BACKGROUND);
    String goToJail = colorText(" 󱢘 ", DARK_BLUE, BACKGROUND);
    String electricCompany = colorText(" 󱠃 ", BLACK, BACKGROUND);
    String luxuryTax = colorText("  ", BLACK, BACKGROUND);
    String jail = colorText(" ", BLACK, BACKGROUND) + colorText("󱨮 ", BLACK, ORANGE);
    String incomeTax = colorText(" ◈ ", BLACK, BACKGROUND);
    String go = colorText(" 󰟓 ", BLACK, BACKGROUND);

    String[] board = new String[] { "┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐",
        String.format("│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│", freeParking, redProperty, blueChance, redProperty,
            redProperty, railroad, yellowProperty, yellowProperty, waterWorks, yellowProperty, goToJail),
        "├───┼───┴───┴───┴───┴───┴───┴───┴───┴───┼───┤",
        rowDelimiter + orangeProperty + rowSpacer + greenProperty + rowDelimiter,
        rowSeparator,
        rowDelimiter + orangeProperty + chestStack + greenProperty + rowDelimiter,
        rowSeparator,
        rowDelimiter + chest + rowSpacer + chest + rowDelimiter,
        rowSeparator,
        rowDelimiter + orangeProperty + rowSpacer + greenProperty + rowDelimiter,
        rowSeparator,
        rowDelimiter + railroad + gameTitle + railroad + rowDelimiter,
        rowSeparator,
        rowDelimiter + pinkProperty + rowSpacer + orangeChance + rowDelimiter,
        rowSeparator,
        rowDelimiter + pinkProperty + rowSpacer + blueProperty + rowDelimiter,
        rowSeparator,
        rowDelimiter + electricCompany + chanceStack + luxuryTax + rowDelimiter,
        rowSeparator,
        rowDelimiter + pinkProperty + rowSpacer + blueProperty + rowDelimiter,
        "├───┼───┬───┬───┬───┬───┬───┬───┬───┬───┼───┤",
        String.format("│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│", jail, lightBlueProperty, lightBlueProperty, pinkChance,
            lightBlueProperty, railroad, incomeTax, brownProperty, chest, brownProperty, go),
        "└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘"
    };
    return board;
  }

  private String[][] getPropertySpacesState() {
    PropertySpace[] propertySpaces = game.getGameBoard().getPropertySpaces();
    String[][] state = new String[propertySpaces.length + 1][5];
    state[0] = new String[] { "", "Name", "Buildings", "Owner", "Rent" };

    for (int i = 0; i < propertySpaces.length; i++) {
      state[i + 1][0] = getIconForPropertySpace(propertySpaces[i]);
      state[i + 1][1] = propertySpaces[i].getName();
      state[i + 1][2] = getIconForBuildings(propertySpaces[i]);
      Optional<Player> owner = propertySpaces[i].getOwner();
      if (owner.isPresent()) {
        state[i + 1][3] = colorText(owner.get().getPiece(), GRAY);
        if (propertySpaces[i] instanceof UtilitySpace) {
          state[i + 1][4] = "";
        } else {
          state[i + 1][4] = propertySpaces[i].getRent(1) + " $";
        }
      } else {
        state[i + 1][3] = "";
        state[i + 1][4] = "";
      }
    }
    return state;
  }

  private String[][] getPlayersState() {
    Player[] players = game.getPlayers();
    String[][] playersState = new String[players.length + 1][5];
    playersState[0] = new String[] { "", "Piece", "Money", "Get out of Jail Free Cards", "Position" };

    for (int i = 0; i < players.length; i++) {
      boolean isPlayerActive = game.getCurPlayerIdx() == i;
      if (isPlayerActive) {
        playersState[i + 1][0] = game.canRollDice() ? " " : " ";
      } else {
        playersState[i + 1][0] = "";
      }
      playersState[i + 1][1] = colorText(players[i].getPiece(), GRAY);
      playersState[i + 1][2] = String.valueOf(players[i].getMoney()) + " $";
      playersState[i + 1][3] = String.valueOf(players[i].getGetOutOfJailFreeCards());
      playersState[i + 1][4] = String.valueOf(players[i].getPosition());
    }
    return playersState;
  }

  private String getIconForPropertySpace(PropertySpace propertySpace) {
    if (propertySpace instanceof ColoredPropertySpace) {
      ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;
      Color color = Color.byChar(coloredPropertySpace.getColor());
      return colorText("▔▔▔", color, Color.BACKGROUND);
    } else if (propertySpace instanceof RailroadSpace) {
      return colorText("  ", BLACK, BACKGROUND);
    } else if (propertySpace.getName().equals("Electric Company")) {
      return colorText(" 󱠃 ", BLACK, BACKGROUND);
    } else if (propertySpace.getName().equals("Water Works")) {
      return colorText(" 󰖏 ", BLACK, BACKGROUND);
    }
    throw new RuntimeException("Invalid property space.");
  }

  private String getIconForBuildings(PropertySpace propertySpace) {
    if (!(propertySpace instanceof ColoredPropertySpace)) {
      return "";
    }

    ColoredPropertySpace coloredPropertySpace = (ColoredPropertySpace) propertySpace;
    int numberOfHouses = coloredPropertySpace.getNumberOfHouses();
    if (numberOfHouses <= 4) {
      return colorText(" ".repeat(numberOfHouses), GREEN);
    } else {
      return colorText(" ", RED);
    }
  }

  private void printTable(String[][] data) {
    int nColumns = data[0].length;
    int[] maximums = new int[nColumns];
    for (int j = 0; j < nColumns; j++) {
      final int k = j;
      maximums[j] = Arrays.stream(data)
          .mapToInt(row -> ansiStringLength(row[k]))
          .max().orElseThrow();
    }

    String s = "";
    for (String[] row : data) {
      for (int j = 0; j < nColumns; j++) {
        int nSpaces = maximums[j] - ansiStringLength(row[j]) + 1;
        s += row[j] + " ".repeat(nSpaces);
      }
      s += "\n";
    }

    System.out.println(s);
  }

  private String readInput() {
    System.out.print("==> ");
    String input = scanner.next().strip();
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

  private void clearConsole() {
    System.out.print("\033[H\033[2J");
  }

  private void printMessageAndWait(String message) {
    if (message.length() == 0) {
      return;
    }
    clearConsole();
    System.out.println(message);
    waitForEnter();
  }

  private int ansiStringLength(String s) {
    // some nerdfont characters have length 2
    s = s.replaceAll("[󱠃󰖏󰭇󰇥󰮤󰻀󰞬]", "x");
    return s.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
  }
}
