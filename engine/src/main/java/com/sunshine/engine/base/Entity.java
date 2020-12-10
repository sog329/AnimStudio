package com.sunshine.engine.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;

import com.sunshine.engine.particle.logic.Particle;

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
  public boolean repeat = false;
  public DrawInfo drawInfo = new DrawInfo();
  public boolean parsed = false;
  public Map<String, Bitmap> mapBmp = new HashMap<>();
  protected boolean recycleBmp = true;
  public Map<String, Render2D.Callback> map2D = new HashMap<>();
  public Map<String, Click> mapClick = new HashMap<>();
  public Map<String, Rect> mapRc = new HashMap<>();
  protected Runnable onStop = null;

  public Entity(ViewHelper helper, String configPath, String picPath, String soundPath) {
    this.helper = helper;
    this.configPath = configPath;
    this.picPath = picPath;
    this.soundPath = soundPath;
  }

  public void destroy() {
    if (bmp != null) {
      helper.addLog("destroy bmp.hashCode()=" + bmp.hashCode());
      bmp.recycle();
      bmp = null;
    }
    if (sound != null) {
      sound.stop();
      sound.release();
      sound = null;
    }
    if (recycleBmp) {
      for (Bitmap bmp : mapBmp.values()) {
        if (bmp != null) {
          bmp.recycle();
        }
      }
    }
    mapBmp.clear();
    if (onStop != null) {
      onStop.run();
    }
  }

  public void setSrcAsync(final Bitmap bitmap, final MediaPlayer soundPlayer) {
    ViewHelper.handler.post(
        () -> {
          if (this.equals(helper.entity)) {
            bmp = bitmap;
            sound = soundPlayer;
            helper.invalidate();
            helper
                .addLog("in setSrcAsync set bmp")
                .addLog("   this.hashCode()=" + this.hashCode())
                .addLog("   helper.entity.hashCode()=" + helper.entity.hashCode())
                .addLog("   bmp.hashCode()=" + bitmap.hashCode());
          } else {
            if (bitmap != null && !bitmap.isRecycled()) {
              Entity hEntity = helper.entity;
              helper
                  .addLog("in setSrcAsync recycle bmp")
                  .addLog("   this.hashCode()=" + this.hashCode())
                  .addLog(
                      "   helper.entity.hashCode()=" + (hEntity == null ? -1 : hEntity.hashCode()))
                  .addLog("   bmp.hashCode()=" + bitmap.hashCode());
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
    } else if (bmp.isRecycled()) {
      helper
          .addLog("before draw")
          .addLog("   bmp.isRecycled()")
          .addLog("   entity.configPath=" + configPath)
          .addLog("   entity.picPath=" + picPath)
          .addLog("   entity.hashCode()=" + hashCode())
          .addLog("   bmp.hashCode()=" + bmp.hashCode())
          .onError();
      bmp = null;
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
      try {
        draw(can);
      } catch (Throwable e) {
        helper
            .addLog("in draw")
            .addLog("   bmp.isRecycled()")
            .addLog("   exp=" + e.toString())
            .addLog("   entity.configPath=" + configPath)
            .addLog("   entity.picPath=" + picPath)
            .addLog("   entity.hashCode()=" + hashCode())
            .addLog("   bmp.hashCode()=" + bmp.hashCode())
            .onError();
        bmp = null;
        return false;
      }
      if (sound != null && Tool.equalsZero(percent) && duration > 0) {
        if (sound.isPlaying()) {
          sound.stop();
        }
        sound.start();
      }
      return needDraw(percent);
    }
  }

  public void mergeDrawInfo(Matrix m) {
    m.reset();
    // move
    m.preTranslate(drawArea.l, drawArea.t);
    m.preScale(scale, scale);
    m.mapRect(drawInfo.rcDst, drawInfo.rcSrc);
    // rotate
    drawInfo.ptDst.x = drawInfo.ptSrc.x * scale + drawArea.l;
    drawInfo.ptDst.y = drawInfo.ptSrc.y * scale + drawArea.t;
    m.preRotate(drawInfo.rt, drawInfo.ptSrc.x, drawInfo.ptSrc.y);
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

  public void setOnStop(Runnable rn) {
    onStop = rn;
  }

  public abstract void draw(Canvas can);

  public abstract DefaultHandler getParser();

  public abstract boolean needDraw(float percent);

  public interface Click {}

  public interface ClickId extends Click {
    void onClick(String id);
  }

  public interface ClickRect extends Click {
    void onClick(String id, RectF rect, int x, int y);
  }

  public interface ClickParticle extends Click {
    void onClick(String id, Particle p);
  }
}
