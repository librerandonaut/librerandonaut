package com.github.librerandonaut.librerandonaut;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.librerandonaut.librerandonaut.randomness.LoadRandomProviderResult;
import com.github.librerandonaut.librerandonaut.randomness.RandomDotOrgEntropyManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.librerandonaut.librerandonaut.attractor.Attractor;
import com.github.librerandonaut.librerandonaut.attractor.AttractorGeneratorFactory;
import com.github.librerandonaut.librerandonaut.attractor.AttractorGeneratorType;
import com.github.librerandonaut.librerandonaut.attractor.Coordinates;
import com.github.librerandonaut.librerandonaut.attractor.IAttractorGenerator;
import com.github.librerandonaut.librerandonaut.attractor.RandomPointsProvider;
import com.github.librerandonaut.librerandonaut.randomness.DeviceEntropyManager;
import com.github.librerandonaut.librerandonaut.randomness.FileEntropyManager;
import com.github.librerandonaut.librerandonaut.randomness.IRandomProvider;
import com.github.librerandonaut.librerandonaut.randomness.AnuEntropyManager;
import com.github.librerandonaut.librerandonaut.randomness.RandomSource;
import com.github.librerandonaut.librerandonaut.randomness.SystemEntropyManager;
import com.github.librerandonaut.librerandonaut.rngdevice.DeviceHandler;
import com.github.librerandonaut.librerandonaut.rngdevice.IProgressHandler;

