package com.sunshine.engine.bone.logic;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/11/30. */
public class Actor {
  public List<Bone> lstBone = new ArrayList<>();

  public Bone getLastBone() {
    Bone bone = null;
    if (lstBone.size() > 0) {
      bone = lstBone.get(lstBone.size() - 1);
    }
    return bone;
  }

  public void draw(Stage stage, Canvas can) {
    for (Bone bone : lstBone) {
      bone.draw(stage, can);
    }
  }
}
