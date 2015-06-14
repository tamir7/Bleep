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

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class LollipopBleScanner extends BleScanner {

    private final ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            onScan(result.getDevice(), result.getRssi(), result.getScanRecord() == null ?
                null : result.getScanRecord().getBytes());
        }
    };

    protected LollipopBleScanner(BluetoothAdapter adapter) {
        super(adapter);
    }

    @Override
    protected void startScan(BluetoothAdapter adapter) {
        adapter.getBluetoothLeScanner().startScan(callback);
    }

    @Override
    protected void stopScan(BluetoothAdapter adapter) {
        adapter.getBluetoothLeScanner().stopScan(callback);
    }
}
