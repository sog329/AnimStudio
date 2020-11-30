package com.sunshine.engine.bone.logic;

import android.graphics.Canvas;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.base.ViewHelper;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class Stage extends Entity {

  public List<Actor> lstActor = new ArrayList<>();

  public boolean canJump = false;
  public boolean autoStop = true;

  private Runnable onRepeat = null;

  public Stage(ViewHelper helper, String configPath, String picPath, String soundPath) {
    super(helper, configPath, picPath, soundPath);
  }

  @Override
  public void draw(Canvas can) {
    if (bmp != null) {
      for (Actor actor : lstActor) {
        actor.draw(this, can);
      }
      if (getPercent() >= 1 && repeat) {
        setPercent(0, 1, duration);
        if (onRepeat != null) {
          onRepeat.run();
        }
      }
      if (!repeat && autoStop && getPercent() >= 1 && !inStudio) {
        helper.stop();
      }
    }
  }

  @Override
  public DefaultHandler getParser() {
    return new StageParser(this);
  }

  @Override
  public boolean needDraw(float percent) {
    if (duration > 0 && percent != percentRange.getTo()) {
      return true;
    } else {
      return false;
    }
  }

  public void setOnRepeat(Runnable rn) {
    onRepeat = rn;
  }

  public Actor getLastActor() {
    Actor actor = null;
    if (lstActor.size() > 0) {
      actor = lstActor.get(lstActor.size() - 1);
    }
    return actor;
  }

  public void setPercent(float fromPercent, float toPercent, int duration) {
    super.setPercent(fromPercent, toPercent, duration);
    if (canJump && parsed) {
      for (Actor actor : lstActor) {
        for (int i = 0; i < actor.lstBone.size(); i++) {
          Bone bone = actor.lstBone.get(i);
          Anim animFrom = bone.animJump == null ? bone.getAnim(fromPercent) : bone.animJump;
          Anim animTo = bone.getAnim(toPercent);
          if (animFrom == null || animTo == null) {
            bone.animJump = null;
          } else {
            float from = fromPercent;
            float to = toPercent;
            if (from > to) {
              float tmp = from;
              from = to;
              to = tmp;
              Anim anim = animFrom;
              animFrom = animTo;
              animTo = anim;
            }
            bone.animJump = new Anim();
            bone.animJump.duration.set(from, to);
            from = animFrom.duration.getPercent(from);
            to = animTo.duration.getPercent(to);
            bone.animJump.halfSize.width = animTo.halfSize.width;
            bone.animJump.halfSize.height = animTo.halfSize.height;
            bone.animJump.centerX.set(
                animFrom.centerX.get(from),
                animTo.centerX.get(to),
                animTo.centerX.getInterpolatorName());
            bone.animJump.centerY.set(
                animFrom.centerY.get(from),
                animTo.centerY.get(to),
                animTo.centerY.getInterpolatorName());
            bone.animJump.scaleX.set(
                animFrom.scaleX.get(from),
                animTo.scaleX.get(to),
                animTo.scaleX.getInterpolatorName());
            bone.animJump.scaleY.set(
                animFrom.scaleY.get(from),
                animTo.scaleY.get(to),
                animTo.scaleY.getInterpolatorName());
            bone.animJump.alpha.set(
                animFrom.alpha.get(from), animTo.alpha.get(to), animTo.alpha.getInterpolatorName());
            bone.animJump.rotate.set(
                animFrom.rotate.get(from),
                animTo.rotate.get(to),
                animTo.rotate.getInterpolatorName());
            bone.animJump.ptRotate.set(animTo.ptRotate.x, animTo.ptRotate.y);
          }
        }
      }
    }
  }
}
