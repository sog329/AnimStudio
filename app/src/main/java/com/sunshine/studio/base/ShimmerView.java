package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/** Created by Jack on 2020/5/9. */
public class ShimmerView extends CardView {
  private final OffsetHelper hpOffset =
      new OffsetHelper()
          .setDuration(500, new DecelerateInterpolator())
          .setTimes(0)
          .setLength(2)
          .setRight(false);
  private final DrawHelper hpDraw =
      new DrawHelper().setColor(Color.argb(30, 255, 255, 255)).setRotate(-1f);

  {
    cb = () -> show(false);
  }

  public ShimmerView(Context context) {
    super(context);
  }

  public ShimmerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ShimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void draw(Canvas can) {
    int offset = hpOffset.getOffset(getWidth());
    if (hpOffset.isShowing()) {
      int saveCount = can.saveLayer(null, hpDraw.paint, Canvas.ALL_SAVE_FLAG);
      super.draw(can);
      hpDraw.draw(can, offset, getWidth() / hpOffset.length, getHeight());
      can.restoreToCount(saveCount);
      postInvalidate();
    } else {
      super.draw(can);
    }
  }

  /**
   * 手动展示扫光
   *
   * @param force 强制重新显示扫光
   */
  public void show(boolean force) {
    hpOffset.show(force);
  }

  private static class DrawHelper {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Xfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    private float rotate = 1f;
    private int color = Color.argb(200, 255, 255, 255);

    public DrawHelper() {
      paint.setAntiAlias(true);
    }

    /**
     * 设置扫光的颜色
     *
     * @param color
     * @return
     */
    private DrawHelper setColor(int color) {
      this.color = color;
      return this;
    }

    /**
     * 设置扫光的角度
     *
     * @param rotate
     * @return
     */
    private DrawHelper setRotate(float rotate) {
      this.rotate = rotate;
      return this;
    }

    /**
     * 绘制扫光
     *
     * @param can
     * @param offset 位移
     * @param w 扫光宽度
     * @param h 扫光高度
     */
    private void draw(Canvas can, int offset, int w, int h) {
      can.translate(offset, 0);
      can.skew(rotate, 0);
      paint.setXfermode(mode);
      paint.setColor(color);
      can.drawRect(0, 0, w, h, paint);
      paint.setXfermode(null);
      paint.setAlpha(255);
    }
  }

  private static class OffsetHelper {
    private long startTime = 0;
    private int times = -1; // repeat
    private boolean showing = false;
    private boolean right = true;
    private int length = 5;
    private int duration = 1000;
    private Interpolator interpolator = new LinearInterpolator();

    /**
     * 设置单次扫光动画时长
     *
     * @param duration
     * @return
     */
    private OffsetHelper setDuration(int duration, Interpolator interpolator) {
      if (duration > 0) {
        this.duration = duration;
      }
      if (interpolator != null) {
        this.interpolator = interpolator;
      }
      return this;
    }

    /**
     * 扫光是否向右
     *
     * @param right
     * @return
     */
    private OffsetHelper setRight(boolean right) {
      this.right = right;
      return this;
    }

    /**
     * 设置扫光循环次数，负数代办一直循环
     *
     * @param times
     * @return
     */
    private OffsetHelper setTimes(int times) {
      this.times = times;
      return this;
    }

    /**
     * 设置扫光的宽度，空间宽度的n分之一
     *
     * @param length
     * @return
     */
    private OffsetHelper setLength(int length) {
      if (length > 0) {
        this.length = length;
      }
      return this;
    }

    /**
     * 是否在扫光过程中
     *
     * @return
     */
    private boolean isShowing() {
      return showing;
    }

    private void show(boolean force) {
      if (times == 0) {
        times = 1;
      }
      if (force) {
        startTime = 0;
      } else {
        if (!showing) {
          startTime = 0;
        }
      }
    }

    private int getResult(int w, int time) {
      float k = 1 + 1f / length;
      return (int) (2 * w * k * interpolator.getInterpolation(time % duration * 1f / duration))
          - (int) (w * k);
    }

    private int getOffset(int w) {
      if (w <= 0) {
        showing = false;
        return 0;
      } else {
        if (startTime == 0) {
          startTime = SystemClock.elapsedRealtime();
          showing = true;
          return 0;
        } else {
          int time = (int) (SystemClock.elapsedRealtime() - startTime);
          if (times < 0) {
            showing = true;
            int result = getResult(w, time);
            if (right) {
              return result;
            } else {
              return w - result;
            }
          } else {
            int t = time / duration;
            if (t < times) {
              showing = true;
              int result = getResult(w, time);
              if (right) {
                return result;
              } else {
                return w - result;
              }
            } else {
              showing = false;
              return 0;
            }
          }
        }
      }
    }
  }
}
