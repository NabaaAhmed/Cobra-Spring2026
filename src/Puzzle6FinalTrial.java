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
        message += "\nA dark presence tears itself free from the chamber's shadows.";

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
            return wrongMessage
                    + "\nYour five Trial Tokens protect you from the Stalker, but the Final Trial still cannot be completed out of order.";
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
                + "The chamber is quiet except for the low hum of an unstable teleporter.\n"
                + "A chest sits near the center of the room, surrounded by signs of old fire and damage.\n"
                + "A looming statue, a broken pillar, and a cracked floor symbol stand nearby.\n"
                + "Everything in the room feels connected. The order of your actions matters.";
    }

    @Override
    public String getHint() {
        return "Hint: Fire reveals what is hidden.\n"
                + "Do not put the flames out.\n"
                + "The statue, the pillar, the cracked symbol, and the teleporter must be handled in the correct order.";
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

                return "You step into the stabilized teleporter.\n"
                        + "The dungeon falls silent as the light carries you beyond the trials.\n"
                        + "You have completed the Final Trial and reached the Ascension Chamber!\n"
                        + "You obtain the Catalyst... You Win!";
            }

            if (cmd.equals("no")) {
                player.setCurrentRoomId("END-01");
                isSolved = true;
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;

                return "You hesitate, but the stabilized teleporter pulls you forward.\n"
                        + "The dungeon refuses to let the final path remain unfinished.\n"
                        + "You arrive in the Ascension Chamber and obtain the Catalyst... You Win!";
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (chestBurned) {
                return countWrongAction(player, "The chest is already burning.");
            }

            chestBurned = true;
            actionsSinceBurn = 1;
            return "The chest catches fire.\n"
                    + "Heat spreads through the chamber, and the unstable teleporter pulses in response.\n"
                    + "Something hidden in the room begins to shift.";
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
                return "The flames die out, and the chamber falls into a heavy silence.\n"
                        + "Your five Trial Tokens protect you from the Stalker, but the normal final path is now broken.\n"
                        + "Without the fire, the cracked floor symbol can no longer fully reveal itself.\n"
                        + "The unstable teleporter remains your only way out.";
            }

            return triggerStalker(player,
                    "The flames die out, and the chamber reacts violently.\n"
                            + "The cracked symbol fades before it can fully appear.",
                    true);
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);

            if (!player.isAlive()) {
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;
                return "You open the chest, and the chamber answers with a violent surge of energy.\n"
                        + "A trap is triggered!\n"
                        + "You lose 5 HP and 5 Max HP.\n"
                        + "You died during the Final Trial.";
            }

            if (player.getTrialTokens() >= 5) {
                return "You open the chest, and the chamber answers with a violent surge of energy.\n"
                        + "A trap is triggered!\n"
                        + "You lose 5 HP and 5 Max HP.\n"
                        + "Your five Trial Tokens prevent the Stalker from appearing, but the final path is now unstable.";
            }

            // No extra -1 ambush here because open chest already punishes heavily.
            return triggerStalker(player,
                    "You open the chest, and the chamber answers with a violent surge of energy.\n"
                            + "A trap is triggered!\n"
                            + "You lose 5 HP and 5 Max HP.",
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

            String message = "You place the explosive device into the statue.\n"
                    + "A sharp crack echoes through the chamber as the statue bursts apart.\n"
                    + "A glowing Core Fragment falls from the rubble.";

            if (crackedSymbolAppeared) {
                message += "\nThe cracked floor symbol now glows clearly beneath your feet.";
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

            String message = "You place the Core Fragment into the broken pillar.\n"
                    + "The pillar glows, sending a thin beam of light toward the teleporter.\n"
                    + "The teleporter activates, but its energy is still unstable.";

            if (crackedSymbolAppeared) {
                message += "\nThe cracked floor symbol now glows clearly beneath your feet.";
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
            return "You step onto the glowing floor symbol.\n"
                    + "The stone beneath it collapses, revealing a hidden chamber below.\n"
                    + "A Final Jewel rises from the darkness, pulsing with the same energy as the teleporter.";
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
                                "The Final Jewel strikes the teleporter, but nothing protects you from the dungeon's judgment.\n"
                                        + "With no Trial Tokens, the chamber rejects your attempt to pass.",
                                true);
                    }

                    return triggerStalker(player,
                            "Your Trial Tokens flicker weakly as the Final Jewel hits the teleporter.\n"
                                    + "For a moment, the path almost stabilizes... then the chamber growls back.",
                            true);
                }
            }

            teleporterStabilized = true;
            awaitingChoice = true;

            if (player.getTrialTokens() >= 5) {
                return "Your five Trial Tokens shine brightly, forming a circle of light around the teleporter.\n"
                        + "The Final Jewel locks into the center of the unstable energy field.\n"
                        + "The teleporter becomes calm and clear.\n"
                        + "Would you like to go through the teleporter? Yes or no";
            }

            return "The Final Jewel strikes the center of the teleporter pad.\n"
                    + "The unstable energy finally settles into a steady glow.\n"
                    + "Would you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("enter unstable teleporter")) {
            if (teleporterStabilized) {
                awaitingChoice = true;
                return "The teleporter is ready.\nWould you like to go through the teleporter? Yes or no";
            }

            player.setCurrentRoomId("TP-TRAP-01");
            isFinished = true;
            trialComplete = true;
            rewardEarned = false;

            return "You step onto the unstable teleporter before it is ready.\n"
                    + "The energy twists violently around you, ripping open a distorted path.\n"
                    + "You are thrown into the Trap Room.";
        }

        return "Invalid command.";
    }
}