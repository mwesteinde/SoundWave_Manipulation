package ca.ubc.ece.cpen221.mp1;

import ca.ubc.ece.cpen221.mp1.utils.WaveGen;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Task2 {

    private final static double epsilon = 1E-6; // Accuracy: TODO: Change up/down?

    private static Stream<Arguments> createSameWaveProvider() {
        return Stream.of(
                Arguments.of(
                        new double[]{1d, -1d},
                        new double[]{-100d, 100d}
                )
        );
    }

    private static boolean sameWave(SoundWave sw0, SoundWave sw1) {
        var sw0r = sw0.getRightChannel();
        var sw0l = sw0.getLeftChannel();
        var sw1r = sw1.getRightChannel();
        var sw1l = sw1.getLeftChannel();

        if ((sw0r.length != sw1r.length) ||
                (sw0l.length != sw1l.length) ||
                (sw0r.length != sw0l.length)) {
            return false;
        }

        for (int i = 0; i < sw0l.length; i++) {
            if (((Math.abs(sw0r[i] - sw1r[i])) >= epsilon) ||
                    (Math.abs(sw0l[i] - sw1l[i])) >= epsilon) {
                return false;
            }
        }
        return true;
    }

    /**
     * Argument provider
     * @return Original, and contained waves as Map.Entry<List<List<double[]>>,Boolean>
     */
    private static Stream<Arguments> createContainsProvider() {
        var outer0 = List.of(new double[]{0.1, 0.5, 0.1}, new double[]{0.3, 0.6, 0.0});
        var inner0  = List.of(new double[]{0.1, 0.5}, new double[]{0.3, 0.6});

        // Doesn't contain
        var outer1 = List.of(
                new double[]{0.5, -0.1, 0.4, 0.6, 0.4, -0.2},
                new double[]{0.6, -0.1, 0.4, 0.1, -0.4, 0.9});
        var inner1  = List.of(new double[]{0.5, -0.1, 0.4}, new double[]{0.9, 0.1, 0.6});

        var outer2 = List.of(
                new double[]{0.1, -0.3, 1, 0.7, -0.6, 0.4, -0.6, 0.4, -0.3},
                new double[]{0.1, 0.4, -0.1, 0.3, -0.6, -0.4, -0.5, 0.3, -0.2});
        var inner2 = List.of(new double[]{-1.0, 0.5, 0.2}, new double[]{-0.5, 0.3, -0.2});

        // Inner too long
        var outer3 = List.of(new double[]{-1.0, 1.0, 0.5}, new double[]{-0.5, 0.2, 0.8});
        var inner3 = List.of(new double[]{-1.0, 1.0, 0.5, 0.4}, new double[]{-0.5, 0.2, 0.8, -0.3});
        return Stream.of(
                Arguments.of(
                        new AbstractMap.SimpleEntry<>(List.of(outer0, inner0), true),
                        new AbstractMap.SimpleEntry<>(List.of(outer1, inner1), false),
                        new AbstractMap.SimpleEntry<>(List.of(outer2, inner2), false),
                        new AbstractMap.SimpleEntry<>(List.of(outer3, inner3), false)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createContainsProvider")
    void testContains(Map.Entry<List<List<double[]>>,Boolean> entries) {
        var w0 = entries.getKey().get(0);
        var w1 = entries.getKey().get(1);
        var sw = new SoundWave(w0.get(0), w0.get(1));
        var cw = new SoundWave(w1.get(0), w1.get(1));
        assertEquals(sw.contains(cw), entries.getValue());
    }

    /**
     * Argument provider
     * @return Original, and contained waves as Map.Entry<List<double[]>,List<double[]>>
     */
    private static Stream<Arguments> createSimilarityProvider() {
        var sw0 = new SoundWave(
                new double[]{-1.0, 1.0, 0.5, 0.4}, new double[]{-0.5, 0.2, 0.8, -0.3});
        var swSimilar0 = new SoundWave(new double[]{-1.0, 0.5, 0.2}, new double[]{0.2, 0.3, -0.9});

        var sw1 = new SoundWave(
                new double[]{-1.0, 1.0, 0.5, 0.4}, new double[]{-0.5, 0.2, 0.8, -0.3});
        var swSimilar1 = new SoundWave(new double[]{0, 0, 0}, new double[]{0, 0, 0});
        return Stream.of(
                Arguments.of(
                        new AbstractMap.SimpleEntry<>(
                                List.of(new SoundWave(), new SoundWave()), 1d),
                        new AbstractMap.SimpleEntry<>(List.of(sw0, swSimilar0), 0.2868767015),
                        new AbstractMap.SimpleEntry<>(List.of(sw1, swSimilar1), 0d)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createSimilarityProvider")
    void testSimilarity(Map.Entry<List<SoundWave>,Double> entries) {
        SoundWave sw0 = entries.getKey().get(0);
        SoundWave swSimilar0 = entries.getKey().get(0);
        assertEquals(entries.getValue(), sw0.similarity(swSimilar0), epsilon);
    }
    
}
