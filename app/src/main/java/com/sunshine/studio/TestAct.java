package com.sunshine.studio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunshine.engine.base.Tool;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;

/** Created by Jack on 2019-11-23. */
public class TestAct extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_demo);
    View.OnClickListener l =
        v -> {
          StageView stage = findViewById(R.id.stage);
          stage.stop();
          stage.play("bone/dlg_match2");
          stage.autoStop(false);
          stage.setExternalBmp("left", Tool.getBmpByAssets(this, "pic/she.png"));
          stage.setExternalBmp("right", Tool.getBmpByAssets(this, "pic/he.png"));
          stage.setExternalBmp("bgLeft", Tool.getBmpByAssets(this, "bone/dlg_match2/bg_w"));
          stage.setExternalBmp("bgRight", Tool.getBmpByAssets(this, "bone/dlg_match2/bg_g"));
          stage.setExternalBmp("icon", Tool.getBmpByAssets(this, "bone/dlg_match2/icon_g"));
          SceneView scene = findViewById(R.id.scene);
          scene.postDelayed(
              () -> {
                scene.stop();
                scene.play("particle/send_heart");
              },
              1250);
        };
    l.onClick(null);

    findViewById(R.id.root).setOnClickListener(l);
  }
}
