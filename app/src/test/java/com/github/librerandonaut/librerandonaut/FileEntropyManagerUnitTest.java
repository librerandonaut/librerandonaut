/*
package com.github.librerandonaut.librerandonaut;

import org.junit.Test;
import com.github.librerandonaut.librerandonaut.randomness.FileEntropyManager;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileEntropyManagerUnitTest {
    @Test
    public void hasEntropyLeft_withFreshEntropy_isCorrect() throws Exception {
        byte[] content = new byte[100];
        FileEntropyManager manager = new FileEntropyManager(content);
        manager.init(0, 0);
        assertTrue(manager.hasEntropyLeft(1));
        assertTrue(manager.hasEntropyLeft(10));
        assertTrue(manager.hasEntropyLeft(100));

        assertFalse(manager.hasEntropyLeft(101));
        assertFalse(manager.hasEntropyLeft(1010));
    }

    @Test
    public void hasEntropyLeft_withUsedEntropy_isCorrect() throws Exception {
        byte[] content = new byte[100];
        FileEntropyManager manager = new FileEntropyManager(content);
        manager.init(50, 0);
        assertTrue(manager.hasEntropyLeft(1));
        assertTrue(manager.hasEntropyLeft(10));
        assertTrue(manager.hasEntropyLeft(50));

        assertFalse(manager.hasEntropyLeft(51));
        assertFalse(manager.hasEntropyLeft(510));
    }
}
*/
