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

class Task3 {

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
     * @return Grouping number, soundwave set as Map.Entry<Integer,Set<SoundWave>>
     */
    private static Stream<Arguments> createSimilarityGroupingProvider() {
        double[] lc0 = {-1.0, 1.0, 0.5, 0.4};
        double[] rc0 = {-0.5, 0.2, 0.8, -0.3};
        double[] lc1 = {-1.0, 0.5, 0.2};
        double[] rc1 = {0.2, 0.3, -0.9};
        double[] lc2 = {0.1, -0.3, 1, 0.7, -0.6, 0.4, -0.6, 0.4, -0.3};
        double[] rc2 = {0.1, 0.4, -0.1, 0.3, -0.6, -0.4, -0.5, 0.3, -0.2};
        double[] lc3 = {-0.1, 0.3, 0.1, 0.6};
        double[] rc3 = {-0.5, 0.4, -0.7, -0.3};
        double[] lc4 = {0.5, -0.1, 0.4, 0.6, 0.4, -0.2};
        double[] rc4 = {0.6, -0.1, 0.4, 0.1, -0.4, 0.9};

        return Stream.of(
                Arguments.of(
                        new AbstractMap.SimpleEntry<>(
                                3,
                                Set.of(
                                        new SoundWave(400, 0.1, 0.8, 0.2),
                                        new SoundWave(360, 1, 0.5, 0.2),
                                        new SoundWave(386, 3, 0.4, 0.2),
                                        new SoundWave(299, 0.8, 0.5, 0.2),
                                        new SoundWave(266, 0.2, 0.5, 0.2),
                                        new SoundWave(125, 0.8, 0.9, 0.2)
                                )),
                        new AbstractMap.SimpleEntry<>(
                                3,
                                Set.of(
                                        new SoundWave(lc0, rc0),
                                        new SoundWave(lc1, rc1),
                                        new SoundWave(lc2, rc2),
                                        new SoundWave(lc3, rc3),
                                        new SoundWave(lc4, rc4)
                                ))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createSimilarityGroupingProvider")
    void testSimilarityGrouping(Map.Entry<Integer,Set<SoundWave>> entry) {
        SoundWaveSimilarity waveSimilarity = new SoundWaveSimilarity();
        var waves = entry.getValue();
        var grouping = entry.getKey();

        for (SoundWave wave : waves) {

            Set<SoundWave> similars = waveSimilarity.getSimilarWaves(waves, grouping, wave);
            Set<SoundWave> different = new HashSet<>(waves);
            different.removeAll(similars);

            Double similarity = similars.stream()
                    .max(Comparator.comparing(wave::similarity))
                    .map(wave::similarity)
                    .get();

            Double similarityOthers = different.stream()
                    .max(Comparator.comparing(wave::similarity))
                    .map(wave::similarity)
                    .get();

            assertTrue(similarity >= similarityOthers);
        }
    }
}
