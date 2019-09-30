package ca.ubc.ece.cpen221.mp1;

import ca.ubc.ece.cpen221.mp1.utils.HasSimilarity;
import javazoom.jl.player.StdPlayer;

import java.util.ArrayList;

public class SoundWave implements HasSimilarity<SoundWave> {

    // We are going to treat the number of samples per second of a sound wave
    // as a constant.
    // The best way to refer to this constant is as
    // SoundWave.SAMPLES_PER_SECOND.
    public static final double SAMPLES_PER_SECOND = 44100.0;
    public static final double PI = 3.14159265358979;

    // some representation fields that you could use
    private ArrayList<Double> lchannel = new ArrayList<>();
    private ArrayList<Double> rchannel = new ArrayList<>();
    private int samples = 0;



    public ComplexNumber(double rval, double ival){
        double r = rval;
        double i = ival;
    }
    /**
     * Create a new SoundWave using the provided left and right channel
     * amplitude values. After the SoundWave is created, changes to the
     * provided arguments will not affect the SoundWave.
     *
     * @param lchannel is not null and represents the left channel.
     * @param rchannel is not null and represents the right channel.
     */
    public SoundWave(double[] lchannel, double[] rchannel) {
        this.append(lchannel, rchannel);
    }

    public SoundWave() {
        this.lchannel = new ArrayList<>();
        this.rchannel = new ArrayList<>();
    }

