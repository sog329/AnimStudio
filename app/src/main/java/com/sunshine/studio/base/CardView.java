package com.sunshine.studio.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

/** Created by Jack on 2020/5/22. */
public class CardView extends FrameLayout {
  private final CameraHelper hpCamera = new CameraHelper();
  private final CardHelper hpCard = new CardHelper();
  protected Callback cb = null;

  public CardView(@NonNull Context context) {
    super(context);
  }

  public CardView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (cb != null) {
          cb.onTouchDown();
        }
      case MotionEvent.ACTION_MOVE:
        hpCamera.rotateCamera(event, getWidth() / 2, getHeight() / 2);
        postInvalidate();
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        hpCamera.toBalance();
        postInvalidate();
        break;
    }
    return super.onTouchEvent(event);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    hpCard.resizePath(w, h);
  }

  @Override
  public void draw(Canvas can) {
    // 3D
    hpCamera.loadMatrix(this, can, getWidth() / 2, getHeight() / 2);
    // 阴影
    can.drawRoundRect(hpCard.rc, hpCard.radius, hpCard.radius, hpCard.pt);
    // 圆角
    can.clipPath(hpCard.path);
    super.draw(can);
    if (hpCamera.inAnim()) {
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
      float scale = (100 - 2f * rt) / 100f;
      matrix.postScale(scale, scale);
      camera.restore();
      matrix.preTranslate(-centerX, -centerY);
      matrix.postTranslate(centerX, centerY);
      can.concat(matrix);
    }
  }

  public CardView setBgColor(int bgColor) {
    hpCard.setBgColor(bgColor);
    invalidate();
    return this;
  }

  public CardView setSdColor(int sdColor) {
    hpCard.setSdColor(sdColor);
    invalidate();
    return this;
  }

  public CardView setShadow(int px) {
    hpCard.setShadow(px, getWidth(), getHeight());
    invalidate();
    return this;
  }

  public CardView setRadius(int px) {
    hpCard.setRadius(px, getWidth(), getHeight());
    invalidate();
    return this;
  }

  private static class CardHelper {
    private final Path path = new Path();
    private final RectF rc = new RectF();
    private final Paint pt = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int shadow = 20;
    private int sdColor = Color.BLACK;
    private int radius = 50;

    public CardHelper() {
      setSdColor(Color.BLACK);
      setBgColor(Color.WHITE);
    }

    private void resizePath(int w, int h) {
      rc.set(shadow, shadow, w - shadow, h - shadow);
      path.reset();
      path.addRoundRect(rc, radius, radius, Path.Direction.CCW);
    }

    public void setBgColor(int bgColor) {
      pt.setColor(bgColor);
    }

    public void setSdColor(int sdColor) {
      this.sdColor = sdColor;
      resetShadow(shadow, sdColor);
    }

    public void setShadow(int px, int w, int h) {
      shadow = px;
      resetShadow(px, sdColor);
      resizePath(w, h);
    }

    private void resetShadow(int px, int sdColor) {
      pt.setShadowLayer(px, 0, 0, sdColor);
    }

    public void setRadius(int px, int w, int h) {
      radius = px;
      resizePath(w, h);
    }
  }

  public interface Callback {
    void onTouchDown();
  }

  @Deprecated
  public void setElevation(float elevation) {}

  @Deprecated
  public void setBackgroundDrawable(Drawable background) {}
}
