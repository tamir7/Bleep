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

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import bolts.Task;

abstract class BleOperation<T> implements BleCallbacksHandler {
    private static final int DEFAULT_TIMEOUT = 200;
    private final Semaphore waitLock = new Semaphore(0);
    private final BleCallbacks callbacks;
    private int timeout = DEFAULT_TIMEOUT;

    public Semaphore getWaitLock() {
        return waitLock;
    }

    ;
    private T response;
    private BleException exception;

    protected BleOperation(BleCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public BleOperation setTimeout(int milliseconds) {
        timeout = milliseconds;
        return this;
    }

    abstract void preformOperation();
    abstract String getOperationName();
    abstract String getDeviceAddress();

    public Task<T> execute() {
        return Task.callInBackground(new Callable<T>() {
            @Override
            public T call() throws Exception {
                callbacks.register(BleOperation.this);
                preformOperation();
                if (!waitLock.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
                    exception = new BleException(BleException.TIMEOUT,
                        String.format("Operation %s for device %s Timed out", getOperationName(),
                            getDeviceAddress()));
                }

                callbacks.unregister(BleOperation.this);
                if (exception != null) {
                    throw exception;
                }

                return response;
            }
        });
    }

    protected void setResponse(T response) {
        this.response = response;
        waitLock.release();
    }

    protected void setException(BleException e) {
        exception = e;
    }

    @Override
    public boolean onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        return false;
    }

    @Override
    public boolean onServicesDiscovered(BluetoothGatt gatt, int status) {
        return false;
    }

    @Override
    public boolean onCharacteristicWrite(BluetoothGatt gatt,
        BluetoothGattCharacteristic characteristic, int status) {
        return false;
    }

    @Override
    public boolean onCharacteristicRead(BluetoothGatt gatt,
        BluetoothGattCharacteristic characteristic, int status) {
        return false;
    }
}
