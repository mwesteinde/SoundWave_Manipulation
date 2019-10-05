package ca.ubc.ece.cpen221.mp1;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicTests {

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

    @Test
    public void testContainsWithZeroBoundary(){
        double[] lchannel = {0.0, 0.2, 0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9, 1.0};
        double[] rchannel = {0.0, 0.6, 0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9, -1};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9};
        double[] rchannel2 = {0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertEquals(true, result);
    }

    @Test
    public void testContainsWithDiffLRTrue(){
        double[] lchannel = {-1, -0.2, -0.3, -0.6, 0.4, -0.5, 0.6, 0.7, 0.8, 0.9, 1, -1};
        double[] rchannel = {0.2, 0.3, 0.5, 0.7, 0.10, 1, -1, -1, -1, -1, 0.8, 0.4};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {-0.2, -0.3, -0.6, 0.4};
        double[] rchannel2 = {0.3, 0.5, 0.7, 0.10};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertEquals(true, result);
    }

    @Test
    public void testContainsContainsWithDiffLRFalse(){
        double[] lchannel = {-1, -0.2, -0.3, -0.6, 0.4, -0.5, 0.6, 0.7, 0.8, 0.9, 1, -1};
        double[] rchannel = {0.2, 0.3, 0.5, 0.7, 0.10, 1, -1, -1, -1, -1, 0.8, 0.4};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {-0.2, -0.3, -0.6, 0.4};
        double[] rchannel2 = {0.3, 0.5, 0.7, 0.2};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertEquals(false, result);
    }

    @Test
    public void testContainsJustFalse(){
        double[] lchannel = {.0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        double[] rchannel = {.0, .0, .0, .0, 1, .0, .0, .0, .02, .0, .0, .0, .0, .0, .0, .0};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {0.0, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8, 0.9};
        double[] rchannel2 = {0.0, 0.2, 0.8, 0.1, 0.1, 1 ,-0.4, 0.6, -0.9, -0.6, 0.9};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        boolean result = outer.contains(inner);
        assertEquals(false, result);
    }

    @Test
    public void testContainsZerosWithNonZeros(){
        double[] lchannel = {.0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0, .0};
        double[] rchannel = {.0, .0, .0, .0, 1, .0, .0, .0, .02, .0, .0, .0, .0, .0, .0, .0};
        SoundWave outer = new SoundWave(lchannel, rchannel);
        double[] lchannel2 = {.0, .0, .0, .0, .0, .0};
        double[] rchannel2 = {.0, 1, .0, .0, .0, .02};
        SoundWave inner = new SoundWave(lchannel2, rchannel2);

        inner.scale(0.6);
        boolean result = outer.contains(inner);
        assertEquals(true, result);
    }

    public void testEcho() {
        double[] lchannelo = {1.0, 1.5, -1, 0.75, 0.86};
        double[] rchannelo = {3.5, 6.7, 1, 0.5, -0.9};
        double[] lchannele = {1.0, 1.0, -.5, 1.0, 0.36};
        double[] rchannele = {1.0, 1.0, 1.0, 1.0, -0.4};
        SoundWave wave = new SoundWave(lchannelo, rchannelo);
        SoundWave testecho = wave.addEcho(2, 0.5);
        Assert.assertArrayEquals(lchannele, testecho.getLeftChannel(), 0.00001);
        Assert.assertArrayEquals(rchannele, testecho.getRightChannel(), 0.00001);
    }

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

    @Test
    public void testHighAmplitudeFreqComponent1() {
        SoundWave a = new SoundWave(200, 0, .5, .5);
        SoundWave b = new SoundWave(758, 0, 0.5, .5);
        SoundWave d = new SoundWave(300, 0, 0.5, .5);
        SoundWave c = new SoundWave(100, 0, 0.5, .5);
        SoundWave e = new SoundWave(48, 5, 0.9, .5);
        SoundWave f = new SoundWave(102, 10, 0.9, .5);
        SoundWave g = new SoundWave(50, 0, 0.5, .5);
        SoundWave merge = new SoundWave();
        /**merge = a.add(b);
         merge = merge.add(c);*/
        merge = merge.add(d);
        merge = merge.add(e);
        merge = merge.add(f);
        merge = merge.add(g);
        double frequency = merge.highAmplitudeFreqComponent();
        assertEquals(102, frequency, 1);
    }

    @Test
    public void testMagnitude() {
        double i = 6574.6;
        double r = 85.6;
        ComplexNumber test = new ComplexNumber(r, i);
        double result = test.Magnitude();
        assertEquals(result, 6575.15722, 0.1);
    }

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
        ComplexNumber result = new ComplexNumber(resulti, resultr);
        ComplexNumber actual = test1.Sum(test2);

        assertEquals(actual.ival, resulti, 0.01);
        assertEquals(actual.rval, resultr, 0.01);
    }



}
