package com.anki.desk.activity.adapter;

import java.util.ArrayList;

import com.anki.desk.R;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeDeviceListAdapter extends BaseAdapter {
	
	private ArrayList<BluetoothDevice> mLeDevices;
    private LayoutInflater mInflator;

    public LeDeviceListAdapter(LayoutInflater mInflator) {
        super();
        this.mInflator = mInflator;
        mLeDevices = new ArrayList<BluetoothDevice>();
    }

    public void addDevices(BluetoothDevice object) {
        if(mLeDevices == null){
        	mLeDevices = new ArrayList<BluetoothDevice>();
        }
        mLeDevices.add(object);
    }
    
    
    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.ble_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.ble_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.ble_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BluetoothDevice device = mLeDevices.get(position);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText("Unkown");
        viewHolder.deviceAddress.setText(device.getAddress());

        return view;
	}
}


class ViewHolder {
    TextView deviceName;
    TextView deviceAddress;
}
