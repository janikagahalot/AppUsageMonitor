package com.appusagemonitor;

import android.app.usage.UsageStats;
import android.graphics.drawable.Drawable;

/**
 * Created by janikagahalot on 08/12/15.
 */
public class AppUsageDetails {

    private static final String TAG = "AppUsageDetails";
    private UsageStats usageStats;
    private Drawable appIcon;
    private long foregroundTime;
    private int launchCount;

    public long getForegroundTime() {
        return foregroundTime;
    }

    public void setForegroundTime(long foregroundTime) {
        this.foregroundTime = foregroundTime;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public UsageStats getUsageStats() {
        return usageStats;
    }

    public void setUsageStats(UsageStats usageStats) {
        this.usageStats = usageStats;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
