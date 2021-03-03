package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.sunshine.engine.base.Entity.ClickRect;
import com.sunshine.engine.base.Render2D.Rect2D;
import com.sunshine.engine.base.Tool;
import com.sunshine.studio.base.DemoRv;
import com.sunshine.studio.base.StudioTool;

/** Created by Jack on 2019-11-20. */
public class BoneRv extends DemoRv {

  public BoneRv(Context context) {
    super(context);
  }

  public BoneRv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public BoneRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void loadData() {
    Rect2D r1 =
        new Rect2D() {
          @Override
          public void init() {
            paint.setColor(Color.rgb(255, 255, 255));
            paint.setStyle(Style.FILL);
          }

          @Override
          public void onDraw(Canvas can, float percent, RectF rect, float scale) {
            can.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
          }
        };
    Tool.log("r1.hashCode() = " + r1.hashCode());
    Rect2D r2 =
        new Rect2D() {
          @Override
          public void init() {
            paint.setColor(Color.rgb(255, 92, 49));
            paint.setStyle(Style.STROKE);
          }

          @Override
          public void onDraw(Canvas can, float percent, RectF rect, float scale) {
            paint.setStrokeWidth((20 - 18f * percent) * scale);
            can.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
          }
        };
    Tool.log("r2.hashCode() = " + r2.hashCode());
    addData()
        .setBone("ripple_heart")
        .setBone2("ripple_circle")
        .setBg(new ColorDrawable(Color.rgb(72, 52, 172)))
        .setBind(h -> h.bBack.setExternal2D("wave", r1));
    addData()
        .setBone("new_match3")
        .setBg(new ColorDrawable(Color.WHITE))
        .setBind(
            h -> {
              h.bFront.setExternalBmp("left", getBmp("pic/she.png"));
              h.bFront.setExternalBmp("right", getBmp("pic/he.png"));
              h.bFront.isRepeat(false);
              h.bFront.autoStop(false);
              Bitmap[] aryBmp = new Bitmap[] {null};
              new Thread(() -> aryBmp[0] = getBmp("bone/new_match3/heart")).start();
              h.bFront.setExternal2D(
                  "heart",
                  new Rect2D() {
                    private long startTime = Tool.NONE;
                    private static final long DURATION = 1050;

                    @Override
                    public void onDraw(Canvas can, float percent, RectF rect, float scale) {
                      if (percent >= 1f) {
                        if (aryBmp[0] != null) {
                          if (startTime == Tool.NONE) {
                            startTime = Tool.getTime();
                          }
                          long runTime = (Tool.getTime() - startTime) % DURATION;
                          if (runTime <= 200) {
                            float s = 1f + .3f * runTime / 200;
                            float w = rect.width() * s / 2;
                            float h = rect.height() * s / 2;
                            RectF r =
                                new RectF(
                                    rect.centerX() - w,
                                    rect.centerY() - h,
                                    rect.centerX() + w,
                                    rect.centerY() + h);
                            can.drawBitmap(aryBmp[0], null, r, paint);
                          } else if (runTime <= 350) {
                            float s = 1.3f - .3f * (runTime - 200) / 150;
                            float w = rect.width() * s / 2;
                            float h = rect.height() * s / 2;
                            RectF r =
                                new RectF(
                                    rect.centerX() - w,
                                    rect.centerY() - h,
                                    rect.centerX() + w,
                                    rect.centerY() + h);
                            can.drawBitmap(aryBmp[0], null, r, paint);
                          } else if (runTime <= 500) {
                            float s = 1f + .2f * (runTime - 350) / 150;
                            float w = rect.width() * s / 2;
                            float h = rect.height() * s / 2;
                            RectF r =
                                new RectF(
                                    rect.centerX() - w,
                                    rect.centerY() - h,
                                    rect.centerX() + w,
                                    rect.centerY() + h);
                            can.drawBitmap(aryBmp[0], null, r, paint);
                          } else if (runTime <= 650) {
                            float s = 1.2f - .2f * (runTime - 500) / 150;
                            float w = rect.width() * s / 2;
                            float h = rect.height() * s / 2;
                            RectF r =
                                new RectF(
                                    rect.centerX() - w,
                                    rect.centerY() - h,
                                    rect.centerX() + w,
                                    rect.centerY() + h);
                            can.drawBitmap(aryBmp[0], null, r, paint);
                          } else {
                            can.drawBitmap(aryBmp[0], null, rect, paint);
                          }
                        }
                        h.bFront.postInvalidate();
                      } else {
                        can.drawBitmap(aryBmp[0], null, rect, paint);
                      }
                    }
                  });
              h.bFront.setOnStop(
                  () -> {
                    if (aryBmp[0] != null) {
                      aryBmp[0].recycle();
                      aryBmp[0] = null;
                    }
                  });
            });
    addData()
        .setBone("loading")
        .setBg(new ColorDrawable(Color.WHITE))
        .setBind(
            h -> {
              h.bFront.setExternalBmp("pic", getBmp("pic/she.png"));
              h.bFront.setExternal2D("circle", r2);
              h.bFront.setOnClick(
                  "pic",
                  (ClickRect)
                      (id, r, x, y) ->
                          StudioTool.showToast(
                              h.bFront.getContext(),
                              id
                                  + " click\nx="
                                  + x
                                  + " in["
                                  + (int) r.left
                                  + ", "
                                  + (int) r.right
                                  + "]\ny="
                                  + y
                                  + " in["
                                  + (int) r.top
                                  + ", "
                                  + (int) r.bottom
                                  + "]"));
              Runnable post =
                  () -> {
                    h.pBg.stop();
                    h.pBg.play("particle/location");
                  };
              Runnable rn =
                  () -> {
                    h.pBg.stop();
                    h.pBg.postDelayed(post, 1250);
                  };
              rn.run();
              h.bFront.setOnRepeat(() -> rn.run());
              h.bFront.setOnStop(() -> h.pBg.removeCallbacks(post));
            });
    addData().setBone("welcome_omi");
    addData()
        .setBone("dlg_match2")
        .setBg(new ColorDrawable(Color.WHITE))
        .setBind(
            h -> {
              h.bFront.setExternalBmp("left", getBmp("pic/she.png"));
              h.bFront.setExternalBmp("right", getBmp("pic/he.png"));
              h.bFront.setExternalBmp("bgLeft", getBmp("bone/dlg_match2/bg_w"));
              h.bFront.setExternalBmp("bgRight", getBmp("bone/dlg_match2/bg_g"));
              h.bFront.setExternalBmp("icon", getBmp("bone/dlg_match2/icon_g"));
              Runnable post =
                  () -> {
                    h.pBg.stop();
                    h.pBg.play("particle/send_heart");
                  };
              Runnable rn =
                  () -> {
                    h.pBg.stop();
                    h.pBg.postDelayed(post, 1250);
                  };
              rn.run();
              h.bFront.setOnRepeat(() -> rn.run());
              h.bFront.setOnStop(() -> h.pBg.removeCallbacks(post));
            });
    addData().setBone("welcomeDemo").setBg(new ColorDrawable(Color.WHITE));
    addData().setBone("card").setBg(new ColorDrawable(Color.WHITE));
    addData()
        .setBone("match")
        .setBind(
            h -> {
              h.bFront.setExternalBmp("left", getBmp("pic/she.png"));
              h.bFront.setExternalBmp("right", getBmp("pic/he.png"));
            });
    addData().setBone("qin");
    addData().setBone("sunglasses");
    addData().setBone("gear");
    addData().setBone("pass");
    addData().setBone("like");
    addData().setBone("me");
    addData().setBone("set");
    addData().setBone("tab1");
    addData().setBone("tab2");
    addData().setBone("tab3");
    addData().setBone("tab4");
    addData().setBone("voice2");
    addData().setBone("arrow");
  }
}
