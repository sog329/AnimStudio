package com.sunshine.studio.base;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sunshine.engine.base.Tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/2. */
public class StudioTool {
  public static int screenWidth = 0;
  public static int screenHeight = 0;

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

  public static void prepareResource(Activity act) {
    prepareFolder(getFilePath("bone"));
    prepareFolder(getFilePath("particle"));

    prepareResource("bone", act, "welcomeDemo", "config.xml", "pic", "pic.plist");
    prepareResource("bone", act, "welcome", "pic", "pic.plist");

    prepareResource("particle", act, "welcomeDemo", "pic", "pic.plist");
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
    String[] aryPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> lstRequest = new ArrayList<>();
    for (int i = 0; i < aryPermission.length; i++) {
      if (ContextCompat.checkSelfPermission(act, aryPermission[i]) != PackageManager.PERMISSION_GRANTED) {
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
}
