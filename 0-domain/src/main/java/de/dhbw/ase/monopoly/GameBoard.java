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
  private final PropertySpace[] propertySpaces;
  private final ColoredPropertySpace[] coloredPropertySpaces;

  public GameBoard(Game game) {
    ActionCard[] communityChestCards = ActionCardFactory.initCommunityChestCards(game);
    ActionCard[] chanceCards = ActionCardFactory.initChanceCards(game);
    ActionSpace communityChestSpace = new ActionSpace("Community Chest", communityChestCards);
    ActionSpace chanceSpace = new ActionSpace("Chance", chanceCards);

    boardSpaces = new BoardSpace[] {
        // bottom side
        new EmptySpace("Go"),
        new ColoredPropertySpace("Mediterranean Avenue", 60, 30, 'b', 50, new int[] { 2, 10, 30, 90, 160, 250 }),
        communityChestSpace,
        new ColoredPropertySpace("Baltic Avenue", 60, 30, 'b', 50, new int[] { 4, 20, 60, 180, 320, 450 }),
        new TaxSpace("Income Tax", 200),
        new RailroadSpace("Reading Railroad"),
        new ColoredPropertySpace("Oriental Avenue", 100, 50, 'l', 50, new int[] { 6, 30, 90, 270, 400, 550 }),
        chanceSpace,
        new ColoredPropertySpace("Vermont Avenue", 100, 50, 'l', 50, new int[] { 6, 30, 90, 270, 400, 550 }),
        new ColoredPropertySpace("Connecticut Avenue", 120, 60, 'l', 50, new int[] { 8, 40, 100, 300, 450, 600 }),

        // left side
        new EmptySpace("Jail Just Visiting"),
        new ColoredPropertySpace("St. Charles Place", 140, 70, 'p', 100, new int[] { 10, 50, 150, 450, 625, 750 }),
        new UtilitySpace("Electric Company"),
        new ColoredPropertySpace("States Avenue", 140, 70, 'p', 100, new int[] { 10, 50, 150, 450, 625, 750 }),
        new ColoredPropertySpace("Virginia Avenue", 160, 80, 'p', 100, new int[] { 12, 60, 180, 500, 700, 900 }),
        new RailroadSpace("Pennsylvania Railroad"),
        new ColoredPropertySpace("St. James Place", 180, 90, 'o', 100, new int[] { 14, 70, 200, 550, 750, 950 }),
        communityChestSpace,
        new ColoredPropertySpace("Tennessee Avenue", 180, 90, 'o', 100, new int[] { 14, 70, 200, 550, 750, 950 }),
        new ColoredPropertySpace("New York Avenue", 200, 100, 'o', 100, new int[] { 16, 80, 220, 600, 800, 1000 }),

        // top side
        new EmptySpace("Free Parking"),
        new ColoredPropertySpace("Kentucky Avenue", 220, 110, 'r', 150, new int[] { 18, 90, 250, 700, 875, 1050 }),
        chanceSpace,
        new ColoredPropertySpace("Indiana Avenue", 220, 110, 'r', 150, new int[] { 18, 90, 250, 700, 875, 1050 }),
        new ColoredPropertySpace("Illinois Avenue", 240, 120, 'r', 150, new int[] { 20, 100, 300, 750, 925, 1100 }),
        new RailroadSpace("B. & O. Railroad"),
        new ColoredPropertySpace("Atlantic Avenue", 260, 130, 'y', 150, new int[] { 22, 110, 330, 800, 975, 1150 }),
        new ColoredPropertySpace("Ventnor Avenue", 260, 130, 'y', 150, new int[] { 22, 110, 330, 800, 975, 1150 }),
        new UtilitySpace("Water Works"),
        new ColoredPropertySpace("Marvin Gardens", 280, 140, 'y', 150, new int[] { 24, 120, 360, 850, 1025, 1200 }),

        // right side
        new GoToJailSpace(),
        new ColoredPropertySpace("Pacific Avenue", 300, 150, 'g', 200, new int[] { 26, 130, 390, 900, 1100, 1275 }),
        new ColoredPropertySpace("North Carolina Avenue", 300, 150, 'g', 200, new int[] { 26, 130, 390, 900, 1100, 1275 }),
        communityChestSpace,
        new ColoredPropertySpace("Pennsylvania Avenue", 320, 160, 'g', 200, new int[] { 28, 150, 450, 1000, 1200, 1400 }),
        new RailroadSpace("Short Line"),
        chanceSpace,
        new ColoredPropertySpace("Park Place", 350, 175, 'd', 200, new int[] { 35, 175, 500, 1100, 1300, 1500 }),
        new TaxSpace("Luxury Tax", 100),
        new ColoredPropertySpace("Boardwalk", 400, 200, 'd', 200, new int[] { 50, 200, 600, 1400, 1700, 2000 }),
    };

    propertySpaces = Arrays.stream(boardSpaces)
        .filter(space -> space instanceof PropertySpace)
        .toArray(PropertySpace[]::new);

    coloredPropertySpaces = Arrays.stream(propertySpaces)
        .filter(space -> space instanceof ColoredPropertySpace)
        .toArray(ColoredPropertySpace[]::new);
  }

  public String enterSpace(int position, Player player, int steps) {
    return boardSpaces[position].enterSpace(player, steps);
  }

  public PropertySpace[] getPropertySpaces() {
    return propertySpaces;
  }

  public ColoredPropertySpace[] getColoredPropertySpaces() {
    return coloredPropertySpaces;
  }

  public BoardSpace getSpace(int position) {
    return boardSpaces[position];
  }
}
