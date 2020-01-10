package com.sunshine.studio.particle.logic;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
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
  private Dialog dlgModel = null;

  public void showModelDlg(ParticleModel pm) {
    dlgModel.show();

    // chance_range_from
    mapFloat(
        R.id.chance_range_from,
        pm.chanceRange.getFrom(),
        v -> {
          pm.chanceRange.setFrom(v);
          updateAnimLv();
        });
    // chance_range_to
    mapFloat(
        R.id.chance_range_to,
        pm.chanceRange.getTo(),
        v -> {
          pm.chanceRange.setTo(v);
          updateAnimLv();
        });
    // activeTime_from
    mapInt(
        R.id.active_time_from,
        pm.activeTime.getFrom(),
        v -> {
          pm.activeTime.setFrom(v);
          updateAnimLv();
        });
    // activeTime_to
    mapInt(
        R.id.active_time_to,
        pm.activeTime.getTo(),
        v -> {
          pm.activeTime.setTo(v);
          updateAnimLv();
        });
    // move_from_left
    mapInt(
        R.id.move_from_left,
        pm.areaFrom.l,
        v -> {
          pm.areaFrom.l = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_from_top
    mapInt(
        R.id.move_from_top,
        pm.areaFrom.t,
        v -> {
          pm.areaFrom.t = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_from_width_match
    mapCheckBox(
        R.id.move_from_width_match,
        pm.areaFrom.w == Area.MATCH_PARENT,
        b -> {
          TextView tv = dlgModel.findViewById(R.id.move_from_width);
          tv.setEnabled(!b);
          tv.setText(String.valueOf(b ? Area.MATCH_PARENT : entity.scriptSize.width));
        });
    // move_from_width
    mapInt(
        R.id.move_from_width,
        pm.areaFrom.w,
        v -> {
          pm.areaFrom.w = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_from_height
    mapInt(
        R.id.move_from_height,
        pm.areaFrom.h,
        v -> {
          pm.areaFrom.h = v;
          entity.setMaxParticle(entity.maxParticle);
        });

    // move_rotate_to
    mapCheckBox(
        R.id.move_rotate_to,
        pm.areaTo.isRotate,
        b -> {
          pm.areaTo.isRotate = b;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_left_offset
    mapCheckBox(
        R.id.move_to_left_offset,
        pm.areaTo.isOffsetLeft,
        b -> {
          pm.areaTo.isOffsetLeft = b;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_left
    mapInt(
        R.id.move_to_left,
        pm.areaTo.l,
        v -> {
          pm.areaTo.l = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_top_offset
    mapCheckBox(
        R.id.move_to_top_offset,
        pm.areaTo.isOffsetTop,
        b -> {
          pm.areaTo.isOffsetTop = b;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_top
    mapInt(
        R.id.move_to_top,
        pm.areaTo.t,
        v -> {
          pm.areaTo.t = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_width
    mapInt(
        R.id.move_to_width,
        pm.areaTo.w,
        v -> {
          pm.areaTo.w = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_to_height
    mapInt(
        R.id.move_to_height,
        pm.areaTo.h,
        v -> {
          pm.areaTo.h = v;
          entity.setMaxParticle(entity.maxParticle);
        });
    // move_interpolator_x
    mapSpinner(R.id.move_interpolator_x, pm.interpolatorMove[0], s -> pm.interpolatorMove[0] = s);
    // move_interpolator_y
    mapSpinner(R.id.move_interpolator_y, pm.interpolatorMove[1], s -> pm.interpolatorMove[1] = s);
    // rotate
    mapInt(R.id.rotate_from_from, pm.rotateBegin.getFrom(), v -> pm.rotateBegin.setFrom(v));
    mapInt(R.id.rotate_from_to, pm.rotateBegin.getTo(), v -> pm.rotateBegin.setTo(v));
    mapCheckBox(
        R.id.rotate_to,
        pm.rotateEnd != null,
        b -> {
          TextView tvFrom = dlgModel.findViewById(R.id.rotate_to_from);
          TextView tvTo = dlgModel.findViewById(R.id.rotate_to_to);
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
        R.id.rotate_to_from,
        pm.rotateEnd == null ? 0 : pm.rotateEnd.getFrom(),
        v -> pm.rotateEnd.setFrom(v));
    mapInt(
        R.id.rotate_to_to,
        pm.rotateEnd == null ? 0 : pm.rotateEnd.getTo(),
        v -> pm.rotateEnd.setTo(v));
    mapFloat(R.id.rotate_x, pm.ptRotate.x, v -> pm.ptRotate.x = v);
    mapFloat(R.id.rotate_y, pm.ptRotate.y, v -> pm.ptRotate.y = v);
    mapSpinner(R.id.rotate_interpolator, pm.interpolatorRotate, s -> pm.interpolatorRotate = s);
    // alpha
    mapInt(R.id.alpha_from_from, pm.alphaBegin.getFrom(), v -> pm.alphaBegin.setFrom(v));
    mapInt(R.id.alpha_from_to, pm.alphaBegin.getTo(), v -> pm.alphaBegin.setTo(v));
    mapInt(R.id.alpha_to_from, pm.alphaEnd.getFrom(), v -> pm.alphaEnd.setFrom(v));
    mapInt(R.id.alpha_to_to, pm.alphaEnd.getTo(), v -> pm.alphaEnd.setTo(v));
    mapSpinner(R.id.alpha_interpolator, pm.interpolatorAlpha, s -> pm.interpolatorAlpha = s);
    // scale
    mapFloat(R.id.scale_from_from, pm.scaleBegin.getFrom(), v -> pm.scaleBegin.setFrom(v));
    mapFloat(R.id.scale_from_to, pm.scaleBegin.getTo(), v -> pm.scaleBegin.setTo(v));
    mapCheckBox(
        R.id.scale_to,
        pm.scaleEnd != null,
        b -> {
          TextView tvFrom = dlgModel.findViewById(R.id.scale_to_from);
          TextView tvTo = dlgModel.findViewById(R.id.scale_to_to);
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
        R.id.scale_to_from,
        pm.scaleEnd == null ? 0f : pm.scaleEnd.getFrom(),
        v -> pm.scaleEnd.setFrom(v));
    mapFloat(
        R.id.scale_to_to,
        pm.scaleEnd == null ? 0f : pm.scaleEnd.getTo(),
        v -> pm.scaleEnd.setTo(v));
    mapSpinner(R.id.scale_interpolator, pm.interpolatorScale, s -> pm.interpolatorScale = s);
  }

  private void mapFloat(int id, float v, MapValue<Float> mapValue) {
    ((StudioEt) dlgModel.findViewById(id)).mapValue(v, mapValue);
  }

  private void mapInt(int id, int v, MapValue<Integer> mapValue) {
    ((StudioEt) dlgModel.findViewById(id)).mapValue(v, mapValue);
  }

  private void mapSpinner(int id, String now, StudioSpinner.Callback cb) {
    ((InterpolatorSpinner) dlgModel.findViewById(id)).interpolator(now, cb);
  }

  private void mapCheckBox(int id, boolean b, Studio.MapValue<Boolean> mapValue) {
    ((StudioCb) dlgModel.findViewById(id)).mapValue(b, mapValue);
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
    dlgModel = builder.create();
  }

  @Override
  protected void initView() {
    StudioSv sceneView = act.findViewById(R.id.sv);
    sceneView.setCallback(() -> updateAnimLv());
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
          new LinearLayout.LayoutParams(MATCH_PARENT, 0, pm.chanceRange.getDelta());
      lp.setMargins(10, 5, 10, 5);
      v.setLayoutParams(lp);
      TextView tvFrom = v.findViewById(R.id.from);
      tvFrom.setText(String.valueOf(pm.chanceRange.getFrom()));
      TextView tvTo = v.findViewById(R.id.to);
      tvTo.setText(String.valueOf(pm.chanceRange.getTo()));
      BoneIv iv = v.findViewById(R.id.iv);
      iv.setBmp(entity.bmp, pm.rcBmp);
      v.setOnClickListener(c -> showModelDlg(pm));
      v.setAlpha(animLv.getChildCount() % 2 == 0 ? 1f : .75f);
      animLv.addView(v);
    }
  }

  @Override
  public void onGetProject(String name) {
    super.onGetProject(name);
    SceneView sceneView = act.findViewById(R.id.sv);
    sceneView.isRepeat(true);
  }
}
