package com.sunshine.engine.particle.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sunshine.engine.base.Render2D;
import com.sunshine.engine.base.Skin;
import com.sunshine.engine.base.Tool;

public class Particle extends Skin {
  protected int activeTimeDuration = 1000;
  protected long activeTimeStart = Tool.NONE;
  protected Rect rcBmp = new Rect();
  protected Anim anim = new Anim();
  public String externalId = null;

  public void setRcBmp(Rect rect) {
    rcBmp.set(rect);
  }

  /**
   * @param scene
   * @param can
   * @param drawTime
   * @return true 表示已走完生命周期，粒子进入idle队列
   */
  protected boolean draw(Scene scene, Canvas can, long drawTime) {
    m.reset();
    showing = false;
    rc.set(0, 0, 0, 0);

    if (activeTimeStart == Tool.NONE) {
      activeTimeStart = drawTime;
    }
    if (externalId == null) {
      float rp = (float) (drawTime - activeTimeStart) / activeTimeDuration;
      if (rp <= 1) {
        if (anim.run(rp, scene)) {
          showing = true;
          rc.set(scene.drawInfo.rcSrc);
          scene.mergeDrawInfo(m);
          Render2D.draw(can, scene.bmp, rcBmp, scene.drawInfo);
        }
        return false;
      } else {
        end();
        return true;
      }
    } else {
      Bitmap bmp = scene.mapBmp.get(externalId);
      Render2D.Callback cb = bmp == null ? scene.map2D.get(externalId) : null;
      if (bmp != null || cb != null) {
        float rp = (float) (drawTime - activeTimeStart) / activeTimeDuration;
        if (rp <= 1) {
          if (anim.run(rp, scene)) {
            showing = true;
            rc.set(scene.drawInfo.rcSrc);
            scene.mergeDrawInfo(m);
            if (bmp != null) {
              Render2D.draw(can, bmp, null, scene.drawInfo);
            } else {
              Render2D.draw(can, cb, rp, scene.drawInfo, scene.scale);
            }
          }
          return false;
        } else {
          end();
          return true;
        }
      } else {
        return false;
      }
    }
  }

  protected void end() {
    showing = false;
    name = null;
    activeTimeStart = Tool.NONE;
  }
}
