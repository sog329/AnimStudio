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
    for (ParticleModel model : entity.lstParticleModel) {
      drawRect(
          can,
          true,
          entity.drawArea.l + entity.scale * model.areaFrom.l,
          entity.drawArea.t + entity.scale * model.areaFrom.t,
          entity.drawArea.l + entity.scale * (model.areaFrom.l + model.areaFrom.w),
          entity.drawArea.t + entity.scale * (model.areaFrom.t + model.areaFrom.h),
          Color.parseColor("#66521a4c"));
      drawRect(
          can,
          true,
          entity.drawArea.l + entity.scale * model.areaTo.l,
          entity.drawArea.t + entity.scale * model.areaTo.t,
          entity.drawArea.l + entity.scale * (model.areaTo.l + model.areaTo.w),
          entity.drawArea.t + entity.scale * (model.areaTo.t + model.areaTo.h),
          Color.parseColor("#66521a4c"));
    }
  }

  public interface Callback {
    void onLoad();
  }
}
