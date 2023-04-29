package com.librerandonaut.librerandonaut.rngdevice;

public interface IDeviceHandler {
    DeviceResult loadDataFromDevice(Device device, int size, IProgressHandler progressHandler);
    Device getProposedDevice();
}
