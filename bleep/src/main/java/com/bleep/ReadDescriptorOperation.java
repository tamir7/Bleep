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
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

public class ReadDescriptorOperation extends BleOperation<BluetoothGattDescriptor> {
    private final BluetoothGatt gatt;
    private final UUID serviceUUID;
    private final UUID characteristicUUID;
    private final UUID descriptorUUID;

    protected ReadDescriptorOperation(BleCallbacks callbacks, int timeout, BluetoothGatt gatt,
        UUID serviceUUID, UUID characteristicUUID, UUID descriptorUUID) {
        super(callbacks, timeout);
        this.gatt = gatt;
        this.serviceUUID = serviceUUID;
        this.characteristicUUID = characteristicUUID;
        this.descriptorUUID = descriptorUUID;
    }

    @Override
    void preformOperation() {
        BluetoothGattService service = gatt.getService(serviceUUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUUID);
        gatt.readDescriptor(descriptor);
    }

    @Override
    String getOperationName() {
        return "Read Descriptor";
    }

    @Override
    String getDeviceAddress() {
        return gatt.getDevice().getAddress();
    }

    @Override
    public boolean onDescriptorRead(BluetoothGatt gatt,
        BluetoothGattDescriptor descriptor, int status) {
        if (this.gatt.getDevice().getAddress().equals(gatt.getDevice().getAddress())) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                setResponse(descriptor);
            } else {
                setException(new BleException(status,
                    String.format("ReadDescriptor operation failed with status %s", status)));
            }

            return true;
        }

        return super.onDescriptorRead(gatt, descriptor, status);
    }
}
