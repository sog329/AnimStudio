package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.studio.base.RenderHelper;
import com.sunshine.studio.base.Studio;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioSv extends StageView implements Studio.Callback<Stage> {
  private RenderHelper render = new RenderHelper();

  public StudioSv(Context context) {
    super(context);
  }

  public StudioSv(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StudioSv(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallback(RenderHelper.Callback callback) {
    render.callback = callback;
  }

  @Override
  public void onDraw(Canvas can) {
    super.onDraw(can);
    render.onDraw(can, helper.entity, this);
  }

  @Override
  public void stop() {
    super.stop();
    render.load = false;
  }

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    render.callback.onMove((int) me.getX(), (int) me.getY());
    return true;
  }

  @Override
  public Stage getEntity() {
    return helper.entity;
  }
}
