package com.anki.desk.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class BleService extends Service {

	private final static String TAG = "BleService: ";

	// public final static String ACTION = "com.anki.desk.service.BleService";
	public final static String BROADCAST_ACTION = "com.anki.desk.service.BleService.Broadcast";

	public final static String REFRESH = "fefresh";
	public final static String CONNECT_BlE = "connect_ble";
	public final static String DISCONNECT_BLE = "disconnect_ble";
	public final static String WRITE_CHARACTERISTIC = "wirte_characteristic";
	public final static String READ_CHARACTERISTIC = "read_characteristic";

	public final static String bleAddress = "bleAddress";

	private BluetoothManager bluetoothManager = null;
	private BluetoothAdapter mBluetoothAdapter = null;

	public Map<String, BluetoothDevice> deviceMap = new HashMap<String, BluetoothDevice>();
	private Map<String, BluetoothGatt> gattMap = new HashMap<String, BluetoothGatt>();
	private StringBuffer crValue = new StringBuffer();

	@Override
	public void onCreate() {
		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (!mBluetoothAdapter.isEnabled())
			mBluetoothAdapter.enable();// 鎵撳紑钃濈墮
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(REFRESH)) {
			scanBle();
		} else if (action.equals(CONNECT_BlE)) {
			String address = intent.getStringExtra(bleAddress);
			connect(address);
		} else if (action.equals(DISCONNECT_BLE)) {
			String address = intent.getStringExtra(bleAddress);
			disconnect(address);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		scanBle();
		return null;
	}

	public Map<String, BluetoothDevice> getDeviceMap() {
		return deviceMap;
	}

	public void scanBle() {

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				sendBroad(false, true, null);
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				System.out.println(TAG + "ending searching BLE");
			}
		}, 10000);
		deviceMap.clear();
		sendBroad(true, false, null);
		mBluetoothAdapter.startLeScan(mLeScanCallback);
		System.out.println(TAG + "start searching BLE");
	}

	public void connect(String bleAddress) {
		BluetoothDevice device = null;
		if (!deviceMap.isEmpty() && deviceMap.containsKey(bleAddress)) {
			device = deviceMap.get(bleAddress);
		}
		if (device != null) {
			BluetoothGatt gatt = device.connectGatt(BleService.this, false,
					mGattCallback);
			gattMap.put(bleAddress, gatt);
		}
	}

	public void disconnect(String bleAddress) {
		if (!gattMap.isEmpty() && gattMap.containsKey(bleAddress)) {
			BluetoothGatt gatt = gattMap.get(bleAddress);
			gatt.disconnect();
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			deviceMap.put(device.getAddress(), device);
			sendBroad(false, false, device);
		}
	};

	public void sendBroad(boolean isBegin, boolean isEnd, BluetoothDevice device) {
		// 骞挎挱ble鍒楄〃鍙樺寲
		Intent intent = new Intent();
		intent.putExtra("isBegin", isBegin);
		intent.putExtra("isEnd", isEnd);
		intent.putExtra("device", device);
		intent.setAction(BROADCAST_ACTION);
		sendBroadcast(intent); // 鍙戦�骞挎挱
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			/*
			 * for(BluetoothGattCharacteristic characteristic:characteristics){
			 * }
			 */
			crValue.setLength(0);
			final byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				final StringBuilder stringBuilder = new StringBuilder(
						data.length);
				for (byte byteChar : data) {
					stringBuilder.append(String.format("%02X聽", byteChar));
					String s = characteristic.getUuid().toString();
					crValue.append(s.substring(6, 8) + ":"
							+ stringBuilder.toString());
				}
			}
			System.out.println(TAG + " read characteristic:"
					+ crValue.toString());
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			System.out.println(TAG + " onConnectionStateChange status:"
					+ status + "   newState:" + newState);
			super.onConnectionStateChange(gatt, status, newState);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			System.out.println(TAG + " onConnectionStateChange status:"
					+ status + "   rssi:" + rssi);
			super.onReadRemoteRssi(gatt, rssi, status);
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onReliableWriteCompleted(gatt, status);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onServicesDiscovered(gatt, status);
		}

	};

}
