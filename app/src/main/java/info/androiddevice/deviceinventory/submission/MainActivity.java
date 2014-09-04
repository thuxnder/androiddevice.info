package info.androiddevice.deviceinventory.submission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import info.androiddevice.deviceinventory.Application;
import info.androiddevice.deviceinventory.DeviceInformation;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private SharedPreferences settings = Application.getContext().getSharedPreferences("inventory", MODE_PRIVATE);
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerOptions;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mListView;
    private DeviceInformationListAdapter mInfoAdapter;
    private AlertDialog mAlertDialog;
    private String mDeviceName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerOptions = (ListView) findViewById(R.id.left_drawer);

        mListView = (ListView) findViewById(R.id.container);
//        mListView.setItemsCanFocus(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                   showDeviceNameInputDialog();

                } else {
                    final TextView value = ((TextView) view.findViewById(android.R.id.text2));
                    if (value.getVisibility() != View.GONE) {
                        if (value.getEllipsize() == TextUtils.TruncateAt.END) {
                            value.setMaxLines(Integer.MAX_VALUE);
                            value.setEllipsize(null);
                        } else {
                            value.setMaxLines(DeviceInformationListAdapter.MAX_LINES);
                            value.setEllipsize(TextUtils.TruncateAt.END);
                        }
                    }
                }
            }
        });
        initNavigationDrawer();

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_background));


    }

    /**
     * Creates and displays a dialog for the user to enter his or her device name
     */
    private void showDeviceNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.setView(LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.device_name_input, null, false))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) ((AlertDialog)dialog).findViewById(R.id.editable);
                        mDeviceName = editText.getText().toString();
                        mInfoAdapter.setDeviceName(mDeviceName);
                        settings.edit().putString("deviceName", mDeviceName).commit();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
        buttonStyleHelper(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
        final Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        EditText editText = (EditText) dialog.findViewById(R.id.editable);
        if(null == mDeviceName || 0 == mDeviceName.length()){
            positiveButton.setEnabled(false);
        } else {
            editText.setText(mDeviceName);
        }
        buttonStyleHelper(positiveButton);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveButton.setEnabled(count > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        editText.requestFocus();
    }

    private void buttonStyleHelper(Button b){
        b.setBackgroundResource(R.drawable.black_background_state);
        b.setTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();
        JSONObject deviceInformation = DeviceInformation.getInstance().getDeviceInformation();
        mInfoAdapter = new DeviceInformationListAdapter(MainActivity.this, deviceInformation);
        mDeviceName = settings.getString("deviceName", null);
        mInfoAdapter.setDeviceName(mDeviceName);
        mListView.setAdapter(mInfoAdapter);

        if(!hasBeenSubmitted()) {
            Boolean autoSubmit;
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
                autoSubmit = applicationInfo.metaData.getBoolean("auto_submit", false);
            } catch (PackageManager.NameNotFoundException e) {
                autoSubmit = false;
            } catch (NullPointerException e) {
                autoSubmit = false;
            }

            if(autoSubmit) {
                submit();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if(item.getItemId() == android.R.id.home){
            if(mDrawerLayout.isDrawerOpen(mDrawerOptions)){
                mDrawerLayout.closeDrawers();
            } else{
                mDrawerLayout.openDrawer(mDrawerOptions);
            }
            return true;
        } else if(item.getItemId() == R.id.send){
             submit();
         }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigationDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon */
                0, /* "open drawer" description */
                0  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                drawerView.bringToFront();
                supportInvalidateOptionsMenu();
            }
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.list_item, new String[]{getString(R.string.action_about), getString(R.string.action_send)});

        mDrawerOptions.setAdapter(adapter);
        mDrawerOptions.setOnItemClickListener(MainActivity.this);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private synchronized void submit() {
        LinearLayout alertLayout = (LinearLayout)LayoutInflater.from(MainActivity.this).inflate(R.layout.sending_alert,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false).setView(alertLayout).setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlertDialog = builder.show();
        //Button isn't null after show
        Button button = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        button.setBackgroundResource(R.drawable.black_background_state);
        button.setTextColor(MainActivity.this.getResources().getColor(android.R.color.white));

        if(hasBeenSubmitted()) {
            ((TextView)mAlertDialog.findViewById(android.R.id.text1)).setText(R.string.alreadySent);
            mAlertDialog.findViewById(R.id.sending_progressbar).setVisibility(View.GONE);
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
            HttpClientTask httpClientTask = new HttpClientTask();
            httpClientTask.execute();
        }
    }

    protected void submitted(Integer errorCode) {
        mAlertDialog.findViewById(R.id.sending_progressbar).setVisibility(View.GONE);
        if(errorCode==200) {
            ((TextView)mAlertDialog.findViewById(android.R.id.text1)).setText(R.string.success);
            setBeenSubmitted(true);
        } else {
            ((TextView)mAlertDialog.findViewById(android.R.id.text1)).setText(R.string.failure);
            setBeenSubmitted(false);
        }
        Button button = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        button.setEnabled(true);
    }

    private void about() {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about)));
        startActivity(myIntent);
    }

    protected boolean hasBeenSubmitted() {
        return settings.getBoolean("submitted", false);
    }

    protected void setBeenSubmitted(boolean value) {
        settings.edit().putBoolean("submitted", value).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            about();
        } else if(position == 1) {
            submit();
        }
    }


    private class HttpClientTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(getString(R.string.submit));
                final JSONObject deviceInformation = DeviceInformation.getInstance().getDeviceInformation();
                if(null != mDeviceName && 0 != mDeviceName.length()) {
                    try {
                        deviceInformation.put("name", mDeviceName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                List<NameValuePair> body = new ArrayList<NameValuePair>() {{
                    add(new BasicNameValuePair("device", deviceInformation.toString()));
                }};
                httpPost.setEntity(new UrlEncodedFormEntity(body));
                HttpResponse response = httpClient.execute(httpPost);
                return response.getStatusLine().getStatusCode();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            submitted(integer);
        }
    }

}
