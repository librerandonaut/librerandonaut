package com.github.librerandonaut.librerandonaut.rngdevice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.Map;

public class DeviceHandler implements IDeviceHandler {
    private ContextWrapper applicationConxt;
    private static final String ACTION_USB_PERMISSION = "com.librerandonaut.accessusbserialdevice";
    private static final int TIMEOUT = 5000;
    private static final int MAX_RETRIES = 100;

    public DeviceHandler(ContextWrapper applicationConxt) {
        this.applicationConxt = applicationConxt;
    }

    public void requestPermissions() {
        UsbManager manager = (UsbManager) applicationConxt.getSystemService(Context.USB_SERVICE);

        PendingIntent permissionIntent;
        permissionIntent = PendingIntent.getBroadcast(applicationConxt.getApplicationContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);

        Map<String, UsbDevice> usbDevices = manager.getDeviceList();
        for (UsbDevice usbDevice : usbDevices.values()) {
            manager.requestPermission(usbDevice, permissionIntent);
        }
    }

    private void updateProgress(IProgressHandler progressHandler, int percent) {
        if(progressHandler != null) {
            progressHandler.updateProcess(percent);
        }
    }

    public DeviceResult loadDataFromDevice(Device device, int size, IProgressHandler progressHandler) {
        updateProgress(progressHandler, 0);
        DeviceResult result = new DeviceResult();

        UsbManager manager = (UsbManager) applicationConxt.getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = manager.getDeviceList().get(device.deviceName);

        UsbDeviceConnection connection = manager.openDevice(usbDevice);
        if (connection == null) {
            result.status = "connection == null";
            return result;
        }
        UsbEndpoint endpoint = usbDevice.getInterface(0).getEndpoint(0);
        connection.claimInterface(usbDevice.getInterface(0), true);

        result.data = new byte[size];

        int byteCount = 0;
        int retryCount = 0;
        byte[] buffer = new byte[endpoint.getMaxPacketSize()];
        while(byteCount < size && retryCount < MAX_RETRIES) {
            int recievedStatus = connection.bulkTransfer(endpoint, buffer, endpoint.getMaxPacketSize(), TIMEOUT);
            if (recievedStatus > 0) {
                for (int i = 0; i < recievedStatus && i + byteCount < size; i++) {
                    result.data[i + byteCount] = buffer[i];
                }
                byteCount += recievedStatus;
                updateProgress(progressHandler, (int)((double)byteCount / (double)size * 100));
            } else  {
                retryCount++;
            }
        }

        result.status = "filled all bytes " + size;
        result.success = true;

        return result;
    }

    public Device[] getAllDevices() {
        UsbManager manager = (UsbManager) applicationConxt.getSystemService(Context.USB_SERVICE);
        Map<String, UsbDevice> usbDevices = manager.getDeviceList();
        Device[] devices = new Device[usbDevices.size()];
        int index = 0;
        for (UsbDevice usbDevice : usbDevices.values()) {
            Log.w("librerandonaut", "333");
            Device device = new Device(usbDevice.getDeviceName(), usbDevice.getManufacturerName(), usbDevice.getProductName(), usbDevice.getSerialNumber());
            if (device.productName != null && device.productName.contains("UART"))
                device.isProposed = true;

            devices[index++] = device;
        }
        return devices;
    }

    public Device getProposedDevice() {
        for (Device device : getAllDevices()) {
            if (device.isProposed)
                return device;
        }
        return null;
    }


    /*private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {


                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //byte[] DATA = new byte[1000];
                            //int TIMEOUT = 100000;

                            UsbManager manager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
                            UsbDeviceConnection connection = manager.openDevice(device);
                            if (connection == null) {
                                labelOutput.setText("connection == null");
                                return;
                            }
                            UsbEndpoint endpoint = device.getInterface(0).getEndpoint(0);

                            boolean hasData = true;
                            byte[] byteA = new byte[endpoint.getMaxPacketSize()];

                            connection.claimInterface(device.getInterface(0), true);

                            //int readBytes = connection.bulkTransfer(endpoint, DATA, DATA.length, TIMEOUT);
                            //labelOutput.setText("readBytes: " + readBytes + ", " + bytesToHex(DATA));

                            int count = 0;
                            while (hasData && count < 1) {
                                int c = connection.bulkTransfer(endpoint, byteA, endpoint.getMaxPacketSize(), 5000);
                                status = "return=" + c + ";getMaxPacketSize=" + endpoint.getMaxPacketSize();
                                if (c <= 0) {
                                    hasData = false;
                                } else {
                                    allDataString += " " + bytesToHex(byteA);
                                }
                                count++;
                            }
                        }
                    } else {
                        Log.d("librerandonaut", "permission denied for device " + device);
                    }
                }
            }
        }
    };*/
}
