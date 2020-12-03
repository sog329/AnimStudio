package com.sunshine.studio.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sunshine.engine.base.Tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/2. */
public class StudioTool {
  public static int screenWidth = 0;
  public static int screenHeight = 0;
  public static final String EXTERNAL = "external";
  public static final String PNG = ".png";

  public static void init(Activity act) {
    Tool.DEBUG = true;

    screenWidth = act.getResources().getDisplayMetrics().widthPixels;
    screenHeight = act.getResources().getDisplayMetrics().heightPixels;
    if (screenWidth < screenHeight) {
      int tmp = screenWidth;
      screenWidth = screenHeight;
      screenHeight = tmp;
    }

    if (initPermission(act)) {
      prepareResource(act);
    }
  }

  private static Toast t = null;

  public static void showToast(Context context, String str) {
    if (t != null) {
      t.cancel();
    }
    t = Toast.makeText(context, str, Toast.LENGTH_SHORT);
    t.show();
  }

  public static void prepareResource(Activity act) {
    prepareFolder(getFilePath("bone"));
    prepareFolder(getFilePath("particle"));

    prepareResource("bone", act, "welcomeDemo", "config.xml", "pic", "pic.plist");
    prepareResource("bone", act, "welcome", "pic", "pic.plist");

    prepareResource("particle", act, "tangyuan", "config.xml", "pic", "pic.plist");
  }

  private static String getSdPath() {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }

  public static String getFilePath(String... aryName) {
    StringBuffer path =
        new StringBuffer().append(getSdPath()).append(File.separator).append("AnimStudio");
    for (String name : aryName) {
      if (name != null) {
        path.append(File.separator).append(name);
      }
    }
    return path.toString();
  }

  private static void prepareFolder(String path) {
    File folder = new File(path);
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  private static void prepareResource(
      String typeName, Activity act, String folderName, String... aryName) {
    try {
      prepareFolder(getFilePath(typeName, folderName));
      AssetManager am = act.getAssets();
      for (String name : aryName) {
        File file = new File(getFilePath(typeName, folderName, name));
        if (!file.exists()) {
          file.createNewFile();
          copyFile(am.open("studio" + File.separator + typeName + File.separator + name), file);
        }
      }
    } catch (Exception e) {
      Tool.log(e);
    }
  }

  public static int getBtnHeight() {
    return screenHeight / 15;
  }

  public static void copyFile(InputStream input, File to) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(to);
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = input.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
      input.close();
      input = null;
      out.flush();
      out.close();
      out = null;
    } catch (Exception e) {
      Tool.log(e);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          Tool.log(e);
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          Tool.log(e);
        }
      }
    }
  }

  public static void deleteFile(File file) {
    if (file != null && file.exists()) {
      if (file.isDirectory()) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
          File f = files[i];
          deleteFile(f);
        }
        file.delete();
      } else if (file.exists()) {
        file.delete();
      }
    }
  }

  public static int getDlgWidth() {
    return screenWidth / 3;
  }

  public static int getDlgHeight() {
    return screenHeight * 2 / 3;
  }

  public static boolean initPermission(@NonNull Activity act) {
    String[] aryPermission =
        new String[] {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> lstRequest = new ArrayList<>();
    for (int i = 0; i < aryPermission.length; i++) {
      if (ContextCompat.checkSelfPermission(act, aryPermission[i])
          != PackageManager.PERMISSION_GRANTED) {
        lstRequest.add(aryPermission[i]);
      }
    }
    boolean result = false;
    if (lstRequest.isEmpty()) {
      result = true;
    } else {
      String[] aryRequest = lstRequest.toArray(new String[lstRequest.size()]);
      ActivityCompat.requestPermissions(act, aryRequest, 1);
    }
    return result;
  }

  public static float format(float f) {
    BigDecimal b = new BigDecimal(f);
    return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
  }

  public static void square(View v, int w, int h, int size) {
    if (w != size || w != h) {
      v.post(
          () -> {
            ViewGroup.LayoutParams lp = v.getLayoutParams();
            lp.width = size;
            lp.height = size;
            v.setLayoutParams(lp);
            v.setPadding(size / 5, size / 5, size / 5, size / 5);
          });
    }
  }

  public static String getPercent(float percent) {
    return (int) (percent * 100) + "%";
  }

  public static Bitmap getBmp(String id) {
    Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    int h = id.hashCode();
    int r = h % 255;
    int g = h / 255;
    bmp.eraseColor(Color.rgb(r, g, 0));
    return bmp;
  }

  public static String getFileName(String name) {
    int n = name.lastIndexOf(".");
    if (n > -1) {
      return name.substring(0, n);
    } else {
      return name;
    }
  }

  public static int getPicDlgWidth() {
    return (int) (StudioTool.getDlgWidth() * 1.5f);
  }

  public static Bitmap getBmp(String path, int w, int h) {
    BitmapFactory.Options o = new BitmapFactory.Options();
    o.inJustDecodeBounds = true;
    o.inSampleSize = 1;
    BitmapFactory.decodeFile(path, o);
    while (o.outWidth > w || o.outHeight > h) {
      o.inSampleSize *= 2;
      BitmapFactory.decodeFile(path, o);
    }
    o.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(path, o);
  }
}
