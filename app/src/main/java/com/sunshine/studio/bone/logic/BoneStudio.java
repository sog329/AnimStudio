package com.sunshine.studio.bone.logic;

import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;

import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.studio.R;
import com.sunshine.studio.base.RenderHelper;
import com.sunshine.studio.base.Studio;
import com.sunshine.studio.base.XmlWriter;

/** Created by songxiaoguang on 2017/12/2. */
public class BoneStudio extends Studio<Stage> {
  @Override
  protected void initBtn() {
    super.initBtn();
    StudioSv studioSv = act.findViewById(R.id.sv);
    // play
    act.findViewById(R.id.play).setOnClickListener(v -> studioSv.setPercent(0, 1, 0));
    // stage progress
    AppCompatSeekBar sbProgress = act.findViewById(R.id.progress);
    sbProgress.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {

          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b) {
              studioSv.setPercent(i / 100f);
            }
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {}
        });
  }

  public void initView() {
    StudioSv stageView = act.findViewById(R.id.sv);
    AnimEditorView editor = act.findViewById(R.id.win_editor);
    AppCompatSeekBar sbProgress = act.findViewById(R.id.progress);
    PercentMask mask = act.findViewById(R.id.mask);
    stageView.setCallback(
        new RenderHelper.Callback() {
          @Override
          public boolean onMove(int x, int y) {
            return editor.onMove(x, y);
          }

          @Override
          public void draw(Canvas can) {
            editor.drawRect(can);
          }

          @Override
          public void onLoad() {
            updateAnimLv();
          }

          @Override
          public void onPercent(float percent) {
            sbProgress.setProgress((int) (100 * percent));
            mask.setPercent(percent);
          }

          @Override
          public void onClickBone(int index) {
            AnimLv animLv = act.findViewById(R.id.lv_anim);
            animLv.selectBone(index);
            animLv.setSelection(index);
          }
        });
  }

  @Override
  public String getProjectFolderName() {
    return "bone";
  }

  @Override
  public XmlWriter.Callback getWriter(Stage stage) {
    if (stage == null) {
      stage = new Stage(null, null, null, null);
    }
    return new StageWriter(stage);
  }

  @Override
  public void updateAnimLv() {
    AnimLv animLv = act.findViewById(R.id.lv_anim);
    animLv.loadData(this);
  }

  @Override
  public void onGetPicRect(BmpRect bmpRect, boolean isExternal) {
    Bone bone = new Bone();
    bone.name = bmpRect.name;
    bone.lstRect = bmpRect.lstRect;
    bone.extendY = bmpRect.extendY;
    bone.checkAnim(entity);
    if (entity.lstActor.size() == 0) {
      entity.lstActor.add(new Actor());
    }
    entity.getLastActor().lstBone.add(bone);
    if (isExternal) {
      bone.externalBmpId = "external";
      bone.getLastAnim().scaleX.set(50f, 50f);
      bone.getLastAnim().scaleY.set(50f, 50f);
    }
    updateAnimLv();
  }

  @Override
  public void onGetProject(String name) {
    super.onGetProject(name);
    ((StageView) act.findViewById(R.id.sv)).setPercent(0);
  }

  public void onEditAnim(Bone bone, Anim anim) {
    AnimEditorView editor = act.findViewById(R.id.win_editor);
    editor.edit(this, bone, anim);
  }
}
