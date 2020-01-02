package com.sunshine.studio.bone.logic;

import android.graphics.Canvas;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.studio.base.RenderHelper;

public class StageRender extends RenderHelper {
  public Callback callback = null;

  @Override
  public void onLoad() {
    if (callback != null) {
      callback.onLoad();
    }
  }

  @Override
  public void onDraw(Canvas can, Entity entity) {
    if (callback != null) {
      callback.draw(can);
      callback.onPercent(entity.getPercent());
    }
  }

  public interface Callback {
    boolean onMove(int x, int y);

    void draw(Canvas can);

    void onLoad();

    void onPercent(float percent);

    void onClickBone(Bone bone);
  }
}
