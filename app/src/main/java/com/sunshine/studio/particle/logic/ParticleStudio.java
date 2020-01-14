package com.sunshine.studio.particle.logic;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunshine.engine.base.Area;
import com.sunshine.engine.base.ProcessFloat;
import com.sunshine.engine.base.ProcessInt;
import com.sunshine.engine.particle.SceneView;
import com.sunshine.engine.particle.logic.ParticleModel;
import com.sunshine.engine.particle.logic.Scene;
import com.sunshine.studio.R;
import com.sunshine.studio.base.InterpolatorSpinner;
import com.sunshine.studio.base.PlistParser;
import com.sunshine.studio.base.Studio;
import com.sunshine.studio.base.StudioCb;
import com.sunshine.studio.base.StudioEt;
import com.sunshine.studio.base.StudioSpinner;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.XmlWriter;
import com.sunshine.studio.bone.logic.BmpRect;
import com.sunshine.studio.bone.logic.BoneIv;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.sunshine.studio.base.StudioTool.getFilePath;

/** Created by songxiaoguang on 2017/12/2. */
public class ParticleStudio extends Studio<Scene> {
  private ParticleModel model = null;

  private void checkRange() {
    boolean changed = false;
    for (int i = 0; i < entity.lstParticleModel.size(); i++) {
      ParticleModel m = entity.lstParticleModel.get(i);
      // self
      if (i == 0 && m.chanceRange.getFrom() != 0f) {
        m.chanceRange.setFrom(0f);
        changed = true;
      }
      if (i == entity.lstActiveParticle.size() - 1 && m.chanceRange.getTo() != 1f) {
        m.chanceRange.setTo(1f);
        changed = true;
      }
      if (m.chanceRange.getFrom() < 0) {
        m.chanceRange.setFrom(0f);
        changed = true;
      }
      if (m.chanceRange.getTo() > 1) {
        m.chanceRange.setTo(1f);
        changed = true;
      }
      // has last
      if (i > 0) {
        float lastTo = entity.lstParticleModel.get(i - 1).chanceRange.getTo();
        if (lastTo == 1f) {
          List<ParticleModel> lst = new ArrayList<>();
          for (int j = 0; j < i; j++) {
            lst.add(entity.lstParticleModel.get(j));
          }
          entity.lstParticleModel = lst;
          changed = true;
          break;
        }
        if (lastTo < m.chanceRange.getFrom()) {
          m.chanceRange.setFrom(lastTo);
          changed = true;
        }
        if (lastTo < m.chanceRange.getFrom()) {
          m.chanceRange.setFrom(lastTo);
          if (m.chanceRange.getTo() < lastTo) {
            float t = lastTo + .1f;
            m.chanceRange.setTo(t > 1f ? 1f : t);
          }
          changed = true;
        }
      }
    }
    if (changed) {
      updateAnimLv();
    }
  }