    /**
     * Create a new sinusoidal sine wave,
     * sampled at a rate of 44,100 samples per second
     *
     * @param freq      the frequency of the sine wave, in Hertz
     * @param phase     the phase of the sine wave, in radians
     * @param amplitude the amplitude of the sine wave, 0 < amplitude <= 1
     * @param duration  the duration of the sine wave, in seconds
     */
    public SoundWave(double freq, double phase, double amplitude, double duration) {
        double A = 0.0;

        for(double t = 0.0; t < duration; t += 1.0/SAMPLES_PER_SECOND) {
            A = amplitude*Math.sin((2*PI)*freq*t + phase);
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

        for(int i = 0; i < this.lchannel.size(); i++) {
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

        for(int i = 0; i < this.rchannel.size(); i++){
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
        StdPlayer.open();

         /*StdPlayer.open("mp3/late.mp3");
         SoundWave sw = new SoundWave();
        SoundWave fw = new SoundWave(543, 0, 1, 10);
        while (!StdPlayer.isEmpty()) {
            double[] lchannel = StdPlayer.getLeftChannel();
            double[] rchannel = StdPlayer.getRightChannel();
            sw.append(lchannel, rchannel);
        }*/
        SoundWave c = new SoundWave(200, 0, 1, 10);
        SoundWave g = new SoundWave( 300, 0, 1, 10);
        SoundWave e = new SoundWave(500, 0, 1, 10);

        SoundWave merge = new SoundWave();
        SoundWave echo = new SoundWave();
         merge = c.add(g);
         merge = merge.add(e);

         echo = merge.addEcho(40000, 0.5);
        echo.sendToStereoSpeaker();
        StdPlayer.close();


    }

    /**
     * Append a wave to this wave.
     *
     * @param lchannel
     * @param rchannel
     */
    public void append(double[] lchannel, double[] rchannel) {
        for(int i = 0; i < lchannel.length; i++) {
            this.lchannel.add(lchannel[i]);
            this.rchannel.add(rchannel[i]);
        }
    }

    /**
     * Append a wave to this wave.
     *
     * @param other the wave to append.
     */
    public void append(SoundWave other) {
        int length;
        if (this.lchannel.size() > other.lchannel.size()) {
            length = other.lchannel.size();
        }
        else {
            length = this.lchannel.size();
        }
        for(int i = 0; i < length; i++) {
            this.lchannel.add(other.lchannel.get(i));
            this.rchannel.add(other.rchannel.get(i));
        }
    }

    /**
     * Create a new wave by adding another wave to this wave.
     * (You should also write clear specifications for this method.)
     */
    public SoundWave add(SoundWave other) {
        SoundWave merge = new SoundWave();
        double sl1 = 0.0;
        double sr1 = 0.0;
        double sl2 = 0.0;
        double sr2 = 0.0;
        double left;
        double right;
        int len;
        int nonlen;
        boolean sent;

        if(other.lchannel.size() > this.lchannel.size()){
            len = other.lchannel.size();
            nonlen = this.lchannel.size();
            sent = false;
        }
        else {
            len = this.lchannel.size();
            nonlen = other.lchannel.size();
            sent = true;
        }

        for(int i = nonlen; i < len; i++){
            if(sent) {
                other.lchannel.add(0.0);
                other.rchannel.add(0.0);
            }
            else{
                this.lchannel.add(0.0);
                this.rchannel.add(0.0);
            }
        }

        for(int i = 0; i < len; i++){
            sl1 = this.lchannel.get(i);
            sr1 = this.rchannel.get(i);
            sl2 = other.lchannel.get(i);
            sr2 = other.rchannel.get(i);
                left = sl1 + sl2;
                right = sr2 + sr1;

                left = Trim(left);
                right = Trim(right);

                merge.lchannel.add(left);
                merge.rchannel.add(right);

        }
        return merge; // change this
    }

    public double Trim(double left) {
        if(left > 1)
            left = 1;
        if(left < -1)
            left = -1;

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
        ArrayList echolchannel = new ArrayList<>();
        ArrayList echorchannel = new ArrayList<>();
        for (int i = 0; i < this.lchannel.size(); i++) {
            if (i < delta) {
                echoleft = this.lchannel.get(i);
                echoright = this.rchannel.get(i);
            }
            else {
                echoleft = this.lchannel.get(i) + this.lchannel.get(i - delta) * alpha;
                echoright = this.rchannel.get(i) + this.rchannel.get(i - delta) * alpha;
            }
            echoleft = Trim(echoleft);
            echoright = Trim(echoright);
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
     */
    public void scale(double scalingFactor) {
        double left;
        double right;
        ArrayList nlchannel = new ArrayList<>();
        ArrayList nrchannel = new ArrayList<>();

       for(int i = 0; i < this.lchannel.size(); i++){
           left = this.lchannel.get(i) * scalingFactor;
           right = this.rchannel.get(i) * scalingFactor;

           left = Trim(left);
           right = Trim(right);

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
     * @return
     */
    public SoundWave highPassFilter(int dt, double RC) {
        double a = RC / (RC + dt);
        SoundWave filtered = new SoundWave();
        double l;
        double r;

        for(int i = 0; i < this.lchannel.size(); i++){
            if(i == 0){
                filtered.lchannel.add((this.lchannel.get(i)));
                filtered.rchannel.add((this.rchannel.get(i)));
            }
            else{
               filtered.lchannel.add(a * filtered.lchannel.get(i - 1) + a * (this.lchannel.get(i) - this.lchannel.get(i - 1)));
               filtered.rchannel.add(a * filtered.rchannel.get(i - 1) + a * (this.rchannel.get(i) - this.rchannel.get(i - 1)));
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
     * return the higher frequency.
     */
    public double highAmplitudeFreqComponent() {
        double sum, left, right, csum, reall, imagl, realr, imagr, bval
        ComplexNumber ltotc = new ComplexNumber(0.0, 0.0);
        ComplexNumber rtotc = new ComplexNumber(0.0, 0.0);

        for(int j = 0; j < this.lchannel.size(); j++) {
            for (int i = 0; i < this.lchannel.size(); i++) {
                bval = (2*PI*i*j)/this.lchannel.size();

                reall = this.lchannel.get(j) * Math.cos(bval);
                imagl = this.lchannel.get(j) * Math.sin(bval);
                realr = this.rchannel.get(j) * Math.cos(bval);
                imagr = this.rchannel.get(j) * Math.sin(bval);

                ComplexNumber leftC = new ComplexNumber(reall, imagl);
                ComplexNumber rightC = new ComplexNumber(realr, imagr);

                ltotc = leftC.Sum(ltotc);
                rtotc = rightC.Sum(rtotc);
            }


        }


        return -1; // change this
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
        // TODO: Implement this method
        return true; // change this
    }

    /**
     * Determine the similarity between this wave and another wave.
     * The similarity metric, gamma, is the sum of squares of
     * instantaneous differences.
     *
     * @param other is not null.
     * @return the similarity between this wave and other.
     */
    public double similarity(SoundWave other) {
        // TODO: Implement this method
        return -1;
    }

    /**
     * Play this wave on the standard stereo device.
     */
    public void sendToStereoSpeaker() {
        // You may not need to change this...
        double[] lchannel = this.lchannel.stream().mapToDouble(x -> x.doubleValue()).toArray();
        double[] rchannel = this.rchannel.stream().mapToDouble(x -> x.doubleValue()).toArray();
        StdPlayer.playWave(lchannel, rchannel);
    }

}