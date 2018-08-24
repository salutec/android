/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package no.nordicsemi.android.nrfbeacon.scanner;

import android.bluetooth.BluetoothDevice;

import no.nordicsemi.android.support.v18.scanner.ScanResult;

public class ExtendedBluetoothDevice {
	public BluetoothDevice device;
	public int rssi;
	public String name;

	public ExtendedBluetoothDevice(final ScanResult scanResult) {
		this.device = scanResult.getDevice();
		this.name = scanResult.getScanRecord() != null ? scanResult.getScanRecord().getDeviceName() : null;
		this.rssi = scanResult.getRssi();
	}

	public boolean matches(final ScanResult scanResult) {
		return device.getAddress().equals(scanResult.getDevice().getAddress());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ExtendedBluetoothDevice) {
			final ExtendedBluetoothDevice that = (ExtendedBluetoothDevice) o;
			return device.getAddress().equals(that.device.getAddress());
		}
		return super.equals(o);
	}
}
