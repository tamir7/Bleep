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
import android.bluetooth.BluetoothProfile;
import android.content.Context;

class ConnectOperation extends BleOperation<BluetoothGatt> {
    private final BluetoothDevice device;
    private final Context context;
    private final BleCallbacks callbacks;

    ConnectOperation(BleCallbacks callbacks, int timeout, BluetoothDevice device, Context context) {
        super(callbacks, timeout);
        this.device = device;
        this.context = context;
        this.callbacks = callbacks;
    }

    @Override
    void preformOperation() {
        device.connectGatt(context, false, callbacks);
    }

    @Override
    String getOperationName() {
        return "Connect";
    }

    @Override
    String getDeviceAddress() {
        return device.getAddress();
    }

    @Override
    public boolean onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (gatt.getDevice().getAddress().equals(device.getAddress())) {
            if (status == BluetoothGatt.GATT_SUCCESS &&
                newState == BluetoothProfile.STATE_CONNECTED) {
                setResponse(gatt);
            } else {
                setException(new BleException(status,
                    String.format("Connect to device %s failed with status %s", device.getAddress(),
                        status)));
            }

            return true;
        }

        return super.onConnectionStateChange(gatt, status, newState);
    }
}
