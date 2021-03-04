package com.sunshine.engine.base;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Tool {
  public static boolean DEBUG = false;
  public static final long NONE = -1l;

  public static boolean equalsFloat(float f1, float f2) {
    return Math.abs(f1 - f2) < .00001f;
  }

  public static boolean equalsZero(float f) {
    return equalsFloat(f, 0f);
  }

  public static Bitmap getBmpByAssets(Context ctx, String str) {
    Bitmap bmp = null;
    if (ctx != null) {
      AssetManager am = ctx.getResources().getAssets();
      if (am != null) {
        InputStream is = null;
        try {
          is = am.open(str);
          bmp = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
          log(e);
        } finally {
          try {
            if (is != null) {
              is.close();
            }
          } catch (Exception e) {
            log(e);
          }
        }
      }
    }
    return bmp;
  }

  public static AssetFileDescriptor getAssetFileDescriptor(Context ctx, String str) {
    AssetFileDescriptor fd = null;
    if (ctx != null) {
      AssetManager am = ctx.getResources().getAssets();
      if (am != null) {
        try {
          fd = am.openFd(str);
        } catch (IOException e) {
          Tool.log(e);
          fd = null;
        }
      }
    }
    return fd;
  }

  public static boolean isInRange(float f, float min, float max) {
    if (Tool.equalsFloat(f, min) || Tool.equalsFloat(f, max)) {
      return true;
    } else {
      return f > min && f < max;
    }
  }

  public static float checkPercent(float percent, float from, float to) {
    if (from > to) {
      float tmp = from;
      from = to;
      to = tmp;
    }
    if (percent > to) {
      percent = to;
    } else if (percent < from) {
      percent = from;
    }
    return percent;
  }

  public static long getTime() {
    return SystemClock.elapsedRealtime();
  }

  public static String[] aryKey(Enum[] aryType) {
    String[] aryStr = new String[aryType.length];
    for (int i = 0; i < aryType.length; i++) {
      aryStr[i] = aryType[i].toString();
    }
    return aryStr;
  }

  public static void log(Exception e) {
    if (DEBUG) {
      e.printStackTrace();
    }
  }

  public static void log(String str) {
    if (DEBUG) {
      Log.d("____", str);
    }
  }

  public static void addLog(List<String> lst, String log) {
    if (lst.size() == 0) {
      lst.add(log);
    } else {
      lst.add("\n" + log);
    }
  }

  public static void addDeviceLog(List<String> lst) {
    addLog(lst, "Build.BRAND=" + Build.BRAND);
    addLog(lst, "Build.MODEL=" + Build.MODEL);
    addLog(lst, "Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
    addLog(lst, "Build.VERSION.RELEASE=" + Build.VERSION.RELEASE);
    addLog(lst, "Build.VERSION.CODENAME=" + Build.VERSION.CODENAME);
  }
}
