package com.sunshine.studio.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;

import com.sunshine.engine.base.AnimView;
import com.sunshine.engine.base.Entity;
import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BmpRect;

import static com.sunshine.studio.base.StudioTool.getFilePath;

/** Created by Jack on 2018/4/12. */
public abstract class Studio<T extends Entity> {
  public Activity act = null;
  protected String projectName = null;
  protected Dialog dlgProject = null;
  public Dialog dlgPic = null;
  protected Dialog dlgEntity = null;

  public T entity = null;

  public void onCreate(Activity act) {
    this.act = act;
    initDlg();
    initBtn();
    initView();
    dlgProject.show();
  }

  protected void initBtn() {
    AnimView studioSv = act.findViewById(R.id.sv);
    // add
    act.findViewById(R.id.add).setOnClickListener(v -> dlgPic.show());
    // open
    act.findViewById(R.id.open).setOnClickListener(v -> dlgProject.show());
    // save
    act.findViewById(R.id.save)
        .setOnClickListener(
            v ->
                XmlWriter.save(
                    getFilePath(getProjectFolderName(), projectName, "config.xml"),
                    getWriter(entity, null)));
    // set
    act.findViewById(R.id.set).setOnClickListener(v -> dlgEntity.show());
    // stage bg color
    StudioCb cbBg = act.findViewById(R.id.cb_bg);
    cbBg.setOnCheckedChangeListener(
        (btn, b) -> studioSv.setBackgroundColor(b ? Color.BLACK : Color.WHITE));
    cbBg.setChecked(true);
    // stage size_w
    AppCompatSeekBar sbSizeW = act.findViewById(R.id.size_w);
    sbSizeW.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            int padding = StudioTool.screenHeight / 4 * (100 - i) / 100;
            studioSv.setPadding(
                padding, studioSv.getPaddingTop(), padding, studioSv.getPaddingBottom());
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    sbSizeW.setMax(100);
    sbSizeW.setProgress(100);
    // stage size_h
    AppCompatSeekBar sbSizeH = act.findViewById(R.id.size_h);
    sbSizeH.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            int padding = StudioTool.screenHeight / 4 * (100 - i) / 100;
            studioSv.setPadding(
                studioSv.getPaddingLeft(), padding, studioSv.getPaddingRight(), padding);
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    sbSizeH.setMax(100);
    sbSizeH.setProgress(100);
  }

  protected abstract void initView();

  public abstract String getProjectFolderName();

  public abstract XmlWriter.Callback getWriter(T entity, String name);

  public abstract void onGetPicRect(BmpRect bmpRect, boolean isExternal);

  public abstract void updateAnimLv();

  protected void initDlg() {
    dlgProject = DlgFactory.project(this);
    dlgPic = DlgFactory.pic(this);
    dlgEntity = DlgFactory.entity(this);
  }

  public void onGetProject(String name) {
    projectName = name;
    AnimView animView = act.findViewById(R.id.sv);
    animView.stop();
    animView.play(getFilePath(getProjectFolderName(), name));

    if (entity != null) {
      entity.destroy();
    }
    entity = ((Callback<T>) animView).getEntity();
    entity.inStudio = true;

    Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    bmp.eraseColor(Color.parseColor("#FFFF00"));
    entity.mapBmp.put("external", bmp);
  }

  public String getPath(String str) {
    return getFilePath(getProjectFolderName(), projectName, str);
  }

  public void onDestroy() {
    act = null;
  }

  public interface Callback<T extends Entity> {
    T getEntity();
  }

  public interface MapValue<T> {
    void update(T value);
  }
}
