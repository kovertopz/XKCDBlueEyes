package net.smert.xkcdblueeyes;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Ferry {

    private EyeColor eyeColorSpoken;

    public void magicallyReadGurusMindForSpokenColor(Guru guru) {
        eyeColorSpoken = guru.getEyeColorSpoken();
    }

    public void logicianWantsToLeave(Logician logician) {
        System.out.println("So you think you are leaving buddy? What is your eye color?");
        System.out.println("Logician " + logician.getLogicianID()
                + " says: I know that my eye color is " + logician.getEyeColorGuess());

        if (logician.getEyeColorGuess() == eyeColorSpoken) {
            System.out.println("Well at least you know that the spoken eye color is " + eyeColorSpoken);

            EyeColor logicianEyeColor = logician.getEyeColor(null);
            if (eyeColorSpoken == logicianEyeColor) {
                System.out.println("Congratulations Logician " + logician.getLogicianID()
                        + " you know your eye color of " + logicianEyeColor + " and can leave.");
                logician.setHasLeftIsland(true);
                logician.setWillAttemptToLeave(false);
            } else {
                System.out.println("You are not leaving the island Logician " + logician.getLogicianID()
                        + " because your eye color is " + logicianEyeColor);
                logician.setBadGuess(true);
                logician.setWillAttemptToLeave(false);
            }
        } else {
            System.out.println("WHOA! WHOA! Logician " + logician.getLogicianID()
                    + " you don't even know that the eye color is " + eyeColorSpoken);
            logician.setBadGuess(true);
            logician.setWillAttemptToLeave(false);
        }
    }

}
