package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class GoToJailSpace extends BoardSpace {
  public GoToJailSpace() {
    super("Go to Jail");
  }

  @Override
  public String enterSpace(Player player, int steps) {
    player.goToJail();
    return "You entered the Go To Jail space and went directly to jail.";
  }
}
