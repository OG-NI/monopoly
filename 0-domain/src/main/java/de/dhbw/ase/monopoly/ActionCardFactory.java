package de.dhbw.ase.monopoly;

public class ActionCardFactory {

  public static ActionCard[] initCommunityChestCards(Game game) {
    return new ActionCard[] {
        new ActionCard("Advance to Go (Collect $200)", game) {
          public void performAction(Player player) {
            player.moveToPosition(GameBoard.GO_POS);
          }
        },
        new ActionCard("Bank error in your favor. Collect $200", game) {
          public void performAction(Player player) {
            player.transferMoney(200);
          }
        },
        new ActionCard("Doctor’s fee. Pay $50", game) {
          public void performAction(Player player) {
            player.transferMoney(-50);
          }
        },
        new ActionCard("From sale of stock you get $50", game) {
          public void performAction(Player player) {
            player.transferMoney(50);
          }
        },
        new ActionCard("Get Out of Jail Free", game) {
          public void performAction(Player player) {
            player.incGetOutOfJailFreeCards();
          }
        },
        new ActionCard("Go to Jail. Go directly to jail, do not pass Go, do not collect $200", game) {
          public void performAction(Player player) {
            player.goToJail();
          }
        },
        new ActionCard("Holiday fund matures. Receive $100", game) {
          public void performAction(Player player) {
            player.transferMoney(100);
          }
        },
        new ActionCard("Income tax refund. Collect $20", game) {
          public void performAction(Player player) {
            player.transferMoney(20);
          }
        },
        new ActionCard("It is your birthday. Collect $10 from every player", game) {
          public void performAction(Player player) {
            game.transferMoneyWithEveryPlayer(10);
          }
        },
        new ActionCard("Life insurance matures. Collect $100", game) {
          public void performAction(Player player) {
            player.transferMoney(100);
          }
        },
        new ActionCard("Pay hospital fees of $100", game) {
          public void performAction(Player player) {
            player.transferMoney(-100);
          }
        },
        new ActionCard("Pay hospital fees of $100", game) {
          public void performAction(Player player) {
            player.transferMoney(-100);
          }
        },
        new ActionCard("Pay hospital fees of $100", game) {
          public void performAction(Player player) {
            player.transferMoney(-100);
          }
        },
        new ActionCard("You are assessed for street repair. $40 per house. $115 per hotel", game) {
          public void performAction(Player player) {
            int costs = 40 * player.getOwnedHouses() + 115 * player.getOwnedHotels();
            player.transferMoney(-costs);
          }
        },
        new ActionCard("You have won second prize in a beauty contest. Collect $10", game) {
          public void performAction(Player player) {
            player.transferMoney(10);
          }
        },
        new ActionCard("You inherit $100", game) {
          public void performAction(Player player) {
            player.transferMoney(100);
          }
        },
    };
  }

  public static ActionCard[] initChanceCards(Game game) {
    return new ActionCard[] {
        new ActionCard("Advance to Boardwalk", game) {
          public void performAction(Player player) {
            player.moveToPosition(GameBoard.BOARDWALK_POS);
          }
        },
        new ActionCard("Advance to Go (Collect $200)", game) {
          public void performAction(Player player) {
            player.moveToPosition(GameBoard.GO_POS);
          }
        },
        new ActionCard("Advance to Illinois Avenue. If you pass Go, collect $200", game) {
          public void performAction(Player player) {
            player.moveToPosition(GameBoard.ILLINOIS_AV_POS);
          }
        },
        new ActionCard("Advance to St. Charles Place. If you pass Go, collect $200", game) {
          public void performAction(Player player) {
            player.moveToPosition(GameBoard.ST_CHARLES_PL_POS);
          }
        },
        new ActionCard("Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled", game) {
          public void performAction(Player player) {
            player.moveToNearestRailroad();
          }
        },
        new ActionCard("Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled", game) {
          public void performAction(Player player) {
            player.moveToNearestRailroad();
          }
        },
        new ActionCard("Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.", game) {
          public void performAction(Player player) {
            player.moveToNearestUtility();
          }
        },
        new ActionCard("Bank pays you dividend of $50", game) {
          public void performAction(Player player) {
            player.transferMoney(50);
          }
        },
        new ActionCard("Get Out of Jail Free", game) {
          public void performAction(Player player) {
            player.incGetOutOfJailFreeCards();
          }
        },
        new ActionCard("Go Back 3 Spaces", game) {
          public void performAction(Player player) {
            player.moveForward(-3);
          }
        },
        new ActionCard("Go to Jail. Go directly to Jail, do not pass Go, do not collect $200", game) {
          public void performAction(Player player) {
            player.goToJail();
          }
        },
        new ActionCard("Make general repairs on all your property. For each house pay $25. For each hotel pay $100", game) {
          public void performAction(Player player) {
            int costs = 25 * player.getOwnedHouses() + 100 * player.getOwnedHotels();
            player.transferMoney(-costs);
          }
        },
        new ActionCard("Speeding fine $15", game) {
          public void performAction(Player player) {
            player.transferMoney(-15);
          }
        },
        new ActionCard("Take a trip to Reading Railroad. If you pass Go, collect $200", game) {
          public void performAction(Player player) {
            player.moveToPosition(GameBoard.READING_RR_POS);
          }
        },
        new ActionCard("You have been elected Chairman of the Board. Pay each player $50", game) {
          public void performAction(Player player) {
            game.transferMoneyWithEveryPlayer(-50);
          }
        },
        new ActionCard("Your building loan matures. Collect $150", game) {
          public void performAction(Player player) {
            player.transferMoney(150);
          }
        },
    };
  }
}
