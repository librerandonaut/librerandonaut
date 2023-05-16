package com.github.librerandonaut.librerandonaut.randomness;

import android.util.Log;

import com.github.librerandonaut.librerandonaut.rngdevice.IProgressHandler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;

// TODO Add support for random.org. Request data directly from them.
public class RandomDotOrgEntropyManager implements IEntropyManager {
    static final String TAG = "RandomDotOrgEntropyManager";
    private final int REQUEST_ENTROPY_MAX_SIZE = 1000000 / 8;   // One million bits
    private final int BUFFER_SIZE = 100;
    private final String DOWNLOAD_URL = "https://www.random.org/cgi-bin/randbyte?nbytes=%s&format=f";
    private final String EMAIL_ADDRESS = "librerandonaut@protonmail.com";
    private IProgressHandler progressHandler;

    public RandomDotOrgEntropyManager(IProgressHandler progressHandler) {
        this.progressHandler = progressHandler;
    }

    private void updateProgress(IProgressHandler progressHandler, int percent) {
        if(progressHandler != null) {
            progressHandler.updateProgress(percent);
        }
    }

    @Override
    public LoadRandomProviderResult loadRandomProvider(int entropyUsage) throws Exception {
        // TODO: Track total entropy usage
        if (entropyUsage > REQUEST_ENTROPY_MAX_SIZE)
            throw new Exception("entropyUsage " + entropyUsage + " exceeding REQUEST_ENTROPY_MAX_SIZE " + REQUEST_ENTROPY_MAX_SIZE);

        byte[] entropy = new byte[entropyUsage];
        byte[] buffer = new byte[BUFFER_SIZE];
        updateProgress(progressHandler, 0);
        String urlString = String.format(DOWNLOAD_URL, entropyUsage);
        Log.v(TAG, urlString);
        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("User-Agent", EMAIL_ADDRESS);

        if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            int contentLength = httpConn.getContentLength();
            int code = httpConn.getResponseCode();
            String message = httpConn.getResponseMessage();
            Log.v(TAG, String.format("Server returned: %s %s, contentLength: %s", code, message, contentLength));

            InputStream responseStream = httpConn.getInputStream();

            int byteReadTotal = 0;
            int bytesRead = -1;
            while ((bytesRead = responseStream.read(buffer)) != -1) {
                System.arraycopy(buffer, 0, entropy, byteReadTotal, bytesRead);
                byteReadTotal += bytesRead;
                updateProgress(progressHandler, (int)((double)byteReadTotal / (double)entropyUsage * 100));
                buffer = new byte[BUFFER_SIZE];
            }

            return new LoadRandomProviderResult(new RandomProvider(entropy, RandomSource.RandomDotOrg), true, message);
        }
        else
        {
            int code = httpConn.getResponseCode();
            String message = String.format("Server returned error: %s %s", code, httpConn.getResponseMessage());
            Log.v(TAG, message);
            return new LoadRandomProviderResult(null, false, message);
        }
    }
}
