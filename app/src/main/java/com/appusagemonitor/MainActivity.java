package com.appusagemonitor;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.container, AppUsageMonitorFragment.newInstance()).commit();
        }else{
            Toast.makeText(getBaseContext(), getString(R.string.invalid_version), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
