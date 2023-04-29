package com.librerandonaut.librerandonaut;

import org.junit.Before;
import org.junit.Test;

import com.librerandonaut.librerandonaut.randomness.ByteArrayHandler;
import com.librerandonaut.librerandonaut.randomness.RandomProvider;
import com.librerandonaut.librerandonaut.randomness.RandomSource;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RandomProviderUnitTest {
    RandomProvider randomProvider;
    private final byte[] content = new byte[]{
            0x4d, (byte)0xb9, 0x6e, 0x79, (byte)0x91, (byte)0xf6, (byte)0xb0, 0x5a,
            (byte)0xf0, (byte)0x99, 0x71, (byte)0xc4, 0x21, 0x09, (byte)0xa4, (byte)0xfd,
            0x72, 0x56, (byte)0xaa, 0x49, 0x2a, 0x76, 0x05, (byte)0x93, 0x77, (byte)0x8a,
            (byte)0xdd, (byte)0xca, 0x7b, 0x7e, (byte)0xb0, (byte)0x95
    };

    @Before
    public void init() {
        randomProvider = new RandomProvider(content, RandomSource.File);
    }

    @Test
    public void nextByte_isCorrect() throws Exception {
        assertEquals(content[0], randomProvider.nextByte());
        assertEquals(content[1], randomProvider.nextByte());
        assertEquals(content[2], randomProvider.nextByte());
        assertEquals(content[3], randomProvider.nextByte());

        assertEquals(4, randomProvider.getByteIndex());
    }

    @Test
    public void nextUnsignedByte_isCorrect() throws Exception {
        assertEquals(content[0], randomProvider.nextUnsignedByte());
        assertEquals(content[1] & 0xFF, randomProvider.nextUnsignedByte());
        assertEquals(content[2], randomProvider.nextUnsignedByte());
        assertEquals(content[3], randomProvider.nextUnsignedByte());
        assertEquals(content[4] & 0xFF, randomProvider.nextUnsignedByte());

        assertEquals(5, randomProvider.getByteIndex());
    }

    @Test
    public void nextInteger_isCorrect() throws Exception {
        assertEquals(1303998073, randomProvider.nextInt());
        assertEquals(301379673, randomProvider.nextInt());
        assertEquals(1889104323, randomProvider.nextInt());
        assertEquals(554280189, randomProvider.nextInt());
        assertEquals(16, randomProvider.getByteIndex());
    }

    @Test
    public void nextInteger_withMaxValue_isCorrect() throws Exception {
        assertEquals(60, randomProvider.nextInt(100));
        assertEquals(14, randomProvider.nextInt(100));
        assertEquals(87, randomProvider.nextInt(100));
        assertEquals(25, randomProvider.nextInt(100));
    }

    @Test
    public void nextInteger_withSimulatedBytes_isCorrect() throws Exception {
        byte[] a = ByteArrayHandler.fromInt(Integer.MAX_VALUE / 2);
        randomProvider = new RandomProvider(a, RandomSource.System);

        assertEquals(49, randomProvider.nextInt(100));

        a = ByteArrayHandler.fromInt(Integer.MAX_VALUE);
        randomProvider = new RandomProvider(a, RandomSource.System);

        assertEquals(99, randomProvider.nextInt(100));

        a = ByteArrayHandler.fromInt(0);
        randomProvider = new RandomProvider(a, RandomSource.System);

        assertEquals(0, randomProvider.nextInt(100));
    }

    @Test
    public void nextDouble_isCorrect() throws Exception {
        assertEquals(2.6782540227721634E66, randomProvider.nextDouble(), 0);
        assertEquals(8, randomProvider.getByteIndex());
    }

    @Test
    public void nextSample_isCorrect() throws Exception {
        assertEquals(0.6072214216027508, randomProvider.sample(), 0);
        assertEquals(0.14034084656291682, randomProvider.sample(), 0);
        assertEquals(0.8796827513164295, randomProvider.sample(), 0);
        assertEquals(0.2581068264591074, randomProvider.sample(), 0);
    }

    @Test
    public void nextSample_withZero_isCorrect() throws Exception {
        byte[] a = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        assertArrayEquals(a, ByteArrayHandler.fromDouble(0));

        randomProvider = new RandomProvider(a, RandomSource.System);

        assertEquals(0, randomProvider.sample(), 0);
    }

    @Test
    public void nextSample_withMaxValue_isCorrect() throws Exception {
        byte[] a = ByteArrayHandler.fromInt(Integer.MAX_VALUE);
        randomProvider = new RandomProvider(a, RandomSource.System);

        assertEquals(0.9999999995343387, randomProvider.sample(), 0);
    }

    @Test
    public void compareZeroInt() {
        byte[] a = {0x0, 0x0, 0x0, 0x0};
        assertArrayEquals(a, ByteArrayHandler.fromInt(0));
    }

    @Test
    public void hasEntropyLeft_isCorrect() throws Exception {
        assertEquals(0, randomProvider.getByteIndex());
        assertEquals(content.length, randomProvider.getEntropyPoolSize());
        assertTrue(randomProvider.hasEntropyLeft(content.length));
        randomProvider.nextByte();
        assertEquals(1, randomProvider.getByteIndex());
        assertFalse(randomProvider.hasEntropyLeft(content.length));
        assertTrue(randomProvider.hasEntropyLeft(content.length-1));
        assertFalse(randomProvider.hasEntropyLeft(content.length+1));
    }

    @Test
    public void getHashCode_isCorrect() throws Exception {
        int hashCode = randomProvider.getHashCode();
        assertTrue(hashCode > 0);
        randomProvider.nextInt();
        assertEquals(hashCode, randomProvider.getHashCode());
    }
}