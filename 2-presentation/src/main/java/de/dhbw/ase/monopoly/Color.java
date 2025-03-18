package de.dhbw.ase.monopoly;

public enum Color {
  BROWN("149;84;54"),
  LIGHT_BLUE("170;255;250"),
  PINK("217;57;150"),
  ORANGE("247;148;28"),
  RED("237;28;35"),
  YELLOW("254;242;5"),
  GREEN("33;178;90"),
  DARK_BLUE("0;114;188"),

  BACKGROUND("204;230;207"),
  BLACK("0;0;0"),
  BLUE("2;175;239"),
  WHITE("255;255;255"),
  GRAY("197;197;197");

  private final String code;

  private Color(String code) {
    this.code = code;
  }

  public static Color byChar(char colorCode) {
    return switch (colorCode) {
      case 'b' -> BROWN;
      case 'l' -> LIGHT_BLUE;
      case 'p' -> PINK;
      case 'o' -> ORANGE;
      case 'r' -> RED;
      case 'y' -> YELLOW;
      case 'g' -> GREEN;
      case 'd' -> DARK_BLUE;
      default -> BLACK;
    };
  }

  @Override
  public String toString() {
    return code;
  }
}
