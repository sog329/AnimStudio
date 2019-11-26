package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Duration;
import com.sunshine.studio.R;
import com.sunshine.studio.base.InterpolatorSpinner;
import com.sunshine.studio.base.StudioEt;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.StudioTv;

/** Created by songxiaoguang on 2017/12/3. */
public class AnimEditorView extends RelativeLayout implements View.OnClickListener {
  private int[] aryIdTab =
      new int[] {
        R.id.tv_base,
        R.id.tv_move_from,
        R.id.tv_move_to,
        R.id.tv_scale,
        R.id.tv_alpha,
        R.id.tv_rotate_range,
        R.id.tv_rotate_anchor
      };

  private int[] aryIdPanel =
      new int[] {
        R.id.edit_base,
        R.id.edit_move_from,
        R.id.edit_move_to,
        R.id.edit_scale,
        R.id.edit_alpha,
        R.id.edit_rotate_range,
        R.id.edit_rotate_anchor
      };

  private BoneStudio studio = null;
  private Anim anim = null;

  public AnimEditorView(Context context) {
    super(context);
  }

  public AnimEditorView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AnimEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    View editor = findViewById(R.id.editor);

    RelativeLayout.LayoutParams lp = (LayoutParams) editor.getLayoutParams();
    lp.width = StudioTool.screenWidth / 2;
    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    lp.topMargin = StudioTool.screenHeight / 12;
    editor.setLayoutParams(lp);

    for (int id : aryIdTab) {
      findViewById(id).setOnClickListener(this);
    }

