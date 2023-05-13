package com.github.librerandonaut.librerandonaut.rngdevice;

public class DeviceResult {
    // TODO Encapsulation ..
    public boolean success = false;
    public byte[] data;
    public String status;

    public DeviceResult(){
        data = new byte[0];
        status = "";
    }
}
