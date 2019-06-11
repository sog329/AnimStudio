package com.sunshine.engine.bone.logic;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.base.Size;

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
    if (entity.drawInfo.alpha == 0 && !entity.inStudio) {
      return false;
    }
    float x = centerX.get(percent);
    float y = centerY.get(percent);
    float sX = scaleX.get(percent);
    float sY = scaleY.get(percent);
    float w = sX * halfSize.width;
    float h = sY * halfSize.height;
    if (w == 0 || h == 0) {
      return false;
    }

    entity.drawInfo.rcSrc.left = x - w;
    entity.drawInfo.rcSrc.top = y - h;
    entity.drawInfo.rcSrc.right = x + w;
    entity.drawInfo.rcSrc.bottom = y + h;

    entity.drawInfo.rt = rotate.get(percent);

    entity.drawInfo.ptSrc.x = (int) (entity.drawInfo.rcSrc.left + ptRotate.x * sX);
    entity.drawInfo.ptSrc.y = (int) (entity.drawInfo.rcSrc.top + ptRotate.y * sY);
    return true;
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
}
