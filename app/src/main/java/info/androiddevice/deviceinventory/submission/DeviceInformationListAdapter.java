package info.androiddevice.deviceinventory.submission;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeviceInformationListAdapter extends BaseAdapter {
    public static final int MAX_LINES = 10;

    private final LayoutInflater mInflater;
    private final int mWhiteColor;
    private final int mGreyColor;
    private List<KeyValuePair> mItems;
    private String mDeviceName;

    public DeviceInformationListAdapter(Context context, JSONObject deviceInformation){
        mInflater = LayoutInflater.from(context);
        mItems = new ArrayList<KeyValuePair>();
        mWhiteColor = context.getResources().getColor(android.R.color.white);
        mGreyColor = context.getResources().getColor(R.color.subtext_grey);
        JSONArray keys = deviceInformation.names();
        for(int ii = 0 ; ii < keys.length(); ++ii) {
            try {
                final String key = keys.getString(ii);
                final Object value = deviceInformation.get(key);
                final String valueString = String.valueOf(value);
                mItems.add(new KeyValuePair(key, value, valueString, key.equals("name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(mItems, new Comparator<KeyValuePair>() {
            @Override
            public int compare(KeyValuePair lhs, KeyValuePair rhs) {
                if(lhs.isEditable){
                    return -1;
                }
                return lhs.key.compareTo(rhs.key);
            }
        });
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public KeyValuePair getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        return getItem(position).isEditable;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(null == view){
            view = mInflater.inflate(R.layout.editable_list_item, parent, false);
        }
        KeyValuePair pair = getItem(position);
        if(null != pair){
            TextView key = ((TextView)view.findViewById(android.R.id.text1));
            key.setText(pair.key);
            key.setTextColor(mWhiteColor);

            final TextView value = ((TextView)view.findViewById(android.R.id.text2));
            final TextView editable  = ((TextView)view.findViewById(R.id.editable));

            if(pair.isEditable){
                value.setVisibility(View.GONE);
                editable.setVisibility(View.VISIBLE);
                if(null != mDeviceName && mDeviceName.length() > 0) {
                    editable.setText(mDeviceName);
                }
            } else {
                value.setVisibility(View.VISIBLE);
                editable.setVisibility(View.GONE);
                value.setText(pair.valueString);
                value.setTextColor(mGreyColor);
                value.setMaxLines(10);
                value.setEllipsize(TextUtils.TruncateAt.END);
            }

            //details.setVisibility(TextView.GONE);

//            ((TextView)view.findViewById(android.R.id.text1)).setOnClickListener(new View.OnClickListener() {
//                private TextView detailsItem = details;
//                @Override
//                public void onClick(View view) {
//                    if(detailsItem.getVisibility() == TextView.VISIBLE) {
//                        detailsItem.setVisibility(TextView.GONE);
//                    } else if(detailsItem.getVisibility() == TextView.GONE) {
//                        detailsItem.setVisibility(TextView.VISIBLE);
//                    }
//                }
//            });
        }
        return view;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
        notifyDataSetChanged();
    }

    static class KeyValuePair {
        String key;
        Object value;
        String valueString;
        boolean isEditable = false;

        KeyValuePair(String key, Object value, String valueString, boolean isEditable){
            this.key = key;
            this.value = value;
            this.valueString = valueString;
            this.isEditable = isEditable;
        }
    }
}
