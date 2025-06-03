package de.dhbw.ase.monopoly;

import java.util.Arrays;

import de.dhbw.ase.monopoly.spaces.*;

/**
 * aggregate
 */
public class GameBoard {
  public static final int GO_POS = 0;
  public static final int READING_RR_POS = 5;
  public static final int JAIL_POS = 10;
  public static final int ST_CHARLES_PL_POS = 11;
  public static final int BOARDWALK_POS = 39;
  public static final int ILLINOIS_AV_POS = 24;

  private final BoardSpace[] boardSpaces;

  public GameBoard(Game game, EventReceiver eventReceiver) {
    ActionCard[] communityChestCards = ActionCardFactory.initCommunityChestCards(game, eventReceiver);
    ActionCard[] chanceCards = ActionCardFactory.initChanceCards(game, eventReceiver);
    ActionSpace communityChestSpace = new ActionSpace("Community Chest", communityChestCards, eventReceiver);
    ActionSpace chanceSpace = new ActionSpace("Chance", chanceCards, eventReceiver);

    boardSpaces = new BoardSpace[] {
        // bottom side
        new EmptySpace("Go", eventReceiver),
        new ColoredPropertySpace("Mediterranean Avenue", 60, 30, this, 'b', 50, new int[] { 2, 10, 30, 90, 160, 250 }, eventReceiver),
        communityChestSpace,
        new ColoredPropertySpace("Baltic Avenue", 60, 30, this, 'b', 50, new int[] { 4, 20, 60, 180, 320, 450 }, eventReceiver),
        new TaxSpace("Income Tax", 200, eventReceiver),
        new RailroadSpace("Reading Railroad", this, eventReceiver),
        new ColoredPropertySpace("Oriental Avenue", 100, 50, this, 'l', 50, new int[] { 6, 30, 90, 270, 400, 550 }, eventReceiver),
        chanceSpace,
        new ColoredPropertySpace("Vermont Avenue", 100, 50, this, 'l', 50, new int[] { 6, 30, 90, 270, 400, 550 }, eventReceiver),
        new ColoredPropertySpace("Connecticut Avenue", 120, 60, this, 'l', 50, new int[] { 8, 40, 100, 300, 450, 600 }, eventReceiver),

        // left side
        new EmptySpace("Jail Just Visiting", eventReceiver),
        new ColoredPropertySpace("St. Charles Place", 140, 70, this, 'p', 100, new int[] { 10, 50, 150, 450, 625, 750 }, eventReceiver),
        new UtilitySpace("Electric Company", this, eventReceiver),
        new ColoredPropertySpace("States Avenue", 140, 70, this, 'p', 100, new int[] { 10, 50, 150, 450, 625, 750 }, eventReceiver),
        new ColoredPropertySpace("Virginia Avenue", 160, 80, this, 'p', 100, new int[] { 12, 60, 180, 500, 700, 900 }, eventReceiver),
        new RailroadSpace("Pennsylvania Railroad", this, eventReceiver),
        new ColoredPropertySpace("St. James Place", 180, 90, this, 'o', 100, new int[] { 14, 70, 200, 550, 750, 950 }, eventReceiver),
        communityChestSpace,
        new ColoredPropertySpace("Tennessee Avenue", 180, 90, this, 'o', 100, new int[] { 14, 70, 200, 550, 750, 950 }, eventReceiver),
        new ColoredPropertySpace("New York Avenue", 200, 100, this, 'o', 100, new int[] { 16, 80, 220, 600, 800, 1000 }, eventReceiver),

        // top side
        new EmptySpace("Free Parking", eventReceiver),
        new ColoredPropertySpace("Kentucky Avenue", 220, 110, this, 'r', 150, new int[] { 18, 90, 250, 700, 875, 1050 }, eventReceiver),
        chanceSpace,
        new ColoredPropertySpace("Indiana Avenue", 220, 110, this, 'r', 150, new int[] { 18, 90, 250, 700, 875, 1050 }, eventReceiver),
        new ColoredPropertySpace("Illinois Avenue", 240, 120, this, 'r', 150, new int[] { 20, 100, 300, 750, 925, 1100 }, eventReceiver),
        new RailroadSpace("B. & O. Railroad", this, eventReceiver),
        new ColoredPropertySpace("Atlantic Avenue", 260, 130, this, 'y', 150, new int[] { 22, 110, 330, 800, 975, 1150 }, eventReceiver),
        new ColoredPropertySpace("Ventnor Avenue", 260, 130, this, 'y', 150, new int[] { 22, 110, 330, 800, 975, 1150 }, eventReceiver),
        new UtilitySpace("Water Works", this, eventReceiver),
        new ColoredPropertySpace("Marvin Gardens", 280, 140, this, 'y', 150, new int[] { 24, 120, 360, 850, 1025, 1200 }, eventReceiver),

        // right side
        new GoToJailSpace(eventReceiver),
        new ColoredPropertySpace("Pacific Avenue", 300, 150, this, 'g', 200, new int[] { 26, 130, 390, 900, 1100, 1275 }, eventReceiver),
        new ColoredPropertySpace("North Carolina Avenue", 300, 150, this, 'g', 200, new int[] { 26, 130, 390, 900, 1100, 1275 }, eventReceiver),
        communityChestSpace,
        new ColoredPropertySpace("Pennsylvania Avenue", 320, 160, this, 'g', 200, new int[] { 28, 150, 450, 1000, 1200, 1400 }, eventReceiver),
        new RailroadSpace("Short Line", this, eventReceiver),
        chanceSpace,
        new ColoredPropertySpace("Park Place", 350, 175, this, 'd', 200, new int[] { 35, 175, 500, 1100, 1300, 1500 }, eventReceiver),
        new TaxSpace("Luxury Tax", 100, eventReceiver),
        new ColoredPropertySpace("Boardwalk", 400, 200, this, 'd', 200, new int[] { 50, 200, 600, 1400, 1700, 2000 }, eventReceiver),
    };

  }

  public void enterSpace(int position, Player player, int steps) {
    boardSpaces[position].enterSpace(player, steps);
  }

  public PropertySpace[] getPropertySpaces() {
    return Arrays.stream(boardSpaces)
        .filter(space -> space instanceof PropertySpace)
        .toArray(PropertySpace[]::new);
  }

  public ColoredPropertySpace[] getColoredPropertySpaces() {
    return Arrays.stream(boardSpaces)
        .filter(space -> space instanceof ColoredPropertySpace)
        .toArray(ColoredPropertySpace[]::new);
  }

  public RailroadSpace[] getRailroadSpaces() {
    return Arrays.stream(boardSpaces)
        .filter(space -> space instanceof RailroadSpace)
        .toArray(RailroadSpace[]::new);
  }

  public UtilitySpace[] getUtilitySpaces() {
    return Arrays.stream(boardSpaces)
        .filter(space -> space instanceof UtilitySpace)
        .toArray(UtilitySpace[]::new);
  }

  public BoardSpace getSpace(int position) {
    return boardSpaces[position];
  }
}
