package de.dhbw.ase.monopoly.consoleui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.dhbw.ase.monopoly.entities.Player;

import static de.dhbw.ase.monopoly.consoleui.Color.*;
import static de.dhbw.ase.monopoly.consoleui.ConsoleFormatter.colorText;

public final class ConsoleBoard {
  private static final String[] boardWithoutPieces = initBoardWithoutPieces();

  private ConsoleBoard() {
  }

  public static String[] get(Player[] players) {
    // group player pieces by position
    List<List<String>> spaces = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      spaces.add(new ArrayList<>());
    }
    int numberOfActivePlayers = 0;
    for (var player : players) {
      if (!player.isBankrupt()) {
        spaces.get(player.getPosition()).add(player.getPiece());
        numberOfActivePlayers++;
      }
    }
    int numberOfRows = (int) Math.ceil(numberOfActivePlayers / 2f);

    // format bottom and top spaces
    String[][] formattedBottomSpaces = new String[11][numberOfRows];
    String[][] formattedTopSpaces = new String[11][numberOfRows];
    for (int i = 0; i < 11; i++) {
      formattedBottomSpaces[i] = formatHorizontalSpace(spaces.get(i), numberOfRows);
      formattedTopSpaces[i] = formatHorizontalSpace(spaces.get(i + 20), numberOfRows);
    }

    // generate new board
    String[] board = new String[boardWithoutPieces.length + numberOfRows * 2];
    System.arraycopy(boardWithoutPieces, 0, board, numberOfRows, boardWithoutPieces.length);

    // insert bottom pieces
    for (int i = 0; i < numberOfRows; i++) {
      int iBoard = numberOfRows + boardWithoutPieces.length + i;
      board[iBoard] = " ";
      for (int j = 10; j >= 0; j--) {
        board[iBoard] += formattedBottomSpaces[j][i];
      }
    }

    // insert top pieces
    for (int i = numberOfRows - 1; i >= 0; i--) {
      int iBoard = numberOfRows - 1 - i;
      board[iBoard] = " ";
      for (int j = 0; j < 11; j++) {
        board[iBoard] += formattedTopSpaces[j][i];
      }
    }

    // insert left and right pieces
    for (int i = 0; i < board.length; i++) {
      int iRow = i - numberOfRows;
      // check if there can be pieces in the row
      if (iRow >= 3 && iRow <= 19 && iRow % 2 == 1) {
        // left pieces
        int iSpaces = (41 - iRow) / 2; // map [3 : 19 : 2] to [19 : 11 : -1]
        int numberOfSpaces = (numberOfActivePlayers - spaces.get(iSpaces).size()) * 2;
        Collections.reverse(spaces.get(iSpaces));
        String pieces = String.join("", spaces.get(iSpaces));
        board[i] = " ".repeat(numberOfSpaces) + pieces + board[i];

        // right pieces
        iSpaces = (59 + iRow) / 2; // map [3 : 19 : 2] to [31 : 39 : 1]
        numberOfSpaces = (numberOfActivePlayers - spaces.get(iSpaces).size()) * 2;
        pieces = String.join("", spaces.get(iSpaces));
        board[i] += pieces + " ".repeat(numberOfSpaces);
      } else {
        // indent rows without pieces
        String indentationWithoutPieces = " ".repeat(numberOfActivePlayers * 2);
        board[i] = indentationWithoutPieces + board[i] + indentationWithoutPieces;
      }
    }

    return board;
  }

  private static String[] formatHorizontalSpace(List<String> pieces, int numberOfRows) {
    // ensure that length of pieces is even
    if (pieces.size() % 2 != 0) {
      pieces.add("  ");
    }

    String[] formattedPieces = new String[numberOfRows];
    Arrays.fill(formattedPieces, "    ");
    for (int i = 0; i < pieces.size(); i += 2) {
      formattedPieces[i / 2] = String.format("%s%s", pieces.get(i), pieces.get(i + 1));
    }
    return formattedPieces;
  }

  private static String[] initBoardWithoutPieces() {
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
}
