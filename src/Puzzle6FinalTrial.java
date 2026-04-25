//nabaa
import java.util.Random;

public class Puzzle6FinalTrial extends Puzzle {
    private boolean chestBurned;
    private boolean fireExtinguished;
    private int actionsSinceBurn;
    private boolean crackedSymbolAppeared;
    private boolean statueBroken;
    private boolean corePlaced;
    private boolean finalJewelAppeared;
    private boolean teleporterStabilized;
    private boolean awaitingChoice;
    private boolean stalkerDefeated;
    private boolean stalkerPathRequired;

    private boolean combatTriggered;
    private Monster failureMonster;

    private boolean bossChanceRolled;
    private int wrongActionCount;
    private final Random random;

    public Puzzle6FinalTrial() {
        super("PZ-06", "Final Trial", "FN-02");
        this.chestBurned = false;
        this.fireExtinguished = false;
        this.actionsSinceBurn = 0;
        this.crackedSymbolAppeared = false;
        this.statueBroken = false;
        this.corePlaced = false;
        this.finalJewelAppeared = false;
        this.teleporterStabilized = false;
        this.awaitingChoice = false;
        this.stalkerDefeated = false;
        this.stalkerPathRequired = false;
        this.combatTriggered = false;
        this.failureMonster = null;

        this.bossChanceRolled = false;
        this.wrongActionCount = 0;
        this.random = new Random();
    }

    public boolean isCombatTriggered() {
        return combatTriggered;
    }

    public Monster getFailureMonster() {
        return failureMonster;
    }

    public void clearCombatTrigger() {
        combatTriggered = false;
        failureMonster = null;
    }

    public void onStalkerDefeated() {
        stalkerDefeated = true;
        teleporterStabilized = true;
        awaitingChoice = true;
        clearCombatTrigger();
    }

    private void advanceBurnCounter() {
        if (chestBurned && !fireExtinguished && !crackedSymbolAppeared) {
            actionsSinceBurn++;
            if (actionsSinceBurn >= 3) {
                crackedSymbolAppeared = true;
            }
        }
    }

    private boolean shouldBossAppearByTokenChance(Player player) {
        int tokens = player.getTrialTokens();

        if (tokens <= 0) {
            return true;
        }

        if (tokens >= 5) {
            return false;
        }

        // 1-4 tokens: 50% chance boss appears.
        return random.nextBoolean();
    }

    private String triggerStalker(Player player, String reasonMessage, boolean ambushDamage) {
        stalkerPathRequired = true;
        failureMonster = new Monster("M-09", "Final Trial Stalker", 6, 1);
        combatTriggered = true;

        String message = reasonMessage;

        if (ambushDamage) {
            player.takeDamage(1);
            message += "\nThe Stalker ambushes you and you lose 1 HP.";
        }

        message += "\nThe Stalker appears!";
        return message;
    }

    private String countWrongAction(Player player, String wrongMessage) {
        int tokens = player.getTrialTokens();

        // 5 tokens means no Stalker from wrong-action rule.
        if (tokens >= 5) {
            return wrongMessage;
        }

        wrongActionCount++;

        if (tokens >= 1 && tokens <= 4 && wrongActionCount >= 2 && !stalkerDefeated) {
            return triggerStalker(player,
                    wrongMessage + "\nYou have made too many mistakes in the Final Trial.",
                    true);
        }

        if (tokens <= 0 && !stalkerDefeated) {
            return triggerStalker(player,
                    wrongMessage + "\nYou have no Trial Tokens to protect you.",
                    true);
        }

        return wrongMessage + "\nMistakes made in this trial: " + wrongActionCount + "/2";
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Final Trial =====\n"
                + "Everything you have learned will now be tested.\n"
                + "A chest, a looming statue, a cracked floor symbol, and an unstable teleporter pad stand before you.";
    }

    @Override
    public String getHint() {
        return "Hint: Do not extinguish the fire.\n"
                + "Timing and order matter.\n"
                + "Some actions cannot be undone.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("enter") || cmd.equals("enter teleporter")) {
                player.setCurrentRoomId("END-01");
                isSolved = true;
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;

                return "You have completed the Final Trial and reached the end room!\n"
                        + "You obtain the Catalyst... You Win!";
            }

            if (cmd.equals("no")) {
                player.setCurrentRoomId("END-01");
                isSolved = true;
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;

                return "You must enter the teleporter to complete your journey.\n"
                        + "You obtain the Catalyst... You Win!";
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (chestBurned) {
                return countWrongAction(player, "The chest is already burning.");
            }

            chestBurned = true;
            actionsSinceBurn = 1;
            return "The chest catches fire.\nThe room grows hotter. Keep moving in the correct order.";
        }

