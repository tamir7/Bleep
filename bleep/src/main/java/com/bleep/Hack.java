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
import android.util.Log;

import java.lang.reflect.Method;

class Hack {
    private static final String TAG = Hack.class.getSimpleName();

    static boolean refreshGatt(BluetoothGatt gatt) {
        try {
            Method method = gatt.getClass().getMethod("refresh");
            return (Boolean)method.invoke(gatt);

        } catch (Exception e) {
            if (Bleep.LOG) {
                Log.e(TAG, "Failed to invoke refresh function in BluetoothGatt class", e);
            }
            return false;
        }
    }
}
