package com.sunshine.studio.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/** Created by Jack on 2020/5/22. */
public class CardView extends android.support.v7.widget.CardView {
  private final CameraHelper helper = new CameraHelper();
  protected Callback cb = null;

  public CardView(@NonNull Context context) {
    super(context);
    init();
  }

  public CardView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    helper.init(this);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (cb != null) {
          cb.onTouchDown();
        }
      case MotionEvent.ACTION_MOVE:
        helper.rotateCamera(event, getWidth() / 2, getHeight() / 2);
        postInvalidate();
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        helper.toBalance();
        postInvalidate();
        break;
    }
    return super.onTouchEvent(event);
  }

  @Override
  public void draw(Canvas can) {
    helper.loadMatrix(this, can, getWidth() / 2, getHeight() / 2);
    super.draw(can);
    if (helper.inAnim()) {
      postInvalidate();
    }
  }

  private static class CameraHelper {
    private final float MAX = 5f;
    private Matrix matrix = new Matrix();
    private Camera camera = new Camera();
    private float rotateX;
    private float rotateY;
    private ValueAnimator animX = null;
    private ValueAnimator animY = null;
    private float elevation = 0;

    private void init(CardView v) {
      elevation = v.getCardElevation();
    }

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

    private void loadMatrix(CardView v, Canvas can, int centerX, int centerY) {
      matrix.reset();
      camera.save();
      camera.rotateX(rotateX);
      camera.rotateY(rotateY);
      camera.getMatrix(matrix);
      float rt = Math.max(Math.abs(rotateX), Math.abs(rotateY));
      v.setCardElevation(rt < 1 ? elevation : 0);
      float scale = (100 - 2f * rt) / 100f;
      matrix.postScale(scale, scale);
      camera.restore();
      matrix.preTranslate(-centerX, -centerY);
      matrix.postTranslate(centerX, centerY);
      can.concat(matrix);
    }
  }

  public interface Callback {
    void onTouchDown();
  }
}