        if (cmd.equals("extinguish fire")) {
            if (!chestBurned) {
                return countWrongAction(player, "There is no fire to extinguish yet.");
            }

            if (fireExtinguished) {
                return countWrongAction(player, "The fire is already out.");
            }

            fireExtinguished = true;

            if (player.getTrialTokens() >= 5) {
                return "The fire goes out.\nThe trial can no longer proceed normally, but no monster appears because you have all 5 Trial Tokens.";
            }

            return triggerStalker(player,
                    "The trial can no longer proceed normally.",
                    true);
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);

            if (!player.isAlive()) {
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;
                return "A trap is triggered!\nYou lose 5 HP and 5 Max HP.\nYou died during the Final Trial.";
            }

            if (player.getTrialTokens() >= 5) {
                return "A trap is triggered!\n"
                        + "You lose 5 HP and 5 Max HP.\n"
                        + "No monster appears because you have all 5 Trial Tokens.";
            }

            // No extra -1 ambush here because open chest already punishes heavily.
            return triggerStalker(player,
                    "A trap is triggered!\nYou lose 5 HP and 5 Max HP.",
                    false);
        }

        if (cmd.equals("insert explosive device")) {
            if (!chestBurned || fireExtinguished) {
                return countWrongAction(player,
                        "The explosive device cannot be used correctly right now.");
            }

            if (statueBroken) {
                return countWrongAction(player, "The statue has already been shattered.");
            }

            advanceBurnCounter();
            statueBroken = true;

            String message = "The statue shatters.\nCore Fragment drops.";
            if (crackedSymbolAppeared) {
                message += "\nA cracked floor symbol is now visible.";
            }

            return message;
        }

        if (cmd.equals("place core fragment")) {
            if (!statueBroken) {
                return countWrongAction(player, "You do not have the Core Fragment yet.");
            }

            if (corePlaced) {
                return countWrongAction(player, "The Core Fragment is already placed.");
            }

            advanceBurnCounter();
            corePlaced = true;

            String message = "The teleporter activates, but it remains unstable.";
            if (crackedSymbolAppeared) {
                message += "\nA cracked floor symbol is now visible.";
            }

            return message;
        }

        if (cmd.equals("step symbol")) {
            if (!crackedSymbolAppeared || !corePlaced) {
                return countWrongAction(player,
                        "The cracked floor symbol is not ready yet.");
            }

            if (finalJewelAppeared) {
                return countWrongAction(player, "The floor has already collapsed.");
            }

            finalJewelAppeared = true;
            return "The floor collapses.\nFinal Jewel appears.";
        }

        if (cmd.equals("throw final jewel")) {
            if (!finalJewelAppeared) {
                return countWrongAction(player,
                        "The teleporter destabilizes violently because the Final Jewel has not appeared yet.");
            }

            if (stalkerPathRequired && !stalkerDefeated) {
                return "The teleporter still rejects the jewel. The Stalker must be dealt with first.";
            }

            if (teleporterStabilized) {
                return countWrongAction(player, "The teleporter is already stabilized.");
            }

            if (!bossChanceRolled) {
                bossChanceRolled = true;

                if (shouldBossAppearByTokenChance(player) && !stalkerDefeated) {
                    if (player.getTrialTokens() <= 0) {
                        return triggerStalker(player,
                                "You have no Trial Tokens.\nThe teleporter rejects your progress.",
                                true);
                    }

                    return triggerStalker(player,
                            "Your Trial Tokens flicker uncertainly.\nThe teleporter resists your progress.",
                            true);
                }
            }

            teleporterStabilized = true;
            awaitingChoice = true;

            if (player.getTrialTokens() >= 5) {
                return "Your five Trial Tokens shine brightly.\n"
                        + "The teleporter stabilizes without resistance.\n"
                        + "Would you like to go through the teleporter? Yes or no";
            }

            return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("enter unstable teleporter")) {
            if (teleporterStabilized) {
                awaitingChoice = true;
                return "The teleporter is ready. Would you like to go through the teleporter? Yes or no";
            }

            player.setCurrentRoomId("TP-TRAP-01");
            isFinished = true;
            trialComplete = true;
            rewardEarned = false;

            return "You are pulled into a distorted space...\nYou are sent to the Trap Room.";
        }

        return countWrongAction(player, "Invalid command.");
    }
}