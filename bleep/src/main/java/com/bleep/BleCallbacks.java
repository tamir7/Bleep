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

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class BleCallbacks extends BluetoothGattCallback {
    private static final String TAG = BleCallbacks.class.getSimpleName();
    private final List<BleCallbacksHandler> handlers = new CopyOnWriteArrayList<>();

    void register(BleCallbacksHandler handler) {
        handlers.add(handler);
    }

    void unregister(BleCallbacksHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        for (BleCallbacksHandler handler : handlers) {
            if (handler.onConnectionStateChange(gatt, status, newState)) {
                return;
            }
        }

        if (Bleep.LOG) {
            Log.w(TAG, String.format("Unhandled BLE Event onConnectionStateChange with status: %s, newState: %s",
                status, newState));
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
        int status) {
        for (BleCallbacksHandler handler : handlers) {
            if (handler.onCharacteristicRead(gatt, characteristic, status)) {
                return;
            }
        }

        if (Bleep.LOG) {
            Log.w(TAG, String.format(
                "Unhandled BLE Event onCharacteristicRead with status: %s, characteristic: %s",
                status, characteristic.getUuid()));
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
        BluetoothGattCharacteristic characteristic, int status) {
        for (BleCallbacksHandler handler : handlers) {
            if (handler.onCharacteristicWrite(gatt, characteristic, status)) {
                return;
            }
        }

        if (Bleep.LOG) {
            Log
                .w(TAG, String.format(
                    "Unhandled BLE Event onCharacteristicWrite with status: %s, characteristic: %s",
                    status, characteristic.getUuid()));
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        for (BleCallbacksHandler handler : handlers) {
            if (handler.onServicesDiscovered(gatt, status)) {
                return;
            }
        }

        if (Bleep.LOG) {
            Log.w(TAG,
                String.format("Unhandled BLE Event onServicesDiscovered with status: %s", status));
        }
    }
}
