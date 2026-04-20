import java.util.HashMap;
import java.util.Map;

public class Puzzle
{
    private String puzzleID;
    private String trialName;
    private String description;
    private String hint;

    private int currentState;
    private int finalState;

    private boolean solved;
    private boolean failed;

    private String successRoomID;
    private String failureRoomID;

    private Map<Integer, String> expectedActions;

    public Puzzle(String puzzleID, String trialName,
                  String description, String hint,
                  int finalState,
                  String successRoomID, String failureRoomID)
    {
        if (puzzleID == null || puzzleID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Puzzle ID cannot be empty.");
        }

        if (trialName == null || trialName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Trial name cannot be empty.");
        }

        if (finalState < 1)
        {
            throw new IllegalArgumentException("Final state must be at least 1.");
        }

        this.puzzleID = puzzleID;
        this.trialName = trialName;
        this.description = description == null ? "" : description;
        this.hint = hint == null ? "" : hint;

        this.currentState = 0;
        this.finalState = finalState;

        this.solved = false;
        this.failed = false;

        this.successRoomID = successRoomID;
        this.failureRoomID = failureRoomID;

        this.expectedActions = new HashMap<>();
    }

    public String getPuzzleID()
    {
        return puzzleID;
    }

    public String getTrialName()
    {
        return trialName;
    }

    public String getDescription()
    {
        return description;
    }

    public String getHint()
    {
        return hint;
    }

    public int getCurrentState()
    {
        return currentState;
    }

    public boolean isSolved()
    {
        return solved;
    }

    public boolean isFailed()
    {
        return failed;
    }

    public String getSuccessRoomID()
    {
        return successRoomID;
    }

    public String getFailureRoomID()
    {
        return failureRoomID;
    }

    public void setExpectedAction(int state, String action)
    {
        if (state < 1)
        {
            throw new IllegalArgumentException("State must be >= 1");
        }

        if (action == null || action.trim().isEmpty())
        {
            throw new IllegalArgumentException("Action cannot be empty");
        }

        expectedActions.put(state, normalize(action));
    }

    public boolean processAction(String action)
    {
        if (solved || failed)
        {
            return false;
        }

        String normalized = normalize(action);
        int nextState = currentState + 1;

        String expected = expectedActions.get(nextState);

        if (expected == null)
        {
            failPuzzle();
            return false;
        }

        if (expected.equals(normalized))
        {
            currentState = nextState;

            if (currentState >= finalState)
            {
                solved = true;
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

    private String normalize(String input)
    {
        return input.trim().toUpperCase();
    }
}
