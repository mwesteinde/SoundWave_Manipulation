package ca.ubc.ece.cpen221.mp1;


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicTests {

    /**  creates a wave and ensures it is correctly initialized **/
    @Test
    public void testCreateWave() {
        double[] lchannel = {1.0, -1.0};
        double[] rchannel = {1.0, -1.0};
        SoundWave wave = new SoundWave(lchannel, rchannel);
        double[] lchannel1 = wave.getLeftChannel();
        Assert.assertArrayEquals(lchannel, lchannel1, 0.00001);
        double[] rchannel1 = wave.getRightChannel();
        Assert.assertArrayEquals(rchannel, rchannel1, 0.00001);
    }

    /** tests our append zeros to make L and R equal lengths **/
    @Test
    public void testCreateWaveLLessR() {
        double[] lchannel = {1.0, -1.0};
        double[] rchannel = {1.0, -1.0, 2.0, 1.5};
        SoundWave wave = new SoundWave(lchannel, rchannel);
        double[] lchannelZ = {1.0, -1.0, 0.0, 0.0};
        double[] rchannelZ = {1.0, -1.0, 2.0, 1.5};
        SoundWave wavez = new SoundWave(lchannelZ, rchannelZ);

        double[] lchannel1 = wave.getRightChannel();
        double[] lchannelz = wavez.getRightChannel();
        Assert.assertArrayEquals(lchannel1, lchannelz, 0.00001);
        double[] rchannel1 = wave.getRightChannel();
        double[] rchannelz = wavez.getRightChannel();
        Assert.assertArrayEquals(rchannel1, rchannelz, 0.00001);
    }

    /** tests our append zeros to make L and R equal lengths **/
    @Test
    public void testCreateWaveRlessL() {
        double[] lchannel = {1.0, -1.0, 1.0, 1, 1, 0};
        double[] rchannel = {1.0, -1.0, 1.0, 1};
        SoundWave wave = new SoundWave(lchannel, rchannel);
        double[] lchannelZ = {1.0, -1.0, 1, 1, 1, 0};
        double[] rchannelZ = {1.0, -1.0, 1, 1, 0, 0};
        SoundWave wavez = new SoundWave(lchannelZ, rchannelZ);

        double[] lchannel1 = wave.getRightChannel();
        double[] lchannelz = wavez.getRightChannel();
        Assert.assertArrayEquals(lchannel1, lchannelz, 0.00001);
        double[] rchannel1 = wave.getRightChannel();
        double[] rchannelz = wavez.getRightChannel();
        Assert.assertArrayEquals(rchannel1, rchannelz, 0.00001);
    }

    /** tests the edge case of dividing by zero for finding beta in our contains function **/
    @Test
    public void testContainsWithZeroBoundary(){
        double[] lchannel = {0.0, 0.2, 0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9, 1.0};
        double[] rchannel = {0.0, 0.6, 0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9, -1};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9};
        double[] rchannel2 = {0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertTrue(result);
    }

    /** tests the edge case of scaling a wave past 1, -1 -
     * our trimming should result in the wave no linger being contained **/
    @Test
    public void testContainsIllegalWaveLargeScale(){
        double[] lchannel = {0.0, 0.2, 0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9, 1.0};
        double[] rchannel = {0.0, 0.6, 0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9, -1};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9};
        double[] rchannel2 = {0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);
        inner.scale(1.5);

        boolean result = outer.contains(inner);
        assertFalse(result);
    }

    /** tests arbitrary scaling factor for contained wave **/
    @Test
    public void testContainsWithDiffLRTrue(){
        double[] lchannel = { 0.7, 0.8, 0.9, 1, -1};
        double[] rchannel = {-1, -1, -1, 0.8, 0.4};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {1, -1};
        double[] rchannel2 = {0.8, 0.4};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);
        inner.scale(.2);
        boolean result = outer.contains(inner);
        assertTrue(result);
    }

    /** tests wave not contained in outer **/
    @Test
    public void testContainsContainsWithDiffLRFalse(){
        double[] lchannel = {-1, -0.2, -0.3, -0.6, 0.4, -0.5, 0.6, 0.7, 0.8, 0.9, 1, -1};
        double[] rchannel = {0.2, 0.3, 0.5, 0.7, 0.10, 1, -1, -1, -1, -1, 0.8, 0.4};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {-0.2, -0.3, -0.6, 0.4};
        double[] rchannel2 = {0.3, 0.5, 0.7, 0.2};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertFalse(result);
    }

    /** tests false answer with weird zero cases **/
    @Test
    public void testContainsJustFalse(){
        double[] lchannel = {.0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        double[] rchannel = {.0, .0, .0, .0, 1, .0, .0, .0, .02, .0, .0, .0, .0, .0, .0, .0};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9};
        double[] rchannel2 = {0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertFalse(result);
    }

    /** tests finding first non zero values to scale wave in right channels **/
    @Test
    public void testContainsZerosWithNonZerosRR(){
        double[] lchannel = {.0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        double[] rchannel = {.0, .0, .0, .0, 1, .0, .0, .0, .02, .0, .0, .0, .0, .0, .0, .0};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {.0, .0, .0, .0, .0, .0};
        double[] rchannel2 = {.0, 1, .0, .0, .0, .02};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        inner.scale(0.6);
        boolean result = outer.contains(inner);
        assertTrue(result);
    }

    /** tests finding first non zero values to scale wave in left channels **/
    @Test
    public void testContainsZerosWithNonZerosLL(){
        double[] lchannel = {.0, .0, .0, .0, 1.0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        double[] rchannel = {.0, .0, .0, .0, 0.0, .0, .0, .0, .02, .0, .0, .0, .0, .0, .0, .0};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {.0, 1, .0, .0, .0, .0};
        double[] rchannel2 = {.0, 0, .0, .0, .0, .02};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        inner.scale(0.6);
        boolean result = outer.contains(inner);
        assertTrue(result);
    }

    /** all zeros fringe case **/
    @Test
    public void testContainsZerosWithAllZeros(){
        double[] lchannel = {.0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        double[] rchannel = {.0, .0, .0, .0, 0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {.0, .0, .0, .0, .0, .0};
        double[] rchannel2 = {.0, 0, .0, .0, .0, .0};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        inner.scale(0.6);
        boolean result = outer.contains(inner);
        assertTrue(result);
    }

    /** tests echo **/
    @Test
    public void testEcho() {
        double[] lchannelo = {1.0, 1.5, -1, 0.75, 0.86};
        double[] rchannelo = {3.5, 6.7, 1, 0.5, -0.9};
        double[] lchannele = {1.0, 1.0, -.5, 1.0, 0.36, 0.375, 0.43};
        double[] rchannele = {1.0, 1.0, 1.0, 1.0, -0.4, 0.25, -.45};
        SoundWave wave = new SoundWave(lchannelo, rchannelo);
        SoundWave testecho = wave.addEcho(2, 0.5);
        Assert.assertArrayEquals(lchannele, testecho.getLeftChannel(), 0.00001);
        Assert.assertArrayEquals(rchannele, testecho.getRightChannel(), 0.00001);
    }

    /** Adds a whole ton of waves together with trimming generates output **/
    @Test
    public void testAdd() {
        double[] lchannelo1 = {1.0, 56.0, 0.05, 0.5, -0.5, -1, 0.6};
        double[] rchannelo1 = {0.05, -56.2, 0.1, -0.02, 0.5, 1, -0.02};
        double[] lchannelo2 = {56.0, 0.05, 0.5, -0.5, -1, 0.6};
        double[] rchannelo2 = {0.05, 56.2, 0.1, -0.02, 0.5, 1};
        double[] lchannelo3 = {-57.2, 0.65, 0.05, 0.05, -0.8, -1, 0.6};
        double[] rchannelo3 = {0.05, 0.7, 0.1, -0.02, 0.5, 1, 0.08};
        double[] lchannelresult = {-1, 1, 0.6, 0.05, -1, -1, 1};
        double[] rchannelresult = {0.15, 0.7, 0.3, -.06, 1, 1, 0.06};
        SoundWave a = new SoundWave(lchannelo1, rchannelo1);
        SoundWave b = new SoundWave(lchannelo2, rchannelo2);
        SoundWave c = new SoundWave(lchannelo3, rchannelo3);
        SoundWave merge = a.add(b);
        merge = merge.add(c);
        double[] lchannelfinal = merge.getLeftChannel();
        double[] rchannelfinal = merge.getRightChannel();
        Assert.assertArrayEquals(lchannelresult, lchannelfinal, 0.00001);
        Assert.assertArrayEquals(rchannelresult, rchannelfinal, 0.00001);
    }

    /** tests append **/
    @Test
    public void testAppend() {
        double[] lchannelo1 = {1.0, 56.0, 0.05, 0.5, -0.5, -1, 0.6};
        double[] rchannelo1 = {0.05, -56.2, 0.1, -0.02, 0.5, 1, -0.02};
        double[] lchannelo2 = {56.0, 0.05, 0.5, -0.5, -1, 0.6};
        double[] rchannelo2 = {0.05, 56.2, 0.1, -0.02, 0.5, 1};
        double[] lchannelo3 = {-57.2, 0.65, 0.05, 0.05, -0.8, -1, 0.6};
        double[] rchannelo3 = {0.05, 0.7, 0.1, -0.02, 0.5, 1, 0.08};
        double[] lchannelresult = {1.0, 56.0, 0.05, 0.5, -0.5, -1, 0.6, 56.0, 0.05, 0.5, -0.5, -1, 0.6, -57.2, 0.65, 0.05, 0.05, -0.8, -1, 0.6};
        double[] rchannelresult = {0.05, -56.2, 0.1, -0.02, 0.5, 1, -0.02, 0.05, 56.2, 0.1, -0.02, 0.5, 1, 0.05, 0.7, 0.1, -0.02, 0.5, 1, 0.08};
        SoundWave a = new SoundWave(lchannelo1, rchannelo1);
        SoundWave b = new SoundWave(lchannelo2, rchannelo2);
        SoundWave c = new SoundWave(lchannelo3, rchannelo3);
        a.append(b);
        a.append(c);
        Assert.assertArrayEquals(a.getLeftChannel(), lchannelresult, 0.00001);
        Assert.assertArrayEquals(a.getRightChannel(), rchannelresult, 0.00001);
    }

    /** tests scaling with arbitrary val **/
    @Test
    public void testScale1() {
        double[] lchannelo1 = {.06, -0.6, 0.79, 1, -1, -.001, 0};
        double[] rchannelo1 = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.02};
        double[] lchannelresult = {0.03, -0.3, 0.395, 0.5, -0.5, -0.0005, 0};
        double[] rchannelresult = {0.025, 0.38, 0.05, -0.01, 0.25, 0.5, -0.01};
        SoundWave a = new SoundWave(lchannelo1, rchannelo1);
        a.scale(0.5);
        Assert.assertArrayEquals(a.getLeftChannel(), lchannelresult, 0.00001);
        Assert.assertArrayEquals(a.getRightChannel(), rchannelresult, 0.00001);
    }

    /** tests scaling with arbitrary val **/
    @Test
    public void testScale2() {
        double[] lchannelo1 = {.06, -0.6, 0.79, 1, -1, -.001, 0};
        double[] rchannelo1 = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.02};
        double[] lchannelresult = {0.18, -1, 1, 1, -1, -0.003, 0};
        double[] rchannelresult = {0.15, 1, 0.3, -0.06, 1, 1, -0.06};
        SoundWave a = new SoundWave(lchannelo1, rchannelo1);
        a.scale(3);
        Assert.assertArrayEquals(a.getLeftChannel(), lchannelresult, 0.00001);
        Assert.assertArrayEquals(a.getRightChannel(), rchannelresult, 0.00001);
    }

    /** tests filter **/
    @Test
    public void testHighPassFilter() {
        double[] lchannelo1 = {0.03, -0.3, 0.395, 0.5, -0.5, -0.0005, 0};
        double[] rchannelo1 = {0.05, 0.7, 0.1, -0.02, 0.5, 1, 0.08};
        double[] lchannelresult = {0.03, -0.13333333333, 0.2496296, 0.1576131556, -.37439415, 0.0556025, 0.0249344884};
        double[] rchannelresult = {0.05, 0.3111111111, -0.12839, -0.110397805, 0.1820454199055022, 0.303131297, -0.274163867672987};
        SoundWave a = new SoundWave(lchannelo1, rchannelo1);
        SoundWave b = a.highPassFilter(5, 4.0);
        Assert.assertArrayEquals(lchannelresult, b.getLeftChannel(), 0.00001);
        Assert.assertArrayEquals(rchannelresult, b.getRightChannel(), 0.00001);
    }

    /** basic DFT test of one wave **/
    @Test
    public void testHighAmplitudeFreqComponent1() {
        SoundWave a = new SoundWave(200, 0, .5, .5);
        double frequency = a.highAmplitudeFreqComponent();
        assertEquals(200, frequency, 1);
    }

    /** Testing DFT with tight amplitude diff tolerances**/
    @Test
    public void testHighAmplitudeFreqComponent2() {
        SoundWave a = new SoundWave(23, 0, 0.2, .5);
        SoundWave b = new SoundWave(100, 0, 0.21, .5);
        SoundWave merge = b.add(a);

        double frequency = merge.highAmplitudeFreqComponent();
        assertEquals(100, frequency, 1);
    }

    /** Tight tolerance dft 3 waves **/
    @Test
    public void testHighAmplitudeFreqComponent3(){
        SoundWave w1 = new SoundWave(40,0,0.2,.33);
        SoundWave w2 = new SoundWave(60,0,0.2,.33);
        SoundWave w3 = new SoundWave(20,0,0.2,.33);

        SoundWave w4 = w3.add(w1).add(w2);

        double a = w4.highAmplitudeFreqComponent();
        assertEquals(60, a, 1);

    }

    @Test
    public void testHighAmplitudeFreqComponent4(){
        SoundWave w1 = new SoundWave(201,0,0.6,.02);
        SoundWave w2 = new SoundWave(1552,0,0.4,.02);

        SoundWave w4 = (w1).add(w2);

        double a = w4.highAmplitudeFreqComponent();
        assertEquals(200, a, 1);

    }

    /** Complex number implementation test **/
    @Test
    public void testMagnitude() {
        double i = 6574.6;
        double r = 85.6;
        ComplexNumber test = new ComplexNumber(r, i);
        double result = test.Magnitude();
        assertEquals(result, 6575.15722, 0.0001);
    }

    /** Complex number implementation test **/
    @Test
    public void testSum() {
        double i1 = 6574.6;
        double r1 = 85.6;
        double i2 = 345.5;
        double r2 = 23445.5;
        double resulti = 6920.1;
        double resultr = 23531.1;

        ComplexNumber test1 = new ComplexNumber(r1, i1);
        ComplexNumber test2 = new ComplexNumber(r2, i2);
        ComplexNumber actual = test1.Sum(test2);

        assertEquals(actual.ival, resulti, 0.00000000000001);
        assertEquals(actual.rval, resultr, 0.00000000000001);
    }

    /** Checks exactly equal waves with diff amplitudes to check beta calculations **/
    @Test
    public void testSimilarity1() {
        SoundWave d = new SoundWave(500, 0, 0.9, .5);
        SoundWave e = new SoundWave(500, 0, 0.1, .5);
        double result = e.similarity(d);
        assertEquals(1.0, result, 0.0001);
    }

    /** ensures extra zero values do not impact calculation **/
    @Test
    public void testSimilarity2() {
        SoundWave d = new SoundWave(500, 0, 0.9, .5);
        SoundWave e = new SoundWave(500, 0, 0.9, .5);
        double[] lchannel = {0, 0.0, 0.0};
        double[] rchannel = {0, 0.0, 0.0};
        SoundWave f = new SoundWave(lchannel, rchannel);
        e.append(f);
        double result = d.similarity(e);
        assertEquals(1.0, result, 0.0001);
    }

    /** hodgepodge of waves with pre-calced gamma **/
    @Test
    public void testSimilarity3() {
        double[] lchannelo1 = {.06, -0.6, 0.79, 1, -1, -.001, 0};
        double[] rchannelo1 = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.02};
        double[] lchannelresult = {.06, -0.6, 0.79, 1, -1, -.001, 0};
        double[] rchannelresult = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.02};
        SoundWave d = new SoundWave(lchannelo1, rchannelo1);
        SoundWave e = new SoundWave(lchannelresult,rchannelresult);
        double result = e.similarity(d);
        assertEquals(1.0, result, 0.0001);
    }

    /** hodgepodge of waves with pre-calced gamma one similarity should be greater than the other **/
    @Test
    public void testSimilarity4() {
        double[] lchannelo1 = {.06, -0.6, 0.79, 1, -1, -.001, 0};
        double[] rchannelo1 = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.02};
        double[] lchannelo2 = {.06, -0.6, 0.79, 1, -1, -.001, 0.1};
        double[] rchannelo2 = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.03};
        double[] lchannelo3 = {.06, -0.6, 0.79, 1, -1, -.001, 0};
        double[] rchannelo3 = {0.05, 0.76, 0.1, -0.02, 0.5, 1, -0.09};
        SoundWave d = new SoundWave(lchannelo1, rchannelo1);
        SoundWave e = new SoundWave(lchannelo2,rchannelo2);
        SoundWave f = new SoundWave(lchannelo3,rchannelo3);
        double result1 = e.similarity(d);
        double result2 = e.similarity(f);

        assertTrue((result1 > result2));
    }

    /** hodgepodge of waves with pre-calced gamma **/
    @Test
    public void testSimilarity5() {
        double[] lchannelo1 = {.06, -0.3, 0.79};
        double[] rchannelo1 = {0.05, 0.76, 0.1};
        double[] lchannelo2 = {.06, -0.6, 0.79};
        double[] rchannelo2 = {0.25, 0.36, 0.5};
        SoundWave d = new SoundWave(lchannelo1, rchannelo1);
        SoundWave e = new SoundWave(lchannelo2,rchannelo2);
        double result1 = e.similarity(d);

        assertEquals(0.7094295, result1, 0.001);
    }

    @Test
    public void testSimilarity6() {
        double[] lchannelo1 = {0.0, 0.0, 0.0};
        double[] rchannelo1 = {0.0, 0.0, 0.0};
        double[] lchannelo2 = {.06, -0.6, 0.79};
        double[] rchannelo2 = {0.25, 0.36, 0.5};
        SoundWave d = new SoundWave(lchannelo1, rchannelo1);
        SoundWave e = new SoundWave(lchannelo2,rchannelo2);
        double result1 = e.similarity(d);

        assertEquals(0, result1, 0.001);
    }

    @Test
    public void testSimilarity7() {
        double[] lchannelo1 = {0.0, 0.0, 0.0};
        double[] rchannelo1 = {0.0, 0.0, 0.0};
        double[] lchannelo2 = {0.0, 0.0, 0.0};
        double[] rchannelo2 = {0.0, 0.0, 0.0};
        SoundWave d = new SoundWave(lchannelo1, rchannelo1);
        SoundWave e = new SoundWave(lchannelo2,rchannelo2);
        double result1 = e.similarity(d);

        assertEquals(1, result1, 0.001);
    }

    @Test
    public void testGetBeta() {
        double[] lchannelo1 = {.06, -0.3, 0.79};
        double[] rchannelo1 = {0.05, 0.76, 0.1};
        double[] lchannelo2 = {.06, -0.6, 0.79};
        double[] rchannelo2 = {0.25, 0.36, 0.5};
        SoundWave d = new SoundWave(lchannelo1, rchannelo1);
        SoundWave e = new SoundWave(lchannelo2,rchannelo2);
        double result1 = d.getBeta(e);

        assertEquals(0.79997202, result1, 0.0001);
    }
}
