package com.sunshine.studio.particle.logic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.R;
import com.sunshine.studio.base.DemoRv;

import java.util.List;

/**
 * Created by Jack on 2019-11-20.
 */
public class ParticleRv extends DemoRv {

  public ParticleRv(Context context) {
    super(context);
  }

  public ParticleRv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ParticleRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void loadData() {
    addData().setParticle("snowing");
    addData().setParticle("tangyuan");
    addData().setParticle("dinner");
    addData().setParticle("superlike");
    addData().setParticle("mahjong");
    addData().setParticle("dumplings");
    addData().setParticle("home");
    addData().setParticle("mic");
    addData().setParticle("lantern");
    addData().setParticle("wave");
    addData().setParticle("blessing");
    addData().setParticle("annual_goods");
    addData().setParticle("fireworks");
    addData().setParticle("miss");
    addData().setParticle("missu");
    addData().setParticle("send_heart");
    addData().setParticle("note");
    addData().setParticle("heart");
    addData().setParticle("welcome_omi_bg");
    addData().setParticle("match_bg");
    addData().setParticle("cry");
    addData().setParticle("hi");
    addData().setParticle("no");
    addData().setParticle("smile");
    addData().setParticle("what");
    addData().setParticle("yeah");
    addData().setParticle("singleDog");
    addData().setParticle("snow");
    addData().setParticle("christmas");
    addData().setParticle("bell");
    addData().setParticle("gift");
    addData().setParticle("star");
  }
}
