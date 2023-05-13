package com.github.librerandonaut.librerandonaut.randomness;

import java.util.Arrays;

public class RandomProvider implements IRandomProvider {
    private int byteIndex = 0;
    private byte[] data;
    private RandomSource randomSource;

    public int getByteIndex() {
        return byteIndex;
    }
    public void setByteIndex(int value) {
        byteIndex = value;
    }
    public int getEntropyPoolSize() {
        return data.length;
    }
    public RandomSource getRandomSource() {
        return randomSource;
    }

    @Override
    public boolean hasEntropyLeft(int entropyUsage) {
        return (data.length - byteIndex) >= entropyUsage;
    }

    public RandomProvider(byte[] data, RandomSource randomSource) {
        this.data = data;
        this.randomSource = randomSource;
    }

    public double sample() throws Exception {
        return internalSample()*(1.0 / Integer.MAX_VALUE);
    }

    public int nextInt() throws Exception {
        return internalSample();
    }

    public double nextDouble() throws Exception {
        byte[] arr = {nextByte(), nextByte(), nextByte(), nextByte(), nextByte(), nextByte(), nextByte(), nextByte()};
        return ByteArrayHandler.toDouble(arr);
    }

    private int internalSample() throws Exception {
        byte[] arr = {nextByte(), nextByte(), nextByte(), nextByte()};
        int ret = ByteArrayHandler.toInt(arr);

        if (ret == Integer.MAX_VALUE)
            ret--;

        if (ret < 0)
            ret += Integer.MAX_VALUE;

        return ret;
    }

    public int nextInt(int maxValue) throws Exception {
        return (int)(sample() * maxValue);
    }

    public int nextUnsignedByte() throws Exception {
        return nextByte() & 0xFF;
    }

    public byte nextByte() throws Exception {
        if(byteIndex >= data.length)
            throw new Exception("Entropy size exceeded. Size = " + data.length);

        return data[byteIndex++];
    }

    public String getUsedEntropyAsString(int byteIndex, int size) {
        if (data.length == 0)
            return "";

        char[] hexChars = new char[size * 3];
        int charIndex = 0;
        for (int j = byteIndex; j < Math.min(byteIndex + size, data.length); j++) {
            int v = data[j] & 0xFF;
            hexChars[charIndex * 3] = HEX_ARRAY[v >>> 4];
            hexChars[charIndex * 3 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[charIndex * 3 + 2] = ' ';
            charIndex++;
        }

        return new String(hexChars);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public int getHashCode() {
        return Arrays.hashCode(data);
    }
}
