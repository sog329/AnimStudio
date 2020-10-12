package com.sunshine.studio.base;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sunshine.engine.base.LayoutHelper;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BmpRect;
import com.sunshine.studio.bone.logic.BoneStudio;
import com.sunshine.studio.bone.logic.ExtendIv;
import com.sunshine.studio.bone.logic.ProjectLv;

import java.util.List;

import static com.sunshine.studio.base.StudioTool.EXTERNAL;

/** Created by songxiaoguang on 2017/12/2. */
public class DlgFactory {
  public static Dialog project(final Studio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_project, null);
    ProjectLv lv = view.findViewById(R.id.lv);
    lv.setLayoutParams(
        new FrameLayout.LayoutParams(
            (int) (StudioTool.getDlgWidth() * 1.5f), StudioTool.getDlgHeight()));
    builder.setView(view);
    final Dialog dialog = builder.create();
    ProjectLv.FileAdapter adapter =
        new ProjectLv.FileAdapter(
            new ProjectLv.FileAdapter.Callback() {
              @Override
              public void onClick(String name) {
                dialog.dismiss();
                studio.onGetProject(name);
              }

              @Override
              public void onDel(String name) {
                if (name.equals(studio.projectName)) {
                  dialog.dismiss();
                  studio.act.finish();
                }
              }

              @Override
              public String getFolderName() {
                return studio.getProjectFolderName();
              }

              @Override
              public String getProjectName() {
                return studio.projectName;
              }

              @Override
              public XmlWriter.Callback getWriter(String name) {
                return studio.getWriter(null, name);
              }

              @Override
              public int getColor() {
                return studio.getColor();
              }
            });
    lv.setAdapter(adapter);
    dialog.setOnShowListener(d -> adapter.loadData());
    dialog.setOnDismissListener(
        d -> {
          if (studio.entity == null) {
            studio.act.finish();
          }
        });
    return dialog;
  }

  public static Dialog pic(final Studio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_pic, null);
    PicGv gv = view.findViewById(R.id.gv);
    gv.setLayoutParams(
        new FrameLayout.LayoutParams(StudioTool.getDlgWidth() * 2, StudioTool.getDlgHeight()));
    builder.setView(view);
    final Dialog dialog = builder.create();
    PicGv.PicAdapter adapter =
        new PicGv.PicAdapter(
            new PicGv.PicAdapter.Callback() {
              @Override
              public void onClick(BmpRect bmpRect, boolean isExternal) {
                studio.onGetPicRect(bmpRect, isExternal);
                dialog.dismiss();
              }

              @Override
              public void buildLstBmpRc(List<BmpRect> lst) {
                lst.clear();
                new PlistParser().parse(studio.getPath("pic.plist"), lst);
              }

              @Override
              public Bitmap getBmp(boolean isExternal) {
                return isExternal ? studio.entity.mapBmp.get(EXTERNAL) : studio.entity.bmp;
              }

              @Override
              public int getColor() {
                return studio.getColor();
              }
            });
    gv.setAdapter(adapter);
    dialog.setOnShowListener(d -> adapter.loadData());
    return dialog;
  }

  public static Dialog entity(final Studio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_entity, null);
    view.findViewById(R.id.ln)
        .setLayoutParams(
            new FrameLayout.LayoutParams(
                StudioTool.getDlgWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
    builder.setView(view);
    final Dialog dialog = builder.create();
    dialog.setOnShowListener(
        d -> {
          // duration
          ((StudioEt<Integer>) view.findViewById(R.id.duration))
              .mapValue(studio.entity.duration, n -> studio.entity.duration = n);
          // w
          ((StudioEt<Integer>) view.findViewById(R.id.width))
              .mapValue(
                  studio.entity.scriptSize.width,
                  n -> {
                    float dif = (n - studio.entity.scriptSize.width) / 2f;
                    if (studio.entity instanceof Stage) {
                      Stage s = (Stage) studio.entity;
                      for (Actor a : s.lstActor) {
                        for (Bone b : a.lstBone) {
                          for (Anim anim : b.lstAnim) {
                            anim.centerX.dif(dif);
                          }
                        }
                      }
                    }
                    studio.entity.scriptSize.width = n;
                    LayoutHelper.resize(studio.entity);
                  });
          // h
          ((StudioEt<Integer>) view.findViewById(R.id.height))
              .mapValue(
                  studio.entity.scriptSize.height,
                  n -> {
                    float dif = (n - studio.entity.scriptSize.height) / 2f;
                    if (studio.entity instanceof Stage) {
                      Stage s = (Stage) studio.entity;
                      for (Actor a : s.lstActor) {
                        for (Bone b : a.lstBone) {
                          for (Anim anim : b.lstAnim) {
                            anim.centerY.dif(dif);
                          }
                        }
                      }
                    }
                    studio.entity.scriptSize.height = n;
                    LayoutHelper.resize(studio.entity);
                  });
          // resize
          StudioSpinner spinner = view.findViewById(R.id.layout);
          spinner.layoutType(
              studio.entity.layoutType,
              s -> {
                studio.entity.layoutType = s;
                LayoutHelper.resize(studio.entity);
              });
        });

    return dialog;
  }

  public static Dialog extend(final BoneStudio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_extend, null);
    // iv_normal
    ExtendIv iv = view.findViewById(R.id.iv_normal);
    iv.autoSize = false;
    iv.setLayoutParams(
        new LinearLayout.LayoutParams(StudioTool.getDlgHeight(), StudioTool.getDlgHeight()));
    // container
    View v = view.findViewById(R.id.container);
    v.setLayoutParams(
        new LinearLayout.LayoutParams(StudioTool.getDlgHeight() / 3, StudioTool.getDlgHeight()));
    // iv_tall
    ExtendIv ivTall = view.findViewById(R.id.iv_tall);
    ivTall.autoSize = false;
    ivTall.inExtendY(true);
    builder.setView(view);
    final Dialog dialog = builder.create();
    dialog.setOnShowListener(
        d -> {
          StudioEt<Integer> et = view.findViewById(R.id.et);
          Runnable rn =
              () -> {
                if (studio.bone.extendY != null) {
                  iv.setExtendY(studio.bone.extendY);
                  ivTall.setVisibility(View.VISIBLE);
                  ivTall.setBmp(studio.entity.bmp, studio.bone.lstRect);
                  ivTall.setExtendY(studio.bone.extendY);
                } else {
                  iv.setExtendY(null);
                  ivTall.setVisibility(View.INVISIBLE);
                  ivTall.setBmp(null, null);
                  ivTall.setExtendY(null);
                }
              };
          et.mapValue(
              studio.bone.extendY,
              y -> {
                if (y <= 0 || y > studio.bone.lstRect.get(0).height()) {
                  studio.bone.extendY = null;
                  rn.run();
                  if (et.getText() != null && et.getText().length() > 0) {
                    et.setText(null);
                  }
                } else {
                  studio.bone.extendY = y;
                  rn.run();
                }
              });
          iv.setBmp(studio.entity.bmp, studio.bone.lstRect);
          rn.run();
        });
    return dialog;
  }

  public static Dialog interpolator(final Studio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_interpolator, null);
    View dlg = view.findViewById(R.id.dlg);
    int w = StudioTool.getDlgHeight() * 2;
    dlg.setLayoutParams(new FrameLayout.LayoutParams(w, StudioTool.getDlgHeight()));
    InterpolatorView iv = view.findViewById(R.id.iv);
    iv.inDetail(true);
    builder.setView(view);
    final Dialog dialog = builder.create();
    return dialog;
  }
}
