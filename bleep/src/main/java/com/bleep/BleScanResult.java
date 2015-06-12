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

public class BleScanResult {
    private final BluetoothDevice device;
    private final int rssi;
    private final byte[] scanRecord;

    BleScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public int getRssi() {
        return rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }
}
