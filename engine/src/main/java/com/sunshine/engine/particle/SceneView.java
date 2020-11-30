package com.sunshine.engine.particle;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.sunshine.engine.base.AnimView;
import com.sunshine.engine.base.Entity.Click;
import com.sunshine.engine.base.Entity.ClickId;
import com.sunshine.engine.base.Entity.ClickParticle;
import com.sunshine.engine.base.Entity.ClickRect;
import com.sunshine.engine.particle.logic.Particle;
import com.sunshine.engine.particle.logic.SceneHelper;

public class SceneView extends AnimView<SceneHelper> {
  public SceneView(Context context) {
    super(context);
  }

  public SceneView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SceneView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public SceneHelper buildHelper() {
    return new SceneHelper();
  }

  /**
   * 设置手动模式
   *
   * @param isManual 是否为手动模式
   * @param interval 手动模式增加粒子的间隔
   */
  public void isManual(boolean isManual, int interval) {
    if (helper.entity != null) {
      helper.entity.isManual(isManual, interval);
    }
  }

  /**
   * 手动模式下增加粒子
   *
   * @return
   */
  public boolean addParticle() {
    if (helper.entity == null) {
      return false;
    } else {
      boolean result = helper.entity.addParticle();
      if (result) {
        invalidate();
      }
      return result;
    }
  }

  @Override
  protected void onClick(int x, int y) {
    RectF rc = new RectF();
    for (int i = helper.entity.lstActiveParticle.size() - 1; i > -1; i--) {
      Particle p = helper.entity.lstActiveParticle.get(i);
      if (p.showing) {
        p.m.mapRect(rc, p.rc);
        if (rc.contains(x, y)) {
          Click click = helper.entity.mapClick.get(p.name);
          if (click != null) {
            if (click instanceof ClickParticle) {
              ((ClickParticle) click).onClick(p.name, p);
            } else if (click instanceof ClickId) {
              ((ClickId) click).onClick(p.name);
            } else if (click instanceof ClickRect) {
              ((ClickRect) click).onClick(p.name, rc, x, y);
            }
          }
          break;
        }
      }
    }
  }
}
