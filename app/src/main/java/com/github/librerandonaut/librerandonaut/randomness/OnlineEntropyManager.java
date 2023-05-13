package com.github.librerandonaut.librerandonaut.randomness;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.github.librerandonaut.librerandonaut.rngdevice.IProgressHandler;

// TODO Add support for random.org. Request data directly from them.
// TODO Rename to AnuEntropyManager
public class OnlineEntropyManager implements IEntropyManager {
    static final String TAG = "OnlineEntropyManager";

    private final int REQUEST_ENTROPY_SIZE = 512;
    private final int REQUEST_ENTROPY_MAX_SIZE = 16000;

    private static String download(URL urlString) throws Exception {
        try (InputStream input = urlString.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        }
    }
    private IProgressHandler progressHandler;

    public OnlineEntropyManager(IProgressHandler progressHandler) {
        this.progressHandler = progressHandler;
    }

    private void updateProgress(IProgressHandler progressHandler, int percent) {
        if(progressHandler != null) {
            progressHandler.updateProcess(percent);
        }
    }

    @Override
    public IRandomProvider loadRandomProvider(int entropyUsage) throws Exception {
        if (entropyUsage > REQUEST_ENTROPY_MAX_SIZE)
            throw new Exception("entropyUsage " + entropyUsage + " exceeding REQUEST_ENTROPY_MAX_SIZE " + REQUEST_ENTROPY_MAX_SIZE);

        int bufferSize = Math.min(entropyUsage, REQUEST_ENTROPY_SIZE);
        int iterations = entropyUsage > REQUEST_ENTROPY_SIZE ? (int)Math.ceil((double)entropyUsage / (double)REQUEST_ENTROPY_SIZE) : 1;
        Log.v(TAG, "iterations = " + iterations);

        byte[] entropy = new byte[bufferSize * iterations];

        String url = "https://qrng.anu.edu.au/API/jsonI.php?length=" + REQUEST_ENTROPY_SIZE + "&type=hex16&size=1";

        updateProgress(progressHandler, 0);
        for(int j = 1; j <= iterations; j++)
        {
            if(j > 1) {
                // TODO it's 60s according to the error message, but it was never enough. find our, what the minimum wait time is.
                Thread.sleep(120000);
                // ANU changed their policy to one request per minute sadly.
                // Generating attactors for 1000m radius requires 100 points.
                // 100 points need 800 bytes of entropy.
                // 800 bytes of entropy needs 2 requests to ANU, that means an additional minute of wait time.
                // An alternative to ANU would be downloading a large file of random data from random.org and using that with the "File" option.
            }
            // TODO when there is an error with downloading the entropy, display the error in the UI
            String jsonString = download(new URL(url));
            JSONObject jsonObject = new JSONObject(jsonString);

            String success = jsonObject.getString("success");
            if( success == "true") {
                JSONArray array = jsonObject.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {
                    String itemString = array.get(i).toString();
                    byte itemByte = (byte) ((Character.digit(itemString.charAt(0), 16) << 4)
                            + Character.digit(itemString.charAt(1), 16));
                    entropy[i + ((j-1) * bufferSize)] = itemByte;
                }

                updateProgress(progressHandler, (int)((double)j / (double)iterations * 100));
            } else {
                Log.v(TAG, "ANU returned success = false after " +  j + " of " + iterations + " iterations.");
                return null;
            }
        }
        updateProgress(progressHandler, 100);
        return new RandomProvider(entropy, RandomSource.Anu);
    }
}
