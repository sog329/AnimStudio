package com.sunshine.studio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.StudioTv;
import com.sunshine.studio.bone.BoneDemoAct;
import com.sunshine.studio.bone.BoneStudioAct;
import com.sunshine.studio.particle.ParticleDemoAct;
import com.sunshine.studio.particle.ParticleStudioAct;

public class MainAct extends AppCompatActivity implements View.OnClickListener, View.OnHoverListener, View.OnTouchListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_main);

    StudioTool.init(this);

    int textSize = StudioTool.screenHeight / 7;
    // bone demo
    StudioTv bDemoTv = findViewById(R.id.bone_demo_tv);
    bDemoTv.getBackground().setAlpha(128);
    bDemoTv.setOnClickListener(this);
    bDemoTv.setOnHoverListener(this);
    bDemoTv.setOnTouchListener(this);
    bDemoTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    StageView bDemoSv = findViewById(R.id.bone_demo_sv);
    bDemoSv.play("bone/sunglasses_main");
    bDemoSv.isRepeat(true);
    // bone studio
    StudioTv bStudioTv = findViewById(R.id.bone_studio_tv);
    bStudioTv.getBackground().setAlpha(128);
    bStudioTv.setOnClickListener(this);
    bStudioTv.setOnHoverListener(this);
    bStudioTv.setOnTouchListener(this);
    bStudioTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize / 2);
    StageView bStudioSv = findViewById(R.id.bone_studio_sv);
    bStudioSv.play("bone/gear");
    bStudioSv.autoStop(false);
    bStudioSv.isRepeat(true);
    // particle demo
    StudioTv particleDemoTv = findViewById(R.id.particle_demo_tv);
    particleDemoTv.getBackground().setAlpha(128);
    particleDemoTv.setOnClickListener(this);
    particleDemoTv.setOnHoverListener(this);
    particleDemoTv.setOnTouchListener(this);
    particleDemoTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    StageView pStudioSv = findViewById(R.id.particle_studio_sv);
    pStudioSv.play("bone/gear");
    pStudioSv.autoStop(false);
    pStudioSv.isRepeat(true);
    pStudioSv.setPercent(.5f, 1f, 9000);
    // particle studio
    StudioTv particleStudioTv = findViewById(R.id.particle_studio_tv);
    particleStudioTv.getBackground().setAlpha(128);
    particleStudioTv.setOnClickListener(this);
    particleStudioTv.setOnHoverListener(this);
    particleStudioTv.setOnTouchListener(this);
    particleStudioTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize / 2);
    SceneView pDemoSv = findViewById(R.id.particle_demo_sv);
    pDemoSv.play("particle/singleDog");
    pDemoSv.isRepeat(true);
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
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bone_demo_tv:
        startActivity(new Intent(this, BoneDemoAct.class));
        break;
      case R.id.bone_studio_tv:
        startActivity(new Intent(this, BoneStudioAct.class));
        break;
      case R.id.particle_demo_tv:
        startActivity(new Intent(this, ParticleDemoAct.class));
        break;
      case R.id.particle_studio_tv:
        startActivity(new Intent(this, ParticleStudioAct.class));
        break;
      default:
        break;
    }
  }

  @Override
  public boolean onHover(View v, MotionEvent event) {
    int a = event.getAction();
    switch (v.getId()) {
      case R.id.bone_demo_tv:
        if (a == MotionEvent.ACTION_HOVER_ENTER) {
          findViewById(R.id.bone_demo_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_HOVER_EXIT) {
          v.bringToFront();
        }
        break;
      case R.id.bone_studio_tv:
        if (a == MotionEvent.ACTION_HOVER_ENTER) {
          findViewById(R.id.bone_studio_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_HOVER_EXIT) {
          v.bringToFront();
        }
        break;
      case R.id.particle_demo_tv:
        if (a == MotionEvent.ACTION_HOVER_ENTER) {
          findViewById(R.id.particle_demo_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_HOVER_EXIT) {
          v.bringToFront();
        }
        break;
      case R.id.particle_studio_tv:
        if (a == MotionEvent.ACTION_HOVER_ENTER) {
          findViewById(R.id.particle_studio_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_HOVER_EXIT) {
          v.bringToFront();
        }
        break;
      default:
        break;
    }
    return false;
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    int a = event.getAction();
    switch (v.getId()) {
      case R.id.bone_demo_tv:
        if (a == MotionEvent.ACTION_DOWN) {
          findViewById(R.id.bone_demo_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {
          v.bringToFront();
        }
        break;
      case R.id.bone_studio_tv:
        if (a == MotionEvent.ACTION_DOWN) {
          findViewById(R.id.bone_studio_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {
          v.bringToFront();
        }
        break;
      case R.id.particle_demo_tv:
        if (a == MotionEvent.ACTION_DOWN) {
          findViewById(R.id.particle_demo_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {
          v.bringToFront();
        }
        break;
      case R.id.particle_studio_tv:
        if (a == MotionEvent.ACTION_DOWN) {
          findViewById(R.id.particle_studio_sv).bringToFront();
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {
          v.bringToFront();
        }
        break;
      default:
        break;
    }
    return false;
  }
}
