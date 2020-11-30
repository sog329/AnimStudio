package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.studio.base.RenderHelper;
import com.sunshine.studio.base.Studio;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioSv extends StageView implements Studio.Callback<Stage> {
  private StageRender render = new StageRender();

  public StudioSv(Context context) {
    super(context);
  }

  public StudioSv(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StudioSv(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallback(StageRender.Callback callback, RenderHelper.Callback cb) {
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
    if (!render.callback.onMove((int) me.getX(), (int) me.getY())) {
      if (me.getAction() == MotionEvent.ACTION_DOWN) {
        if (helper.entity != null) {
          RectF rc = new RectF();
          for (int i = helper.entity.lstActor.size() - 1; i > -1; i--) {
            Actor a = helper.entity.lstActor.get(i);
            if (a.showing) {
              boolean out = false;
              for (int j = a.lstBone.size() - 1; j > -1; j--) {
                Bone b = a.lstBone.get(j);
                if (b.showing) {
                  a.m.mapRect(rc, b.rc);
                  b.m.mapRect(rc);
                  if (rc.contains(me.getX(), me.getY())) {
                    render.callback.onClickBone(b);
                    out = true;
                    break;
                  }
                } else {
                  continue;
                }
              }
              if (out) {
                break;
              }
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public Stage getEntity() {
    return helper.entity;
  }
}
