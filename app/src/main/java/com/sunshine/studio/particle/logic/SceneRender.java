package com.sunshine.studio.particle.logic;

import android.graphics.Canvas;

import com.sunshine.engine.base.Entity;
import com.sunshine.studio.base.RenderHelper;

public class SceneRender extends RenderHelper {
  public Callback callback = null;

  @Override
  public void onLoad() {
    if (callback != null) {
      callback.onLoad();
    }
  }

  @Override
  public void onDraw(Canvas can, Entity entity) {}

  public interface Callback {
    void onLoad();
  }
}
