package com.sunshine.engine.particle.logic;

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

  public void setRcBmp(Rect rect) {
    rcBmp.set(rect);
  }

  protected boolean draw(Scene scene, Canvas can, long drawTime) {
    m.reset();
    showing = false;
    rc.set(0, 0, 0, 0);

    if (activeTimeStart == Tool.NONE) {
      activeTimeStart = drawTime;
    }
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
  }

  protected void end() {
    showing = false;
    name = null;
    activeTimeStart = Tool.NONE;
  }
}