    setOnClickListener(v -> setVisibility(GONE));
  }

  public void edit(BoneStudio studio, Anim.Helper helper, Anim anim) {
    setVisibility(VISIBLE);
    this.anim = anim;
    this.studio = studio;
    onClick(findViewById(R.id.tv_base));

    // base
    View left = findViewById(R.id.edit_base_left);
    if (anim.duration.getFrom() <= .01f) {
      left.setVisibility(INVISIBLE);
      left.setOnClickListener(null);
    } else {
      left.setVisibility(VISIBLE);
      left.setOnClickListener(
          v -> {
            int index = helper.lstAnim.indexOf(anim);
            Anim last = helper.lstAnim.get(index - 1);
            float nowFrom = StudioTool.format(anim.duration.getFrom() - .01f);
            if (last.duration.getFrom() < nowFrom) {
              last.duration.setTo(nowFrom);
              anim.duration.setFrom(nowFrom);
              ((StudioEt<Float>) findViewById(R.id.edit_base_from)).setText(String.valueOf(nowFrom));
              if (last.duration.getFrom() >= StudioTool.format(nowFrom - .01f)) {
                v.setVisibility(INVISIBLE);
                v.setOnClickListener(null);
              }
            } else {
              v.setVisibility(INVISIBLE);
              v.setOnClickListener(null);
            }
          });
    }
    View right = findViewById(R.id.edit_base_right);
    if (anim.duration.getTo() >= .99f) {
      right.setVisibility(INVISIBLE);
      right.setOnClickListener(null);
    } else {
      right.setVisibility(VISIBLE);
      right.setOnClickListener(
          v -> {
            int index = helper.lstAnim.indexOf(anim);
            Anim next = helper.lstAnim.get(index + 1);
            float nowTo = StudioTool.format(anim.duration.getTo() + .01f);
            if (next.duration.getTo() > nowTo) {
              next.duration.setFrom(nowTo);
              anim.duration.setTo(nowTo);
              ((StudioEt<Float>) findViewById(R.id.edit_base_to)).setText(String.valueOf(nowTo));
              if (next.duration.getTo() <= StudioTool.format(nowTo + .01f)) {
                v.setVisibility(INVISIBLE);
                v.setOnClickListener(null);
              }
            } else {
              v.setVisibility(INVISIBLE);
              v.setOnClickListener(null);
            }
          });
    }
    ((StudioEt<Float>) findViewById(R.id.edit_base_from))
        .mapValue(
            anim.duration.getFrom(),
            f -> {
              anim.duration.setFrom(f);
              helper.checkAnim(studio.entity);
              studio.updateAnimLv();
            })
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, false);
            });
    ((StudioEt<Float>) findViewById(R.id.edit_base_to))
        .mapValue(
            anim.duration.getTo(),
            f -> {
              anim.duration.setTo(f);
              helper.checkAnim(studio.entity);
              studio.updateAnimLv();
            })
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, true);
            });
    findViewById(R.id.edit_base_add)
        .setOnClickListener(
            v -> {
              int index = helper.lstAnim.indexOf(anim);
              float delta = anim.duration.getDelta();
              anim.duration.setTo(anim.duration.getFrom() + delta / 2);
              Anim nextAnim = helper.buildAnim(studio.entity, anim);
              nextAnim.duration.setTo(anim.duration.getFrom() + delta);
              helper.lstAnim.remove(anim);
              helper.lstAnim.add(index, nextAnim);
              helper.lstAnim.add(index, anim);
              helper.checkAnim(studio.entity);
              studio.updateAnimLv();
              setVisibility(GONE);
            });
    findViewById(R.id.edit_base_delete)
        .setOnClickListener(
            v -> {
              int index = helper.lstAnim.indexOf(anim);
              if (index > 0) {
                helper.lstAnim.get(index - 1).duration.setTo(anim.duration.getTo());
              }
              helper.lstAnim.remove(anim);
              helper.checkAnim(studio.entity);
              studio.updateAnimLv();
              setVisibility(GONE);
            });
    // scale
    ((StudioEt<Float>) findViewById(R.id.edit_scale_from_x))
        .mapValue(anim.scaleX.getFrom(), f -> anim.scaleX.setFrom(f))
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, false);
            });
    ((StudioEt<Float>) findViewById(R.id.edit_scale_from_y))
        .mapValue(anim.scaleY.getFrom(), f -> anim.scaleY.setFrom(f))
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, false);
            });
    ((StudioEt<Float>) findViewById(R.id.edit_scale_to_x))
        .mapValue(anim.scaleX.getTo(), f -> anim.scaleX.setTo(f))
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, true);
            });
    ((StudioEt<Float>) findViewById(R.id.edit_scale_to_y))
        .mapValue(anim.scaleY.getTo(), f -> anim.scaleY.setTo(f))
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, true);
            });
    ((InterpolatorSpinner) findViewById(R.id.edit_scale_interpolator_x))
        .interpolator(anim.scaleX.getInterpolatorName(), s -> anim.scaleX.setInterpolator(s));

    ((InterpolatorSpinner) findViewById(R.id.edit_scale_interpolator_y))
        .interpolator(anim.scaleY.getInterpolatorName(), s -> anim.scaleY.setInterpolator(s));

    findViewById(R.id.edit_scale_copy)
        .setOnClickListener(
            v -> {
              ((EditText) findViewById(R.id.edit_scale_to_x))
                  .setText(((EditText) findViewById(R.id.edit_scale_from_x)).getText());
              ((EditText) findViewById(R.id.edit_scale_to_y))
                  .setText(((EditText) findViewById(R.id.edit_scale_from_y)).getText());
            });
    // alpha_from
    StudioTv tvFrom = findViewById(R.id.edit_alpha_tip_from);
    AppCompatSeekBar sbAlphaFrom = findViewById(R.id.edit_alpha_from);
    sbAlphaFrom.setOnSeekBarChangeListener(null);
    sbAlphaFrom.setProgress(anim.alpha.getFrom());
    tvFrom.setText(String.valueOf(anim.alpha.getFrom()));
    sbAlphaFrom.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            anim.alpha.setFrom(i);
            tvFrom.setText(String.valueOf(i));
            setPercent(anim.duration, false);
            studio.updateAnimLv();
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    // alpha_to
    StudioTv tvTo = findViewById(R.id.edit_alpha_tip_to);
    AppCompatSeekBar sbAlphaTo = findViewById(R.id.edit_alpha_to);
    sbAlphaTo.setOnSeekBarChangeListener(null);
    sbAlphaTo.setProgress(anim.alpha.getTo());
    tvTo.setText(String.valueOf(anim.alpha.getTo()));
    sbAlphaTo.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            anim.alpha.setTo(i);
            tvTo.setText(String.valueOf(i));
            setPercent(anim.duration, true);
            studio.updateAnimLv();
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    // alpha_interpolator
    ((InterpolatorSpinner) findViewById(R.id.edit_alpha_interpolator))
        .interpolator(anim.alpha.getInterpolatorName(), s -> anim.alpha.setInterpolator(s));
    // rotate
    ((StudioEt<Integer>) findViewById(R.id.edit_rotate_from))
        .mapValue(anim.rotate.getFrom(), n -> anim.rotate.setFrom(n))
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, false);
            });
    ((StudioEt<Integer>) findViewById(R.id.edit_rotate_to))
        .mapValue(anim.rotate.getTo(), n -> anim.rotate.setTo(n))
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, true);
            });
    ((InterpolatorSpinner) findViewById(R.id.edit_rotate_interpolator))
        .interpolator(anim.rotate.getInterpolatorName(), s -> anim.rotate.setInterpolator(s));
    findViewById(R.id.edit_rotate_copy)
        .setOnClickListener(
            v ->
                ((EditText) findViewById(R.id.edit_rotate_to))
                    .setText(((EditText) findViewById(R.id.edit_rotate_from)).getText()));
    // from
    ((StudioEt<Float>) findViewById(R.id.edit_move_from_x))
        .mapValue(anim.centerX.getFrom(), n -> anim.centerX.setFrom(n));
    ((StudioEt<Float>) findViewById(R.id.edit_move_from_y))
        .mapValue(anim.centerY.getFrom(), n -> anim.centerY.setFrom(n));
    findViewById(R.id.edit_move_from_copy)
        .setOnClickListener(
            v -> {
              ((StudioEt) findViewById(R.id.edit_move_to_x))
                  .setText(((StudioEt) findViewById(R.id.edit_move_from_x)).getText());
              ((StudioEt) findViewById(R.id.edit_move_to_y))
                  .setText(((StudioEt) findViewById(R.id.edit_move_from_y)).getText());
            });
    // to
    findViewById(R.id.edit_move_to_copy)
        .setOnClickListener(
            v -> {
              ((StudioEt) findViewById(R.id.edit_move_from_x))
                  .setText(((StudioEt) findViewById(R.id.edit_move_to_x)).getText());
              ((StudioEt) findViewById(R.id.edit_move_from_y))
                  .setText(((StudioEt) findViewById(R.id.edit_move_to_y)).getText());
            });
    ((StudioEt<Float>) findViewById(R.id.edit_move_to_x))
        .mapValue(anim.centerX.getTo(), n -> anim.centerX.setTo(n));
    ((StudioEt<Float>) findViewById(R.id.edit_move_to_y))
        .mapValue(anim.centerY.getTo(), n -> anim.centerY.setTo(n));
    ((InterpolatorSpinner) findViewById(R.id.edit_move_interpolator_x))
        .interpolator(anim.centerX.getInterpolatorName(), s -> anim.centerX.setInterpolator(s));
    ((InterpolatorSpinner) findViewById(R.id.edit_move_interpolator_y))
        .interpolator(anim.centerY.getInterpolatorName(), s -> anim.centerY.setInterpolator(s));
    // anchor
    ((StudioEt<Float>) findViewById(R.id.edit_rotate_x))
        .mapValue(anim.ptRotate.x, n -> anim.ptRotate.x = n)
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, false);
            });
    ((StudioEt<Float>) findViewById(R.id.edit_rotate_y))
        .mapValue(anim.ptRotate.y, n -> anim.ptRotate.y = n)
        .setOnFocusChangeListener(
            (v, b) -> {
              if (b) setPercent(anim.duration, false);
            });
  }

  public void drawRect(Canvas can) {
    if (getVisibility() == VISIBLE) {
      boolean drawLine = isShow(R.id.edit_rotate_anchor) || isShow(R.id.edit_rotate_range);
      if (isShow(R.id.edit_move_from) || isShow(R.id.edit_move_to) || drawLine) {

        if (anim.run(anim.duration.getPercent(studio.entity.getPercent()), studio.entity)) {
          anim.updateDrawInfo(studio.entity);
          studio.entity.mergeDrawInfo();
          int color = getResources().getColor(R.color.btn_bg);
          StudioRender2D.draw(studio.entity, can, color);
          if (drawLine) {
            StudioRender2D.draw(can, studio.entity.drawInfo.ptDst, color);
          }
        }
      }
    }
  }

  private void setPercent(Duration duration, boolean to) {
    StudioSv studioSv = studio.act.findViewById(R.id.sv);
    studioSv.setPercent(to ? (duration.getTo() - .00001f) : duration.getFrom());
  }

  private boolean isShow(int id) {
    return findViewById(id).getVisibility() == VISIBLE;
  }

  public boolean onMove(int x, int y) {
    if (getVisibility() == VISIBLE) {
      x = (int) ((x - studio.entity.drawArea.l) / studio.entity.scale);
      y = (int) ((y - studio.entity.drawArea.t) / studio.entity.scale);
      if (isShow(R.id.edit_move_from)) {
        ((StudioEt) findViewById(R.id.edit_move_from_x)).setText(String.valueOf(x));
        ((StudioEt) findViewById(R.id.edit_move_from_y)).setText(String.valueOf(y));
      } else if (isShow(R.id.edit_move_to)) {
        ((StudioEt) findViewById(R.id.edit_move_to_x)).setText(String.valueOf(x));
        ((StudioEt) findViewById(R.id.edit_move_to_y)).setText(String.valueOf(y));
      } else if (isShow(R.id.edit_rotate_anchor)) {
        float ptX = (anim.halfSize.width + (x - anim.centerX.getFrom()) / anim.scaleX.getFrom());
        float ptY = (anim.halfSize.height + (y - anim.centerY.getFrom()) / anim.scaleY.getFrom());
        ((StudioEt) findViewById(R.id.edit_rotate_x)).setText(String.valueOf(ptX));
        ((StudioEt) findViewById(R.id.edit_rotate_y)).setText(String.valueOf(ptY));
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void onClick(View view) {
    for (int i = 0; i < aryIdTab.length; i++) {
      boolean is = view.getId() == aryIdTab[i];
      findViewById(aryIdTab[i]).setAlpha(is ? 1f : .2f);
      findViewById(aryIdPanel[i]).setVisibility(is ? VISIBLE : GONE);
      setPercent(anim.duration, view.getId() == R.id.tv_move_to);
    }
  }

  @Override
  public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    if (visibility != VISIBLE) {
      anim = null;
      studio = null;
    }
  }
}
