package com.sunshine.engine.particle.logic;

import com.sunshine.engine.base.DrawInfo;
import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.base.Size;

public class Anim {
  protected ProcessInt centerX = new ProcessInt(0, 0);
  protected ProcessInt centerY = new ProcessInt(0, 0);
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
    int x = centerX.get(percent);
    int y = centerY.get(percent);
    float sX = scale.get(percent);
    float sY = sX;
    int w = (int) (sX * halfSize.width);
    int h = (int) (sY * halfSize.height);

    drawInfo.rcSrc.left = x - w;
    drawInfo.rcSrc.top = y - h;
    drawInfo.rcSrc.right = x + w;
    drawInfo.rcSrc.bottom = y + h;

    drawInfo.rt = rotate.get(percent);

    drawInfo.ptSrc.x = drawInfo.rcSrc.left + (int) (ptRotate.x * sX);
    drawInfo.ptSrc.y = drawInfo.rcSrc.top + (int) (ptRotate.y * sY);

    drawInfo.alpha = alpha.get(percent);
  }
}
