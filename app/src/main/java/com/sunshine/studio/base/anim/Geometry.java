package com.sunshine.studio.base.anim;

import android.animation.ValueAnimator;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.sunshine.studio.base.AnimLayout;

/** Created by Jack on 2020/5/28. */
public class Geometry implements AnimLayout.Anim {
  private final CameraHelper hpCamera = new CameraHelper();

  @Override
  public void draw(View v, Canvas can) {
    hpCamera.loadMatrix(can, v.getWidth() / 2, v.getHeight() / 2);
    v.draw(can);
    if (hpCamera.inAnim()) {
      v.postInvalidate();
    }
  }

  @Override
  public void dispatchTouchEvent(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
        hpCamera.rotateCamera(event, v.getWidth() / 2, v.getHeight() / 2);
        v.postInvalidate();
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        hpCamera.toBalance();
        v.postInvalidate();
        break;
    }
  }

  @Override
  public void onSizeChanged(View v, int w, int h) {}

  private static class CameraHelper {
    private final float MAX = 5f;
    private Matrix matrix = new Matrix();
    private Camera camera = new Camera();
    private float rotateX;
    private float rotateY;
    private ValueAnimator animX = null;
    private ValueAnimator animY = null;

    private boolean inAnim() {
      return animX != null && animX.isRunning() && animY != null && animY.isRunning();
    }

    public void toBalance() {
      cancelAnim();
      Interpolator it = new OvershootInterpolator();
      animX = ValueAnimator.ofFloat(rotateX, 0).setDuration(500);
      animX.setInterpolator(it);
      animX.addUpdateListener(anim -> rotateX = (float) anim.getAnimatedValue());
      animX.start();

      animY = ValueAnimator.ofFloat(rotateY, 0).setDuration(500);
      animY.setInterpolator(it);
      animY.addUpdateListener(anim -> rotateY = (float) anim.getAnimatedValue());
      animY.start();
    }

    private void cancelAnim() {
      if (animX != null) {
        animX.cancel();
        animX = null;
      }
      if (animY != null) {
        animY.cancel();
        animY = null;
      }
    }

    private void rotateCamera(MotionEvent event, int centerX, int centerY) {
      cancelAnim();
      float rotateX = -(event.getY() - centerY);
      float rotateY = (event.getX() - centerX);
      int range = centerX + centerY;
      float percentX = rotateX / range;
      float percentY = rotateY / range;

      if (percentX > 1) {
        percentX = 1;
      } else if (percentX < -1) {
        percentX = -1;
      }

      if (percentY > 1) {
        percentY = 1;
      } else if (percentY < -1) {
        percentY = -1;
      }

      this.rotateX = percentX * MAX;
      this.rotateY = percentY * MAX;
    }

    private void loadMatrix(Canvas can, int centerX, int centerY) {
      matrix.reset();
      camera.save();
      camera.rotateX(rotateX);
      camera.rotateY(rotateY);
      camera.getMatrix(matrix);
      float rt = Math.max(Math.abs(rotateX), Math.abs(rotateY));
      float scale = (100 - 2f * rt) / 100f;
      matrix.postScale(scale, scale);
      camera.restore();
      matrix.preTranslate(-centerX, -centerY);
      matrix.postTranslate(centerX, centerY);
      can.concat(matrix);
    }
  }
}
