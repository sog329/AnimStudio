package com.sunshine.engine.particle.logic;

import com.sunshine.engine.base.AnimLoader;
import com.sunshine.engine.base.ViewHelper;

public class SceneHelper extends ViewHelper<Scene> {

  @Override
  protected Scene buildEntity(
      ViewHelper helper, String configPath, String picPath, String soundPath) {
    Scene scene = new Scene(this, configPath, picPath, soundPath);
    AnimLoader.load(scene);
    return scene;
  }
}
