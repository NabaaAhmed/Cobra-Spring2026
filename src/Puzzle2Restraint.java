public class Puzzle2Restraint extends Puzzle{
private boolean mimicTriggered;
private boolean coinTaken;
private boolean chestExamined;
private boolean chestOpened;
private boolean coinPlaced;
private Item baitCoin;

public Puzzle2Restraint() {
    super("PZ-02", "Restraint", "RS-02",
            "A golden chest sits in the center with a bait coin nearby.",
            "examine chest", "Don't pick up the coin before examining the chest");

    this.mimicTriggered = false;
    this.coinTaken = false;
    this.chestExamined = false;
    this.chestOpened = false;
    this.coinPlaced = false;
    this.baitCoin = new QuestItems("I-11", "Bait Coin", "A dull coin.", "RS-02", false);
}

public boolean isMimicTriggered() { return mimicTriggered; }
public boolean isCoinTaken() { return coinTaken; }
public Item getBaitCoin() { return baitCoin; }
public void setCoinTaken(boolean taken) { this.coinTaken = taken; }

@Override
public String startPuzzle() {
    String result = "\n===== Trial of Restraint =====\n";
    result += "A golden chest sits before you.\n";
    if (!coinTaken) result += "A dull coin lies nearby.\n";
    return result;
}

@Override
public String handleCommand(Player player, String command) {
    if (command == null) return "Invalid command.";
    if (isFinished()) return "Trial already complete.";

    String cmd = command.trim().toLowerCase();

    if (cmd.equals("take coin")) {
        if (coinTaken) return "Coin already taken.";
        player.takeItem(baitCoin);
        coinTaken = true;
        return "You took the Bait Coin.";
    }

    if (cmd.equals("examine chest")) {
        chestExamined = true;
        if (!coinTaken) {
            return "The chest is normal. Nothing happens.\nType 'complete' to finish.";
        } else {
            player.takeDamage(1);
            mimicTriggered = true;
            setFinished(true);
            return "Mimic attacks! You lose 1 HP! Trial failed (No Reward).";
        }
    }

    if (cmd.equals("open chest")) {
        chestOpened = true;
        if (coinTaken) return "There's a slot for the coin. Type 'place coin'.";
        return "The chest is locked. The coin might be a key.";
    }

    if (cmd.equals("place coin")) {
        if (!coinTaken) return "You don't have the coin.";
        if (!chestOpened) return "Open the chest first.";
        if (coinPlaced) return "Coin already placed.";
        coinPlaced = true;
        return "Coin placed. Type 'complete' to finish.";
    }

    if (cmd.equals("complete")) {
        if ((!coinTaken && chestExamined && !mimicTriggered) ||
                (coinTaken && chestOpened && coinPlaced && !mimicTriggered)) {
            setSolved(true);
            setFinished(true);
            completePuzzle(player);
            return "Trial of Restraint Complete! +1 Max HP, Token, Full Heal!";
        }
        return "You haven't solved the puzzle yet.";
    }

    return "Invalid command. Try: take coin, examine chest, open chest, place coin, complete";
}
}