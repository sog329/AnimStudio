package com.sunshine.engine.bone.logic;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/11/30. */
public class Actor extends Anim.Helper {
  public Stage stage = null;
  public List<Bone> lstBone = new ArrayList<>();

  public Actor(Stage stage) {
    this.stage = stage;
    Anim anim = buildAnim();
    anim.duration.set(0f, 1f);
    anim.alpha.set(255, 255);
    lstAnim.add(anim);
  }

  public Bone getLastBone() {
    Bone bone = null;
    if (lstBone.size() > 0) {
      bone = lstBone.get(lstBone.size() - 1);
    }
    return bone;
  }

  public void draw(Stage stage, Canvas can) {
    if (lstBone.size() > 0) {
      Anim anim = getAnim(stage.getPercent());
      if (anim != null) {
        float percent = anim.duration.getPercent(stage.getPercent());
        if (anim.run(percent, stage)) {
          anim.updateDrawInfo(stage);
          stage.mergeDrawInfo();
          int cs = can.save();

          can.rotate(anim.rotate.getNow() - anim.rotate.getFrom(), stage.drawInfo.ptDst.x, stage.drawInfo.ptDst.y);

          can.scale(anim.scaleX.getNow(), anim.scaleY.getNow(), stage.drawInfo.rcDst.centerX(),
              stage.drawInfo.rcDst.centerY());

          can.translate(
              (anim.centerX.getNow() - anim.centerX.getFrom()) * stage.scale,
              (anim.centerY.getNow() - anim.centerY.getFrom()) * stage.scale);

          drawBone(stage, can);
          can.restoreToCount(cs);
        }
      } else {
        drawBone(stage, can);
      }
    }
  }

  private void drawBone(Stage stage, Canvas can) {
    for (Bone bone : lstBone) {
      bone.draw(stage, can);
    }
  }

  @Override
  protected Anim buildAnim() {
    Anim anim = new Anim();
    float x = stage.scriptSize.width / 2f;
    float y = stage.scriptSize.height / 2f;
    anim.centerX.set(x, x);
    anim.centerY.set(y, y);
    x /= 2;
    y /= 2;
    anim.halfSize.width = x;
    anim.halfSize.height = y;
    anim.ptRotate.set(x, y);
    return anim;
  }
}
