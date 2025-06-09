package de.dhbw.ase.monopoly.generators;

import de.dhbw.ase.monopoly.entities.*;
import de.dhbw.ase.monopoly.valueobjects.ActionCard;
import de.dhbw.ase.monopoly.valueobjects.ActionCard.Builder;

import static de.dhbw.ase.monopoly.ActionType.*;

public class ActionCardsGenerator {

  public static ActionCard[] generateActionCards() {
    return new ActionCard[] {
        new Builder(COMMUNITY_CHEST, "Advance to Go (Collect $200).").moveToSpaceByName("Go").build(),
        new Builder(COMMUNITY_CHEST, "Bank error in your favor. Collect $200.").moneyTransferWithBank(200).build(),
        new Builder(COMMUNITY_CHEST, "Doctor’s fee. Pay $50.").moneyTransferWithBank(-50).build(),
        new Builder(COMMUNITY_CHEST, "From sale of stock you get $50.").moneyTransferWithBank(50).build(),
        new Builder(COMMUNITY_CHEST, "Get Out of Jail Free.").getOutOfJailFreeCard().build(),
        new Builder(COMMUNITY_CHEST, "Go to Jail. Go directly to jail, do not pass Go, do not collect $200.")
            .moveToJail().build(),
        new Builder(COMMUNITY_CHEST, "Holiday fund matures. Receive $100.").moneyTransferWithBank(100).build(),
        new Builder(COMMUNITY_CHEST, "Income tax refund. Collect $20.").moneyTransferWithBank(20).build(),
        new Builder(COMMUNITY_CHEST, "It is your birthday. Collect $10 from every player.")
            .moneyTransferWithAllPlayers(10).build(),
        new Builder(COMMUNITY_CHEST, "Life insurance matures. Collect $100.").moneyTransferWithBank(100).build(),
        new Builder(COMMUNITY_CHEST, "Pay hospital fees of $100.").moneyTransferWithBank(-100).build(),
        new Builder(COMMUNITY_CHEST, "Pay hospital fees of $100.").moneyTransferWithBank(-100).build(),
        new Builder(COMMUNITY_CHEST, "Pay hospital fees of $100.").moneyTransferWithBank(-100).build(),
        new Builder(COMMUNITY_CHEST, "You are assessed for street repair. $40 per house. $115 per hotel.")
            .moneyPerHouse(-40).moneyPerHotel(-115).build(),
        new Builder(COMMUNITY_CHEST, "You have won second prize in a beauty contest. Collect $10.")
            .moneyTransferWithBank(10).build(),
        new Builder(COMMUNITY_CHEST, "You inherit $100.").moneyTransferWithBank(100).build(),

        new Builder(CHANCE, "Advance to Boardwalk.").moveToSpaceByName("Boardwalk").build(),
        new Builder(CHANCE, "Advance to Go (Collect $200).").moveToSpaceByName("Go").build(),
        new Builder(CHANCE, "Advance to Illinois Avenue. If you pass Go, collect $200.")
            .moveToSpaceByName("Illinois Avenue").build(),
        new Builder(CHANCE, "Advance to St. Charles Place. If you pass Go, collect $200.")
            .moveToSpaceByName("St. Charles Place").build(),
        new Builder(CHANCE,
            "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled.")
            .moveToSpaceByType(RailroadSpace.class).build(),
        new Builder(CHANCE,
            "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled.")
            .moveToSpaceByType(RailroadSpace.class).build(),
        new Builder(CHANCE,
            "Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.")
            .moveToSpaceByType(UtilitySpace.class).build(),
        new Builder(CHANCE, "Bank pays you dividend of $50.").moneyTransferWithBank(50).build(),
        new Builder(CHANCE, "Get Out of Jail Free.").getOutOfJailFreeCard().build(),
        new Builder(CHANCE, "Go Back 3 Spaces.").moveSteps(-3).build(),
        new Builder(CHANCE, "Go to Jail. Go directly to Jail, do not pass Go, do not collect $200.")
            .moveToJail().build(),
        new Builder(CHANCE,
            "Make general repairs on all your property. For each house pay $25. For each hotel pay $100.")
            .moneyPerHouse(-25).moneyPerHotel(-100).build(),
        new Builder(CHANCE, "Speeding fine $15.").moneyTransferWithBank(-15).build(),
        new Builder(CHANCE, "Take a trip to Reading Railroad. If you pass Go, collect $200.")
            .moveToSpaceByName("Reading Railroad").build(),
        new Builder(CHANCE, "You have been elected Chairman of the Board. Pay each player $50.")
            .moneyTransferWithAllPlayers(-50).build(),
        new Builder(CHANCE, "Your building loan matures. Collect $150.").moneyTransferWithBank(150).build(),
    };
  }
}
