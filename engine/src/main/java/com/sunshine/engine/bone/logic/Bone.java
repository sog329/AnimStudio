package com.sunshine.engine.bone.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.sunshine.engine.base.Render2D;
import com.sunshine.engine.base.Tool;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/11/30. */
public class Bone {
  public List<Rect> lstRect = new ArrayList<>();
  public String externalBmpId = null;
  public List<Anim> lstAnim = new ArrayList<>();
  public Anim animJump = null;
  public Integer extendY = null;
  private static Rect RECT_BMP = new Rect();
  private static RectF RECT_TMP = new RectF();

  public void draw(Stage stage, Canvas can) {
    Bitmap bmp = stage.bmp;
    Rect rect = lstRect.get(0);
    if (externalBmpId != null) {
      bmp = stage.mapBmp.get(externalBmpId);
      rect = null;
    }
    if (bmp == null) {
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
          stage.mergeDrawInfo();
          if (lstRect.size() > 1) {
            rect = lstRect.get((int) (percent * (lstRect.size() - 1)));
          }
          if (extendY == null) {
            Render2D.draw(can, bmp, rect, stage.drawInfo);
          } else {
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
                RECT_BMP.set(rect.left, rect.bottom - extendY, rect.right, rect.bottom);
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
    Anim anim = new Anim();
    if (last == null) {
      anim.duration.set(0, 1);
      float centerX = stage.scriptSize.width / 2f;
      anim.centerX.set(centerX, centerX);
      float centerY = stage.scriptSize.height / 2f;
      anim.centerY.set(centerY, centerY);
      anim.halfSize.width = lstRect.get(0).width() / 2f;
      anim.halfSize.height = lstRect.get(0).height() / 2f;
      anim.alpha.set(255, 255);
      anim.ptRotate.set(anim.halfSize.width, anim.halfSize.height);
    } else {
      anim.duration.set(last.duration.getTo(), 1);
      anim.centerX.set(last.centerX.getTo(), last.centerX.getTo());
      anim.centerY.set(last.centerY.getTo(), last.centerY.getTo());
      anim.halfSize.width = lstRect.get(0).width() / 2f;
      anim.halfSize.height = lstRect.get(0).height() / 2f;
      anim.ptRotate.set(last.ptRotate.x, last.ptRotate.y);
      anim.rotate.set(last.rotate.getTo(), last.rotate.getTo());
      anim.alpha.set(last.alpha.getTo(), last.alpha.getTo());
      anim.scaleX.set(last.scaleX.getTo(), last.scaleX.getTo());
      anim.scaleY.set(last.scaleY.getTo(), last.scaleY.getTo());
    }
    return anim;
  }
}
