package net.smert.xkcdblueeyes;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        for (int i = 1; i <= Config.MAX_COLORS; i++) {
            EyeColorGroups eyeColorGroups = new EyeColorGroups(i);
            eyeColorGroups.createRandomEyeColors();
            System.out.println("Generated " + eyeColorGroups.getTotalEyeColors()
                    + " random colors for eye color groups.");

            System.out.println("Created a new island.");
            Island island = new Island(eyeColorGroups);

            for (int j = 1; j <= Config.MAX_LOGICIANS; j++) {
                System.out.println("Creating logician " + j + " and adding him to the island.");
                Logician logician = new Logician(eyeColorGroups.getRandomColor(), j);
                island.addLogician(logician);
                System.out.println("There are now a total of " + island.getTotalLogicians() + " on the island.");
                island.simulate();
                island.reset();
            }
        }

    }

}
