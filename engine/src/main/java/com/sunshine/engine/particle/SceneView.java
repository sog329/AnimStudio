package com.sunshine.engine.particle;

import android.content.Context;
import android.util.AttributeSet;

import com.sunshine.engine.base.AnimView;
import com.sunshine.engine.particle.logic.SceneHelper;

public class SceneView extends AnimView<SceneHelper> {
  public SceneView(Context context) {
    super(context);
  }

  public SceneView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SceneView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public SceneHelper buildHelper() {
    return new SceneHelper();
  }
}
