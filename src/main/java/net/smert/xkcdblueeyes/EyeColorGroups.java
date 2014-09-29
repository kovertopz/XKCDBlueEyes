package net.smert.xkcdblueeyes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class EyeColorGroups {

    private final int maxNumberOfGroups;
    private final List<EyeColor> eyeColors;
    private final Map<Integer, EyeColor> valuesToEyeColors;

    public EyeColorGroups(int maxNumberOfGroups) {

        // There can be no more than the total number of eye colors
        if (maxNumberOfGroups > Config.MAX_COLORS) {
            maxNumberOfGroups = Config.MAX_COLORS;
        }

        this.maxNumberOfGroups = maxNumberOfGroups;
        valuesToEyeColors = new HashMap<>();
        eyeColors = new ArrayList<>();
        mapValuesToEyeColors();
    }

    private void mapValuesToEyeColors() {
        for (EyeColor type : EyeColor.values()) {
            valuesToEyeColors.put(type.ordinal(), type);
        }
    }

    public void createRandomEyeColors() {

        // Cretea a set with random colors so each color can only be added once
        Set<EyeColor> eyeColorsSet = new HashSet<>();
        while (eyeColorsSet.size() < maxNumberOfGroups) {
            int randomOrdinal = RandomInt.next(0, EyeColor.MAX_EYE_COLOR.ordinal() - 1);
            EyeColor eyeColor = valuesToEyeColors.get(randomOrdinal);
            if (eyeColor == null) {
                throw new RuntimeException("Unknown ordinal for EyeColor: " + randomOrdinal);
            }
            eyeColorsSet.add(eyeColor); // This might not add a color each time since we only add unique values
        }

        // Add unique set of colors to the list
        Iterator<EyeColor> iterator = eyeColorsSet.iterator();
        while (iterator.hasNext()) {
            EyeColor eyeColor = iterator.next();
            eyeColors.add(eyeColor); // If we didnt use a set we would have duplicates
        }
    }

    public List<EyeColor> getEyeColors() {
        return eyeColors;
    }

    public EyeColor getRandomColor() {
        assert (eyeColors.size() > 0);
        int randomIndex = RandomInt.next(0, eyeColors.size() - 1);
        return eyeColors.get(randomIndex);
    }

    public int getTotalEyeColors() {
        return eyeColors.size();
    }

}
