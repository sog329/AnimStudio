package com.sunshine.studio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunshine.engine.base.Render2D;
import com.sunshine.engine.base.Tool;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;

/** Created by Jack on 2019-11-23. */
public class TestAct extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_demo);
    StageView stage = findViewById(R.id.stage);
    SceneView scene = findViewById(R.id.scene);
    stage.stop();
    stage.play("bone/loading");
    stage.autoStop(false);
    stage.isRepeat(true);
    stage.setExternalBmp("pic", Tool.getBmpByAssets(this, "pic/she.png"));
    stage.setExternalCb(
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
            Tool.log("percent: " + percent);
            Tool.log("scale: " + scale);
            Tool.log("(10 - 8f * percent) * scale: " + ((10 - 8f * percent) * scale));

            paint.setStrokeWidth((20 - 18f * percent) * scale);
            can.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
          }
        });
    Runnable rn =
        () ->
            scene.postDelayed(
                () -> {
                  scene.stop();
                  scene.play("particle/location");
                },
                1250);
    rn.run();
    stage.setCallback(() -> rn.run());
  }
}
