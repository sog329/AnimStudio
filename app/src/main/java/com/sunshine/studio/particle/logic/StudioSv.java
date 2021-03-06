package com.sunshine.studio.particle.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sunshine.engine.particle.SceneView;
import com.sunshine.engine.particle.logic.Scene;
import com.sunshine.studio.base.RenderHelper;
import com.sunshine.studio.base.Studio;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioSv extends SceneView implements Studio.Callback<Scene> {
  private SceneRender render = new SceneRender();

  public StudioSv(Context context) {
    super(context);
  }

  public StudioSv(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StudioSv(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallback(SceneRender.Callback callback, RenderHelper.Callback cb) {
    render.callback = callback;
    render.cb = cb;
  }

  @Override
  public void onDraw(Canvas can) {
    render.preOnDraw(can, helper.entity, this);
    super.onDraw(can);
    render.afterOnDraw(can, helper.entity, this);
  }

  @Override
  public void stop() {
    super.stop();
    render.load = false;
  }

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    return true;
  }

  @Override
  public Scene getEntity() {
    return helper.entity;
  }
}
