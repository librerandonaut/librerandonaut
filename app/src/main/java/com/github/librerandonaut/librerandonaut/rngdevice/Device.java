package com.github.librerandonaut.librerandonaut.rngdevice;

import androidx.annotation.NonNull;

public class Device {

    public Device(String deviceName, String manufacturerName, String productName, String serialNumber){
        this.deviceName = deviceName;
        this.manufacturerName = manufacturerName;
        this.productName = productName;
        this.serialNumber = serialNumber;
    }

    public String deviceName;
    public String manufacturerName;
    public String productName;
    public String serialNumber;

    public boolean isProposed;

    @NonNull
    @Override
    public String toString() {
        return deviceName + ", " + manufacturerName + ", " + productName + ", " + serialNumber;
    }
}
