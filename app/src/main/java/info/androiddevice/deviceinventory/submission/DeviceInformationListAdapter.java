package info.androiddevice.deviceinventory.submission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceInformationListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final int mWhiteColor;
    private final int mGreyColor;
    private List<KeyValuePair> mItems;

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
                mItems.add(new KeyValuePair(key, value));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(null == view){
            view = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        KeyValuePair pair = getItem(position);
        if(null != pair){
            TextView key = ((TextView)view.findViewById(android.R.id.text1));
            key.setText(pair.key);
            key.setTextColor(mWhiteColor);

            final TextView value = ((TextView)view.findViewById(android.R.id.text2));
            value.setText(String.valueOf(pair.value));
            value.setTextColor(mGreyColor);

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

    static class KeyValuePair{
        String key;
        Object value;
        KeyValuePair(String key, Object value){
            this.key = key;
            this.value = value;
        }
    }
}
