/*
 * Copyright 2015 Tamir Shomer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bleep;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import java.util.List;
import java.util.UUID;

import bolts.Continuation;
import bolts.Task;

public class BleDevice {
    private static final int DEFAULT_OPERATION_TIMEOUT = 800;
    private final BluetoothDevice device;
    private final Context context;
    private final BleCallbacks callbacks;
    private BluetoothGatt gatt;

    public BleDevice(BluetoothDevice device) {
        this.device = device;
        context = Bleep.getSelf().getContext();
        callbacks = Bleep.getSelf().getCallbacks();
    }

    public Task<Void> connect(int timeoutInMilliseconds) {
        return new ConnectOperation(callbacks, timeoutInMilliseconds,
            device, context).execute().onSuccess(new Continuation<BluetoothGatt, Void>() {
            @Override
            public Void then(Task<BluetoothGatt> task) throws Exception {
                gatt = task.getResult();
                return null;
            }
        });
    }

    public boolean refresh() {
        if (gatt != null) {
            throw new IllegalStateException("Device is not connected");
        }

        return Hack.refreshGatt(gatt);
    }

    public Task<Void> connect() {
        return connect(DEFAULT_OPERATION_TIMEOUT);
    }

    public Task<List<BluetoothGattService>> discoverServices(int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new DiscoverServicesOperation(callbacks, timeoutInMilliseconds, gatt).execute();
    }

    public Task<List<BluetoothGattService>> discoverServices() {
        return discoverServices(DEFAULT_OPERATION_TIMEOUT);
    }

    public Task<BluetoothGattCharacteristic> readCharacteristic(UUID serviceUUID,
        UUID characteristicUUID) {
        return readCharacteristic(serviceUUID, characteristicUUID, DEFAULT_OPERATION_TIMEOUT);
    }

    public Task<BluetoothGattCharacteristic> readCharacteristic(UUID serviceUUID,
        UUID characteristicUUID, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new ReadCharacteristicOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID).execute();
    }

    public Task<BluetoothGattCharacteristic> writeCharacteristic(UUID serviceUUID,
        UUID characteristicUUID, byte[] value) {
        return writeCharacteristic(serviceUUID, characteristicUUID, value,
            DEFAULT_OPERATION_TIMEOUT);
    }

    public Task<BluetoothGattCharacteristic> writeCharacteristic(UUID serviceUUID,
        UUID characteristicUUID, byte[] value, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new WriteCharacteristicOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID, value).execute();
    }

    public Task<BluetoothGattDescriptor> readDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new ReadDescriptorOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID, descriptorUUID).execute();
    }

    public Task<BluetoothGattDescriptor> readDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID) {
        return readDescriptor(serviceUUID, characteristicUUID, descriptorUUID,
            DEFAULT_OPERATION_TIMEOUT);
    }

    public Task<BluetoothGattDescriptor> writeDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID, byte[] value, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new WriteDescriptorOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID, descriptorUUID, value).execute();
    }

    public Task<BluetoothGattDescriptor> writeDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID, byte[] value) {
        return writeDescriptor(serviceUUID, characteristicUUID, descriptorUUID, value,
            DEFAULT_OPERATION_TIMEOUT);
    }

    public Task<Void> disconnect(int timeoutInMilliseconds) {
        if (gatt == null) {
            return Task.forResult(null);
        }

        return new DisconnectOperation(callbacks, timeoutInMilliseconds, gatt).execute();
    }

    public Task<Void> disconnect() {
        return disconnect(DEFAULT_OPERATION_TIMEOUT);
    }

    public void close() {
        if (gatt != null) {
            gatt.close();
        }
    }
}
