package net.smert.xkcdblueeyes;

import java.util.List;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Logician {

    private boolean badGuess; // We went to the ferry and we were wrong
    private boolean hasLeftIsland; // We went to the ferry and we were right and got to leave the island
    private boolean willAttemptToLeave; // On the first day I make a prediction and this means my prediction is good
    private int daysToWait; // Days to wait until I can leave. When this is zero I leave that night.
    private int tomorrowsDayCount; // The day count of what I think it should be tomorrow
    private int totalOtherLogiciansOnIslandWithSpokenColorSoICanLeave; // This guess must remain true
    private final int logicianID; // Unique ID
    private final EyeColor eyeColor; // They must figure this out
    private EyeColor eyeColorGuess; // Current guess

    public Logician(EyeColor eyeColor, int logicianID) {
        this.logicianID = logicianID;
        this.eyeColor = eyeColor;
        reset();
    }

    public void doILeaveTonight(Ferry ferry) {
        if ((daysToWait == 0) && (willAttemptToLeave)) {
            System.out.println("... It is my time to leave this place.");
            if (eyeColorGuess == EyeColor.UNKNOWN) {
                System.out.println("... I should have left but I realized my guess is " + eyeColorGuess + ".");
                badGuess = true; // Bad guess because this is an illegal state.
                willAttemptToLeave = false; // Deactivate logician
            } else {
                ferry.logicianWantsToLeave(this);
            }
        } else if (willAttemptToLeave) {
            System.out.println("... I still need to wait " + daysToWait + " day before I can leave.");
            daysToWait--;
        } else {
            if (!hasLeftIsland) {
                System.out.println("... I can never leave.");
            }
        }
    }

    public boolean hasLeftIsland() {
        return hasLeftIsland;
    }

    public boolean hasWillAttemptToLeave() {
        return willAttemptToLeave;
    }

    public boolean isBadGuess() {
        return badGuess;
    }

    public void setBadGuess(boolean badGuess) {
        this.badGuess = badGuess;
    }

    public void setHasLeftIsland(boolean hasLeftIsland) {
        this.hasLeftIsland = hasLeftIsland;
    }

    public void setWillAttemptToLeave(boolean willAttemptToLeave) {
        this.willAttemptToLeave = willAttemptToLeave;
    }

    public int getLogicianID() {
        return logicianID;
    }

    public EyeColor getEyeColor(Logician logician) {
        if (this != logician) {
            return eyeColor;
        }
        return null;
    }

    public EyeColor getEyeColorGuess() {
        return eyeColorGuess;
    }

    public void reset() {
        badGuess = false;
        hasLeftIsland = false;
        willAttemptToLeave = false;
        daysToWait = -1;
        eyeColorGuess = EyeColor.UNKNOWN;
    }

    private int[] thinkEyeColorsICanSee(EyeColorGroups eyeColorGroups, List<Logician> logicians) {
        int[] eyeColorsICanSee = new int[Config.MAX_COLORS];
        for (int i = 0; i < eyeColorsICanSee.length; i++) {
            eyeColorsICanSee[i] = 0;
        }
        for (Logician logician : logicians) {
            // Logician can't read his own color. Only count logicians that haven't left
            if (!logician.hasLeftIsland()) {
                EyeColor logicianEyeColor = logician.getEyeColor(this);
                // Is null when we read our own color
                if (logicianEyeColor != null) {
                    eyeColorsICanSee[logicianEyeColor.ordinal()]++;
                }
            }
        }
        for (int i = 0; i < eyeColorGroups.getEyeColors().size(); i++) {
            EyeColor randomEyeColor = eyeColorGroups.getEyeColors().get(i);
            int totalNumberWithColor = eyeColorsICanSee[randomEyeColor.ordinal()];
            if (totalNumberWithColor > 0) {
                System.out.println("... I see a total of " + totalNumberWithColor
                        + " with the eye color " + randomEyeColor + " on the island.");
            }
        }
        return eyeColorsICanSee;
    }

    private EyeColor thinkEyeColorSpoken(Guru guru) {
        EyeColor eyeColorSpoken = guru.getEyeColorSpoken();
        System.out.println("... The guru said the eye color " + eyeColorSpoken);
        return eyeColorSpoken;
    }

    private int thinkTotalLogiciansOnIsland(List<Logician> logicians) {
        int totalLogiciansOnIsland = 0;
        for (Logician logician : logicians) {
            // Count logicians that are still on the island
            if (!logician.hasLeftIsland()) {
                totalLogiciansOnIsland++;
            }
        }
        System.out.println("... There are a total of " + totalLogiciansOnIsland + " on the island.");
        return totalLogiciansOnIsland;
    }

    private int thinkTotalLogiciansOnIslandWithEyeColorsNotSpoken(
            int[] eyeColorsICanSee, EyeColor eyeColorSpoken) {
        int totalLogiciansOnIslandWithEyeColorsNotSpoken = 0;
        for (int i = 0; i < eyeColorsICanSee.length; i++) {
            int totalEyeColorCount = eyeColorsICanSee[i];
            if ((totalEyeColorCount > 0) && (eyeColorSpoken.ordinal() != i)) {
                totalLogiciansOnIslandWithEyeColorsNotSpoken += totalEyeColorCount;
            }
        }
        System.out.println("... I see a total of " + totalLogiciansOnIslandWithEyeColorsNotSpoken
                + " logicians with other eye colors than the one the guru spoke of on the island.");
        return totalLogiciansOnIslandWithEyeColorsNotSpoken;
    }

    private int thinkTotalOtherEyeColorsICanSeeNotSpoken(int[] eyeColorsICanSee, EyeColor eyeColorSpoken) {
        int totalOtherEyeColorsICanSeeNotSpoken = 0;
        for (int i = 0; i < eyeColorsICanSee.length; i++) {
            if ((eyeColorsICanSee[i] > 0) && (eyeColorSpoken.ordinal() != i)) {
                totalOtherEyeColorsICanSeeNotSpoken++;
            }
        }
        System.out.println("... I see a total of " + totalOtherEyeColorsICanSeeNotSpoken
                + " other eye colors not including the spoken color on the island.");
        return totalOtherEyeColorsICanSeeNotSpoken;
    }

    private int thinkTotalOtherLogiciansOnIsland(List<Logician> logicians) {
        int totalOtherLogiciansOnIsland = 0;
        for (Logician logician : logicians) {
            // Count logicians that haven't left yet
            if (!logician.hasLeftIsland()) {
                totalOtherLogiciansOnIsland++;
            }
        }
        totalOtherLogiciansOnIsland -= 1;
        System.out.println("... I see a total of " + totalOtherLogiciansOnIsland + " other logicians on the island.");
        return totalOtherLogiciansOnIsland;
    }

    private int thinkTotalOtherLogiciansOnIslandWithSpokenColor(int[] eyeColorsICanSee, EyeColor eyeColorSpoken) {
        int totalOtherLogiciansOnIslandWithSpokenColor = eyeColorsICanSee[eyeColorSpoken.ordinal()];
        System.out.println("... I see a total of " + totalOtherLogiciansOnIslandWithSpokenColor
                + " with the spoken eye color " + eyeColorSpoken + " and don't know my own color.");
        return totalOtherLogiciansOnIslandWithSpokenColor;
    }

    public void thinkLogically(int dayCount, EyeColorGroups eyeColorGroups, Guru guru, List<Logician> logicians) {
        System.out.println("... Today is day " + dayCount + " on the island.");

        if ((!hasLeftIsland) && (daysToWait == -1)) {
            EyeColor eyeColorSpoken = thinkEyeColorSpoken(guru);
            int totalLogiciansOnIsland = thinkTotalLogiciansOnIsland(logicians);
            int totalOtherLogiciansOnIsland = thinkTotalOtherLogiciansOnIsland(logicians);
            int[] eyeColorsICanSee = thinkEyeColorsICanSee(eyeColorGroups, logicians);
            int totalOtherLogiciansOnIslandWithSpokenColor
                    = thinkTotalOtherLogiciansOnIslandWithSpokenColor(eyeColorsICanSee, eyeColorSpoken);
            int totalLogiciansOnIslandWithEyeColorsNotSpoken
                    = thinkTotalLogiciansOnIslandWithEyeColorsNotSpoken(eyeColorsICanSee, eyeColorSpoken);
            int totalOtherEyeColorsICanSeeNotSpoken = thinkTotalOtherEyeColorsICanSeeNotSpoken(eyeColorsICanSee, eyeColorSpoken);
            int prediction = (totalLogiciansOnIsland - totalLogiciansOnIslandWithEyeColorsNotSpoken - 1);

            daysToWait = prediction; // Days I have to wait before I can validate my prediction
            eyeColorGuess = eyeColorSpoken; // Would always guess the guru's spoken color
            tomorrowsDayCount = dayCount + 1;
            // Total logicians I expect to be there during the day I leave
            totalOtherLogiciansOnIslandWithSpokenColorSoICanLeave = prediction;
            willAttemptToLeave = true; // Makes me active

            if ((totalLogiciansOnIsland >= 1) && (prediction == 0)) {
                System.out.println("... Since there are no others on the island and the guru "
                        + "mentioned an eye color of " + eyeColorSpoken
                        + " then eye color " + eyeColorGuess + " must belong to me.");
            } else if ((totalLogiciansOnIsland >= 1) && (prediction == 1)) {
                System.out.println("... Since there is only one other person with the spoken eye"
                        + " color it can't be just me. If the other\n    person is gone in " + daysToWait
                        + " days then I know I don't have the spoken eye color and can't leave.");
            } else if ((totalLogiciansOnIsland >= 1) && (prediction > 1)) {
                System.out.println("... Since there are others with the spoken eye"
                        + " color it can't be just me. If the other's haven't left in " + daysToWait
                        + " days then I know I  don't have the spoken eye color and can't leave.");
            }

        } else if ((!hasLeftIsland) && (willAttemptToLeave)) {
            if (dayCount == tomorrowsDayCount) {
                System.out.println("... It is now tomorrow.");

                EyeColor eyeColorSpoken = thinkEyeColorSpoken(guru);
                int[] eyeColorsICanSee = thinkEyeColorsICanSee(eyeColorGroups, logicians);
                int totalOtherLogiciansOnIslandWithSpokenColor
                        = thinkTotalOtherLogiciansOnIslandWithSpokenColor(eyeColorsICanSee, eyeColorSpoken);

                // Make sure my prediction is still valid
                if (totalOtherLogiciansOnIslandWithSpokenColorSoICanLeave
                        == totalOtherLogiciansOnIslandWithSpokenColor) {
                    System.out.println("... Since that other with the spoken eye color hasn't left"
                            + " then this must mean I have the spoken eye color too.");
                } else {
                    System.out.println("... Since my prediction failed I cannot leave"
                            + " then this must mean I DON'T have the spoken eye color too.");
                    willAttemptToLeave = false;
                }

                tomorrowsDayCount = dayCount + 1;
            } else {
                System.out.println("... ERROR: I haven't left yet and will attempt to leave but my tomorrow day count prediction failed.");
                throw new RuntimeException("Something went wrong.");
            }
        }

        if (willAttemptToLeave) {
            System.out.println("... I am done thinking. I will wait " + daysToWait + " days before I leave.");
        } else {
            if (!hasLeftIsland) {
                System.out.println("... I am done thinking.");
            } else {
                System.out.println("... I have already left the island.");
            }
        }
    }

}
