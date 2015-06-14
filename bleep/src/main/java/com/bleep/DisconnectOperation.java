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
import android.bluetooth.BluetoothProfile;

class DisconnectOperation extends BleOperation<Void> {
    private final BluetoothGatt gatt;

    DisconnectOperation(BleCallbacks callbacks, int timeout, BluetoothGatt gatt) {
        super(callbacks, timeout);
        this.gatt = gatt;
    }

    @Override
    protected void preformOperation() {
        gatt.disconnect();
    }

    @Override
    protected String getOperationName() {
        return "Disconnect";
    }

    @Override
    protected String getDeviceAddress() {
        return gatt.getDevice().getAddress();
    }

    @Override
    public boolean onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (this.gatt.getDevice().getAddress().equals(gatt.getDevice().getAddress())) {
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                setResponse(null);
            } else {
                setException(new BleException(status,
                    String.format("Failed to Disconnect from device %s with status %s",
                        gatt.getDevice().getAddress(), status)));
            }

            return true;
        }
        return super.onConnectionStateChange(gatt, status, newState);
    }
}
