package de.dhbw.ase.monopoly;

public enum Piece {
  DOG(" "), SHOE("󰭇 "), SHIP(" "), DUCK("󰇥 "), HAT("󰮤 "), PENGUIN("󰻀 "), CAR("󰞬 ");

  private final String icon;

  private Piece(String icon) {
    this.icon = icon;
  }

  public static Piece byName(String name) {
    return Piece.valueOf(name.toUpperCase());
  }

  public static boolean includesName(String name) {
    try {
      byName(name);
      return true;
    } catch (IllegalArgumentException exception) {
      return false;
    }
  }

  @Override
  public String toString() {
    return String.valueOf(icon);
  }
}
