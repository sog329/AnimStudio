package com.sunshine.engine.particle.logic;

import com.sunshine.engine.base.DrawInfo;
import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.base.Size;

public class Anim {
  protected ProcessFloat centerX = new ProcessFloat(0f, 0f);
  protected ProcessFloat centerY = new ProcessFloat(0f, 0f);
  public Size<Float> halfSize = new Size(0f, 0f);
  protected Point<Float> ptRotate = new Point(0f, 0f);
  protected ProcessInt rotate = new ProcessInt(0, 0);
  protected ProcessFloat scale = new ProcessFloat(1f, 1f);
  protected ProcessInt alpha = new ProcessInt(50, 255);

  public void run(float percent, DrawInfo drawInfo) {
    if (percent < 0) {
      percent = 0;
    } else if (percent > 1) {
      percent = 1;
    }
    float x = centerX.get(percent);
    float y = centerY.get(percent);
    float sX = scale.get(percent);
    float sY = sX;
    float w = sX * halfSize.width;
    float h = sY * halfSize.height;

    drawInfo.rcSrc.left = x - w;
    drawInfo.rcSrc.top = y - h;
    drawInfo.rcSrc.right = x + w;
    drawInfo.rcSrc.bottom = y + h;

    drawInfo.rt = rotate.get(percent);

    drawInfo.ptSrc.x = drawInfo.rcSrc.left + ptRotate.x * sX;
    drawInfo.ptSrc.y = drawInfo.rcSrc.top + ptRotate.y * sY;

    drawInfo.alpha = alpha.get(percent);
  }
}
