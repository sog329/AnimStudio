package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.sunshine.engine.base.Render2D;
import com.sunshine.studio.base.DemoRv;

/**
 * Created by Jack on 2019-11-20.
 */
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
    addData().setBone("loading").setBg(new ColorDrawable(Color.WHITE)).setBind((b, p) -> {
      b.setExternalBmp("pic", getBmp("pic/she.png"));
      b.setExternalCb(
          "circle",
          new Render2D.Callback() {
            @Override
            public void init() {
              paint.setAntiAlias(true);
              paint.setColor(Color.rgb(255, 92, 49));
              paint.setStyle(Style.STROKE);
            }

            @Override
            public void onDraw(Canvas can, float percent, RectF rect, float scale) {
              paint.setStrokeWidth((20 - 18f * percent) * scale);
              can.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
            }
          });
      Runnable post = () -> {
        p.stop();
        p.play("particle/location");
      };
      Runnable rn = () -> p.postDelayed(post, 1250);
      rn.run();
      b.setOnRepeat(() -> rn.run());
      b.setOnStop(() -> p.removeCallbacks(post));
    });
    addData().setBone("welcome_omi");
    addData().setBone("dlg_match2").setBg(new ColorDrawable(Color.WHITE)).setBind((b, p) -> {
      b.setExternalBmp("left", getBmp("pic/she.png"));
      b.setExternalBmp("right", getBmp("pic/he.png"));
      b.setExternalBmp("bgLeft", getBmp("bone/dlg_match2/bg_w"));
      b.setExternalBmp("bgRight", getBmp("bone/dlg_match2/bg_g"));
      b.setExternalBmp("icon", getBmp("bone/dlg_match2/icon_g"));
      Runnable post = () -> {
        p.stop();
        p.play("particle/send_heart");
      };
      Runnable rn = () -> {
        p.stop();
        p.postDelayed(post, 1250);
      };
      rn.run();
      b.setOnRepeat(() -> rn.run());
      b.setOnStop(() -> p.removeCallbacks(post));
    });
    addData().setBone("welcomeDemo").setBg(new ColorDrawable(Color.WHITE));
    addData().setBone("card").setBg(new ColorDrawable(Color.WHITE));
    addData().setBone("match").setBind((b, p) -> {
      b.setExternalBmp("left", getBmp("pic/she.png"));
      b.setExternalBmp("right", getBmp("pic/he.png"));
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
