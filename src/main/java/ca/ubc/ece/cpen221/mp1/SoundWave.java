package ca.ubc.ece.cpen221.mp1;

import ca.ubc.ece.cpen221.mp1.utils.HasSimilarity;
import javazoom.jl.player.StdPlayer;

import java.util.ArrayList;
import java.util.Collections;

public class SoundWave implements HasSimilarity<SoundWave> {

    private static final double SAMPLES_PER_SECOND = 44100.0;
    private static final double NYQUIST_LIMIT = SAMPLES_PER_SECOND / 2.0;

    private ArrayList<Double> lchannel = new ArrayList<>();
    private ArrayList<Double> rchannel = new ArrayList<>();


    /**
     * Create a new SoundWave using the provided left and right channel
     * amplitude values. After the SoundWave is created, changes to the
     * provided arguments will not affect the SoundWave.
     *
     * If either channel has less values than the other, append zeros until they match
     *
     * @param lchannel is not null and represents the left channel.
     * @param rchannel is not null and represents the right channel.
     */
    public SoundWave(double[] lchannel, double[] rchannel) {
        if (lchannel.length < rchannel.length) {
            lchannel = zeroAdder(lchannel, rchannel.length);
        }
        if (rchannel.length < lchannel.length) {
            rchannel = zeroAdder(rchannel, lchannel.length);
        }
        this.append(lchannel, rchannel);
    }

    /**
     * Creates a new soundwave with empty channels
     */
    public SoundWave() {
    }

    /**
     * @param channel double array to have zeros appended onto
     * @param length length of other channel to be matched
     * @return a new double array of the correct length with zeros appended.
     */
    private double[] zeroAdder(double[] channel, int length) {
        double[] newchannel = new double[length];
        for (int i = 0; i < length; i++) {
            if (i < channel.length) {
                newchannel[i] = channel[i];
            } else {
                newchannel[i] = 0.0;
            }
        }
        return newchannel;
    }

    /**
     * Create a new sinusoidal sine wave,
     * sampled at a rate of 44,100 samples per second
     * Makes left and right channels the same
     *
     * @param freq      the frequency of the sine wave, in Hertz
     * @param phase     the phase of the sine wave, in radians
     * @param amplitude the amplitude of the sine wave, 0 < amplitude <= 1
     * @param duration  the duration of the sine wave, in seconds
     */
    public SoundWave(double freq, double phase, double amplitude, double duration) {
        double A;
        for (double t = 0.0; t < duration; t += 1.0 / SAMPLES_PER_SECOND) {
            A = amplitude * Math.sin((2 * Math.PI) * freq * t + phase);
            this.lchannel.add(A);
            this.rchannel.add(A);
        }
    }

    /**
     * Obtain the left channel for this wave.
     * Changes to the returned array should not affect this SoundWave.
     *
     * @return an array that represents the left channel for this wave.
     */
    public double[] getLeftChannel() {
        double[] dlchannel = new double[this.lchannel.size()];
        for (int i = 0; i < this.lchannel.size(); i++) {
            dlchannel[i] = this.lchannel.get(i);
        }
        return dlchannel;
    }

    /**
     * Obtain the right channel for this wave.
     * Changes to the returned array should not affect this SoundWave.
     *
     * @return an array that represents the right channel for this wave.
     */
    public double[] getRightChannel() {
        double[] drchannel = new double[this.rchannel.size()];
        for (int i = 0; i < this.rchannel.size(); i++) {
            drchannel[i] = this.rchannel.get(i);
        }
        return drchannel;
    }


    /**
     * A simple main method to play an MP3 file. Note that MP3 files should
     * be encoded to use stereo channels and not mono channels for the sound to
     * play out correctly.
     * <p>
     * One should try to get this method to work correctly at the start.
     * </p>
     *
     * @param args are currently ignored but you could be creative.
     */

    public static void main(String[] args) {
        StdPlayer.open("mp3/anger.mp3");
        SoundWave sw = new SoundWave();
        while (!StdPlayer.isEmpty()) {
            double[] lchannel = StdPlayer.getLeftChannel();
            double[] rchannel = StdPlayer.getRightChannel();
            sw.append(lchannel, rchannel);
        }
        sw.sendToStereoSpeaker();
        StdPlayer.close();

    }

    /**
     * Append a wave to this wave.
     *
     * @param leftchannel left channel double array to be converted to arraylist
     * @param rightchannel right channel double array to be converted to arraylist
     */

    private void append(double[] leftchannel, double[] rightchannel) {
        for (int i = 0; i < leftchannel.length; i++) {
            this.lchannel.add(leftchannel[i]);
            this.rchannel.add(rightchannel[i]);
        }
    }

