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
