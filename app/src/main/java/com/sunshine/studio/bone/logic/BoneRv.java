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
import android.view.View;
import android.view.ViewGroup;
import com.sunshine.engine.base.Entity.ClickRect;
import com.sunshine.engine.base.Render2D.Rect2D;
import com.sunshine.engine.base.Tool;
import com.sunshine.engine.bone.StageView;
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
    addData().setBone("insert");
    addData()
        .setBone("guide")
        .setBind(
            h -> {
              View p = (View) h.bFront.getParent();
              p.post(
                  () -> {
                    ViewGroup.LayoutParams lp = h.bFront.getLayoutParams();
                    lp.width = p.getHeight() / 2;
                    lp.height = p.getHeight();
                    h.bFront.setLayoutParams(lp);
                  });

              StageView stageView = h.bFront;

              stageView.setExternal2D(
                  "bg",
                  new Rect2D() {
                    private RectF rc = new RectF();

                    @Override
                    public void onDraw(Canvas can, float percent, RectF rect, float scale) {
                      float w = rect.width() / 25;
                      float r = w * 3;
                      float e = w / 2;
                      rc.set(rect.left + e, rect.top + e, rect.right - e, rect.bottom - e);

                      // 绘制手机屏幕
                      paint.setStyle(Style.FILL);
                      paint.setARGB(128, 255, 255, 255);
                      can.drawRoundRect(rc, r, r, paint);

                      // 绘制手机边框
                      paint.setStyle(Style.STROKE);
                      paint.setARGB(255, 255, 255, 255);
                      paint.setStrokeWidth(w);
                      can.drawRoundRect(rc, r, r, paint);

                      // 设计师一定要的刘海，真丑
                      paint.setStyle(Style.FILL);
                      float len = rect.width() / 2.7f;
                      float left = (rect.width() - len) / 2;
                      rc.set(left, 0, left + len, 2.3f * w);
                      can.drawRoundRect(rc, w, w, paint);

                      // 回调 这里根据percent设置tv展示文字内容&变化 todo
                    }
                  });

              Rect2D rd =
                  new Rect2D() {
                    @Override
                    public void onDraw(Canvas can, float percent, RectF rect, float scale) {
                      // 绘制卡片
                      paint.setStyle(Style.FILL);
                      int a = paint.getAlpha();
                      paint.setARGB((int) (128f * a / 255), 255, 255, 255);
                      float r = rect.width() / 8;
                      can.drawRoundRect(rect, r, r, paint);
                    }
                  };
              stageView.setExternal2D("card1", rd);
              stageView.setExternal2D("card2", rd);
              stageView.setExternal2D("card3", rd);
              stageView.setExternal2D("card4", rd);
              stageView.setExternal2D("card5", rd);
              stageView.setExternal2D("card6", rd);
              stageView.setExternal2D("card7", rd);
            });
    addData()
        .setBone("ripple_heart")
        .setBone2("ripple_circle")
        .setBone3("ripple_bg")
        .setParticle("ripple_star")
        .setBg(new ColorDrawable(Color.rgb(72, 52, 172)))
        .setBind(
            h -> {
              h.pBg.isRepeat(true);
              h.bBack.setExternal2D(
                  "wave",
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
                  });
            });
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
              h.bFront.setExternal2D(
                  "circle",
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
                  });
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
