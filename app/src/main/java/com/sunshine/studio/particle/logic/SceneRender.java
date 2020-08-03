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
        int left = model.areaFrom.getScriptLeft(entity);
        int width = model.areaFrom.getScriptWidth(entity, left);
        int top = model.areaFrom.t;
        int height = model.areaFrom.h;
        drawRect(
            can,
            true,
            entity.drawArea.l + entity.scale * left,
            entity.drawArea.t + entity.scale * top,
            entity.drawArea.l + entity.scale * (left + width),
            entity.drawArea.t + entity.scale * (top + height),
            Color.argb(
                102,
                Color.red(cb.getLightColor()),
                Color.green(cb.getLightColor()),
                Color.blue(cb.getLightColor())));
        // end
        if (model.areaTo.isOffsetLeft) {
          left += model.areaTo.l;
          width += model.areaTo.w;
        } else {
          left = model.areaTo.getScriptLeft(entity);
          width = model.areaTo.getScriptWidth(entity, left);
        }
        if (model.areaTo.isOffsetTop) {
          top += model.areaTo.t;
          height += model.areaTo.h;
        } else {
          top = model.areaTo.t;
          height = model.areaTo.h;
        }
        drawRect(
            can,
            true,
            entity.drawArea.l + entity.scale * left,
            entity.drawArea.t + entity.scale * top,
            entity.drawArea.l + entity.scale * (left + width),
            entity.drawArea.t + entity.scale * (top + height),
            Color.argb(
                187,
                Color.red(cb.getDarkColor()),
                Color.green(cb.getDarkColor()),
                Color.blue(cb.getDarkColor())));
      }
    }
  }

  public interface Callback {
    void onLoad();

    ParticleModel getModel();
  }
}
