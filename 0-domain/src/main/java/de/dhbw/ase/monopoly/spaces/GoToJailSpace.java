package de.dhbw.ase.monopoly.spaces;

import de.dhbw.ase.monopoly.*;

public class GoToJailSpace extends BoardSpace {
  public GoToJailSpace() {
    super("Go to Jail");
  }

  @Override
  public void enterSpace(Player player, int steps) {
    player.goToJail();
  }
}
