package com.sunshine.engine.particle.logic;

import android.graphics.Rect;

import com.sunshine.engine.base.Area;
import com.sunshine.engine.base.InterpolatorType;
import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.base.Size;
import com.sunshine.engine.base.Tool;

public class ParticleModel {
  public String name = null;
  public Size<Integer> size = new Size(0, 0);
  public Rect rcBmp = new Rect(0, 0, 0, 0);
  public ProcessFloat chanceRange = new ProcessFloat(0f, 1f);
  public ProcessInt activeTime = new ProcessInt(1500, 2000);
  public Area areaFrom = new Area();
  public Area areaTo = new Area();
  public String[] interpolatorMove =
      new String[] {InterpolatorType.linear.toString(), InterpolatorType.linear.toString()};
  public Point<Float> ptRotate = new Point(0f, 0f);
  public ProcessInt rotateBegin = new ProcessInt(0, 100);
  public ProcessInt rotateEnd = new ProcessInt(200, 300);
  public String interpolatorRotate = InterpolatorType.linear.toString();
  public ProcessInt alphaBegin = new ProcessInt(0, 0);
  public ProcessInt alphaEnd = new ProcessInt(255, 255);
  public String interpolatorAlpha = InterpolatorType.spring.toString();
  public ProcessFloat scaleBegin = new ProcessFloat(1f, 1.2f);
  public ProcessFloat scaleEnd = new ProcessFloat(1f, 1f);
  //  public ProcessFloat scaleBeginY = new ProcessFloat(1f, 2f);
  //  public ProcessFloat scaleEndY = new ProcessFloat(0f, .2f);
  public String interpolatorScale = InterpolatorType.linear.toString();
  //  public String interpolatorScaleY = DEFAULT;

  // x&y为Scene坐标系
  public void build(Scene scene, Particle p) {
    p.activeTimeDuration = activeTime.random();
    p.activeTimeStart = Tool.NONE;
    p.setRcBmp(rcBmp);
    // alpha
    int alpha = alphaBegin.random();
    p.anim.alpha.set(alpha, alphaEnd == null ? alpha : alphaEnd.random());
    p.anim.alpha.setInterpolator(interpolatorAlpha);
    // move
    Point<Integer> ptFrom = areaFrom.getPoint(scene);
    Point<Integer> ptTo = areaTo.getPoint(scene, ptFrom.x, ptFrom.y);
    p.anim.centerX.set((float) ptFrom.x, (float) ptTo.x);
    p.anim.centerY.set((float) ptFrom.y, (float) ptTo.y);
    p.anim.centerX.setInterpolator(interpolatorMove[0]);
    p.anim.centerY.setInterpolator(interpolatorMove[1]);
    p.anim.halfSize.width = size.width / 2f;
    p.anim.halfSize.height = size.height / 2f;
    // scale
    float scaleFrom = scaleBegin.random();
    float scaleTo = scaleEnd == null ? scaleFrom : scaleEnd.random();
    p.anim.scale.set(scaleFrom, scaleTo);
    p.anim.scale.setInterpolator(interpolatorScale);
    // rotate
    int rtFrom = rotateBegin.random();
    int rtEnd = rotateEnd == null ? rtFrom : rotateEnd.random();
    if (areaTo.isRotate) {
      int rt = 0;
      if (Tool.equalsZero(p.anim.centerY.getDelta())) {
        if (p.anim.centerX.getDelta() > 0) {
          rt = 90;
        } else {
          rt = 270;
        }
      } else {
        rt = (int) (Math.atan(-p.anim.centerX.getDelta() / p.anim.centerY.getDelta()) / Math.PI * 180);
        if (p.anim.centerY.getDelta() > 0) {
          rt += 180;
        }
      }
      rtFrom += rt;
      rtEnd += rt;
    }
    p.anim.rotate.set(rtFrom, rtEnd);
    p.anim.rotate.setInterpolator(interpolatorRotate);
    p.anim.ptRotate.set(ptRotate.x, ptRotate.y);
  }
}
