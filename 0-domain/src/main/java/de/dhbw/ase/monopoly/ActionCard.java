package de.dhbw.ase.monopoly;

import java.util.Arrays;

public abstract class ActionCard { // TODO: extends value object
  private final String text;

  public ActionCard(String text, Player[] players) {
    this.text = text;
  }

  public abstract void action(Player player);

  protected void transferMoneyWithEveryPlayer(int money, Player currentPlayer, Player[] players) {
    Arrays.stream(players).forEach(p -> p.transferMoney(-money));
    int receivedMoney = money * players.length;
    currentPlayer.transferMoney(receivedMoney);
  }
}
