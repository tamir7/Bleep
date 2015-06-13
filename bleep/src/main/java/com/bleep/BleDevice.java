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
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class BleDevice {
    private final BluetoothDevice device;
    private final Context context;
    private final BleCallbacks callbacks;
    private BluetoothGatt gatt;

    public BleDevice(BluetoothDevice device) {
        this.device = device;
        context = Bleep.getSelf().getContext();
        callbacks = Bleep.getSelf().getCallbacks();
    }

    Task<Void> connect() {
        Bleep bleep = Bleep.getSelf();
        return new ConnectOperation(device, bleep.getContext(), bleep.getCallbacks()).execute().onSuccess(
            new Continuation<BluetoothGatt, Void>() {
                @Override
                public Void then(Task<BluetoothGatt> task) throws Exception {
                    gatt = task.getResult();
                    return null;
                }
            });
    }

    Task<List<BluetoothGattService>> discoverServices() {
        if (gatt == null) {
            throw new IllegalStateException("Device is not connected");
        }

        return new DiscoverServicesOperation(gatt, callbacks).execute();
    }
}
