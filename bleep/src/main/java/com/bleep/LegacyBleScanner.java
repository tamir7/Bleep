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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

@SuppressWarnings("deprecation")
public class LegacyBleScanner extends BleScanner implements BluetoothAdapter.LeScanCallback {

    protected LegacyBleScanner(BluetoothAdapter adapter) {
        super(adapter);
    }

    @Override
    protected void startScan(BluetoothAdapter adapter) {
        adapter.startLeScan(this);
    }

    @Override
    protected void stopScan(BluetoothAdapter adapter) {
        adapter.stopLeScan(this);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        onScan(device, rssi, scanRecord);
    }
}