    /**
     * Append a wave to this wave.
     * use the length of the larger wave
     * @param other the wave to append.
     */
    public void append(SoundWave other) {
        int length;
        if (this.lchannel.size() > other.lchannel.size()) {
            length = other.lchannel.size();
        } else {
            length = this.lchannel.size();
        }
        for (int i = 0; i < length; i++) {
            this.lchannel.add(other.lchannel.get(i));
            this.rchannel.add(other.rchannel.get(i));
        }
    }

    /**
     * Create a new wave by adding another wave to this wave.
     * @param other wave to be added.
     * @Return a new soundwave with merged/added amplitude values, trimmed at -1, 1
     *
     * uses the larger of the two soundwave lengths as the value to be added to, appends zeros to the smaller to accommodate
     */
    public SoundWave add(SoundWave other) {
        SoundWave merge = new SoundWave();
        double sl1, sr1, sl2, sr2, left, right;
        int len;
        int nonlen;
        boolean sent;

        if (other.lchannel.size() > this.lchannel.size()) {
            len = other.lchannel.size();
            nonlen = this.lchannel.size();
            sent = false;
        } else {
            len = this.lchannel.size();
            nonlen = other.lchannel.size();
            sent = true;
        }

        for (int i = nonlen; i < len; i++) {
            if (sent) {
                other.lchannel.add(0.0);
                other.rchannel.add(0.0);
            } else {
                this.lchannel.add(0.0);
                this.rchannel.add(0.0);
            }
        }

        for (int i = 0; i < len; i++) {
            sl1 = this.lchannel.get(i);
            sr1 = this.rchannel.get(i);
            sl2 = other.lchannel.get(i);
            sr2 = other.rchannel.get(i);
            left = sl1 + sl2;
            right = sr2 + sr1;
            left = trim(left);
            right = trim(right);

            merge.lchannel.add(left);
            merge.rchannel.add(right);

        }
        return merge;
    }


    /**
     * @param left double value to be trimmed if greater than or less than 1, -1 respectively
     * @return a new double shortened to 1, -1 if needed
     */
    private double trim(double left) {
        if (left > 1) {
            left = 1;
        }
        if (left < -1) {
            left = -1;
        }

        return left;
    }

    /**
     * Create a new wave by adding an echo to this wave.
     *
     * @param delta > 0. delta is the lag between this wave and the echo wave.
     * @param alpha <= 1, alpha > 0. alpha is the damping factor applied to the echo wave.
     * @return a new sound wave with an echo.
     */
    public SoundWave addEcho(int delta, double alpha) {
        SoundWave echo = new SoundWave();
        double echoleft;
        double echoright;

        for (int i = 0; i < this.lchannel.size() + delta; i++) {
            if (i < delta) {
                echoleft = this.lchannel.get(i);
                echoright = this.rchannel.get(i);
            } else if (i >= this.lchannel.size()) {
                echoleft = this.lchannel.get(i - delta) * alpha;
                echoright = this.rchannel.get(i - delta) * alpha;
            } else {
                echoleft = this.lchannel.get(i) + this.lchannel.get(i - delta) * alpha;
                echoright = this.rchannel.get(i) + this.rchannel.get(i - delta) * alpha;
            }
            echoleft = trim(echoleft);
            echoright = trim(echoright);
            echo.lchannel.add(echoleft);
            echo.rchannel.add(echoright);
        }
        return echo;
    }


    /**
     * Scale the amplitude of this wave by a scaling factor.
     * After scaling, the amplitude values are clipped to remain
     * between -1 and +1.
     *
     * @param scalingFactor is a value > 0.
     *        mutates original soundwave by multiplying it's values by scaling factor
     */
    public void scale(double scalingFactor) {
        double left;
        double right;
        ArrayList nlchannel = new ArrayList<>();
        ArrayList nrchannel = new ArrayList<>();

        for (int i = 0; i < this.lchannel.size(); i++) {
            left = this.lchannel.get(i) * scalingFactor;
            right = this.rchannel.get(i) * scalingFactor;
            left = trim(left);
            right = trim(right);
            nlchannel.add(left);
            nrchannel.add(right);
        }

        this.lchannel.clear();
        this.rchannel.clear();
        this.lchannel = nlchannel;
        this.rchannel = nrchannel;
    }

