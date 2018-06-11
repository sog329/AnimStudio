package com.sunshine.studio.particle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunshine.studio.R;
import com.sunshine.studio.particle.logic.ParticleStudio;

/** Created by songxiaoguang on 2017/12/2. */
public class ParticleStudioAct extends AppCompatActivity {
  private ParticleStudio studio = new ParticleStudio();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_particle_studio);
    studio.onCreate(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    studio.onDestroy();
  }
}
