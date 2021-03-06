package cn.modificator.launcher.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.modificator.launcher.Config;
import cn.modificator.launcher.R;
import cn.modificator.launcher.widgets.EInkLauncherView;

/**
 * Created by mod on 16-4-22.
 */
public class AppDataCenter {
  Context mContext;
  private static List<ResolveInfo> mApps;
  int pageIndex = 0;
  int pageCount = 1;
  //列数
  int colNum = 5;
  //行数
  int rowNum = 5;
  EInkLauncherView launcherView;
  TextView pageStatus;
  private Set<String> hideApps = new HashSet<>();

  public static final String wifiPackageName = "E-ink_Launcher.WiFi";
  public static final String oneKeyLockPackageName = "E-ink_Launcher.Lock";
  public static final String notebookPackageName = "E-ink_Launcher.Notebook";

  public AppDataCenter(Context context) {
    this.mContext = context;
    mApps = new ArrayList<>();
//        loadApps();
  }

  public void setLauncherView(EInkLauncherView launcherView) {
    this.launcherView = launcherView;
    this.launcherView.setOnSingleAppHideChangeListener(new EInkLauncherView.OnSingleAppHideChange() {
      @Override
      public void change(String pkg) {
//                if (!hideApps.add(pkg))
//                    hideApps.remove(pkg);
        refreshAppList();
      }
    });
    launcherView.setHideAppPkg(hideApps);
    setPageShow();
  }

  public void setPageStatus(TextView pageStatus) {
    this.pageStatus = pageStatus;
    pageStatus.setText((pageIndex + 1) + "/" + (pageCount + 1));
  }

  public void setHideApps(Set<String> hideApps) {
    this.hideApps.clear();
    this.hideApps.addAll(hideApps);
    loadApps();
  }

  public Set<String> getHideApps() {
    return hideApps;
  }

  private void loadApps() {
    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    if (launcherView != null) {
//        if (launcherView!=null&&!launcherView.getHideAppPkg().isEmpty()) {
      hideApps.clear();
      hideApps.addAll(launcherView.getHideAppPkg());
    }
    mApps.clear();
    for (ResolveInfo resolveInfo : mContext.getPackageManager().queryIntentActivities(mainIntent, 0)) {
      if ("cn.modificator.launcher.Launcher".equals(resolveInfo.activityInfo.name)) continue;
      if (!hideApps.contains(resolveInfo.activityInfo.packageName)) {
        mApps.add(resolveInfo);
      }
    }
    if (!hideApps.contains(oneKeyLockPackageName))
      mApps.add(createPowerIcon());
    if (!hideApps.contains(wifiPackageName))
      mApps.add(createWifiIcon());
//        mApps.addAll();
    if (!hideApps.contains(notebookPackageName))
      mApps.add(createNotebookIcon());
    updatePageCount();
  }

  private void loadAllApps() {
    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    mApps.clear();
    mApps.addAll(mContext.getPackageManager().queryIntentActivities(mainIntent, 0));
    mApps.add(createPowerIcon());
    mApps.add(createWifiIcon());
    mApps.add(createNotebookIcon());
    launcherView.setHideAppPkg(hideApps);
    updatePageCount();
  }

  public void showNextPage() {
    if (pageIndex >= pageCount) return;
    pageIndex++;
    setPageShow();
  }

  public void showLastPage() {
    if (pageIndex <= 0) return;
    pageIndex--;
    setPageShow();
  }

  public void setColNum(int colNum) {
    this.colNum = colNum;
    updatePageCount();
    setPageShow();
  }

  public void setRowNum(int rowNum) {
    this.rowNum = rowNum;
    updatePageCount();
    setPageShow();
  }

  public void refreshAppList() {
    refreshAppList(false);
  }

  public void refreshAppList(boolean showAll) {
    if (showAll) loadAllApps();
    else loadApps();
    setPageShow();
  }
/*
    public void addAppInfo(ResolveInfo info) {
        mApps.add(info);
        hideApps.add(info.activityInfo.packageName);
        updatePageCount();
        setPageShow();
    }

    public void removeAppInfo(ResolveInfo info) {
        mApps.remove(info);
        hideApps.remove(info.activityInfo.packageName);
        updatePageCount();
        setPageShow();
    }*/

  private void setPageShow() {
    int itemCount = colNum * rowNum;
    int pageStart = pageIndex * itemCount;
    int pageEnd = (pageStart + itemCount) > mApps.size() ? mApps.size() : (pageStart + itemCount);
    launcherView.setAppList(mApps.subList(pageStart, pageEnd));
    pageStatus.setText((pageIndex + 1) + "/" + (pageCount + 1));
  }

  private void updatePageCount() {
//        pageCount = (int) Math.ceil(mApps.size() * 1f / (colNum * rowNum));
    pageCount = mApps.size() / (colNum * rowNum) - (mApps.size() % (colNum * rowNum) == 0 ? 1 : 0);
    pageCount = pageCount < 0 ? 0 : pageCount;
    pageIndex = pageIndex > pageCount ? pageCount : pageIndex;
  }

   /* public boolean isHide(String pkg) {
        return hideApps.contains(pkg);
    }

    public void addHide(String pkg) {
        hideApps.add(pkg);
    }

    public void removeHide(String pkg) {
        hideApps.remove(pkg);
    }*/

   private ResolveInfo createWifiIcon(){
     ResolveInfo resolveInfo  = new ResolveInfo();
     resolveInfo.icon = R.drawable.wifi_on;
     resolveInfo.activityInfo = new ActivityInfo();
     resolveInfo.activityInfo.packageName = wifiPackageName;
     return resolveInfo;
   }

   private ResolveInfo createPowerIcon(){
     ResolveInfo resolveInfo = new ResolveInfo();
     resolveInfo.icon = R.drawable.ic_onekeylock;
     resolveInfo.activityInfo = new ActivityInfo();
     resolveInfo.activityInfo.packageName =oneKeyLockPackageName;
     return resolveInfo;
   }

   private ResolveInfo createNotebookIcon() {
     ResolveInfo resolveInfo = new ResolveInfo();
     resolveInfo.icon = R.drawable.notebook;
     resolveInfo.activityInfo = new ActivityInfo();
     resolveInfo.activityInfo.packageName = notebookPackageName;
     return resolveInfo;
   }
}
