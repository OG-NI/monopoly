package de.dhbw.ase.monopoly.generators;

import de.dhbw.ase.monopoly.*;
import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.services.*;

public class SpacesGenerator {
  public static BoardSpace[] generateSpaces(EventReceiver eventReceiver, MovementService movementService,
      PropertyCountService propertyCountService, ActionCardService actionCardService) {

    ActionSpace communityChestSpace = new ActionSpace(ActionType.COMMUNITY_CHEST, eventReceiver, actionCardService);
    ActionSpace chanceSpace = new ActionSpace(ActionType.CHANCE, eventReceiver, actionCardService);

    return new BoardSpace[] {
        // bottom side
        new EmptySpace("Go", eventReceiver),
        new ColoredPropertySpace("Mediterranean Avenue", 60, 30, 'b', 50, new int[] { 2, 10, 30, 90, 160, 250 },
            eventReceiver, propertyCountService),
        communityChestSpace,
        new ColoredPropertySpace("Baltic Avenue", 60, 30, 'b', 50, new int[] { 4, 20, 60, 180, 320, 450 },
            eventReceiver, propertyCountService),
        new TaxSpace("Income Tax", 200, eventReceiver),
        new RailroadSpace("Reading Railroad", eventReceiver, propertyCountService),
        new ColoredPropertySpace("Oriental Avenue", 100, 50, 'l', 50, new int[] { 6, 30, 90, 270, 400, 550 },
            eventReceiver, propertyCountService),
        chanceSpace,
        new ColoredPropertySpace("Vermont Avenue", 100, 50, 'l', 50, new int[] { 6, 30, 90, 270, 400, 550 },
            eventReceiver, propertyCountService),
        new ColoredPropertySpace("Connecticut Avenue", 120, 60, 'l', 50, new int[] { 8, 40, 100, 300, 450, 600 },
            eventReceiver, propertyCountService),

        // left side
        new EmptySpace("Jail Just Visiting", eventReceiver),
        new ColoredPropertySpace("St. Charles Place", 140, 70, 'p', 100, new int[] { 10, 50, 150, 450, 625, 750 },
            eventReceiver, propertyCountService),
        new UtilitySpace("Electric Company", eventReceiver, propertyCountService),
        new ColoredPropertySpace("States Avenue", 140, 70, 'p', 100, new int[] { 10, 50, 150, 450, 625, 750 },
            eventReceiver, propertyCountService),
        new ColoredPropertySpace("Virginia Avenue", 160, 80, 'p', 100, new int[] { 12, 60, 180, 500, 700, 900 },
            eventReceiver, propertyCountService),
        new RailroadSpace("Pennsylvania Railroad", eventReceiver, propertyCountService),
        new ColoredPropertySpace("St. James Place", 180, 90, 'o', 100, new int[] { 14, 70, 200, 550, 750, 950 },
            eventReceiver, propertyCountService),
        communityChestSpace,
        new ColoredPropertySpace("Tennessee Avenue", 180, 90, 'o', 100, new int[] { 14, 70, 200, 550, 750, 950 },
            eventReceiver, propertyCountService),
        new ColoredPropertySpace("New York Avenue", 200, 100, 'o', 100, new int[] { 16, 80, 220, 600, 800, 1000 },
            eventReceiver, propertyCountService),

        // top side
        new EmptySpace("Free Parking", eventReceiver),
        new ColoredPropertySpace("Kentucky Avenue", 220, 110, 'r', 150, new int[] { 18, 90, 250, 700, 875, 1050 },
            eventReceiver, propertyCountService),
        chanceSpace,
        new ColoredPropertySpace("Indiana Avenue", 220, 110, 'r', 150, new int[] { 18, 90, 250, 700, 875, 1050 },
            eventReceiver, propertyCountService),
        new ColoredPropertySpace("Illinois Avenue", 240, 120, 'r', 150, new int[] { 20, 100, 300, 750, 925, 1100 },
            eventReceiver, propertyCountService),
        new RailroadSpace("B. & O. Railroad", eventReceiver, propertyCountService),
        new ColoredPropertySpace("Atlantic Avenue", 260, 130, 'y', 150, new int[] { 22, 110, 330, 800, 975, 1150 },
            eventReceiver, propertyCountService),
        new ColoredPropertySpace("Ventnor Avenue", 260, 130, 'y', 150, new int[] { 22, 110, 330, 800, 975, 1150 },
            eventReceiver, propertyCountService),
        new UtilitySpace("Water Works", eventReceiver, propertyCountService),
        new ColoredPropertySpace("Marvin Gardens", 280, 140, 'y', 150, new int[] { 24, 120, 360, 850, 1025, 1200 },
            eventReceiver, propertyCountService),

        // right side
        new GoToJailSpace(eventReceiver, movementService),
        new ColoredPropertySpace("Pacific Avenue", 300, 150, 'g', 200, new int[] { 26, 130, 390, 900, 1100, 1275 },
            eventReceiver, propertyCountService),
        new ColoredPropertySpace("North Carolina Avenue", 300, 150, 'g', 200,
            new int[] { 26, 130, 390, 900, 1100, 1275 }, eventReceiver, propertyCountService),
        communityChestSpace,
        new ColoredPropertySpace("Pennsylvania Avenue", 320, 160, 'g', 200,
            new int[] { 28, 150, 450, 1000, 1200, 1400 }, eventReceiver, propertyCountService),
        new RailroadSpace("Short Line", eventReceiver, propertyCountService),
        chanceSpace,
        new ColoredPropertySpace("Park Place", 350, 175, 'd', 200, new int[] { 35, 175, 500, 1100, 1300, 1500 },
            eventReceiver, propertyCountService),
        new TaxSpace("Luxury Tax", 100, eventReceiver),
        new ColoredPropertySpace("Boardwalk", 400, 200, 'd', 200, new int[] { 50, 200, 600, 1400, 1700, 2000 },
            eventReceiver, propertyCountService),
    };

  }
}
