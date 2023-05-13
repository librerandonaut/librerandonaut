package com.github.librerandonaut.librerandonaut.randomness;

import android.content.ContextWrapper;
import android.net.Uri;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FileEntropyManager implements IEntropyManager {
    static final String TAG = "FileEntropyManager";
    private Uri fileUri;
    private ContextWrapper context;

    public FileEntropyManager(Uri fileUri, ContextWrapper context) {
        this.fileUri = fileUri;
        this.context = context;
    }

    private byte[] openPath(Uri uri){
        byte[] fileData = null;
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);

            fileData = new byte[is.available()];
            is.read(fileData);

            is.close();

        } catch (FileNotFoundException e) {
            Log.w(TAG, e);
        } catch (IOException e) {
            Log.w(TAG, e);
        }
        Log.w(TAG, "length=" + String.valueOf(fileData.length));
        Log.w(TAG, "hashCode=" + String.valueOf(Arrays.hashCode(fileData)));
        return fileData;
    }

    @Override
    public IRandomProvider loadRandomProvider(int entropyUsage) throws Exception {
        return new RandomProvider(openPath(fileUri), RandomSource.File);
    }
}
