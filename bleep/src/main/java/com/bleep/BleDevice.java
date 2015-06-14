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

    Task<Void> connect(int timeoutInMilliseconds) {
        return new ConnectOperation(callbacks, timeoutInMilliseconds,
            device, context).execute().onSuccess(new Continuation<BluetoothGatt, Void>() {
            @Override
            public Void then(Task<BluetoothGatt> task) throws Exception {
                gatt = task.getResult();
                return null;
            }
        });
    }

    boolean refresh() {
        if (gatt != null) {
            throw new IllegalStateException("Device is not connected");
        }

        return Hack.refreshGatt(gatt);
    }

    Task<Void> connect() {
        return connect(DEFAULT_OPERATION_TIMEOUT);
    }

    Task<List<BluetoothGattService>> discoverServices(int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new DiscoverServicesOperation(callbacks, timeoutInMilliseconds, gatt).execute();
    }

    Task<List<BluetoothGattService>> discoverServices() {
        return discoverServices(DEFAULT_OPERATION_TIMEOUT);
    }

    Task<BluetoothGattCharacteristic> readCharacteristic(UUID serviceUUID,
        UUID characteristicUUID) {
        return readCharacteristic(serviceUUID, characteristicUUID, DEFAULT_OPERATION_TIMEOUT);
    }

    Task<BluetoothGattCharacteristic> readCharacteristic(UUID serviceUUID,
        UUID characteristicUUID, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new ReadCharacteristicOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID).execute();
    }

    Task<BluetoothGattCharacteristic> writeCharacteristic(UUID serviceUUID,
        UUID characteristicUUID, byte[] value) {
        return writeCharacteristic(serviceUUID, characteristicUUID, value,
            DEFAULT_OPERATION_TIMEOUT);
    }

    Task<BluetoothGattCharacteristic> writeCharacteristic(UUID serviceUUID,
        UUID characteristicUUID, byte[] value, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new WriteCharacteristicOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID, value).execute();
    }

    Task<BluetoothGattDescriptor> readDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new ReadDescriptorOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID, descriptorUUID).execute();
    }

    Task<BluetoothGattDescriptor> readDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID) {
        return readDescriptor(serviceUUID, characteristicUUID, descriptorUUID,
            DEFAULT_OPERATION_TIMEOUT);
    }

    Task<BluetoothGattDescriptor> writeDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID, byte[] value, int timeoutInMilliseconds) {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new WriteDescriptorOperation(callbacks, timeoutInMilliseconds, gatt, serviceUUID,
            characteristicUUID, descriptorUUID, value).execute();
    }

    Task<BluetoothGattDescriptor> writeDescriptor(UUID serviceUUID,
        UUID characteristicUUID, UUID descriptorUUID, byte[] value) {
        return writeDescriptor(serviceUUID, characteristicUUID, descriptorUUID, value,
            DEFAULT_OPERATION_TIMEOUT);
    }
}
