package com.sunshine.engine.bone.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.sunshine.engine.base.Render2D;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/11/30. */
public class Bone extends Anim.Helper {
  public List<Rect> lstRect = new ArrayList<>();
  public String externalId = null;
  public Anim animJump = null;
  public Integer extendY = null;
  private static Rect RECT_BMP = new Rect();
  private static RectF RECT_TMP = new RectF();
  public Actor actor = null;

  public Bone(Actor actor) {
    this.actor = actor;
  }

  public void draw(Stage stage, Canvas can) {
    onDraw();
    rc.set(0, 0, 0, 0);

    Bitmap bmp = stage.bmp;
    Render2D.Callback cb = null;
    Rect rect = lstRect.get(0);
    if (externalId != null) {
      bmp = stage.mapBmp.get(externalId);
      cb = stage.map2D.get(externalId);
      rect = null;
    }
    if (bmp == null && cb == null) {
      return;
    } else {
      Anim anim;
      if (stage.canJump && animJump != null) {
        anim = animJump;
      } else {
        anim = getAnim(stage.getPercent());
      }
      if (anim != null) {
        float percent = anim.duration.getPercent(stage.getPercent());
        if (anim.run(percent, stage)) {
          anim.updateDrawInfo(stage);
          stage.mergeDrawInfo(rc, m);
          showing = true;
          if (lstRect.size() > 1) {
            rect = lstRect.get((int) (percent * (lstRect.size() - 1)));
          }
          if (extendY == null) {
            if (cb == null) {
              Render2D.draw(can, bmp, rect, stage.drawInfo);
            } else {
              Render2D.draw(can, cb, percent, stage.drawInfo, stage.scale);
            }
          } else if (bmp != null) {
            RECT_BMP.set(rect);
            RECT_TMP.set(stage.drawInfo.rcDst);
            if (0 < extendY && extendY <= rect.height()) {
              float topH = 0;
              float bottomH = 0;
              // up
              if (extendY > 1) {
                RECT_BMP.set(rect.left, rect.top, rect.right, rect.top + extendY - 1);
                topH = stage.drawInfo.rcDst.width() * RECT_BMP.height() / RECT_BMP.width();
                stage.drawInfo.rcDst.set(
                    RECT_TMP.left, RECT_TMP.top, RECT_TMP.right, RECT_TMP.top + topH);
                Render2D.draw(can, bmp, RECT_BMP, stage.drawInfo);
              }
              // down
              if (extendY < rect.height()) {
                RECT_BMP.set(rect.left, rect.top + extendY, rect.right, rect.bottom);
                bottomH = stage.drawInfo.rcDst.width() * RECT_BMP.height() / RECT_BMP.width();
                stage.drawInfo.rcDst.set(
                    RECT_TMP.left, RECT_TMP.bottom - bottomH, RECT_TMP.right, RECT_TMP.bottom);
                Render2D.draw(can, bmp, RECT_BMP, stage.drawInfo);
              }
              // middle
              RECT_BMP.set(rect.left, rect.top + extendY - 1, rect.right, rect.top + extendY);
              stage.drawInfo.rcDst.set(
                  RECT_TMP.left,
                  RECT_TMP.top + topH - 1,
                  RECT_TMP.right,
                  RECT_TMP.bottom - bottomH + 1);
              Render2D.draw(can, bmp, RECT_BMP, stage.drawInfo);
            }
          }
        }
      }
    }
  }

  @Override
  public Anim buildAnim() {
    Anim anim = new Anim();
    anim.halfSize.width = lstRect.get(0).width() / 2f;
    anim.halfSize.height = lstRect.get(0).height() / 2f;
    return anim;
  }
}
