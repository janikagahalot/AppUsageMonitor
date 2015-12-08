package com.appusagemonitor;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView mAppName;
    private final TextView mAccessedOn;
    private final ImageView mAppIcon;

    public ViewHolder(View v) {
        super(v);
        mAppName = (TextView) v.findViewById(R.id.textview_app_name);
        mAccessedOn = (TextView) v.findViewById(R.id.textview_last_accessed_on);
        mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
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

}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_usage_details, viewGroup, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(AppUsageMonitorListAdapter.ViewHolder viewHolder, int position) {

        viewHolder.getPackageName().setText(
                mCustomUsageStatsList.get(position).usageStats.getPackageName());
        long lastTimeUsed = mCustomUsageStatsList.get(position).usageStats.getLastTimeUsed();
        viewHolder.getLastTimeUsed().setText(mDateFormat.format(new Date(lastTimeUsed)));
        viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).appIcon);

    }

    @Override
    public int getItemCount() {
        return mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<AppUsageDetails> customUsageStats) {
        mCustomUsageStatsList = customUsageStats;
    }
}
