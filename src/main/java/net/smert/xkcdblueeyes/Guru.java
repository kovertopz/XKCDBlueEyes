package net.smert.xkcdblueeyes;

import java.util.List;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Guru {

    private boolean hasSpoken;
    private final EyeColor eyeColor; // Eye color of the Guru. Not used in this implementation.
    private EyeColor eyeColorSpoken;

    public Guru(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
        reset();
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public EyeColor getEyeColorSpoken() {
        return eyeColorSpoken;
    }

    public void hasSpoken(EyeColorGroups eyeColorGroups, List<Logician> logicians) {
        if (!hasSpoken) {
            while (true) {
                boolean foundLogicianWithEyeColor = false;
                eyeColorSpoken = eyeColorGroups.getRandomColor();
                // Make sure the random color we picked is actually one that a logician has
                for (Logician logician : logicians) {
                    if (!logician.hasLeftIsland()) {
                        EyeColor logicianEyeColor = logician.getEyeColor(null);
                        if (logicianEyeColor == eyeColorSpoken) {
                            foundLogicianWithEyeColor = true;
                        }
                    }
                }
                // If we guessed a color they didnt have then we try again
                if (foundLogicianWithEyeColor) {
                    break;
                }
            }
            System.out.println("The time on the island is noon the Guru says that he sees at least one person with "
                    + eyeColorSpoken + " eye color.");
            hasSpoken = true;
        }
    }

    public final void reset() {
        hasSpoken = false;
        eyeColorSpoken = null;
    }

}
