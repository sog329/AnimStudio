package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
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
    if (!render.callback.onMove((int) me.getX(), (int) me.getY())) {
      if (me.getAction() == MotionEvent.ACTION_DOWN) {
        if (helper.entity != null) {
          for (int i = helper.entity.getLastActor().lstBone.size() - 1; i > -1; i--) {
            Bone b = helper.entity.getLastActor().lstBone.get(i);
            Anim anim = b.getAnim(helper.entity.getPercent());
            if (anim.run(getEntity().getPercent(), getEntity())) {
              getEntity().mergeDrawInfo();
              if (getEntity().drawInfo.rcDst.contains(me.getX(), me.getY())) {
                render.callback.onClickBone(i);
                break;
              }
            } else {
              continue;
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
