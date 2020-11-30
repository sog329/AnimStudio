package com.sunshine.engine.bone.logic;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.base.Size;
import com.sunshine.engine.base.Skin;
import com.sunshine.engine.base.Tool;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/11/30. */
public class Anim {
  public Duration duration = new Duration();

  public ProcessFloat centerX = new ProcessFloat(0f, 0f);
  public ProcessFloat centerY = new ProcessFloat(0f, 0f);
  public Size<Float> halfSize = new Size(0f, 0f);

  public ProcessFloat scaleX = new ProcessFloat(1f, 1f);
  public ProcessFloat scaleY = new ProcessFloat(1f, 1f);

  public Point<Float> ptRotate = new Point(0f, 0f);
  public ProcessInt rotate = new ProcessInt(0, 0);

  public ProcessInt alpha = new ProcessInt(0, 0);

  public boolean run(float percent, Entity entity) {
    if (percent < 0) {
      percent = 0;
    } else if (percent > 1) {
      percent = 1;
    }
    entity.drawInfo.alpha = alpha.get(percent);
    if (Tool.equalsZero(entity.drawInfo.alpha) && !entity.inStudio) {
      return false;
    }
    float sX = scaleX.get(percent);
    float sY = scaleY.get(percent);
    if (Tool.equalsZero(sX) || Tool.equalsZero(sY)) {
      return false;
    }
    centerX.get(percent);
    centerY.get(percent);
    rotate.get(percent);
    return true;
  }

  public void updateDrawInfo(Entity entity) {
    float w = scaleX.getNow() * halfSize.width;
    float h = scaleY.getNow() * halfSize.height;

    entity.drawInfo.rcSrc.left = centerX.getNow() - w;
    entity.drawInfo.rcSrc.top = centerY.getNow() - h;
    entity.drawInfo.rcSrc.right = centerX.getNow() + w;
    entity.drawInfo.rcSrc.bottom = centerY.getNow() + h;

    entity.drawInfo.rt = rotate.getNow();

    entity.drawInfo.ptSrc.x = entity.drawInfo.rcSrc.left + ptRotate.x * scaleX.getNow();
    entity.drawInfo.ptSrc.y = entity.drawInfo.rcSrc.top + ptRotate.y * scaleY.getNow();
  }

  public String toString() {
    return new StringBuilder()
        .append("[")
        .append(duration.getFrom())
        .append(",")
        .append(duration.getTo())
        .append("]: ")
        .append("move(")
        .append(centerX.getFrom())
        .append(",")
        .append(centerY.getFrom())
        .append(")->(")
        .append(centerX.getTo())
        .append(",")
        .append(centerY.getTo())
        .append("), w=")
        .append(2 * halfSize.width)
        .append(", h=")
        .append(2 * halfSize.height)
        .toString();
  }

  public abstract static class Helper extends Skin {
    public List<Anim> lstAnim = new ArrayList<>();

    protected void onDraw() {
      m.reset();
      showing = false;
    }

    public Anim getAnim(float percent) {
      Anim anim = null;
      for (int i = 0; i < lstAnim.size(); i++) {
        boolean isLast = i == (lstAnim.size() - 1);
        anim = lstAnim.get(i);
        if (anim.duration.isIn(percent, isLast)) {
          break;
        } else {
          anim = null;
        }
      }
      return anim;
    }

    public Anim getLastAnim() {
      Anim anim = null;
      if (lstAnim.size() > 0) {
        anim = lstAnim.get(lstAnim.size() - 1);
      }
      return anim;
    }

    public void checkAnim(Stage stage) {
      if (lstAnim.size() == 0) {
        lstAnim.add(buildAnim(stage, null));
      } else {
        for (int i = 0; i < lstAnim.size(); i++) {
          Anim anim = lstAnim.get(i);
          if (anim.duration.getTo() <= anim.duration.getFrom() && anim.duration.getTo() != 0) {
            anim.duration.setTo(1);
          }
          if (anim.duration.getTo() > 1) {
            anim.duration.setTo(1);
          } else if ((i < lstAnim.size() - 1) && Tool.equalsFloat(anim.duration.getTo(), 1)) {
            for (int j = lstAnim.size() - 1; j > i; i--) {
              lstAnim.remove(j);
              break;
            }
          }
          if (i == 0) {
            anim.duration.setFrom(0);
          } else {
            anim.duration.setFrom(lstAnim.get(i - 1).duration.getTo());
          }
          if (i == lstAnim.size() - 1) {
            if (anim.duration.getTo() < 1) {
              lstAnim.add(buildAnim(stage, anim));
            }
          }
        }
      }
    }

    public Anim buildAnim(Stage stage, Anim last) {
      Anim anim = buildAnim();
      anim.ptRotate.set(anim.halfSize.width, anim.halfSize.height);
      if (last == null) {
        anim.duration.set(0, 1);
        float centerX = stage.scriptSize.width / 2f;
        anim.centerX.set(centerX, centerX);
        float centerY = stage.scriptSize.height / 2f;
        anim.centerY.set(centerY, centerY);
        anim.alpha.set(255, 255);
      } else {
        anim.duration.set(last.duration.getTo(), 1);
        anim.centerX.set(last.centerX.getTo(), last.centerX.getTo());
        anim.centerY.set(last.centerY.getTo(), last.centerY.getTo());
        anim.ptRotate.set(last.ptRotate.x, last.ptRotate.y);
        anim.rotate.set(last.rotate.getTo(), last.rotate.getTo());
        anim.alpha.set(last.alpha.getTo(), last.alpha.getTo());
        anim.scaleX.set(last.scaleX.getTo(), last.scaleX.getTo());
        anim.scaleY.set(last.scaleY.getTo(), last.scaleY.getTo());
      }
      return anim;
    }

    public abstract Anim buildAnim();
  }
}
