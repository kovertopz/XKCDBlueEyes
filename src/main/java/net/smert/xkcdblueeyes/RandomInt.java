package net.smert.xkcdblueeyes;

import java.util.Random;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RandomInt {

    private final static Random random = new Random();

    public static int next(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

}
