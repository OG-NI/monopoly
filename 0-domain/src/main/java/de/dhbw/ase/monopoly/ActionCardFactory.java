package de.dhbw.ase.monopoly;

public class ActionCardFactory {

  public static ActionCard[] initCommunityChestCards(Game game) {
    return new ActionCard[] {
        new ActionCard("Advance to Go (Collect $200)", game) {
          public String performAction(Player player) {
            player.moveToPosition(GameBoard.GO_POS);
            return "";
          }
        },
        new ActionCard("Bank error in your favor. Collect $200", game) {
          public String performAction(Player player) {
            player.transferMoney(200);
            return "";
          }
        },
        new ActionCard("Doctor’s fee. Pay $50", game) {
          public String performAction(Player player) {
            player.transferMoney(-50);
            return "";
          }
        },
        new ActionCard("From sale of stock you get $50", game) {
          public String performAction(Player player) {
            player.transferMoney(50);
            return "";
          }
        },
        new ActionCard("Get Out of Jail Free", game) {
          public String performAction(Player player) {
            player.incGetOutOfJailFreeCards();
            return "";
          }
        },
        new ActionCard("Go to Jail. Go directly to jail, do not pass Go, do not collect $200", game) {
          public String performAction(Player player) {
            player.goToJail();
            return "";
          }
        },
        new ActionCard("Holiday fund matures. Receive $100", game) {
          public String performAction(Player player) {
            player.transferMoney(100);
            return "";
          }
        },
        new ActionCard("Income tax refund. Collect $20", game) {
          public String performAction(Player player) {
            player.transferMoney(20);
            return "";
          }
        },
        new ActionCard("It is your birthday. Collect $10 from every player", game) {
          public String performAction(Player player) {
            game.transferMoneyWithEveryPlayer(10);
            return "";
          }
        },
        new ActionCard("Life insurance matures. Collect $100", game) {
          public String performAction(Player player) {
            player.transferMoney(100);
            return "";
          }
        },
        new ActionCard("Pay hospital fees of $100", game) {
          public String performAction(Player player) {
            player.transferMoney(-100);
            return "";
          }
        },
        new ActionCard("Pay hospital fees of $100", game) {
          public String performAction(Player player) {
            player.transferMoney(-100);
            return "";
          }
        },
        new ActionCard("Pay hospital fees of $100", game) {
          public String performAction(Player player) {
            player.transferMoney(-100);
            return "";
          }
        },
        new ActionCard("You are assessed for street repair. $40 per house. $115 per hotel", game) {
          public String performAction(Player player) {
            GameBoard gameBoard = game.getGameBoard();
            int costs = 40 * BuildingService.getPlayerHouseCount(gameBoard, player)
                + 115 * BuildingService.getPlayerHotelCount(gameBoard, player);
            player.transferMoney(-costs);
            return String.format("The repairs set you back %d$.", costs);
          }
        },
        new ActionCard("You have won second prize in a beauty contest. Collect $10", game) {
          public String performAction(Player player) {
            player.transferMoney(10);
            return "";
          }
        },
        new ActionCard("You inherit $100", game) {
          public String performAction(Player player) {
            player.transferMoney(100);
            return "";
          }
        },
    };
  }

  public static ActionCard[] initChanceCards(Game game) {
    return new ActionCard[] {
        new ActionCard("Advance to Boardwalk", game) {
          public String performAction(Player player) {
            return player.moveToPosition(GameBoard.BOARDWALK_POS);
          }
        },
        new ActionCard("Advance to Go (Collect $200)", game) {
          public String performAction(Player player) {
            return player.moveToPosition(GameBoard.GO_POS);
          }
        },
        new ActionCard("Advance to Illinois Avenue. If you pass Go, collect $200", game) {
          public String performAction(Player player) {
            return player.moveToPosition(GameBoard.ILLINOIS_AV_POS);
          }
        },
        new ActionCard("Advance to St. Charles Place. If you pass Go, collect $200", game) {
          public String performAction(Player player) {
            return player.moveToPosition(GameBoard.ST_CHARLES_PL_POS);
          }
        },
        new ActionCard("Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled", game) {
          public String performAction(Player player) {
            return player.moveToNearestRailroad();
          }
        },
        new ActionCard("Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled", game) {
          public String performAction(Player player) {
            return player.moveToNearestRailroad();
          }
        },
        new ActionCard("Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.", game) {
          public String performAction(Player player) {
            return player.moveToNearestUtility();
          }
        },
        new ActionCard("Bank pays you dividend of $50", game) {
          public String performAction(Player player) {
            player.transferMoney(50);
            return "";
          }
        },
        new ActionCard("Get Out of Jail Free", game) {
          public String performAction(Player player) {
            player.incGetOutOfJailFreeCards();
            return "";
          }
        },
        new ActionCard("Go Back 3 Spaces", game) {
          public String performAction(Player player) {
            return player.moveForward(-3);
          }
        },
        new ActionCard("Go to Jail. Go directly to Jail, do not pass Go, do not collect $200", game) {
          public String performAction(Player player) {
            player.goToJail();
            return "";
          }
        },
        new ActionCard("Make general repairs on all your property. For each house pay $25. For each hotel pay $100", game) {
          public String performAction(Player player) {
            GameBoard gameBoard = game.getGameBoard();
            int costs = 25 * BuildingService.getPlayerHouseCount(gameBoard, player)
                + 100 * BuildingService.getPlayerHotelCount(gameBoard, player);
            player.transferMoney(-costs);
            return String.format("The repairs set you back %d$.", costs);
          }
        },
        new ActionCard("Speeding fine $15", game) {
          public String performAction(Player player) {
            player.transferMoney(-15);
            return "";
          }
        },
        new ActionCard("Take a trip to Reading Railroad. If you pass Go, collect $200", game) {
          public String performAction(Player player) {
            return player.moveToPosition(GameBoard.READING_RR_POS);
          }
        },
        new ActionCard("You have been elected Chairman of the Board. Pay each player $50", game) {
          public String performAction(Player player) {
            game.transferMoneyWithEveryPlayer(-50);
            return "";
          }
        },
        new ActionCard("Your building loan matures. Collect $150", game) {
          public String performAction(Player player) {
            player.transferMoney(150);
            return "";
          }
        },
    };
  }
}
