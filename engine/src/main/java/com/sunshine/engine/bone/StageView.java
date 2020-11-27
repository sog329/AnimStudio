package com.sunshine.engine.bone;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sunshine.engine.base.AnimView;
import com.sunshine.engine.base.Entity.Click;
import com.sunshine.engine.base.Tool;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.StageHelper;

public class StageView extends AnimView<StageHelper> {

  public StageView(Context context) {
    super(context);
  }

  public StageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public StageHelper buildHelper() {
    return new StageHelper();
  }

  public void canJump(boolean can) {
    helper.canJump(can);
  }

  /**
   * 在调用play(..)后生效
   *
   * @param toPercent 立即跳到指定percent
   */
  public void setPercent(float toPercent) {
    helper.setPercent(toPercent, toPercent, 0);
    invalidate();
  }

  /**
   * 在调用play(..)后生效
   *
   * @param toPercent
   * @param duration
   */
  public void setPercent(float toPercent, int duration) {
    helper.setPercent(toPercent, duration);
    invalidate();
  }

  /**
   * 在调用play(..)后生效
   *
   * @param fromPercent
   * @param toPercent
   * @param duration
   */
  public void setPercent(float fromPercent, float toPercent, int duration) {
    helper.setPercent(fromPercent, toPercent, duration);
    invalidate();
  }

  public void setOnRepeat(Runnable rn) {
    helper.setOnRepeat(rn);
  }

  /**
   * 在播放最后一帧后，是否stop动画，true即执行stop
   *
   * @param auto
   * @return
   */
  public StageView autoStop(boolean auto) {
    if (helper.entity != null) {
      helper.entity.autoStop = auto;
    }
    return this;
  }

  private long actionDownTime = Tool.NONE;

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    if (helper.entity != null && helper.entity.mapClick.size() > 0) {
      if (me.getAction() == MotionEvent.ACTION_DOWN) {
        actionDownTime = Tool.getTime();
      } else if (me.getAction() == MotionEvent.ACTION_UP && Tool.getTime() - actionDownTime < 200) {
        RectF rc = new RectF();
        for (int i = helper.entity.lstActor.size() - 1; i > -1; i--) {
          Actor a = helper.entity.lstActor.get(i);
          if (a.showing) {
            boolean out = false;
            for (int j = a.lstBone.size() - 1; j > -1; j--) {
              Bone b = a.lstBone.get(j);
              if (b.clickId != null && b.showing) {
                a.m.mapRect(rc, b.rcBone);
                b.m.mapRect(rc);
                if (rc.contains(me.getX(), me.getY())) {
                  Click click = helper.entity.mapClick.get(b.clickId);
                  if (click != null) {
                    click.onClick(b.clickId, rc, (int) me.getX(), (int) me.getY());
                  }
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
    return super.onTouchEvent(me);
  }
}
