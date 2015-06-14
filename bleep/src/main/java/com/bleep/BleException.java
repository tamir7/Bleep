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

public class BleException extends Exception {
    public static final int OTHER_CAUSE = 324345;
    public static final int TIMEOUT = 324346;
    private final int status;

    BleException(int status, String msg) {
        super(msg);
        this.status = status;
    }

    BleException(int status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status;
    }

    static BleException construct(Exception e) {
        if (e == null) {
            return null;
        }

        if (e instanceof BleException) {
            return (BleException)e;
        }

        return new BleException(OTHER_CAUSE, e.getMessage(), e);
    }

    public int getStatus() {
        return status;
    }

    public String getStatusString() {
        switch (status) {
            case BluetoothGatt.GATT_READ_NOT_PERMITTED:
                return "GATT_READ_NOT_PERMITTED";
            case BluetoothGatt.GATT_WRITE_NOT_PERMITTED:
                return "GATT_WRITE_NOT_PERMITTED";
            case BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION:
                return "GATT_INSUFFICIENT_AUTHENTICATION";
            case BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED:
                return "GATT_REQUEST_NOT_SUPPORTED";
            case BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION:
                return "GATT_INSUFFICIENT_ENCRYPTION";
            case BluetoothGatt.GATT_INVALID_OFFSET:
                return "GATT_INVALID_OFFSET";
            case BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH:
                return "GATT_INVALID_ATTRIBUTE_LENGTH";
            case BluetoothGatt.GATT_CONNECTION_CONGESTED:
                return "GATT_CONNECTION_CONGESTED";
            case BluetoothGatt.GATT_FAILURE:
                return "GATT_FAILURE";
            case TIMEOUT:
                return "TIMEOUT";
            case OTHER_CAUSE:
                return "OTHER_CAUSE";
            default:
                return String.format("UNKNOWN STATUS: %s", status);
        }
    }
}
