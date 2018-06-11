package com.sunshine.studio.bone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.R;

public class BoneDemoAct extends AppCompatActivity {
  private StageView stageView = null;
  private SceneView sceneView = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_bone_demo);

    sceneView = findViewById(R.id.scene);

    stageView = findViewById(R.id.stage);
    stageView.canTouch(true);
    stageView.setOnClickListener(
        v -> {
          stageView.stop();
          stageView.play(
              "bone/welcome_omi/config.xml", "bone/welcome_omi/pic", "bone/welcome_omi/sound.mp3");
          stageView.autoStop(false);

          sceneView.stop();
          sceneView.play("particle/welcome_omi_bg");
          sceneView.autoStop(false);
          sceneView.isRepeat(true);
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    stageView.isMute(false);
  }

  @Override
  protected void onPause() {
    super.onPause();
    stageView.isMute(true);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stageView.stop();
    sceneView.stop();
  }
}
