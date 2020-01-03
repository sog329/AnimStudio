package com.sunshine.engine.particle.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.Tool;
import com.sunshine.engine.base.ViewHelper;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scene extends Entity {

  public int maxParticle = 6;
  public List<Particle> lstIdleParticle = new ArrayList<>();
  public List<Particle> lstActiveParticle = new ArrayList<>();
  public List<ParticleModel> lstParticleModel = new ArrayList<>(); // 粒子模型
  private long interval = Tool.NONE;
  private long lastBornTime = Tool.NONE;
  private long drawTime = Tool.NONE;
  private ProcessFloat intensity = new ProcessFloat(0f, 1f, "spring");

  private boolean isManual = false;
  private boolean toActive = false;
  private long manualInterval = Tool.NONE;
  private long manualTime = Tool.NONE;

  public Scene(ViewHelper helper, String configPath, String picPath, String soundPath) {
    super(helper, configPath, picPath, soundPath);
    scriptSize.height = scriptSize.width;
  }

  public void addParticleModel(ParticleModel pm) {
    lstParticleModel.add(pm);
  }

  public ParticleModel getLastParticleModel() {
    ParticleModel particleModel = null;
    if (lstParticleModel.size() > 0) {
      particleModel = lstParticleModel.get(lstParticleModel.size() - 1);
    }
    return particleModel;
  }

  public void setMaxParticle(int max) {
    maxParticle = max;
    lstIdleParticle.clear();
    lstActiveParticle.clear();
    while (lstIdleParticle.size() < maxParticle) {
      lstIdleParticle.add(new Particle());
    }
  }

  @Override
  public void draw(Canvas can) {
    buildActiveParticle();
    renderActiveParticle(can);
    if (!(repeat || isManual) && getPercent() >= 1 && lstActiveParticle.size() == 0 && !inStudio) {
      helper.stop();
    }
  }

  @Override
  public DefaultHandler getParser() {
    return new SceneParser(this);
  }

  @Override
  public boolean needDraw(float percent) {
    return isManual ? lstActiveParticle.size() > 0 || toActive : true;
  }

  private void buildActiveParticle() {
    int numActive =
        (isManual || repeat) ? maxParticle : (int) (intensity.get(getPercent()) * maxParticle);
    if (numActive > lstActiveParticle.size()) {
      int num = maxParticle - lstActiveParticle.size();
      if (judgeBorn(num)) {
        // 产生粒子
        for (int i = 0; i < 1; i++) { // 每帧最多产生粒子数
          int dValue = lstIdleParticle.size();
          if (dValue > 0) {
            Particle p = lstIdleParticle.get(0);
            lastBornTime = Tool.getTime();
            ParticleModel pm = null;
            if (lstParticleModel.size() == 1) {
              pm = lstParticleModel.get(0);
            } else {
              float random = (float) Math.random();
              for (int j = 0; j < lstParticleModel.size(); j++) {
                pm = lstParticleModel.get(j);
                if (pm != null
                    && Tool.isInRange(random, pm.chanceRange.getFrom(), pm.chanceRange.getTo())) {
                  break;
                }
              }
            }
            if (pm != null) {
              pm.build(this, p);
              if (!isManual) {
                if (repeat && dValue > 4) { // 排队如果>4个，立即显示出非出生状态的粒子
                  p.activeTimeStart = drawTime - (long) (p.activeTimeDuration * Math.random());
                }
              }
              lstIdleParticle.remove(p);
              lstActiveParticle.add(p);
            }
          } else {
            break;
          }
        }
      }
    }
  }

  private void renderActiveParticle(Canvas can) {
//    int cs = can.save();
//    can.translate(-viewArea.l, -viewArea.t);
    Iterator<Particle> it = lstActiveParticle.iterator();
    while (it.hasNext()) {
      Particle particle = it.next();
      if (particle.draw(this, can, drawTime)) {
        it.remove();
        lstIdleParticle.add(particle);
      }
    }
//    can.restoreToCount(cs);
  }

  private boolean judgeBorn(int n) {
    boolean b = false;
    if (isManual) {
      b = toActive;
      toActive = false;
    } else {
      if (n > 2) { // 憋到N个，就立即生产1个粒子，避免越憋越多的问题。
        b = true;
      } else {
        long now = Tool.getTime();
        if (Math.abs(now - lastBornTime) >= interval) {
          b = true;
        }
      }
    }
    return b;
  }

  public void setSrcAsync(final Bitmap bitmap, final MediaPlayer sound) {
    super.setSrcAsync(bitmap, sound);
    // 计算粒子诞生间隔
    int sum = 0;
    for (ParticleModel particleModel : lstParticleModel) {
      float p = particleModel.chanceRange.getTo() - particleModel.chanceRange.getFrom();
      long d = (particleModel.activeTime.getFrom() + particleModel.activeTime.getTo()) / 2;
      sum += p * d;
    }
    interval = sum / maxParticle;
  }

  @Override
  public boolean draw(Canvas can, long dt) {
    drawTime = dt;
    return super.draw(can, dt);
  }

  public void isManual(boolean isManual, long manualInterval) {
    this.isManual = isManual;
    this.manualInterval = manualInterval;
  }

  public boolean addParticle() {
    if (isManual) {
      if (toActive) {
        return false;
      } else {
        long now = Tool.getTime();
        if (Math.abs(now - manualTime) >= manualInterval) {
          manualTime = now;
          toActive = true;
          return true;
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
  }
}
