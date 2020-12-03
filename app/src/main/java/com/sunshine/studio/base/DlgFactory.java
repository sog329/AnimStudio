package com.sunshine.studio.base;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sunshine.engine.base.Function;
import com.sunshine.engine.base.LayoutHelper;
import com.sunshine.engine.base.ViewHelper;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BmpRect;
import com.sunshine.studio.bone.logic.BoneIv;
import com.sunshine.studio.bone.logic.BoneStudio;
import com.sunshine.studio.bone.logic.ExtendIv;
import com.sunshine.studio.bone.logic.ProjectLv;

import java.util.ArrayList;
import java.util.List;


import static com.sunshine.studio.base.StudioTool.EXTERNAL;
import static com.sunshine.studio.base.StudioTool.PNG;

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

              @Override
              public void onLoading(boolean b) {
                view.findViewById(R.id.loading).setVisibility(b ? View.VISIBLE : View.INVISIBLE);
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

  public static Dialog packer(final Studio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_packer, null);
    View left = view.findViewById(R.id.left);
    int w = StudioTool.getDlgHeight();
    int h = StudioTool.getDlgHeight();
    left.setLayoutParams(new LinearLayout.LayoutParams(w, h));
    PicGv gv = view.findViewById(R.id.gv);
    gv.setLayoutParams(
        new LinearLayout.LayoutParams(StudioTool.getPicDlgWidth(), StudioTool.getDlgHeight()));
    BoneIv iv = view.findViewById(R.id.iv);
    iv.autoSize = false;
    iv.drawRc = true;
    View loading = view.findViewById(R.id.loading);
    builder.setView(view);
    final Dialog dialog = builder.create();
    PicGv.PicAdapter adapter =
        new PicGv.PicAdapter(
            new PicGv.PicAdapter.Packer() {
              @Override
              public void buildLstBmpRc(List<BmpRect> lst) {
                lst.clear();
                new PlistParser().parse(studio.getPath("pic.plist"), lst);
              }

              @Override
              public int getColor() {
                return studio.getColor();
              }
            });
    gv.setAdapter(adapter);
    View build = view.findViewById(R.id.build);
    build.setBackgroundResource(
        studio instanceof BoneStudio ? R.drawable.bg_btn : R.drawable.bg_btn_p);
    build.setOnClickListener(
        v -> {
          List<String> lst = new ArrayList<>(adapter.lstCheck);
          if (lst.size() > 0) {
            loading.setVisibility(View.VISIBLE);
            new Thread(
                    () -> {
                      Packer.buildPic(
                          studio.getPath(null),
                          (dir, name) ->
                              lst.contains(StudioTool.getFileName(name)) && name.endsWith(PNG));
                      Bitmap bmp =
                          StudioTool.getBmp(studio.getPath("pic"), iv.getWidth(), iv.getHeight());
                      ViewHelper.handler.post(
                          () -> {
                            iv.setTag(bmp);
                            iv.setBmp(bmp, null);
                            studio.onGetProject(studio.projectName);
                            adapter.loadData(studio.getPath(null));
                            loading.setVisibility(View.GONE);
                            studio.act.findViewById(R.id.save).performClick();
                          });
                    })
                .start();
          }
        });

    dialog.setOnShowListener(
        d -> {
          Bitmap bmp = (Bitmap) iv.getTag();
          if (bmp != null) {
            bmp.recycle();
            iv.setTag(null);
          }
          iv.setBmp(studio.entity.bmp, null);
          adapter.loadData(studio.getPath(null));

          boolean hasPic = adapter.lstCheck.size() > 0;
          build.setVisibility(hasPic ? View.VISIBLE : View.GONE);
          gv.setVisibility(hasPic ? View.VISIBLE : View.GONE);
          if (!hasPic) {
            StudioTool.showToast(studio.act, "Unable to find resources");
          }
        });
    return dialog;
  };

  public static Dialog pic(final Studio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_pic, null);
    PicGv gv = view.findViewById(R.id.gv);
    gv.setLayoutParams(
        new FrameLayout.LayoutParams(StudioTool.getPicDlgWidth(), StudioTool.getDlgHeight()));
    builder.setView(view);
    final Dialog dialog = builder.create();
    PicGv.PicAdapter adapter =
        new PicGv.PicAdapter(
            new PicGv.PicAdapter.Pic() {
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
    dialog.setOnShowListener(d -> adapter.loadData(null));
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
              .map(studio.entity.duration, n -> studio.entity.duration = n);
          // w
          ((StudioEt<Integer>) view.findViewById(R.id.width))
              .map(
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
              .map(
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
          // plus
          studio.onInitDlgEntity(view);
        });

    return dialog;
  }

  public static Dialog extend(final BoneStudio studio) {
    AlertDialog.Builder builder = new AlertDialog.Builder(studio.act, R.style.AppDialog);
    View view = LayoutInflater.from(studio.act).inflate(R.layout.dlg_studio_extend, null);
    // container_left
    View left = view.findViewById(R.id.container_left);
    left.setLayoutParams(
        new LinearLayout.LayoutParams(StudioTool.getDlgHeight(), StudioTool.getDlgHeight()));
    // iv_normal
    ExtendIv iv = view.findViewById(R.id.iv_normal);
    iv.autoSize = false;
    // container_right
    View right = view.findViewById(R.id.container_right);
    // iv_tall
    ExtendIv ivTall = view.findViewById(R.id.iv_tall);
    ivTall.autoSize = false;
    ivTall.inExtendY(true);
    // tv
    StudioTv tv = view.findViewById(R.id.tv);
    builder.setView(view);
    // extendY
    View extendY = view.findViewById(R.id.extendY);
    extendY.setOnClickListener(
        v -> right.setVisibility(right.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
    final Dialog dialog = builder.create();
    StudioEt<Integer> et = view.findViewById(R.id.et);

    Function<Boolean> extend =
        e -> {
          ivTall.setVisibility(e ? View.GONE : View.VISIBLE);
          left.setVisibility(e ? View.GONE : View.VISIBLE);
          right.setLayoutParams(
              new LinearLayout.LayoutParams(
                  e ? StudioTool.getDlgHeight() : StudioTool.getDlgHeight() / 2,
                  e ? LayoutParams.WRAP_CONTENT : StudioTool.getDlgHeight()));
          et.setHint(null);
          if (!e) {
            right.setVisibility(studio.bone.extendY == null ? View.GONE : View.VISIBLE);
          } else {
            right.setVisibility(View.VISIBLE);
          }
        };

    dialog.setOnShowListener(
        d -> {
          tv.setText(studio.bone.name);
          tv.setVisibility(studio.bone.name == null ? View.GONE : View.VISIBLE);
          boolean external = studio.bone.externalId != null;
          extend.call(external);
          if (external) {
            et.map(
                studio.bone.externalId,
                s -> {
                  if (s == null || s.isEmpty()) {
                    studio.bone.externalId = EXTERNAL;
                  } else {
                    studio.bone.externalId = s;
                  }
                  studio.updateAnimLv();
                });
          } else {
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
            final int max = studio.bone.lstRect.get(0).height();
            et.setHint("[1, " + max + "]");
            et.map(
                studio.bone.extendY,
                y -> {
                  if (y <= 0 || y > max) {
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
          }
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
