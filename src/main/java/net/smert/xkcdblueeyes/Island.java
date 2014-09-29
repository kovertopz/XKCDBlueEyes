package net.smert.xkcdblueeyes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Island {

    private int dayCount;
    private final EyeColorGroups eyeColorGroups;
    private final Ferry ferry;
    private final Guru guru;
    private final List<Logician> logicians;

    public Island(EyeColorGroups eyeColorGroups) {
        this.eyeColorGroups = eyeColorGroups;
        ferry = new Ferry();
        // Guru is not a part of the people that can leave. Even if he was this would not change anything for him since
        // the guru doesn't know his own eye color.
        guru = new Guru(EyeColor.GREEN);
        logicians = new ArrayList<>();
        reset();
    }

    public void addLogician(Logician logician) {
        logicians.add(logician);
    }

    public int getTotalLogicians() {
        return logicians.size();
    }

    public boolean isRunning() {
        return (dayCount <= Config.MAX_DAYS);
    }

    public final void reset() {
        dayCount = 1;
        for (Logician logician : logicians) {
            logician.reset();
        }
    }

    public void simulate() {
        boolean failure = false;

        while (isRunning()) {
            System.out.println("It is now day " + dayCount + " on the island.");
            guru.hasSpoken(eyeColorGroups, logicians);

            for (Logician logician : logicians) {
                logician.thinkLogically(dayCount, eyeColorGroups, guru, logicians);
            }

            System.out.println("The time on the island is midnight and the ferry is approaching.");
            ferry.magicallyReadGurusMindForSpokenColor(guru);

            for (Logician logician : logicians) {
                logician.doILeaveTonight(ferry);
            }

            // Someone guessed wrong and Logicians are not allowed to guess wrong. This is a failure.
            {
                boolean someoneWithBadGuess = false;
                for (Logician logician : logicians) {
                    if (logician.isBadGuess()) {
                        someoneWithBadGuess = true;
                    }
                }

                if (someoneWithBadGuess) {
                    System.out.println("Someone has a bad guess at the end of the " + dayCount + " day.");
                    failure = true;
                    break;
                }
            }

            // Everybody left the island. This only happens for a single color for all Logicians.
            {
                boolean everyoneHasLeft = true;
                for (Logician logician : logicians) {
                    if (!logician.hasLeftIsland()) {
                        everyoneHasLeft = false;
                    }
                }

                if (everyoneHasLeft) {
                    System.out.println("Everyone has left the island at the end of the " + dayCount + " day.");
                    break;
                }
            }

            // Someone left the island. This is not a failure case when there is more than one color.
            {
                boolean someoneHasLeft = false;
                for (Logician logician : logicians) {
                    if (logician.hasLeftIsland()) {
                        someoneHasLeft = true;
                    }
                }

                if (someoneHasLeft) {
                    System.out.println("Someone has left the island at the end of the " + dayCount + " day.");
                    break;
                }
            }

            // If someone didn't leave the island but no longer will attempt to leave then we have a problem.
            {
                boolean atLeastOnePersonHasWillAttemptToLeave = false;
                for (Logician logician : logicians) {
                    if (logician.hasWillAttemptToLeave()) {
                        atLeastOnePersonHasWillAttemptToLeave = true;
                    }
                }

                if (!atLeastOnePersonHasWillAttemptToLeave) {
                    System.out.println("Everyone that will leave already has by the end of the " + dayCount + " day.");
                    failure = true;
                    break;
                }
            }

            dayCount++;
        }

        int totalCorrectGuesses = 0;
        for (Logician logician : logicians) {
            if (logician.getEyeColor(null) == logician.getEyeColorGuess()) {
                totalCorrectGuesses++;
            }
        }

        int totalLeftIsland = 0;
        for (Logician logician : logicians) {
            if (logician.hasLeftIsland()) {
                totalLeftIsland++;
            }
        }

        System.out.println("There is a total of " + totalCorrectGuesses
                + " correct guesses and " + totalLeftIsland
                + " left the island due to a correct guess out of " + logicians.size() + " total logicians.");

        if ((totalCorrectGuesses != totalLeftIsland) || (failure)) {
            System.exit(1);
        }
    }

}
