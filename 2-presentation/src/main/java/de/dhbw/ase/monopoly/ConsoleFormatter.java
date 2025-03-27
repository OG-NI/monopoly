package de.dhbw.ase.monopoly;

import java.util.Arrays;

public final class ConsoleFormatter {
  public static final String CLEAR_CONSOLE = "\033[H\033[2J";

  private ConsoleFormatter() {
  }

  public static String colorText(String text, Color textColor) {
    return String.format("\u001B[38;2;%sm%s\u001B[0m", textColor, text);
  }

  public static String colorText(String text, Color textColor, Color backgroundColor) {
    return String.format("\u001B[38;2;%sm\u001B[48;2;%sm%s\u001B[0m", textColor, backgroundColor, text);
  }

  /**
   * calculates the length of a {@code String} ignoring ANSI escape characters
   */
  public static int ansiStringLength(String s) {
    // some nerd fonts characters have length 2
    s = s.replaceAll("[󱠃󰖏󰭇󰇥󰮤󰻀󰞬󱙖]", "x");
    return s.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
  }

  public static String[] joinTablesVertically(String[] table1, String[] table2) {
    String[] smallerTable;
    if (table1[0].length() > table2[0].length()) {
      smallerTable = table2;
    } else {
      smallerTable = table1;
    }

    // horizontal padding to center smaller table relative to larger table
    int widthDifference = Math.abs(table1[0].length() - table2[0].length());
    String spacesBefore = " ".repeat((int) Math.floor(widthDifference / 2d));
    String spacesAfter = " ".repeat((int) Math.ceil(widthDifference / 2d));
    for (int i = 0; i < smallerTable.length; i++) {
      smallerTable[i] = spacesBefore + smallerTable[i] + spacesAfter;
    }

    // join tables to new table
    String[] table = new String[table1.length + table2.length];
    System.arraycopy(table1, 0, table, 0, table1.length);
    System.arraycopy(table2, 0, table, table1.length, table2.length);
    return table;
  }

  public static String[] joinTablesHorizontally(String[] table1, String[] table2) {
    // identify smaller table
    String[] smallerTable;
    int largerTableLength;
    if (table1.length > table2.length) {
      smallerTable = table2;
      largerTableLength = table1.length;
    } else {
      smallerTable = table1;
      largerTableLength = table2.length;
    }

    // vertical padding to center smaller table relative to larger table
    int heightDifference = Math.abs(table1.length - table2.length);
    int emptyLinesBefore = (int) Math.floor(heightDifference / 2d);
    String[] paddedSmallerTable = new String[largerTableLength];
    String spaces = " ".repeat(smallerTable[0].length());
    Arrays.fill(paddedSmallerTable, spaces);
    System.arraycopy(smallerTable, 0, paddedSmallerTable, emptyLinesBefore, smallerTable.length);

    // change reference to smaller table with padding
    if (table1.length > table2.length) {
      table2 = paddedSmallerTable;
    } else {
      table1 = paddedSmallerTable;
    }

    // join tables to new table
    String[] table = table1.clone();
    for (int i = 0; i < table.length; i++) {
      table[i] += table2[i];
    }
    return table;
  }

  public static String[] formatTable(String[][] data) {
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

    String[] table = new String[data.length + 3];
    table[0] = "┌" + horizontalLine + "┐";
    table[1] = formatTableRow(data[0], maximums);
    table[2] = "├" + horizontalLine + "┤";
    for (int i = 1; i < data.length; i++) {
      table[i + 2] = formatTableRow(data[i], maximums);
    }
    table[table.length - 1] = "└" + horizontalLine + "┘";
    return table;
  }

  private static String formatTableRow(String[] row, int[] maximums) {
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
    s += "│";
    return s;
  }
}
