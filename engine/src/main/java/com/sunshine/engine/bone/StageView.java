package com.sunshine.engine.bone;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.sunshine.engine.base.AnimView;
import com.sunshine.engine.base.Entity.Click;
import com.sunshine.engine.base.Entity.ClickId;
import com.sunshine.engine.base.Entity.ClickRect;
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

  @Override
  protected void onClick(int x, int y) {
    RectF rc = new RectF();
    for (int i = helper.entity.lstActor.size() - 1; i > -1; i--) {
      Actor a = helper.entity.lstActor.get(i);
      if (a.showing) {
        for (int j = a.lstBone.size() - 1; j > -1; j--) {
          Bone b = a.lstBone.get(j);
          if (b.showing) {
            a.m.mapRect(rc, b.rc);
            b.m.mapRect(rc);
            if (rc.contains(x, y)) {
              Click click = helper.entity.mapClick.get(b.name);
              if (click != null) {
                if (click instanceof ClickRect) {
                  ((ClickRect) click).onClick(b.name, rc, x, y);
                } else if (click instanceof ClickId) {
                  ((ClickId) click).onClick(b.name);
                }
              }
              break;
            }
          }
        }
      }
    }
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
}
