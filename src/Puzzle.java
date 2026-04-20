import java.util.HashMap;
import java.util.Map;

public class Puzzle
{
    public static final String VARIANT_A = "A";
    public static final String VARIANT_B = "B";

    private String puzzleID;
    private String trialName;
    private String variant;
    private String description;
    private String hint;

    private int currentState;
    private int finalState;

    private boolean solved;
    private boolean failed;

    private String successRoomID;
    private String failureRoomID;

    private Map<Integer, String> expectedActions;

    public Puzzle(String puzzleID, String trialName, String variant,
                  String description, String hint,
                  int finalState,
                  String successRoomID, String failureRoomID)
    {
        if (puzzleID == null || puzzleID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Puzzle ID cannot be null or empty.");
        }

        if (trialName == null || trialName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Trial name cannot be null or empty.");
        }

        if (finalState < 1)
        {
            throw new IllegalArgumentException("Final state must be at least 1.");
        }

        if (successRoomID == null || successRoomID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Success room ID cannot be null or empty.");
        }

        if (failureRoomID == null || failureRoomID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Failure room ID cannot be null or empty.");
        }

        this.puzzleID = puzzleID.trim();
        this.trialName = trialName.trim();
        this.variant = normalizeVariant(variant);
        this.description = description == null ? "" : description.trim();
        this.hint = hint == null ? "" : hint.trim();
        this.currentState = 0;
        this.finalState = finalState;
        this.solved = false;
        this.failed = false;
        this.successRoomID = successRoomID.trim();
        this.failureRoomID = failureRoomID.trim();
        this.expectedActions = new HashMap<>();
    }

    public String getPuzzleID()
    {
        return puzzleID;
    }

    public void setPuzzleID(String puzzleID)
    {
        if (puzzleID == null || puzzleID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Puzzle ID cannot be null or empty.");
        }

        this.puzzleID = puzzleID.trim();
    }

    public String getTrialName()
    {
        return trialName;
    }

    public void setTrialName(String trialName)
    {
        if (trialName == null || trialName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Trial name cannot be null or empty.");
        }

        this.trialName = trialName.trim();
    }

    public String getVariant()
    {
        return variant;
    }

    public void setVariant(String variant)
    {
        this.variant = normalizeVariant(variant);
    }

    public boolean isVariantA()
    {
        return VARIANT_A.equals(variant);
    }

    public boolean isVariantB()
    {
        return VARIANT_B.equals(variant);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description == null ? "" : description.trim();
    }

    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint == null ? "" : hint.trim();
    }

    public int getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(int currentState)
    {
        if (currentState < 0)
        {
            throw new IllegalArgumentException("Current state cannot be negative.");
        }

        this.currentState = currentState;

        if (this.currentState >= finalState)
        {
            solved = true;
            failed = false;
        }
    }

    public int getFinalState()
    {
        return finalState;
    }

    public void setFinalState(int finalState)
    {
        if (finalState < 1)
        {
            throw new IllegalArgumentException("Final state must be at least 1.");
        }

        this.finalState = finalState;
    }

    public boolean isSolved()
    {
        return solved;
    }

    public void setSolved(boolean solved)
    {
        this.solved = solved;

        if (solved)
        {
            failed = false;
            currentState = finalState;
        }
    }

    public boolean isFailed()
    {
        return failed;
    }

    public void setFailed(boolean failed)
    {
        this.failed = failed;

        if (failed)
        {
            solved = false;
        }
    }

    public String getSuccessRoomID()
    {
        return successRoomID;
    }

    public void setSuccessRoomID(String successRoomID)
    {
        if (successRoomID == null || successRoomID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Success room ID cannot be null or empty.");
        }

        this.successRoomID = successRoomID.trim();
    }

    public String getFailureRoomID()
    {
        return failureRoomID;
    }

    public void setFailureRoomID(String failureRoomID)
    {
        if (failureRoomID == null || failureRoomID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Failure room ID cannot be null or empty.");
        }

        this.failureRoomID = failureRoomID.trim();
    }

    public void setExpectedAction(int stateNumber, String action)
    {
        if (stateNumber < 1)
        {
            throw new IllegalArgumentException("State number must be at least 1.");
        }

        if (action == null || action.trim().isEmpty())
        {
            throw new IllegalArgumentException("Expected action cannot be null or empty.");
        }

        expectedActions.put(stateNumber, normalizeAction(action));
    }

    public String getExpectedAction(int stateNumber)
    {
        return expectedActions.get(stateNumber);
    }

    public Map<Integer, String> getExpectedActions()
    {
        return new HashMap<>(expectedActions);
    }

    public boolean processAction(String action)
    {
        if (solved || failed)
        {
            return false;
        }

        String normalizedAction = normalizeAction(action);
        int nextState = currentState + 1;
        String expectedAction = expectedActions.get(nextState);

        if (expectedAction == null)
        {
            failPuzzle();
            return false;
        }

        if (expectedAction.equals(normalizedAction))
        {
            currentState = nextState;

            if (currentState >= finalState)
            {
                solved = true;
                failed = false;
            }

            return true;
        }

        failPuzzle();
        return false;
    }

    public void advanceState()
    {
        if (!solved && !failed)
        {
            currentState++;

            if (currentState >= finalState)
            {
                solved = true;
                failed = false;
            }
        }
    }

    public void failPuzzle()
    {
        if (!solved)
        {
            failed = true;
        }
    }

    public void resetPuzzle()
    {
        currentState = 0;
        solved = false;
        failed = false;
    }

    public boolean shouldTeleportOnSuccess()
    {
        return solved;
    }

    public boolean shouldTeleportOnFailure()
    {
        return failed;
    }

    private String normalizeAction(String action)
    {
        if (action == null)
        {
            return "";
        }

        return action.trim().toUpperCase();
    }

    private String normalizeVariant(String variant)
    {
        if (variant == null || variant.trim().isEmpty())
        {
            return VARIANT_A;
        }

        String normalized = variant.trim().toUpperCase();

        if (!normalized.equals(VARIANT_A) && !normalized.equals(VARIANT_B))
        {
            throw new IllegalArgumentException("Variant must be A or B.");
        }

        return normalized;
    }

    @Override
    public String toString()
    {
        return puzzleID + " - " + trialName + " (Variant " + variant + ")";
    }

    public String getRoomID() {
        return successRoomID;  }
}
