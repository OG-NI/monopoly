package de.dhbw.ase.monopoly;

import java.util.Arrays;

public final class ConsoleFormatter {
  public static final String CLEAR_CONSOLE = "\033[H\033[2J";

  private ConsoleFormatter() {
  }

  public static String colorText(String text, Color foregroundColor) {
    return String.format("\u001B[38;2;%sm%s\u001B[0m", foregroundColor, text);
  }

  public static String colorText(String text, Color foregroundColor, Color backgroundColor) {
    return String.format("\u001B[38;2;%sm\u001B[48;2;%sm%s\u001B[0m", foregroundColor, backgroundColor, text);
  }

  public static int ansiStringLength(String s) {
    // some nerd fonts characters have length 2
    s = s.replaceAll("[󱠃󰖏󰭇󰇥󰮤󰻀󰞬󱙖]", "x");
    return s.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
  }

  public static String[][] splitTableVertically(String[][] data) {
    int halfHeight = data.length / 2;
    int width = data[0].length;
    String[][] newData = new String[halfHeight + 1][width * 2];
    for (int i = 0; i < halfHeight; i++) {
      System.arraycopy(data[i], 0, newData[i + 1], 0, width);
      System.arraycopy(data[halfHeight + i], 0, newData[i + 1], width, width);
    }
    return newData;
  }

  public static String formatTable(String[][] data) {
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
    s += "│\n";
    return s;
  }
}
