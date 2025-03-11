package de.dhbw.ase.monopoly;

import de.dhbw.ase.monopoly.spaces.*;

public class GameBoard {
  public static final int GO_POS = 0;
  public static final int READING_RR_POS = 5;
  public static final int JAIL_POS = 10;
  public static final int ST_CHARLES_PL_POS = 11;
  public static final int BOARDWALK_POS = 39;
  public static final int ILLINOIS_AV_POS = 24;

  private final BoardSpace[] gameBoard;

  public GameBoard(ActionCard[] communityChestCards, ActionCard[] chanceCards) {
    ActionSpace communityChestSpace = new ActionSpace("Community Chest", communityChestCards);
    ActionSpace chanceSpace = new ActionSpace("Chance", chanceCards);

    gameBoard =  new BoardSpace[] {
        new EmptySpace("Go"),
        new PropertySpace(new PropertyCard("Mediterranean Avenue", 'v', 60, 50, new int[] { 2, 10, 30, 90, 160, 250 }, 30)),
        communityChestSpace,
        new PropertySpace(new PropertyCard("Baltic Avenue", 'v', 60, 50, new int[] { 4, 20, 60, 180, 320, 450 }, 30)),
        new TaxSpace("Income Tax", 200),
        new RailroadSpace("Reading Railroad"),
        new PropertySpace(new PropertyCard("Oriental Avenue", 'l', 100, 50, new int[] { 6, 30, 90, 270, 400, 550 }, 50)),
        chanceSpace,
        new PropertySpace(new PropertyCard("Vermont Avenue", 'l', 100, 50, new int[] { 6, 30, 90, 270, 400, 550 }, 50)),
        new PropertySpace(new PropertyCard("Connecticut Avenue", 'l', 120, 50, new int[] { 8, 40, 100, 300, 450, 600 }, 60)),

        new EmptySpace("Jail"),
        new PropertySpace(new PropertyCard("St. Charles Place", 'p', 140, 100, new int[] { 10, 50, 150, 450, 625, 750 }, 70)),
        new UtilitySpace("Electric Company"),
        new PropertySpace(new PropertyCard("States Avenue", 'p', 140, 100, new int[] { 10, 50, 150, 450, 625, 750 }, 70)),
        new PropertySpace(new PropertyCard("Virginia Avenue", 'p', 160, 100, new int[] { 12, 60, 180, 500, 700, 900 }, 80)),
        new RailroadSpace("Pennsylvania Railroad"),
        new PropertySpace(new PropertyCard("St. James Place", 'o', 180, 100, new int[] { 14, 70, 200, 550, 750, 950 }, 90)),
        communityChestSpace,
        new PropertySpace(new PropertyCard("Tennessee Avenue", 'o', 180, 100, new int[] { 14, 70, 200, 550, 750, 950 }, 90)),
        new PropertySpace(new PropertyCard("New Yorkl Avenue", 'o', 200, 100, new int[] { 16, 80, 220, 600, 800, 1000 }, 100)),

        new EmptySpace("Free Parking"),
        new PropertySpace(new PropertyCard("Kentucky Avenue", 'r', 220, 150, new int[] { 18, 90, 250, 700, 875, 1050 }, 110)),
        chanceSpace,
        new PropertySpace(new PropertyCard("Indiana Avenue", 'r', 220, 150, new int[] { 18, 90, 250, 700, 875, 1050 }, 110)),
        new PropertySpace(new PropertyCard("Illinois Avenue", 'r', 240, 150, new int[] { 20, 100, 300, 750, 925, 1100 }, 120)),
        new RailroadSpace("B. & O. Railroad"),
        new PropertySpace(new PropertyCard("Atlantic Avenue", 'y', 260, 150, new int[] { 22, 110, 330, 800, 975, 1150 }, 130)),
        new PropertySpace(new PropertyCard("Ventnor Avenue", 'y', 260, 150, new int[] { 22, 110, 330, 800, 975, 1150 }, 130)),
        new UtilitySpace("Water Works"),
        new PropertySpace(new PropertyCard("Marvin Gardens", 'y', 280, 150, new int[] { 24, 120, 360, 850, 1025, 1200 }, 140)),

        new GoToJailSpace(),
        new PropertySpace(new PropertyCard("Pacific Avenue", 'g', 300, 200, new int[] { 26, 130, 390, 900, 1100, 1275 }, 150)),
        new PropertySpace(new PropertyCard("North Carolina Avenue", 'g', 300, 200, new int[] { 26, 130, 390, 900, 1100, 1275 }, 150)),
        communityChestSpace,
        new PropertySpace(new PropertyCard("Pennsylvania Avenue", 'g', 320, 200, new int[] { 28, 150, 450, 1000, 1200, 1400 }, 160)),
        new RailroadSpace("Short Line"),
        chanceSpace,
        new PropertySpace(new PropertyCard("Park Place", 'b', 350, 200, new int[] { 35, 175, 500, 1100, 1300, 1500 }, 175)),
        new TaxSpace("Luxury Tax", 100),
        new PropertySpace(new PropertyCard("Boardwalk", 'b', 400, 200, new int[] { 50, 200, 600, 1400, 1700, 2000 }, 200)),
    };
  }

  public void enterSpace(Player player, int steps) {

  }
}