    /**
     * Return a new sound wave that is obtained by applying a high-pass filter to
     * this wave.
     *
     * @param dt >= 0. dt is the time interval used in the filtering process.
     * @param RC > 0. RC is the time constant for the high-pass filter.
     * @return a filtered wave
     */
    public SoundWave highPassFilter(int dt, double RC) {
        double a = RC / (RC + dt);
        SoundWave filtered = new SoundWave();

        for (int i = 0; i < this.lchannel.size(); i++) {
            if (i == 0) {
                filtered.lchannel.add((this.lchannel.get(i)));
                filtered.rchannel.add((this.rchannel.get(i)));
            } else {
                filtered.lchannel.add(a * filtered.lchannel.get(i - 1)
                        + a * (this.lchannel.get(i) - this.lchannel.get(i - 1)));
                filtered.rchannel.add(a * filtered.rchannel.get(i - 1)
                        + a * (this.rchannel.get(i) - this.rchannel.get(i - 1)));
            }

        }

        return filtered;
    }

    /**
     * Return the frequency of the component with the greatest amplitude
     * contribution to this wave. This component is obtained by applying the
     * Discrete Fourier Transform to this wave.
     *
     * @return the frequency of the wave component of highest amplitude.
     * If more than one frequency has the same amplitude contribution then
     * return the lower frequency for high frequencies, returns higher frequency for low frequencies.
     *
     * accurate to values with amplitudes spaced by more than 0.2, works for any length of wave - longer durations
     *      * result in more accurate frequency measurements.
     */
    public double highAmplitudeFreqComponent() {
        double freqmax, reall, imagl, realr, imagr, bval, magnitudel, magnituder;
        double max = 0;
        double indexmax;

        ArrayList<Integer> maxes= new ArrayList<>();
        for(int j = 0; j < this.lchannel.size() / 2; j++) {

            ComplexNumber ltotc = new ComplexNumber(0.0, 0.0);
            ComplexNumber rtotc = new ComplexNumber(0.0, 0.0);

            for (int i = 0; i < this.lchannel.size(); i++) {
                bval = (2 * Math.PI * i * j) / (this.lchannel.size());

                reall = this.lchannel.get(i) * Math.cos(bval);
                imagl = this.lchannel.get(i) * Math.sin(bval);
                realr = this.rchannel.get(i) * Math.cos(bval);
                imagr = this.rchannel.get(i) * Math.sin(bval);

                ComplexNumber leftC = new ComplexNumber(reall, imagl);
                ComplexNumber rightC = new ComplexNumber(realr, imagr);

                ltotc = ltotc.Sum(leftC);
                rtotc = rtotc.Sum(rightC);
            }
            magnitudel = ltotc.Magnitude();
            magnituder = rtotc.Magnitude();

            if (magnitudel >= (max)) {
                if (j * (SAMPLES_PER_SECOND / (this.lchannel.size())) < NYQUIST_LIMIT) {
                    max = magnitudel;
                    maxes.add(j);
                } else {
                    break;
                }
            }
            if (magnituder >= (max)) {
                if (j * (SAMPLES_PER_SECOND / (this.lchannel.size())) < NYQUIST_LIMIT) {
                    max = magnituder;
                    maxes.add(j);
                } else {
                    break;
                }
            }
        }
        indexmax = Collections.max(maxes);
        freqmax = indexmax * (SAMPLES_PER_SECOND / ((this.lchannel.size())));
        return freqmax;
    }

