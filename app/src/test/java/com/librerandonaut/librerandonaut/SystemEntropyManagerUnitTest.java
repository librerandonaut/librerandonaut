package com.librerandonaut.librerandonaut;

import org.junit.Test;

import com.librerandonaut.librerandonaut.randomness.IRandomProvider;
import com.librerandonaut.librerandonaut.randomness.SystemEntropyManager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SystemEntropyManagerUnitTest {
    @Test
    public void nextInt_isCorrect() throws Exception {
        SystemEntropyManager manager = new SystemEntropyManager();
        IRandomProvider provider = manager.loadRandomProvider(Integer.MAX_VALUE);
        assertNotNull(provider);
        int value = provider.nextInt(100);
        assertTrue(0 <= value && value < 100);
    }
}
