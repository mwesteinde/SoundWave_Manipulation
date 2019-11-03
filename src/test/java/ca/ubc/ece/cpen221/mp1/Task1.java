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

class Task1 {

    private final static double epsilon = 1E-6; 

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

    @ParameterizedTest
    @MethodSource("createSameWaveProvider")
    void testCreateSameWave(double[] channel) {
        double[] lchannel = Arrays.copyOf(channel, channel.length);
        double[] rchannel = Arrays.copyOf(channel, channel.length);

        SoundWave wave = new SoundWave(lchannel, rchannel);

        double[] lchannel1 = wave.getLeftChannel();
        double[] rchannel1 = wave.getRightChannel();

        assertArrayEquals(lchannel, lchannel1, epsilon);
        assertArrayEquals(rchannel, rchannel1, epsilon);
        assertTrue(sameWave(wave, new SoundWave(lchannel1, rchannel1)));
    }

    @Test
    void testWaveSame() {
        SoundWave wave = new SoundWave(400, 3.5, 0.6, 20);
        SoundWave waveDuplicate = new SoundWave(wave.getLeftChannel(), wave.getRightChannel());
        assertTrue(sameWave(wave, waveDuplicate));
    }

    @Test
    void testWaveNotSame() {
        SoundWave wave0 = new SoundWave(400, 3.5, 0.6, 20);
        SoundWave wave1 = new SoundWave(400, 3.5, 0.7, 20);
        SoundWave wave2 = new SoundWave(wave0.getLeftChannel(), wave0.getRightChannel());
        assertFalse(sameWave(wave1, wave2));
    }

    @Test
    void testWaveEqualLengths() {
        SoundWave sw = new SoundWave(400, 3.5, 0.6, 0.1);
        assertEquals(sw.getLeftChannel().length, sw.getRightChannel().length);
    }

    @Test
    void testWaveExpected() {
        WaveGen wave = new WaveGen(400, 3.5, 0.6, 5);
        SoundWave sw = new SoundWave(400, 3.5, 0.6, 5);

        double[] w0 = wave.getWaveArray();
        double[] wl = sw.getLeftChannel();
        double[] wr = sw.getRightChannel();

        assertEquals(w0.length, wl.length, 1);
        assertEquals(w0.length, wr.length, 1);

        IntStream.range(0, Math.min(w0.length, wl.length)).parallel().forEach(i -> {
            assertEquals(w0[i], wl[i], epsilon);
            assertEquals(w0[i], wr[i], epsilon);
        });
    }

    @Test
    void testWaveCancels() {
        SoundWave sw = new SoundWave(400, 3.5, 0.6, 1);
        double[] swr = sw.getRightChannel();
        double[] swl = sw.getLeftChannel();

        double[] wr = Arrays.copyOf(swr, swr.length);
        double[] wl = Arrays.copyOf(swl, swl.length);

        IntStream.range(0, wr.length).parallel().forEach(i -> { wl[i]*=-1; wr[i]*=-1; });

        SoundWave swInvert = sw.add(new SoundWave(wr, wl));

        double[] swrInverted = swInvert.getRightChannel();
        double[] swlInverted = swInvert.getLeftChannel();

        double[] zeros = new double[swrInverted.length];

        assertArrayEquals(zeros, swrInverted);
        assertArrayEquals(zeros, swlInverted);
    }

    private static double[] concatArray(double[] c0, double[] c1) {
        return DoubleStream.concat(Arrays.stream(c0), Arrays.stream(c1)).toArray();
    }