    /**
     * Determine if this wave fully contains the other sound wave as a pattern.
     *
     * @param other is the wave to search for in this wave.
     * @return true if the other wave is contained in this after amplitude scaling,
     * and false if the other wave is not contained in this with any
     * possible amplitude scaling.
     */
    public boolean contains(SoundWave other) {

        double betal, lround, rround;
        int sum = 0;

        for (int i = 0; i <= this.lchannel.size() - other.lchannel.size(); i++) {
            SoundWave scaled = new SoundWave(other.getLeftChannel(), other.getRightChannel());

            if (other.lchannel.get(0) != 0 && other.rchannel.get(0) != 0) {
                betal = this.lchannel.get(i) / other.lchannel.get(0);
                scaled.scale(betal);
            } else {
                betal = this.betaZeroChecker(other, i);
                scaled.scale(betal);
            }

            for (int j = 0; j < other.lchannel.size(); j++) {
                double l = this.lchannel.get(i + j);
                double r = this.rchannel.get(i + j);
                lround = Math.round(scaled.lchannel.get(j) * 1000000d) / 1000000d;
                rround = Math.round(scaled.rchannel.get(j) * 1000000d) / 1000000d;

                if (betal > 0 && l == lround && r == rround) {
                    sum++;
                } else {
                    sum = 0;
                    break;
                }
                if (sum == other.lchannel.size()) {
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Implemented to avoid some fringe cases of dividing by zero when calculating beta
     *
     * @param other other soundwave to check
     * @param i current itteration of the outside loop
     * @return real beta value calculated using the first non-zero
     * amplitudes found in L,R channels in two different waves
     */
    private double betaZeroChecker(SoundWave other, int i){
        double betal;
        int firstNonZeroIInner = 0;
        int firstNonZeroIOuter = 0;
        boolean sentinel = false;
        boolean rvalouter = false;
        boolean lvalouter = false;
        boolean rvaloinner = false;
        boolean lvalinner = false;


        for(int w = i; w < this.lchannel.size(); w++){
            if(!sentinel) {
                for (int x = 0; x < other.lchannel.size(); x++) {
                    if (other.rchannel.get(x) != 0) {
                        firstNonZeroIInner = x;
                        rvaloinner = true;
                        sentinel = true;
                        break;
                    } else if (other.lchannel.get(x) != 0) {
                        firstNonZeroIInner = x;
                        lvalinner = true;
                        sentinel = true;
                        break;
                    }
                }
            }

            if((this.rchannel.get(w) != 0) && sentinel) {
                firstNonZeroIOuter = w;
                rvalouter = true;
                break;
            }

            else if(this.lchannel.get(w) != 0 && sentinel) {
                firstNonZeroIOuter = w;
                lvalouter = true;
                break;
            }

        }
        if(rvalouter && rvaloinner) {
            betal = this.rchannel.get(firstNonZeroIOuter) / other.rchannel.get(firstNonZeroIInner);
            return betal;
        }
        else if(lvalouter && lvalinner) {
            betal = this.lchannel.get(firstNonZeroIOuter) / other.lchannel.get(firstNonZeroIInner);
            return betal;
        }

        betal = 1.0;
        return  betal;
    }



    /**
     * Determine the similarity between this wave and another wave.
     * The similarity metric, gamma, is the sum of squares of
     * instantaneous differences.
     *
     * @param other is not null. Other and this have equal length left and right channels.
     * @return the similarity between this wave and other.
     */
    public double similarity(SoundWave other) {
        double gamma;
        double gamma1;
        double gamma2;
        double a;
        double b;
        double asum = 0;
        double bsum = 0;
        double beta;
        double[] zeros = {};

        if (other.lchannel.size() < this.lchannel.size()) {
            for (int i = other.lchannel.size() + 1; i <= this.lchannel.size(); i++) {
                other.lchannel.add(0.0);
                other.rchannel.add(0.0);
            }
        }
        if (this.lchannel.size() < other.lchannel.size()) {
            for (int i = this.lchannel.size() + 1; i <= other.lchannel.size(); i++) {
                this.lchannel.add(0.0);
                this.rchannel.add(0.0);
            }
        }

        beta = this.getBeta(other);
        for (int i = 0; i < this.lchannel.size(); i++) {
            a = Math.pow((this.lchannel.get(i) - beta * other.lchannel.get(i)), 2)
                    + Math.pow((this.rchannel.get(i) - beta * other.rchannel.get(i)), 2);
            asum += a;
        }

        beta = other.getBeta(this);
        for (int i = 0; i < other.lchannel.size(); i++) {
            b = Math.pow((other.lchannel.get(i) - beta * this.lchannel.get(i)), 2)
                    + Math.pow((other.rchannel.get(i) - beta * this.rchannel.get(i)), 2);
            bsum += b;
        }

        gamma1 = 1.0 / (1 + asum);
        gamma2 = 1.0 / (1 + bsum);

        gamma = (gamma1 + gamma2) / 2.0;

        return gamma;
    }

    private double getBeta(SoundWave other) {
        //this is w1, other is w2
        double c, a, beta;
        double csum = 0;
        double asum = 0;

        for (int i = 0; i < other.lchannel.size(); i++) {
            c = ((this.rchannel.get(i)) * (other.rchannel.get(i)))
                    + ((this.lchannel.get(i)) * (other.lchannel.get(i)));
            a = Math.pow((other.lchannel.get(i)), 2) + Math.pow((other.rchannel.get(i)), 2);
            csum += c;
            asum += a;
        }

        beta = csum / asum;
        if (beta <= 0) {
            beta = 0.0000000000001;
        }
        return beta;
    }

    /**
     * Play this wave on the standard stereo device.
     * Not needed in testing line coverage.
     */
    private void sendToStereoSpeaker() {
        double[] lchannel = this.lchannel.stream().mapToDouble(x -> x.doubleValue()).toArray();
        double[] rchannel = this.rchannel.stream().mapToDouble(x -> x.doubleValue()).toArray();
        StdPlayer.playWave(lchannel, rchannel);
    }
}
