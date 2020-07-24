package com.sunshine.engine.base;

import com.sunshine.engine.particle.logic.Scene;
import com.sunshine.engine.particle.logic.SceneParser;

/** Created by songxiaoguang on 2017/9/13. */
public class Area {
  public static final int MATCH_PARENT = -1;
  public boolean isRotate = false;
  public int l = 0;
  public boolean isOffsetLeft = false;
  public int t = 0;
  public boolean isOffsetTop = false;
  public int w = 100;
  public int h = 100;
  private Point<Integer> pt = new Point(0, 0);

  public Point<Integer> getPoint(Scene scene) {
    int left = getScriptLeft(scene);
    int width = getScriptWidth(scene, left);
    pt.x = left + (int) (width * Math.random());
    pt.y = t + (int) (h * Math.random());
    return pt;
  }

  public int getScriptLeft(Scene scene) {
    if (w == MATCH_PARENT) {
      //      return (int) (l - scene.drawArea.l / scene.scale);
      return (int) ((scene.viewArea.l - scene.drawArea.l) / scene.scale) + l;
    } else {
      return l;
    }
  }

  public int getScriptWidth(Scene scene, int left) {
    if (w == MATCH_PARENT) {
      //      return (int) (scene.viewArea.w / scene.scale) - left;
      return (int) (scene.viewArea.w / scene.scale);
    } else {
      return w;
    }
  }

  public Point<Integer> getPoint(Scene sc, int fromX, int fromY) {
    getPoint(sc);
    if (isOffsetLeft) {
      pt.x += fromX;
    }
    if (isOffsetTop) {
      pt.y += fromY;
    }
    return pt;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    // left
    if (isOffsetLeft) {
      sb.append(SceneParser.OFFSET);
      sb.append(":");
    }
    sb.append(l);
    sb.append(",");
    // top
    if (isOffsetTop) {
      sb.append(SceneParser.OFFSET);
      sb.append(":");
    }
    sb.append(t);
    sb.append(",");
    // w
    if (w == MATCH_PARENT) {
      sb.append(SceneParser.MATCH_PARENT);
    } else {
      sb.append(w);
    }
    sb.append(",");
    // h
    sb.append(h);
    return sb.toString();
  }
}
