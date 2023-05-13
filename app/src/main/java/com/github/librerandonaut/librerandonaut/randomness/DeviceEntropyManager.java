package com.github.librerandonaut.librerandonaut.randomness;

import android.util.Log;

import com.github.librerandonaut.librerandonaut.rngdevice.IDeviceHandler;
import com.github.librerandonaut.librerandonaut.rngdevice.Device;
import com.github.librerandonaut.librerandonaut.rngdevice.DeviceResult;
import com.github.librerandonaut.librerandonaut.rngdevice.IProgressHandler;

public class DeviceEntropyManager implements IEntropyManager {
    private IDeviceHandler deviceHandler;
    private IProgressHandler progressHandler;

    public DeviceEntropyManager(IDeviceHandler deviceHandler, IProgressHandler progressHandler) {
        this.deviceHandler = deviceHandler;
        this.progressHandler = progressHandler;
    }


    @Override
    public IRandomProvider loadRandomProvider(int entropyUsage) {
        Device device = deviceHandler.getProposedDevice();
        if( device != null) {
            DeviceResult result = deviceHandler.loadDataFromDevice(device, entropyUsage, progressHandler);
            if (result.success) {
                return new RandomProvider(result.data, RandomSource.Device);
            } else {
                Log.w("DeviceEntropyManager", "loading data not successful");
            }
        } else {
            Log.w("DeviceEntropyManager", "device is null");
        }
        return null;
    }
}