  private void renderEditor(View editor, ParticleModel pm) {
    model = pm;
    editor.setVisibility(View.VISIBLE);
    // chance_range_from
    mapFloat(
        editor,
        R.id.chance_range_from,
        pm.chanceRange.getFrom(),
        v -> {
          pm.chanceRange.setFrom(v);
          updateAnimLv();
        });
    // chance_range_to
    mapFloat(
        editor,
        R.id.chance_range_to,
        pm.chanceRange.getTo(),
        v -> {
          pm.chanceRange.setTo(v);
          updateAnimLv();
        });
    // activeTime_from
    mapInt(
        editor,
        R.id.active_time_from,
        pm.activeTime.getFrom(),
        v -> {
          pm.activeTime.setFrom(v);
          updateAnimLv();
        });
    // activeTime_to
    mapInt(
        editor,
        R.id.active_time_to,
        pm.activeTime.getTo(),
        v -> {
          pm.activeTime.setTo(v);
          updateAnimLv();
        });
    // move_from_left
    mapInt(
        editor,
        R.id.move_from_left,
        pm.areaFrom.l,
        v -> {
          pm.areaFrom.l = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_from_top
    mapInt(
        editor,
        R.id.move_from_top,
        pm.areaFrom.t,
        v -> {
          pm.areaFrom.t = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_from_width_match
    mapCheckBox(
        editor,
        R.id.move_from_width_match,
        pm.areaFrom.w == Area.MATCH_PARENT,
        b -> {
          TextView tv = editor.findViewById(R.id.move_from_width);
          tv.setEnabled(!b);
          tv.setText(String.valueOf(b ? Area.MATCH_PARENT : entity.scriptSize.width));
        });
    // move_from_width
    mapInt(
        editor,
        R.id.move_from_width,
        pm.areaFrom.w,
        v -> {
          pm.areaFrom.w = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_from_height
    mapInt(
        editor,
        R.id.move_from_height,
        pm.areaFrom.h,
        v -> {
          pm.areaFrom.h = v;
          entity.setMaxParticle(entity.maxParticle);
        });

    // move_rotate_to
    mapCheckBox(
        editor,
        R.id.move_rotate_to,
        pm.areaTo.isRotate,
        b -> {
          pm.areaTo.isRotate = b;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_left_offset
    mapCheckBox(
        editor,
        R.id.move_to_left_offset,
        pm.areaTo.isOffsetLeft,
        b -> {
          pm.areaTo.isOffsetLeft = b;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_left
    mapInt(
        editor,
        R.id.move_to_left,
        pm.areaTo.l,
        v -> {
          pm.areaTo.l = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_top_offset
    mapCheckBox(
        editor,
        R.id.move_to_top_offset,
        pm.areaTo.isOffsetTop,
        b -> {
          pm.areaTo.isOffsetTop = b;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_top
    mapInt(
        editor,
        R.id.move_to_top,
        pm.areaTo.t,
        v -> {
          pm.areaTo.t = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_width_match
    mapCheckBox(
        editor,
        R.id.move_to_width_match,
        pm.areaTo.w == Area.MATCH_PARENT,
        b -> {
          TextView tv = editor.findViewById(R.id.move_to_width);
          tv.setEnabled(!b);
          tv.setText(String.valueOf(b ? Area.MATCH_PARENT : entity.scriptSize.width));
        });
    // move_to_width
    mapInt(
        editor,
        R.id.move_to_width,
        pm.areaTo.w,
        v -> {
          pm.areaTo.w = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_height
    mapInt(
        editor,
        R.id.move_to_height,
        pm.areaTo.h,
        v -> {
          pm.areaTo.h = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_interpolator_x
    mapSpinner(
        editor, R.id.move_interpolator_x, pm.interpolatorMove[0], s -> pm.interpolatorMove[0] = s);
    // move_interpolator_y
    mapSpinner(
        editor, R.id.move_interpolator_y, pm.interpolatorMove[1], s -> pm.interpolatorMove[1] = s);
    // rotate
    mapInt(editor, R.id.rotate_from_from, pm.rotateBegin.getFrom(), v -> pm.rotateBegin.setFrom(v));
    mapInt(editor, R.id.rotate_from_to, pm.rotateBegin.getTo(), v -> pm.rotateBegin.setTo(v));
    mapCheckBox(
        editor,
        R.id.rotate_to,
        pm.rotateEnd != null,
        b -> {
          TextView tvFrom = editor.findViewById(R.id.rotate_to_from);
          TextView tvTo = editor.findViewById(R.id.rotate_to_to);
          tvFrom.setEnabled(b);
          tvTo.setEnabled(b);
          if (b) {
            pm.rotateEnd = new ProcessInt(200, 300);
            tvFrom.setText(String.valueOf(pm.rotateEnd.getFrom()));
            tvTo.setText(String.valueOf(pm.rotateEnd.getTo()));
          } else {
            pm.rotateEnd = null;
            tvFrom.setText(String.valueOf(0));
            tvTo.setText(String.valueOf(0));
          }
        });
    mapInt(
        editor,
        R.id.rotate_to_from,
        pm.rotateEnd == null ? 0 : pm.rotateEnd.getFrom(),
        v -> pm.rotateEnd.setFrom(v));
    mapInt(
        editor,
        R.id.rotate_to_to,
        pm.rotateEnd == null ? 0 : pm.rotateEnd.getTo(),
        v -> pm.rotateEnd.setTo(v));
    mapFloat(editor, R.id.rotate_x, pm.ptRotate.x, v -> pm.ptRotate.x = v);
    mapFloat(editor, R.id.rotate_y, pm.ptRotate.y, v -> pm.ptRotate.y = v);
    mapSpinner(
        editor, R.id.rotate_interpolator, pm.interpolatorRotate, s -> pm.interpolatorRotate = s);
    // alpha
    mapInt(editor, R.id.alpha_from_from, pm.alphaBegin.getFrom(), v -> pm.alphaBegin.setFrom(v));
    mapInt(editor, R.id.alpha_from_to, pm.alphaBegin.getTo(), v -> pm.alphaBegin.setTo(v));
    mapInt(editor, R.id.alpha_to_from, pm.alphaEnd.getFrom(), v -> pm.alphaEnd.setFrom(v));
    mapInt(editor, R.id.alpha_to_to, pm.alphaEnd.getTo(), v -> pm.alphaEnd.setTo(v));
    mapSpinner(
        editor, R.id.alpha_interpolator, pm.interpolatorAlpha, s -> pm.interpolatorAlpha = s);
    // scale
    mapFloat(editor, R.id.scale_from_from, pm.scaleBegin.getFrom(), v -> pm.scaleBegin.setFrom(v));
    mapFloat(editor, R.id.scale_from_to, pm.scaleBegin.getTo(), v -> pm.scaleBegin.setTo(v));
    mapCheckBox(
        editor,
        R.id.scale_to,
        pm.scaleEnd != null,
        b -> {
          TextView tvFrom = editor.findViewById(R.id.scale_to_from);
          TextView tvTo = editor.findViewById(R.id.scale_to_to);
          tvFrom.setEnabled(b);
          tvTo.setEnabled(b);
          if (b) {
            pm.scaleEnd = new ProcessFloat(1f, 1f);
            tvFrom.setText(String.valueOf(pm.scaleEnd.getFrom()));
            tvTo.setText(String.valueOf(pm.scaleEnd.getTo()));
          } else {
            pm.scaleEnd = null;
            tvFrom.setText(String.valueOf(0));
            tvTo.setText(String.valueOf(0));
          }
        });
    mapFloat(
        editor,
        R.id.scale_to_from,
        pm.scaleEnd == null ? 0f : pm.scaleEnd.getFrom(),
        v -> pm.scaleEnd.setFrom(v));
    mapFloat(
        editor,
        R.id.scale_to_to,
        pm.scaleEnd == null ? 0f : pm.scaleEnd.getTo(),
        v -> pm.scaleEnd.setTo(v));
    mapSpinner(
        editor, R.id.scale_interpolator, pm.interpolatorScale, s -> pm.interpolatorScale = s);
  }

  private void mapFloat(View editor, int id, float v, MapValue<Float> mapValue) {
    ((StudioEt) editor.findViewById(id)).mapValue(v, mapValue);
  }

  private void mapInt(View editor, int id, int v, MapValue<Integer> mapValue) {
    ((StudioEt) editor.findViewById(id)).mapValue(v, mapValue);
  }

  private void mapSpinner(View editor, int id, String now, StudioSpinner.Callback cb) {
    ((InterpolatorSpinner) editor.findViewById(id)).interpolator(now, cb);
  }

  private void mapCheckBox(View editor, int id, boolean b, Studio.MapValue<Boolean> mapValue) {
    ((StudioCb) editor.findViewById(id)).mapValue(b, mapValue);
  }

  @Override
  protected void initDlg() {
    super.initDlg();

    AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.AppDialog);
    View view = LayoutInflater.from(act).inflate(R.layout.dlg_particle_studio_model, null);
    View sv = view.findViewById(R.id.sv);
    sv.setLayoutParams(
        new FrameLayout.LayoutParams(StudioTool.getDlgWidth() * 2, StudioTool.getDlgHeight()));
    builder.setView(view);
  }

  @Override
  protected void initView() {
    StudioSv sceneView = act.findViewById(R.id.sv);
    sceneView.setCallback(
        new SceneRender.Callback() {
          @Override
          public void onLoad() {
            if (entity.lstParticleModel.size() > 0) {
              model = entity.lstParticleModel.get(0);
              renderEditor(act.findViewById(R.id.anim_editor), model);
            } else {
              model = null;
            }
            updateAnimLv();
          }

          @Override
          public ParticleModel getModel() {
            return model;
          }
        });
  }

  @Override
  public String getProjectFolderName() {
    return "particle";
  }

  @Override
  public XmlWriter.Callback getWriter(Scene entity, String name) {
    if (entity == null) {
      entity = new Scene(null, null, null, null);
      List<BmpRect> lst = new ArrayList<>();
      new PlistParser().parse(getFilePath(getProjectFolderName(), name, "pic.plist"), lst);
      for (BmpRect bmpRect : lst) {
        entity.lstParticleModel.add(buildModel(entity, bmpRect));
      }
    }
    return new SceneWriter(entity);
  }

  private ParticleModel buildModel(Scene entity, BmpRect bmpRect) {
    ParticleModel model = new ParticleModel();

    model.name = bmpRect.name;

    if (entity.lstParticleModel.size() > 0) {
      ParticleModel last = entity.lstParticleModel.get(entity.lstParticleModel.size() - 1);
      last.chanceRange.setTo(last.chanceRange.getFrom() / 2 + .5f);
      model.chanceRange.set(last.chanceRange.getTo(), 1f);
    }

    model.rcBmp.set(bmpRect.lstRect.get(0));
    model.size.width = model.rcBmp.width();
    model.size.height = model.rcBmp.height();

    int h = entity.scriptSize.height / 20;
    model.areaFrom.l = 0;
    model.areaFrom.t = -model.size.height - h;
    model.areaFrom.w = entity.scriptSize.width;
    model.areaFrom.h = h;

    model.areaTo.l = 0;
    model.areaTo.t = entity.scriptSize.height + model.size.height;
    model.areaTo.w = entity.scriptSize.width;
    model.areaTo.h = h;

    model.ptRotate.x = model.size.width / 2f;
    return model;
  }

  @Override
  public void onGetPicRect(BmpRect bmpRect, boolean isExternal) {
    entity.lstParticleModel.add(buildModel(entity, bmpRect));
    updateAnimLv();
  }

  @Override
  public void updateAnimLv() {
    LinearLayout animLv = act.findViewById(R.id.anim_lv);
    animLv.removeAllViews();
    for (ParticleModel pm : entity.lstParticleModel) {
      View v = LayoutInflater.from(act).inflate(R.layout.item_particle_studio_anim_lv, null);
      LinearLayout.LayoutParams lp =
          new LinearLayout.LayoutParams(0, MATCH_PARENT, pm.chanceRange.getDelta());
      lp.setMargins(10, 5, 10, 5);
      v.setLayoutParams(lp);
      TextView tvFrom = v.findViewById(R.id.from);
      tvFrom.setText(String.valueOf(pm.chanceRange.getFrom()));
      TextView tvTo = v.findViewById(R.id.to);
      tvTo.setText(String.valueOf(pm.chanceRange.getTo()));
      BoneIv iv = v.findViewById(R.id.iv);
      iv.setBmp(entity.bmp, pm.rcBmp);
      v.setTag(pm);
      v.setOnClickListener(
          c -> {
            checkRange();
            renderEditor(act.findViewById(R.id.anim_editor), pm);
            onSelectModel(animLv);
          });
      changeBg(v, pm);
      animLv.addView(v);
    }
  }

  private void onSelectModel(ViewGroup vg) {
    for (int i = 0; i < vg.getChildCount(); i++) {
      View v = vg.getChildAt(i);
      ParticleModel m = (ParticleModel) v.getTag();
      changeBg(v, m);
    }
  }

  private void changeBg(View v, ParticleModel pm) {
    if (pm == model) {
      v.setBackgroundResource(R.drawable.bg_anim2);
    } else {
      v.setBackgroundResource(R.drawable.bg_anim);
    }
  }

  @Override
  public void onGetProject(String name) {
    super.onGetProject(name);
    SceneView sceneView = act.findViewById(R.id.sv);
    sceneView.isRepeat(true);
    act.findViewById(R.id.anim_editor).setVisibility(View.INVISIBLE);
  }
}
