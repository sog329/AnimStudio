package com.sunshine.studio.bone.logic;

import static com.sunshine.studio.base.StudioTool.EXTERNAL;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.SeekBar;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.studio.R;
import com.sunshine.studio.base.DlgFactory;
import com.sunshine.studio.base.PlistParser;
import com.sunshine.studio.base.RenderHelper;
import com.sunshine.studio.base.Studio;
import com.sunshine.studio.base.StudioImageBtn;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.XmlWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by songxiaoguang on 2017/12/2. */
public class BoneStudio extends Studio<Stage> {
  protected Actor actor = null;
  public Bone bone = null;
  protected Dialog dlgExtend = null;

  @Override
  protected void initDlg() {
    super.initDlg();
    dlgExtend = DlgFactory.extend(this);
  }

  @Override
  protected void initBtn() {
    super.initBtn();
    act.findViewById(R.id.add)
        .setOnClickListener(
            v -> {
              Actor actor = new Actor(entity);
              Anim anim = actor.buildAnim();
              anim.duration.set(0f, 1f);
              anim.alpha.set(255, 255);
              actor.lstAnim.add(anim);
              entity.lstActor.add(actor);
              updateAnimLv();
            });
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
        new StageRender.Callback() {
          @Override
          public boolean onMove(int x, int y) {
            return editor.onMove(x, y);
          }

          @Override
          public void draw(Canvas can) {
            editor.drawRect(BoneStudio.this, can);
          }

          @Override
          public void onLoad() {
            // 替换之前脚本中的皮肤映射
            List<BmpRect> lst = new ArrayList<>();
            new PlistParser().parse(getPath("pic.plist"), lst);
            Map<String, BmpRect> map = new HashMap<>();
            if (lst.size() > 0) {
              for (BmpRect r : lst) {
                map.put(r.name, r);
              }
              for (Actor a : entity.lstActor) {
                for (Bone b : a.lstBone) {
                  BmpRect r = map.get(b.name);
                  if (r != null) {
                    b.lstRect = r.lstRect;
                    b.extendY = r.extendY;
                  }
                }
              }
            }
            // 更新mapRc
            entity.mapRc.clear();
            for (BmpRect r : lst) {
              entity.mapRc.put(r.name, r.lstRect.get(0));
            }
            // 刷新动画列表
            updateAnimLv();
            // 生成已读外部调用的临时图片
            for (Actor a : entity.lstActor) {
              for (Bone b : a.lstBone) {
                if (b.externalId != null) {
                  Bitmap bmp = entity.mapBmp.get(b.externalId);
                  if (bmp == null) {
                    bmp = StudioTool.getBmp(b.externalId);
                    entity.mapBmp.put(b.externalId, bmp);
                  }
                }
              }
            }
          }

          @Override
          public void onPercent(float percent) {
            sbProgress.setProgress((int) (100 * percent));
            mask.setPercent(percent);
          }

          @Override
          public void onClickBone(Bone bone) {
            AnimLv animLv = act.findViewById(R.id.lv_anim);
            animLv.selectBone(bone);
          }
        },
        new RenderHelper.Callback() {
          @Override
          public int getDarkColor() {
            return act.getResources().getColor(R.color.btn_bg);
          }

          @Override
          public int getLightColor() {
            return act.getResources().getColor(R.color.btn_bg2);
          }

          @Override
          public int getAnimBgColor() {
            StudioImageBtn cbBg = act.findViewById(R.id.cb_bg);
            return cbBg.isChecked() ? Color.BLACK : Color.WHITE;
          }
        });
  }

  @Override
  public String getProjectFolderName() {
    return "bone";
  }

  @Override
  public XmlWriter.Callback getWriter(Stage stage, String name) {
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
  public int getColor() {
    return act.getResources().getColor(R.color.btn_bg);
  }

  @Override
  public void onGetPicRect(BmpRect bmpRect, boolean isExternal) {
    if (actor != null) {
      Bone bone = new Bone(actor);
      bone.name = bmpRect.name;
      bone.lstRect = bmpRect.lstRect;
      bone.extendY = bmpRect.extendY;
      bone.checkAnim(entity);
      actor.lstBone.add(bone);
      if (isExternal) {
        bone.externalId = EXTERNAL;
        bone.getLastAnim().scaleX.set(50f, 50f);
        bone.getLastAnim().scaleY.set(50f, 50f);
      }
      updateAnimLv();
    }
  }

  @Override
  public void onGetProject(String name) {
    super.onGetProject(name);
    ((StageView) act.findViewById(R.id.sv)).setPercent(0);
  }

  @Override
  public void onInitDlgEntity(View v) {
    // do nothing
  }

  public void onEditAnim(Anim.Helper helper, Anim anim) {
    AnimEditorView editor = act.findViewById(R.id.win_editor);
    editor.edit(this, helper, anim);
  }
}
