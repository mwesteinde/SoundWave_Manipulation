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

}
