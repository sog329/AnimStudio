package com.sunshine.engine.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class Entity {
  public ViewHelper helper = null;
  public String configPath = null;
  public String picPath = null;
  public String soundPath = null;
  public Bitmap bmp = null;
  public MediaPlayer sound = null;
  public float scale = 0f;
  public Size<Integer> scriptSize = new Size(720, 1280);
  public Area viewArea = new Area();
  public Area drawArea = new Area();
  public String layoutType = LayoutType.center.toString();
  public boolean useScriptDuration = true;
  public int duration = 6000;
  public boolean inStudio = false;
  protected long firstDrawTime = Tool.NONE;
  public ProcessFloat percentRange = new ProcessFloat(0f, 1f);
  private float percent = 0;
  public boolean autoStop = true;
  public boolean repeat = false;
  public DrawInfo drawInfo = new DrawInfo();
  public boolean parsed = false;
  public Map<String, Bitmap> mapBmp = new HashMap<>();

  public Entity(ViewHelper helper, String configPath, String picPath, String soundPath) {
    this.helper = helper;
    this.configPath = configPath;
    this.picPath = picPath;
    this.soundPath = soundPath;
  }

  public void destroy() {
    if (bmp != null) {
      bmp.recycle();
      bmp = null;
    }
    if (sound != null) {
      sound.stop();
      sound.release();
      sound = null;
    }
    for (Bitmap bmp : mapBmp.values()) {
      if (bmp != null) {
        bmp.recycle();
      }
    }
    mapBmp.clear();
  }

  public void setSrcAsync(final Bitmap bitmap, final MediaPlayer soundPlayer) {
    ViewHelper.handler.post(
        () -> {
          if (this.equals(helper.entity)) {
            bmp = bitmap;
            sound = soundPlayer;
            helper.invalidate();
          } else {
            if (bitmap != null && !bitmap.isRecycled()) {
              bitmap.recycle();
            }
            if (soundPlayer != null) {
              soundPlayer.stop();
              soundPlayer.release();
            }
          }
        });
  }

  public void setPercent(float from, float to, int duration) {
    firstDrawTime = Tool.NONE;
    percent = from;
    if (inStudio) {
      useScriptDuration = true;
      percentRange.set(from, to);
    } else {
      useScriptDuration = false;
      this.duration = duration;
      percentRange.set(from, to);
    }
  }

  public float getPercent() {
    return percent;
  }

  public boolean draw(Canvas can, long dt) {
    if (Tool.equalsZero(scale)) {
      return false;
    }
    if (bmp == null) {
      return false;
    } else {
      if (firstDrawTime == Tool.NONE) {
        firstDrawTime = dt;
      }
      if (duration > 0) {
        percent = percentRange.get((float) (dt - firstDrawTime) / duration);
      } else {
        percent = percentRange.getTo();
      }
      percent = Tool.checkPercent(percent, percentRange.getFrom(), percentRange.getTo());
      draw(can);
      if (sound != null && Tool.equalsZero(percent) && duration > 0) {
        if (sound.isPlaying()) {
          sound.stop();
        }
        sound.start();
      }
      return needDraw(percent);
    }
  }

  public void mergeDrawInfo() {
    // move
    drawInfo.rcDst.left = (int) (drawInfo.rcSrc.left * scale) + drawArea.l;
    drawInfo.rcDst.top = (int) (drawInfo.rcSrc.top * scale) + drawArea.t;
    drawInfo.rcDst.right = (int) (drawInfo.rcSrc.right * scale) + drawArea.l;
    drawInfo.rcDst.bottom = (int) (drawInfo.rcSrc.bottom * scale) + drawArea.t;
    // rotate
    drawInfo.ptDst.x = (int) (drawInfo.ptSrc.x * scale) + drawArea.l;
    drawInfo.ptDst.y = (int) (drawInfo.ptSrc.y * scale) + drawArea.t;
  }

  public void isMute(boolean mute) {
    if (sound != null) {
      if (mute) {
        sound.setVolume(0, 0);
      } else {
        sound.setVolume(1, 1);
      }
    }
  }

  public abstract void draw(Canvas can);

  public abstract DefaultHandler getParser();

  public abstract boolean needDraw(float percent);
}
