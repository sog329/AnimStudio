package com.sunshine.studio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.base.AnimLayout;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.StudioTv;
import com.sunshine.studio.base.anim.Geometry;
import com.sunshine.studio.base.anim.Shimmer;
import com.sunshine.studio.base.anim.Wave;
import com.sunshine.studio.bone.BoneDemoAct;
import com.sunshine.studio.bone.BoneStudioAct;
import com.sunshine.studio.particle.ParticleDemoAct;
import com.sunshine.studio.particle.ParticleStudioAct;

public class MainAct extends AppCompatActivity
    implements View.OnClickListener,
    View.OnLongClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_main);

    StudioTool.init(this);

    int textSize = StudioTool.screenHeight / 7;
    // bone demo
    StudioTv bDemoTv = findViewById(R.id.bone_demo_tv);
    bDemoTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    StageView bDemoSv = findViewById(R.id.bone_demo_sv);
    bDemoSv.play("bone/sunglasses_main");
    bDemoSv.isRepeat(true);
    setListener(
        ((AnimLayout) findViewById(R.id.bone_demo))
            .addAnim(new Geometry())
            .addAnim(new Shimmer())
            .addAnim(new Wave().setColor(Color.argb(90, 82, 26, 76), Color.argb(180, 82, 26, 76))));

    // bone studio
    StudioTv bStudioTv = findViewById(R.id.bone_studio_tv);
    bStudioTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize / 2);
    StageView bStudioSv = findViewById(R.id.bone_studio_sv);
    bStudioSv.play("bone/gear");
    bStudioSv.autoStop(false);
    bStudioSv.isRepeat(true);
    setListener(
        ((AnimLayout) findViewById(R.id.bone_studio))
            .addAnim(new Geometry())
            .addAnim(new Shimmer())
            .addAnim(new Wave().setColor(Color.argb(90, 82, 26, 76), Color.argb(180, 82, 26, 76))));
    // particle demo
    StudioTv particleDemoTv = findViewById(R.id.particle_demo_tv);
    particleDemoTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    StageView pStudioSv = findViewById(R.id.particle_studio_sv);
    pStudioSv.play("bone/gear");
    pStudioSv.autoStop(false);
    pStudioSv.isRepeat(true);
    pStudioSv.setPercent(.5f, 1f, 9000);
    setListener(
        ((AnimLayout) findViewById(R.id.particle_studio))
            .addAnim(new Geometry())
            .addAnim(new Shimmer())
            .addAnim(
                new Wave().setColor(Color.argb(90, 42, 63, 103), Color.argb(180, 42, 63, 103))));
    // particle studio
    StudioTv particleStudioTv = findViewById(R.id.particle_studio_tv);
    particleStudioTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize / 2);
    SceneView pDemoSv = findViewById(R.id.particle_demo_sv);
    pDemoSv.play("particle/singleDog");
    pDemoSv.isRepeat(true);
    setListener(
        ((AnimLayout) findViewById(R.id.particle_demo))
            .addAnim(new Geometry())
            .addAnim(new Shimmer())
            .addAnim(
                new Wave().setColor(Color.argb(90, 42, 63, 103), Color.argb(180, 42, 63, 103))));
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    boolean needRequest = false;
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        needRequest = true;
        break;
      }
    }
    if (needRequest) {
      StudioTool.initPermission(this);
    } else {
      StudioTool.prepareResource(this);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bone_demo:
        startActivity(new Intent(this, BoneDemoAct.class));
        break;
      case R.id.bone_studio:
        startActivity(new Intent(this, BoneStudioAct.class));
        break;
      case R.id.particle_demo:
        startActivity(new Intent(this, ParticleDemoAct.class));
        break;
      case R.id.particle_studio:
        startActivity(new Intent(this, ParticleStudioAct.class));
        break;
      default:
        break;
    }
  }

  @Override
  public boolean onLongClick(View v) {
    return true;
  }

  private void setListener(View v) {
    v.setOnClickListener(this);
    v.setOnLongClickListener(this);
  }
}