public class MainActivity extends AppCompatActivity implements LocationListener {
    static final String TAG = "MainActivity";
    private Button buttonGenerate;
    private Button buttonOpen;
    private TextView labelDevice;
    private TextView labelLocation;
    private TextView labelAttractor;
    private TextView labelAttractorData;
    private TextView labelRandomData;
    private RadioButton radioButtonAnu;
    private RadioButton radioButtonRandomDotOrg;
    private RadioButton radioButtonFile;
    private RadioButton radioButtonDevice;
    private RadioButton radioButtonSystem;
    private RadioButton radioButtonFatum;
    private RadioButton radioButtonGaussKde1;
    private RadioButton radioButtonGaussKde2;
    private EditText textBoxRadius;
    private ProgressDialog progressDialog;
    private LocationManager locationManager;
    private SharedPreferences sharedPref;
    private Coordinates coordinates;
    private Attractor attractor;
    private DeviceHandler deviceHandler = new DeviceHandler(this);
    private Uri selectedFile;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        handlePermissions();
        requestLocation();
    }

    private void requestLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            labelLocation.setText("GPS not enabled");
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS Enable")
                    .setPositiveButton("Settings", (paramDialogInterface, paramInt) -> startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1))
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            labelLocation.setText("Loading ...");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK)
            return;

        switch (requestCode)
        {
            case 1:
                requestLocation();
                break;

            case 2:
                selectedFile = data.getData();
                labelDevice.setText(selectedFile.getPath());
                break;
        }
    }


    private void initComponents() {
        labelRandomData = findViewById(R.id.labelRandomData);
        labelRandomData.setMovementMethod(new ScrollingMovementMethod());

        labelDevice = findViewById(R.id.labelDevice);
        labelLocation = findViewById(R.id.labelLocation);
        labelAttractor = findViewById(R.id.labelAttractor);
        radioButtonAnu = findViewById(R.id.radioButtonAnu);
        radioButtonRandomDotOrg = findViewById(R.id.radioButtonRandomDotOrg);
        radioButtonFile = findViewById(R.id.radioButtonFile);
        //radioButtonDevice = findViewById(R.id.radioButtonDevice);
        //radioButtonSystem = findViewById(R.id.radioButtonSystem);
        //radioButtonFatum = findViewById(R.id.radioButtonFatum);
        radioButtonGaussKde1 = findViewById(R.id.radioButtonGaussKde1);
        radioButtonGaussKde2 = findViewById(R.id.radioButtonGaussKde2);
        textBoxRadius = findViewById(R.id.textBoxRadius);
        labelAttractorData = findViewById(R.id.labelAttractorData);
        labelAttractorData.setMovementMethod(new ScrollingMovementMethod());

        buttonGenerate = findViewById(R.id.buttonGenerate);
        // TODO: DEBUG
        //buttonGenerate.setEnabled(false);
        coordinates = new Coordinates(48, 8.5);

        buttonOpen = findViewById(R.id.buttonOpen);
        buttonOpen.setEnabled(false);

        labelLocation.setText("Loading ...");
        labelAttractor.setText("");

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        /*radioButtonDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = deviceHandler.getProposedDevice();
                if( device != null) {
                    labelDevice.setText(device.toString());
                } else {
                    labelDevice.setText("device not found");
                }
            }
        });*/

        radioButtonFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select file"), 2);
            }
        });
    }

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //call method to set up device communication
                        }
                    }
                    else {
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };

    private void handlePermissions() {
        // GPS
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 666);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 666);
        }

        // File access
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        666);
            }
        }

        // TODO: TRNG USB device disabled
        // USB Devices
        // deviceHandler.requestPermissions();
    }

    public void onLabelAttractorDataTouch(View view) throws Exception {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("AttractorData", labelAttractorData.getText());
        clipboard.setPrimaryClip(clip);
    }

    public void onButtonGenerateTouch(View view) throws Exception {

        int radius;
        try {
            radius = Integer.valueOf(textBoxRadius.getText().toString());
        } catch (Exception e) {
            radius = 1000;
        }
        textBoxRadius.setText(String.valueOf(radius));

        labelAttractor.setText("");
        labelAttractorData.setText("");
        labelRandomData.setText("");

        RandomSource randomSource = RandomSource.Anu;

        if (radioButtonAnu.isChecked()) {
            randomSource = RandomSource.Anu;
        } else if (radioButtonRandomDotOrg.isChecked()) {
            randomSource = RandomSource.RandomDotOrg;
        } else if (radioButtonFile.isChecked()) {
            randomSource = RandomSource.File;
        } else if (radioButtonDevice.isChecked()) {
            randomSource = RandomSource.Device;
        } /*else if (radioButtonSystem.isChecked()) {
            randomSource = RandomSource.System;
        }*/

        AttractorGeneratorType generatorType = AttractorGeneratorType.Fatum;
        /*if (radioButtonFatum.isChecked()) {
            generatorType = AttractorGeneratorType.Fatum;
        } else */

        // TODO: Disabled kde2 because its library (libs/kde/target/bits_kde.jar) does not build correctly with f-droid
        generatorType = AttractorGeneratorType.Kde1;
        /*
        if(radioButtonGaussKde1.isChecked()) {
            generatorType = AttractorGeneratorType.Kde1;
        } else if(radioButtonGaussKde2.isChecked()) {
            generatorType = AttractorGeneratorType.Kde2;
        }
        */
        GenerateAsyncTask asyncTask = new GenerateAsyncTask();
        AttractorGenerationRequest request = new AttractorGenerationRequest();
        request.coordinates = coordinates;
        request.radius = radius;
        request.randomSource = randomSource;
        request.attractorGeneratorType = generatorType;
        asyncTask.execute(request);
    }

    private void showAttractorInformation(AttractorGenerationResult result) {
        if (result.attractor == null) {
            labelAttractorData.setText("");
            labelAttractor.setText("");
        } else {
            Coordinates location = result.attractor.getCoordinates();
            labelAttractor.setText(location.getLatitude() + ", " + location.getlongitude());
            String s = "approximateRadius: " + result.attractor.getAttractorTest().getApproximateRadius();
            s += "\n";
            s += "relativeDensity: " + result.attractor.getAttractorTest().getRelativeDensity();
            s += "\n";
            s += "nearestDistance: " + result.attractor.getAttractorTest().getNearestDistance();
            s += "\n";
            s += "usedBytes: " + (result.byteIndexAfter - result.byteIndexBefore) + ", bytesLeft: " + result.bytesLeft;
            if(result.randomDotOrgQuota > 0) {
                s += "\n";
                double quotaPercent = (double) result.randomDotOrgQuota / (double) RandomDotOrgEntropyManager.REQUEST_ENTROPY_MAX_SIZE * 100;
                s += "random.org quota: " + new DecimalFormat("#.#").format(quotaPercent) + "%";
            }
            labelAttractorData.setText(s);
        }

        if( result.entropyAsString != null)
            labelRandomData.setText(result.entropyAsString);
        else
            labelRandomData.setText("");
    }

    public void onButtonOpenTouch(View view) throws Exception {
        if (attractor == null)
            return;

        double lon = attractor.getCoordinates().getlongitude();
        double lat = attractor.getCoordinates().getLatitude();

        // TODO: Culture format
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lat + "," + lon + ""));
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        labelLocation.setText(location.getLatitude() + ", " + location.getLongitude());
        coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
        buttonGenerate.setEnabled(true);
    }

    public class GenerateAsyncTask extends AsyncTask<AttractorGenerationRequest, Integer, AttractorGenerationResult>
            implements IProgressHandler {
        private final static String ENTROPY_BYTE_INDEX_PREFIX = "entropy_byte_index_";
        private final static String RANDOM_DOT_ORG_QUOTA = "random_dot_org_quota";
        private final static String RANDOM_DOT_ORG_QUOTA_TIMESTAMP = "random_dot_org_quota_timestamp";

        @RequiresApi(api = Build.VERSION_CODES.O)
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setMessage("Please wait... fetching data and generating attractor. Progress " + progress[0] + "%");
        }

        @Override
        protected AttractorGenerationResult doInBackground(AttractorGenerationRequest... requests) {

            AttractorGenerationResult result = new AttractorGenerationResult();
            int entropyUsage = RandomPointsProvider.getEntropyUsage(requests[0].radius);
            Log.v(TAG, "entropyUsage =" + entropyUsage);
            LoadRandomProviderResult loadRandomProviderResult = getLoadRandomProviderResult(result, entropyUsage, requests);

            if (loadRandomProviderResult != null && loadRandomProviderResult.getRandomProvider() != null) {
                Log.v(TAG, "getByteIndex =" + loadRandomProviderResult.getRandomProvider().getByteIndex());
                Log.v(TAG, "getEntropyPoolSize =" + loadRandomProviderResult.getRandomProvider().getEntropyPoolSize());
            }

            try {
                if(loadRandomProviderResult == null)
                {
                    result.status = "failed to initialize source";
                    return result;
                }
                if (!loadRandomProviderResult.getStatus())
                {
                    result.status = "loading random data failed: " + loadRandomProviderResult.getMessage();
                    return result;
                }
                IRandomProvider randomProvider = loadRandomProviderResult.getRandomProvider();
                if (randomProvider != null && randomProvider.hasEntropyLeft(entropyUsage)) {
                    Log.v(TAG, "generating attractor");

                    generateAttractor(result, entropyUsage, randomProvider, requests);

                } else if (randomProvider != null) {
                    result.status = "no entropy left";
                } else {
                    result.status = "failed to initialize source (2)";
                }
            } catch (Exception e) {
                Log.w(TAG, e);
            }

            publishProgress(100);
            return result;
        }

        private void generateAttractor(AttractorGenerationResult result, int entropyUsage, IRandomProvider randomProvider, AttractorGenerationRequest[] requests) {
            int byteIndexBefore = randomProvider.getByteIndex();
            IAttractorGenerator generator = AttractorGeneratorFactory.getAttractorGenerator(requests[0].attractorGeneratorType, randomProvider);

            try {
                result.attractor = generator.getAttractor(requests[0].coordinates, requests[0].radius);
                result.entropyAsString = randomProvider.getUsedEntropyAsString(byteIndexBefore, entropyUsage);
                Log.v(TAG, "attractor generated");
            } catch (Exception e) {
                Log.v(TAG, "attractor generation failed. " + e);
            }

            int byteIndexAfter = randomProvider.getByteIndex();
            Log.v(TAG, "byteIndexAfter =" + byteIndexAfter);

            result.byteIndexAfter = byteIndexAfter;
            result.byteIndexBefore = byteIndexBefore;
            result.bytesLeft = randomProvider.getEntropyPoolSize() - byteIndexAfter;
        }

        @Nullable
        private LoadRandomProviderResult getLoadRandomProviderResult(AttractorGenerationResult result, int entropyUsage, AttractorGenerationRequest[] requests) {
            LoadRandomProviderResult loadRandomProviderResult = null;
            try {
                switch (requests[0].randomSource) {
                    case File:
                        loadRandomProviderResult = new FileEntropyManager(selectedFile, MainActivity.this).loadRandomProvider(entropyUsage);
                        {
                            IRandomProvider randomProvider = loadRandomProviderResult.getRandomProvider();
                            if (randomProvider != null) {
                                int hashCode = randomProvider.getHashCode();
                                result.bytesHashCode = hashCode;
                                int byteIndex = sharedPref.getInt(ENTROPY_BYTE_INDEX_PREFIX + hashCode, 0);
                                Log.w(TAG, "Read byte index " + byteIndex + " for hash " + result.bytesHashCode);
                                randomProvider.setByteIndex(byteIndex);
                            }
                        }
                        break;
                    case System:
                        loadRandomProviderResult = new SystemEntropyManager().loadRandomProvider(entropyUsage);
                        break;
                    case Device:
                        DeviceEntropyManager deviceEntropyManager = new DeviceEntropyManager(deviceHandler, this);
                        loadRandomProviderResult = deviceEntropyManager.loadRandomProvider(entropyUsage);
                        break;
                    case Anu:
                        loadRandomProviderResult = new AnuEntropyManager(this).loadRandomProvider(entropyUsage);
                        break;
                    default:
                    case RandomDotOrg:
                        int randomDotOrgQuota = sharedPref.getInt(RANDOM_DOT_ORG_QUOTA, 0);
                        long randomDotOrgQuotaTimestamp = sharedPref.getLong(RANDOM_DOT_ORG_QUOTA_TIMESTAMP, 0);
                        long currentTimestamp = System.currentTimeMillis();

                        /* TODO: This is only a simple quota handling with a reset every 24h.
                            A better handling would be to check for errors from the http request and
                            then set a cool down timeout of 4-5 hours. See random.org FAQ.
                        */
                        if (currentTimestamp - randomDotOrgQuotaTimestamp > 24 * 60 * 60 * 1000) // 24 hours
                        {
                            randomDotOrgQuota = 0;
                        }

                        if (randomDotOrgQuota >= (RandomDotOrgEntropyManager.REQUEST_ENTROPY_MAX_SIZE))
                        {
                            // Entropy quota was used up
                            Log.w(TAG, "Entropy quota was used up. Quota = " + randomDotOrgQuota);
                            loadRandomProviderResult = null;
                            result.randomDotOrgQuota = randomDotOrgQuota;
                            result.randomDotOrgQuotaTimestamp = randomDotOrgQuotaTimestamp;
                        }
                        else
                        {
                            loadRandomProviderResult = new RandomDotOrgEntropyManager(this).loadRandomProvider(entropyUsage);
                            {
                                IRandomProvider randomProvider = loadRandomProviderResult.getRandomProvider();
                                if (randomProvider != null) {
                                    randomDotOrgQuota += randomProvider.getEntropyPoolSize();
                                    result.randomDotOrgQuota = randomDotOrgQuota;
                                    result.randomDotOrgQuotaTimestamp = currentTimestamp;
                                }
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                Log.w(TAG, e);
            }
            return loadRandomProviderResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.buttonGenerate.setEnabled(false);

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait... generating attractor.");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(AttractorGenerationResult result) {
            super.onPostExecute(result);
            MainActivity.this.buttonGenerate.setEnabled(true);
            MainActivity.this.attractor = result.attractor;

            buttonGenerate.setEnabled(true);
            buttonOpen.setEnabled(true);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(ENTROPY_BYTE_INDEX_PREFIX + result.bytesHashCode, result.byteIndexAfter);
            editor.putInt(RANDOM_DOT_ORG_QUOTA, result.randomDotOrgQuota);
            editor.putLong(RANDOM_DOT_ORG_QUOTA_TIMESTAMP, result.randomDotOrgQuotaTimestamp);
            Log.w(TAG, "Saved byte index " + result.byteIndexAfter + " for hash " + result.bytesHashCode);
            Log.w(TAG, "Saved randomDotOrgQuota " + result.randomDotOrgQuota + " for timestamp " + result.randomDotOrgQuotaTimestamp);
            editor.commit();

            int usedBytes = result.byteIndexAfter - result.byteIndexBefore;
            int bytesLeft = result.bytesLeft;

            if (result.attractor != null) {
                showAttractorInformation(result);
                persistAttractorData(result);
            } else {
                String s = "attractor generation failed.";
                s += "\n";
                s += "usedBytes: " + usedBytes;
                s += "\n";
                s += "bytesLeft: " + bytesLeft;

                if (result.status != null) {
                    s += "\n";
                    s += "status: " + result.status;
                }

                labelAttractorData.setText(s);
            }
            progressDialog.hide();
        }

        @Override
        public void updateProgress(int percent) {
            publishProgress(percent);
        }
    }

    private void persistAttractorData(AttractorGenerationResult result) {
        Gson gson = new Gson();
        String json = gson.toJson(result.attractor);
        String id = result.attractor.getIdentifier();
        writeToFile(json, id, this);
    }

    private void writeToFile(String data, String fileName, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
