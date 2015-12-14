package com.appusagemonitor;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by janikagahalot on 08/12/15.
 */
public class AppUsageMonitorListAdapter extends RecyclerView.Adapter<AppUsageMonitorListAdapter.ViewHolder> {

    private static final String TAG = "AppUsageMonitorListAdapter";
    private List<AppUsageDetails> mCustomUsageStatsList = new ArrayList<>();
    private DateFormat mDateFormat = new SimpleDateFormat();
    private Context mContext;



    public AppUsageMonitorListAdapter(Context context) {
        this.mContext = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView mAppName;
    private final TextView mAccessedOn;
    private final ImageView mAppIcon;
        private final TextView mForeground;
        private final TextView mLaunchcount;


        public ViewHolder(View v) {
        super(v);
        mAppName = (TextView) v.findViewById(R.id.textview_app_name);
        mAccessedOn = (TextView) v.findViewById(R.id.textview_last_accessed_on);
        mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
            mForeground = (TextView) v.findViewById(R.id.foreground_time);
            mLaunchcount = (TextView) v.findViewById(R.id.launch_count);
        }

        public TextView getLastTimeUsed() {
            return mAccessedOn;
        }

        public TextView getPackageName() {
            return mAppName;
        }

        public ImageView getAppIcon() {
            return mAppIcon;
        }

        public TextView getForeground() {
            return mForeground;
        }

        public TextView getLaunchcount() {
            return mLaunchcount;
        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_usage_details, viewGroup, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(AppUsageMonitorListAdapter.ViewHolder viewHolder, int position) {

        String packageName = mCustomUsageStatsList.get(position).getUsageStats().getPackageName();
        PackageManager packageManager = mContext.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            String name = packageManager.getApplicationLabel(applicationInfo).toString();
            viewHolder.getPackageName().setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        long lastTimeUsed = mCustomUsageStatsList.get(position).getUsageStats().getLastTimeUsed();
        viewHolder.getLastTimeUsed().setText("  " + mDateFormat.format(new Date(lastTimeUsed)));
        viewHolder.getForeground().setText("Total: " + (mCustomUsageStatsList.get(position).getForegroundTime())/60000 + " mins");
        viewHolder.getLaunchcount().setText("Count "+ mCustomUsageStatsList.get(position).getLaunchCount());
        viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).getAppIcon());

    }

    @Override
    public int getItemCount() {
        return mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<AppUsageDetails> customUsageStats) {
        mCustomUsageStatsList = customUsageStats;
    }
}
