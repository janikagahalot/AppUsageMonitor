package com.appusagemonitor;

import android.app.Fragment;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by janikagahalot on 08/12/15.
 */
public class AppUsageMonitorFragment extends Fragment implements View.OnClickListener, Comparator<UsageStats> {

    private static final String TAG = "AppUsageMonitorFragment";
    AppUsageMonitorListAdapter mAppusageListAdapter;
    UsageStatsManager mUsageStatsManager;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Button mOpenUsageSettingButton;
    boolean mActivityResult = false;

    public static AppUsageMonitorFragment newInstance() {

        AppUsageMonitorFragment fragment = new AppUsageMonitorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsageStatsManager = (UsageStatsManager)getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_usage_monitor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppusageListAdapter = new AppUsageMonitorListAdapter(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mAppusageListAdapter);
        mOpenUsageSettingButton = (Button)view.findViewById(R.id.button_open_usage_setting);
        // List<UsageStats> usageStatsList =
        //        getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
        List<AppUsageDetails> usageStatsList = getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
        if(usageStatsList == null) return;
        updateAppsList(usageStatsList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivityResult) {
            // List<UsageStats> usageStatsList =
            //        getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
            List<AppUsageDetails> usageStatsList = getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);

            if(usageStatsList == null) {
                Toast.makeText(getActivity(),
                        getString(R.string.permission_required),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mActivityResult = false;
            mOpenUsageSettingButton.setVisibility(View.GONE);
            updateAppsList(usageStatsList);
        }

    }


    public List<AppUsageDetails> getUsageStatistics(int intervalType) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.permission_required),
                    Toast.LENGTH_LONG).show();
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(this);
            return null;
        }
        HashMap<String, Long> map = new HashMap<>();
        for (int i = 0; i < queryUsageStats.size(); i++) {
            String packageName = queryUsageStats.get(i).getPackageName();
            Long time = queryUsageStats.get(i).getTotalTimeInForeground();
            if (map.containsKey(packageName)) {
                Long prevTime = map.get(packageName);
                prevTime += time;
                map.put(packageName, prevTime);
            } else {
                map.put(packageName, time);
            }
        }
        HashMap<String, LaunchAndTime> appMap = new HashMap<>();
        for(int i = 0; i < queryUsageStats.size(); i++){
            String packageName = queryUsageStats.get(i).getPackageName();
            Long time = queryUsageStats.get(i).getTotalTimeInForeground();
            LaunchAndTime object =new LaunchAndTime();
            int launchCount1 =1;
            object.setTotalForegound(time);
            object.setCount(launchCount1);
            Log.d("outside ifff", time+ " ");
            if (appMap.containsKey(packageName)) {
                Long prevTime; //= map.get(packageName);
                LaunchAndTime preTime = appMap.get(packageName);
                Log.d("time per launch", preTime.getCount() + "   " + preTime.getTotalForegound());
                prevTime = preTime.getTotalForegound();
                int launchCount = preTime.getCount();
                prevTime += time;
                launchCount++;
                object.setTotalForegound(prevTime);
                object.setCount(launchCount);
                appMap.put(packageName, object);
            } else {
                appMap.put(packageName, object);
            }
        }
        ValueComparator bvc = new ValueComparator(map);
        TreeMap treeMap  = new TreeMap<>(bvc);
        treeMap.putAll(map);

        TimeComparator tvc = new TimeComparator(appMap);
        TreeMap treeMap1 = new TreeMap<>(tvc);
        treeMap1.putAll(appMap);
        /*List<UsageStats> updatedList = new ArrayList<>();
        Iterator it = treeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String , Long> entry = (Map.Entry)it.next();
            Log.d("tag", entry.getKey() + "->" + entry.getValue());
            updatedList.add(getUsageStatObj(entry.getKey(), queryUsageStats));
        }*/
        List<AppUsageDetails> customUsageStatsList = new ArrayList<>();
        List<UsageStats> updatedList = new ArrayList<>();
        Iterator it = treeMap1.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, LaunchAndTime> entry = (Map.Entry)it.next();
            updatedList.add(getUsageStatObj(entry.getKey(), queryUsageStats));
            AppUsageDetails obj = new AppUsageDetails();
            obj.setForegroundTime(entry.getValue().getTotalForegound());
            obj.setLaunchCount(entry.getValue().getCount());
            Log.d("taglalalalalalalalala", entry.getKey() + "->" + entry.getValue().getTotalForegound() + "    " + entry.getValue().getCount());
            obj.setUsageStats(getUsageStatObj(entry.getKey(), queryUsageStats));
            Log.d("thisisisisisisi" , obj.getLaunchCount()+" ");
            customUsageStatsList.add(obj);
        }

        return customUsageStatsList;
    }


    private UsageStats getUsageStatObj(String key, List<UsageStats> usageStatses) {
        for (int i = 0; i < usageStatses.size(); i++) {
            if (usageStatses.get(i).getPackageName().equals(key)) {
                return usageStatses.get(i);
            }

        }
        return null;
    }

    void updateAppsList(List<AppUsageDetails> usageStatsList) {
        List<AppUsageDetails> customUsageStatsList = new ArrayList<>();
        for (int i = 0; i < usageStatsList.size(); i++) {
            AppUsageDetails customUsageStats = new AppUsageDetails();
            customUsageStats.setUsageStats(usageStatsList.get(i).getUsageStats());

            try {
                Drawable appIcon = getActivity().getPackageManager()
                        .getApplicationIcon(customUsageStats.getUsageStats().getPackageName());
                customUsageStats.setAppIcon(appIcon);
                customUsageStats.setForegroundTime(usageStatsList.get(i).getForegroundTime());
                customUsageStats.setLaunchCount(usageStatsList.get(i).getLaunchCount());
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, String.format("App Icon is not found for %s",
                        customUsageStats.getUsageStats().getPackageName()));
            }
            customUsageStatsList.add(customUsageStats);
        }
        mAppusageListAdapter.setCustomUsageStatsList(customUsageStatsList);
        mAppusageListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        mActivityResult = true;
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

    }
    @Override
    public int compare(UsageStats lhs, UsageStats rhs) {
        return Long.compare(rhs.getLastTimeUsed(), lhs.getLastTimeUsed());

    }

    private class ValueComparator implements Comparator {
        HashMap<String, Long> base;

        public ValueComparator(HashMap<String, Long> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(Object a, Object b) {
            if (base.get(a.toString()) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }

    }
    private class TimeComparator implements Comparator {
        HashMap<String, LaunchAndTime> base;

        public TimeComparator(HashMap<String, LaunchAndTime> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(Object a, Object b) {
            if (base.get(a).getTotalForegound() >= base.get(b).getTotalForegound()) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }

    }

    public class LaunchAndTime {

        private long totalForegound;
        private int count;

        public long getTotalForegound() {
            return totalForegound;
        }

        public void setTotalForegound(long totalForegound) {
            this.totalForegound = totalForegound;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }

}
