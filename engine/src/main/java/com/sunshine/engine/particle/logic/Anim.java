package com.sunshine.engine.particle.logic;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.base.Size;
import com.sunshine.engine.base.Tool;

public class Anim {
  protected ProcessFloat centerX = new ProcessFloat(0f, 0f);
  protected ProcessFloat centerY = new ProcessFloat(0f, 0f);
  public Size<Float> halfSize = new Size(0f, 0f);
  protected Point<Float> ptRotate = new Point(0f, 0f);
  protected ProcessInt rotate = new ProcessInt(0, 0);
  protected ProcessFloat scale = new ProcessFloat(1f, 1f);
  protected ProcessInt alpha = new ProcessInt(50, 255);

  public boolean run(float percent, Entity entity) {
    if (percent < 0) {
      percent = 0;
    } else if (percent > 1) {
      percent = 1;
    }
    float x = centerX.get(percent);
    float y = centerY.get(percent);
    float sX = scale.get(percent);
    float sY = sX;

    if (Tool.equalsZero(sX) || Tool.equalsZero(sY)) {
      return false;
    }

    float w = sX * halfSize.width;
    float h = sY * halfSize.height;

    entity.drawInfo.rcSrc.left = x - w;
    entity.drawInfo.rcSrc.top = y - h;
    entity.drawInfo.rcSrc.right = x + w;
    entity.drawInfo.rcSrc.bottom = y + h;

    entity.drawInfo.rt = rotate.get(percent);

    entity.drawInfo.ptSrc.x = entity.drawInfo.rcSrc.left + ptRotate.x * sX;
    entity.drawInfo.ptSrc.y = entity.drawInfo.rcSrc.top + ptRotate.y * sY;

    entity.drawInfo.alpha = alpha.get(percent);
    if (Tool.equalsZero(entity.drawInfo.alpha) && !entity.inStudio) {
      return false;
    }
    return true;
  }
}
