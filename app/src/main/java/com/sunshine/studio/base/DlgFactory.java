package com.sunshine.studio.base;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sunshine.engine.base.LayoutHelper;
import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BmpRect;
import com.sunshine.studio.bone.logic.ProjectLv;

import java.util.List;

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
              public String getFolderName() {
                return studio.getProjectFolderName();
              }

              @Override
              public XmlWriter.Callback getWriter() {
                return studio.getWriter(null);
              }
            });
    lv.setAdapter(adapter);
    dialog.setOnShowListener(d -> adapter.loadData());
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
    PicGv.BoneAdapter adapter =
        new PicGv.BoneAdapter(
            new PicGv.BoneAdapter.Callback() {
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
                return isExternal ? studio.entity.mapBmp.get("external") : studio.entity.bmp;
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
                    studio.entity.scriptSize.width = n;
                    LayoutHelper.resize(studio.entity);
                  });
          // h
          ((StudioEt<Integer>) view.findViewById(R.id.height))
              .mapValue(
                  studio.entity.scriptSize.height,
                  n -> {
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
}
