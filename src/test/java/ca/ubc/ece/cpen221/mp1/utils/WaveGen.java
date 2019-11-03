package ca.ubc.ece.cpen221.mp1.utils;

import ca.ubc.ece.cpen221.mp1.SoundWave;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WaveGen {

    private final List<Double> wave;
    private final int samples;
    private static final int SAMPLES_PER_SECOND = 44100;

    /**
     * Create a wave,
     * @param freq Wave frequency
     * @param phase Wave phase
     * @param amp Wave amplitude
     * @param duration Wave duration
     * @param chop Clip at this value if amplitude larger than chop. If chop == 0, don't clip.
     */
    public WaveGen(double freq, double phase, double amp, double duration, double chop) {
        // Define angular frequency, w, to be 2 * pi * frequency
        double w = 2 * Math.PI * freq;
        this.samples = (int) Math.round(duration * SAMPLES_PER_SECOND);

        this.wave = IntStream.range(0, this.samples)
                .mapToDouble(d -> (double) d)
                .map(t -> {
                    double a = amp * Math.sin(w * (t / SAMPLES_PER_SECOND) + phase);
                    return (chop == 0d) ? a : clip(a, chop);
                })
                .boxed().collect(Collectors.toUnmodifiableList());
    }

    public WaveGen(double freq, double phase, double amp, double duration) {
        this(freq, phase, amp, duration, 1d);
    }

    public int getSamples() {
        return samples;
    }

    public List<Double> getWave() {
        return wave;
    }
    public double[] getWaveArray() {
        double[] waveArray = new double[this.wave.size()];
        for (int i = 0; i < this.wave.size(); i++) {
            waveArray[i] = this.wave.get(i);
        }
        return waveArray;
    }

    private static Double clip(Double amplitude, double chop) {
        return (amplitude > chop) ? chop : Math.max(amplitude, -chop);
    }
}
