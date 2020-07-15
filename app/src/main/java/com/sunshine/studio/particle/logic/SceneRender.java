package com.sunshine.studio.particle.logic;

import android.graphics.Canvas;
import android.graphics.Color;

import com.sunshine.engine.particle.logic.ParticleModel;
import com.sunshine.engine.particle.logic.Scene;
import com.sunshine.studio.base.RenderHelper;

public class SceneRender extends RenderHelper<Scene> {
  public Callback callback = null;

  @Override
  public void onLoad() {
    if (callback != null) {
      callback.onLoad();
    }
  }

  @Override
  public void onDraw(Canvas can, Scene entity) {
    if (callback != null) {
      ParticleModel model = callback.getModel();
      if (model != null) {
        // color
        // start
        int fromLeft = model.areaFrom.getScriptLeft(entity);
        int fromWidth = model.areaFrom.getScriptWidth(entity, fromLeft);
        int fromTop = model.areaFrom.t;
        int fromHeight = model.areaFrom.h;
        drawRect(
            can,
            true,
            entity.drawArea.l + entity.scale * fromLeft,
            entity.drawArea.t + entity.scale * fromTop,
            entity.drawArea.l + entity.scale * (fromLeft + fromWidth),
            entity.drawArea.t + entity.scale * (fromTop + fromHeight),
            Color.argb(
                102,
                Color.red(cb.colorLight()),
                Color.green(cb.colorLight()),
                Color.blue(cb.colorLight())));
        // end
        if (model.areaTo.isOffsetLeft) {
          fromLeft += model.areaTo.l;
          fromWidth += model.areaTo.w;
        } else {
          fromLeft = model.areaTo.getScriptLeft(entity);
          fromWidth = model.areaTo.getScriptWidth(entity, fromLeft);
        }
        if (model.areaTo.isOffsetTop) {
          fromTop += model.areaTo.t;
          fromHeight += model.areaTo.h;
        } else {
          fromTop = model.areaTo.t;
          fromHeight = model.areaTo.h;
        }
        drawRect(
            can,
            true,
            entity.drawArea.l + entity.scale * fromLeft,
            entity.drawArea.t + entity.scale * fromTop,
            entity.drawArea.l + entity.scale * (fromLeft + fromWidth),
            entity.drawArea.t + entity.scale * (fromTop + fromHeight),
            Color.argb(
                187,
                Color.red(cb.colorDark()),
                Color.green(cb.colorDark()),
                Color.blue(cb.colorDark())));
      }
    }
  }

  public interface Callback {
    void onLoad();

    ParticleModel getModel();
  }
}
