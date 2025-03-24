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
            p.toString(),
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
    System.out.println(formatTable(getPlayersState()));
    System.out.println(formatTable(getPropertySpacesState()));
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
    String[][] splitState = splitTableVertically(state);
    splitState[0] = new String[] { "", "Id", "Name", "Buildings", "Owner", "Rent",
        "", "Id", "Name", "Buildings", "Owner", "Rent" };
    return splitState;
  }

  private String[][] getPlayersState() {
    Player[] players = game.getPlayers();
    String[][] playersState = new String[players.length + 1][5];
    playersState[0] = new String[] { "", "Piece", "Money", "Get out of Jail Free Cards", "Position" };

    for (int i = 0; i < players.length; i++) {
      // can roll dice, current player
      boolean isPlayerActive = game.getCurPlayerIdx() == i;
      boolean isBankrupt = players[i].isBankrupt();
      if (isPlayerActive) {
        playersState[i + 1][0] = game.canRollDice() ? " " : " ";
      } else if (isBankrupt) {
        playersState[i + 1][0] = colorText("󱙖 ", GRAY);
      } else {
        playersState[i + 1][0] = "";
      }

      // player piece
      playersState[i + 1][1] = players[i].getPiece();

      // money
      int money = players[i].getMoney();
      String moneyFormatted = "$" + money;
      if (isBankrupt) {
        playersState[i + 1][2] = colorText(moneyFormatted, GRAY);
      } else if (money < 0) {
        playersState[i + 1][2] = colorText(moneyFormatted, RED);
      } else {
        playersState[i + 1][2] = moneyFormatted;
      }

      // get out of jail free cards
      int jailCards = players[i].getGetOutOfJailFreeCards();
      playersState[i + 1][3] = jailCards == 0 ? "" : String.valueOf(jailCards);

      // bankruptcy
      if (isBankrupt) {
        playersState[i + 1][1] = colorText(playersState[i + 1][1], GRAY);
        playersState[i + 1][3] = colorText(playersState[i + 1][3], GRAY);
      }

      // TODO remove when shown on game board
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
    int numberOfHouses = coloredPropertySpace.getNumberOfBuildings();
    if (numberOfHouses <= 4) {
      return colorText(" ".repeat(numberOfHouses), GREEN);
    } else {
      return colorText(" ", RED);
    }
  }

  private String[][] splitTableVertically(String[][] data) {
    int halfHeight = data.length / 2;
    int width = data[0].length;
    String[][] newData = new String[halfHeight + 1][width * 2];
    for (int i = 0; i < halfHeight; i++) {
      System.arraycopy(data[i], 0, newData[i + 1], 0, width);
      System.arraycopy(data[halfHeight + i], 0, newData[i + 1], width, width);
    }
    return newData;
  }

  private String formatTable(String[][] data) {
    int nColumns = data[0].length;

    // maximum width for each column
    int[] maximums = new int[nColumns];
    for (int j = 0; j < nColumns; j++) {
      final int k = j;
      maximums[j] = Arrays.stream(data)
          .mapToInt(row -> ansiStringLength(row[k]))
          .max().orElseThrow();
    }
    String horizontalLine = "─".repeat(Arrays.stream(maximums).sum() + nColumns - 1);

    String s = "┌" + horizontalLine + "┐\n";
    s += formatTableRow(data[0], maximums);
    s += "├" + horizontalLine + "┤\n";
    for (int i = 1; i < data.length; i++) {
      s += formatTableRow(data[i], maximums);
    }
    s += "└" + horizontalLine + "┘";
    return s;
  }

  private String formatTableRow(String[] row, int[] maximums) {
    String s = "│";
    for (int j = 0; j < row.length; j++) {
      String spaces = " ".repeat(maximums[j] - ansiStringLength(row[j]));
      if (row[j].matches("\\d.*")) {
        // numbers alrigned right
        s += spaces + row[j];
      } else if (row[j].contains("$")) {
        // spaces between dollar sign and number
        s += row[j].replace("$", "$" + spaces);
      } else {
        // text aligned left
        s += row[j] + spaces;
      }
      // space between columns
      if (j < row.length - 1) {
        s += " ";
      }
    }
    s += "│\n";
    return s;
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
    s = s.replaceAll("[󱠃󰖏󰭇󰇥󰮤󰻀󰞬󱙖]", "x");
    return s.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
  }
}
