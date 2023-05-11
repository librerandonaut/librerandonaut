package com.librerandonaut.librerandonaut.randomness;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import com.librerandonaut.librerandonaut.rngdevice.IProgressHandler;

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