    /**
     * Argument provider
     * @return List of Map.Entry containing left channel keys and expected array values.
     */
    private static Stream<Arguments> createAppendWaveProvider() {
        // Append waves
        var lc0 = List.of(new double[]{0.1, 0.2, 0.1}, new double[]{0.5, -0.5, -0.1});
        var rc0 = List.of(new double[]{0.2, 0.1, 0.2}, new double[]{1.0, 0.0, -1.0});
        var la0 = new AbstractMap.SimpleEntry<>(lc0, concatArray(lc0.get(0), lc0.get(1)));
        var ra0 = new AbstractMap.SimpleEntry<>(rc0, concatArray(rc0.get(0), rc0.get(1)));

        // Fill remainder with zeros
        var lc1 = List.of(new double[]{0.1, 0.2, 0.1}, new double[]{-0.1, -0.2, -0.3});
        var rc1 = List.of(new double[]{0.2, 0.1, 0.2}, new double[]{-1.0});
        var la1 = new AbstractMap.SimpleEntry<>(lc1, concatArray(lc1.get(0), lc1.get(1)));
        var ra1 = new AbstractMap.SimpleEntry<>(rc1, new double[]{-1.0, 0d, 0d});

        return Stream.of(
                Arguments.of(
                        List.of(la0, ra0), List.of(la1, ra1)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createAppendWaveProvider")
    void testAppend(List<Map.Entry<List<double[]>, double[]>> entries) {
        var lArgs = entries.get(0);
        var rArgs = entries.get(1);

        var lc0 = lArgs.getKey().get(0);
        var rc0 = rArgs.getKey().get(0);

        var lc1 = lArgs.getKey().get(1);
        var rc1 = rArgs.getKey().get(1);

        var lExpected = lArgs.getValue();
        var rExpected = rArgs.getValue();

        SoundWave sw0 = new SoundWave(lc0, rc0);

        sw0.append(lc1, rc1);

        assertArrayEquals(lExpected, sw0.getLeftChannel());
        assertArrayEquals(rExpected, sw0.getRightChannel());
    }

    /**
     * Argument provider
     * @return Wave, scale Map.Entry<double[],Double>  arguments
     */
    private static Stream<Arguments> createScaledWaveProvider() {
        double[] testArgs0 = {400, 4d, 0.5, 5};
        double[] testArgs1 = {400, 4d, -0.5, 5};
        return Stream.of(
                Arguments.of(
                        new AbstractMap.SimpleEntry<>(testArgs0, 0d),
                        new AbstractMap.SimpleEntry<>(testArgs0, 0.1), // Decrease
                        new AbstractMap.SimpleEntry<>(testArgs0, 1d),
                        new AbstractMap.SimpleEntry<>(testArgs0, 1.1),
                        new AbstractMap.SimpleEntry<>(testArgs0, 2),   // Clipping occurs
                        new AbstractMap.SimpleEntry<>(testArgs1, 0d),
                        new AbstractMap.SimpleEntry<>(testArgs1, 0.1),
                        new AbstractMap.SimpleEntry<>(testArgs1, 1d),
                        new AbstractMap.SimpleEntry<>(testArgs1, 1.1),
                        new AbstractMap.SimpleEntry<>(testArgs1, 2)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createScaledWaveProvider")
    void testScaled(Map.Entry<double[],Double> entries) {
        var wa = entries.getKey();
        var scale = entries.getValue();

        var sw0 = new SoundWave(wa[0], wa[1], wa[2], wa[3]);
        var sw1 = new SoundWave(wa[0], wa[1], wa[2]*scale, wa[3]);

        sw0.scale(scale);

        assertTrue(sameWave(sw0, sw1));
    }


    /**
     * Argument provider
     * @return Wave to echo, expected, and echo values in List<List<double[]>>
     */
    private static Stream<Arguments> createEchoWaveProvider() {
        double[] lc0 = {0.1, 0.2, 0.3};
        double[] rc0 = {0.2, 0.3, 0.4};
        double[] lExpect0 = {0.1, 0.3, 0.5, 0.3};
        double[] rExpect0 = {0.2, 0.5, 0.7, 0.4};
        double[] echo0 = {1, 1};

        // Clipping will occur
        double[] lc1 = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] rc1 = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7};
        double[] lExpect1 =  {0.1, 0.2, 0.3, 0.5, 0.7, 0.9, 0.4, 0.5, 0.6};
        double[] rExpect1 = {0.2, 0.3, 0.4, 0.7, 0.9, 1.0, 0.5, 0.6, 0.7};
        double[] echo1 = {3, 1};

        // Clipping will occur
        double[] lc2 = {0.4, 0.5, 0.6};
        double[] rc2 = {0.5, 0.6, 0.7};
        double[] lExpect2 = {0.4, 0.5 + 0.4 * 0.9, 1.0, 0.6 * 0.9};
        double[] rExpect2 = {0.5, 1.0, 1.0, 0.7 * 0.9};
        double[] echo2 = {1, 0.9};

        // Clipping will occur
        double[] lc3 = {-0.4, -0.5, -0.6};
        double[] rc3 = {-0.5, -0.6};
        double[] lExpect3 = {-0.4, -0.5 - 0.4 * 0.9, -1.0, -0.6 * 0.9};
        double[] rExpect3 = {-0.5, -1.0, -0.6 * 0.9};
        double[] echo3 = {1, 0.9};

        // Add echo to empty wave
        double[] c4 = {};
        double[] expect4 = {0, 0};
        double[] echo4 = {2, 0.9};

        double[] lc5 = {0.5, 0.4, 0.3, 0.2, 0.1};
        double[] rc5 = {-0.5, -0.4, -0.3, -0.2, -0.1};
        double[] lExpect5 = {0.5, 0.4, 0.55, 0.4, 0.25};
        double[] rExpect5 = {-0.5, -0.4, -0.55, -0.4, -0.25};
        double[] echo5 = {2, 0.5};

        return Stream.of(
                Arguments.of(
                        List.of(List.of(lc0, rc0), List.of(lExpect0, rExpect0), List.of(echo0)),
                        List.of(List.of(lc1, rc1), List.of(lExpect1, rExpect1), List.of(echo1)),
                        List.of(List.of(lc2, rc2), List.of(lExpect2, rExpect2), List.of(echo2)),
                        List.of(List.of(lc3, rc3), List.of(lExpect3, rExpect3), List.of(echo3)),
                        List.of(List.of(c4, c4), List.of(expect4, expect4), List.of(echo4)),
                        List.of(List.of(lc5, rc5), List.of(lExpect5, rExpect5), List.of(echo5))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createEchoWaveProvider")
    void testEchoWaves(List<List<double[]>> entries) {
        var lc0 = entries.get(0).get(0);
        var rc0 = entries.get(0).get(1);

        var lExpected = entries.get(1).get(0);
        var rExpected = entries.get(1).get(1);

        var echoV = entries.get(2).get(0);

        var echo = new SoundWave(lc0, rc0).addEcho((int) echoV[0], echoV[1]);

        assertTrue(sameWave(new SoundWave(lExpected, rExpected), echo));
    }

    /**
     * Argument provider
     * @return Wave to echo, expected, and echo values in List<List<double[]>>
     */
    private static Stream<Arguments> createHighPassFilterProvider() {
        // dt = 1
        double[] lc0 = {-0.4, -0.5};
        double[] rc0 = {-0.5, -0.6};
        double[] lExpect0 = {-0.4, -0.1875};
        double[] rExpect0 = {-0.5, -0.225};
        double[] hpf0 = {1, 0.6};

        double[] lc1 = {-1.0, 1.0, 0.5, 0.4};
        double[] rc1 ={-0.5, 0.2, 0.8, -0.3};
        double[] lExpect1 = {-1.0, 0.5, 0, -0.05};
        double[] rExpect1 = {-0.5, 0.1, 0.35, -0.375};
        double[] hpf1 = {1, 1};

        return Stream.of(
                Arguments.of(
                        List.of(List.of(lc0, rc0), List.of(lExpect0, rExpect0), List.of(hpf0)),
                        List.of(List.of(lc1, rc1), List.of(lExpect1, rExpect1), List.of(hpf1))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createHighPassFilterProvider")
        // Basic test with dt = 1 and small channel size, so only one item affected.
    void testHighPassFilter(List<List<double[]>> entries) {
        var lc0 = entries.get(0).get(0);
        var rc0 = entries.get(0).get(1);

        var lExpected = entries.get(1).get(0);
        var rExpected = entries.get(1).get(1);

        var hpfV = entries.get(2).get(0);

        var hpf = new SoundWave(lc0, rc0).highPassFilter((int) hpfV[0], hpfV[1]);

        assertTrue(sameWave(new SoundWave(lExpected, rExpected), hpf));
    }

    /**
     * Argument provider
     * @return Wave with hafc to test as  Map.Entry<double[],Double>
     */
    private static Stream<Arguments> createHighAmplitudeFreqComponentProvider() {
        
        return Stream.of(
                Arguments.of(
                        new AbstractMap.SimpleEntry<>(new SoundWave(300, 0, 0.2, 1), 300d),
                        new AbstractMap.SimpleEntry<>(new SoundWave(300, 0, 0.4, 1).add(new SoundWave(1000, 0, 0.2, 1)), 300d),
                        new AbstractMap.SimpleEntry<>(new SoundWave(300, 0, 0.1, 1).add(new SoundWave(1000, 0, 0.5, 1)), 1000d)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createHighAmplitudeFreqComponentProvider")
    void testHighAmplitudeFreqComponent(Map.Entry<SoundWave,Double> entries) {
        assertEquals(entries.getValue(), entries.getKey().highAmplitudeFreqComponent(), epsilon);
    }

}
