package com.sunshine.studio.bone.logic;

import android.graphics.Rect;

import com.sunshine.engine.base.Tool;

import java.util.ArrayList;
import java.util.List;

/** Created by Jack on 2018/3/1. */
public class BmpRect {
  public List<Rect> lstRect = new ArrayList<>();
  public String name = null;

  public Rect getRect(int index) {
    if (index != lstRect.size() - 1) {
      lstRect.add(new Rect(0, 0, 0, 0));
    }
    return lstRect.get(lstRect.size() - 1);
  }

  public static String getFrameName(String n) {
    String result = null;
    if (n != null && n.contains("_")) {
      String[] ary = n.split("_");
      if (ary.length == 2) {
        try {
          Integer.parseInt(ary[1]);
          result = ary[0];
        } catch (Exception e) {
          Tool.log(e);
        }
      }
    }
    return result;
  }

  public static int getFrameIndex(String n) {
    int i = 0;
    if (n != null && n.contains("_")) {
      String[] ary = n.split("_");
      if (ary.length == 2) {
        try {
          i = Integer.parseInt(ary[1]);
        } catch (Exception e) {
          Tool.log(e);
        }
      }
    }
    return i;
  }

  public static boolean inSameFrame(String a, String b) {
    String na = getFrameName(a);
    if (na == null) {
      return false;
    } else {
      String nb = getFrameName(b);
      if (na.equals(nb)) {
        return true;
      } else {
        return false;
      }
    }
  }
}
