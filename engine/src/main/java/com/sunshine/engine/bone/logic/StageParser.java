package com.sunshine.engine.bone.logic;

import android.graphics.Rect;

import com.sunshine.engine.base.XmlParser;

public class StageParser extends XmlParser {
  public static final String STAGE = "stage";
  public static final String ACTOR = "actor";
  public static final String BONE = "bone";
  public static final String ANIM = "anim";

  public static final String SRC_ID_WH = "src_id_wh";
  public static final String RANGE = "range";
  public static final String MOVE = "move";
  public static final String MOVE_INTERPOLATOR = "move_interpolator";
  public static final String SCALE = "scale";
  public static final String SCALE_INTERPOLATOR = "scale_interpolator";
  public static final String ROTATE = "rotate";
  public static final String ROTATE_INTERPOLATOR = "rotate_interpolator";
  public static final String ALPHA = "alpha";
  public static final String ALPHA_INTERPOLATOR = "alpha_interpolator";

  private Stage stage = null;

  public StageParser(Stage stage) {
    this.stage = stage;
  }

  @Override
  public void parseTag(String tag, String[] ary, boolean start) {
    if (start) {
      switch (tag) {
        case ACTOR:
          stage.lstActor.add(new Actor());
          break;
        case BONE:
          stage.getLastActor().lstBone.add(new Bone());
          break;
        case ANIM:
          Bone bone = stage.getLastActor().getLastBone();
          bone.lstAnim.add(new Anim());
          Anim anim = bone.getLastAnim();
          anim.halfSize.width = bone.lstRect.get(0).width() / 2f;
          anim.halfSize.height = bone.lstRect.get(0).height() / 2f;
          break;
        default:
          break;
      }
    } else {
      Anim anim;
      Bone bone;
      switch (tag) {
        case DURATION:
          if (stage.useScriptDuration) {
            stage.duration = Integer.parseInt(ary[0]);
          }
          break;
        case WIDTH_HEIGHT:
          stage.scriptSize.width = Integer.parseInt(ary[0]);
          stage.scriptSize.height = Integer.parseInt(ary[1]);
          break;
        case LAYOUT_TYPE:
          stage.layoutType = ary[0];
          break;
        case NAME:
          bone = stage.getLastActor().getLastBone();
          bone.name = ary[0];
          break;
        case SRC_LTWH:
          bone = stage.getLastActor().getLastBone();
          int left = Integer.parseInt(ary[0]);
          int top = Integer.parseInt(ary[1]);
          int right = Integer.parseInt(ary[2]) + left;
          int bottom = Integer.parseInt(ary[3]) + top;
          Rect rc = new Rect(left, top, right, bottom);
          bone.lstRect.add(rc);
          break;
        case EXTEND_Y:
          bone = stage.getLastActor().getLastBone();
          bone.extendY = Integer.parseInt(ary[0]);
          break;
        case SRC_ID_WH:
          bone = stage.getLastActor().getLastBone();
          bone.externalBmpId = ary[0];
          bone.lstRect.add(new Rect(0, 0, Integer.parseInt(ary[1]), Integer.parseInt(ary[2])));
          break;
        case RANGE:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.duration.set(Float.parseFloat(ary[0]), Float.parseFloat(ary[1]));
          break;
        case MOVE:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.centerX.set(Float.parseFloat(ary[0]), Float.parseFloat(ary[2]));
          anim.centerY.set(Float.parseFloat(ary[1]), Float.parseFloat(ary[3]));
          break;
        case MOVE_INTERPOLATOR:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.centerX.setInterpolator(ary[0]);
          anim.centerY.setInterpolator(ary[1]);
          break;
        case SCALE:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.scaleX.set(Float.parseFloat(ary[0]), Float.parseFloat(ary[2]));
          anim.scaleY.set(Float.parseFloat(ary[1]), Float.parseFloat(ary[3]));
          break;
        case SCALE_INTERPOLATOR:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.scaleX.setInterpolator(ary[0]);
          anim.scaleY.setInterpolator(ary[1]);
          break;
        case ROTATE:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.rotate.set(Integer.parseInt(ary[0]), Integer.parseInt(ary[1]));
          anim.ptRotate.set(Float.parseFloat(ary[2]), Float.parseFloat(ary[3]));
          break;
        case ROTATE_INTERPOLATOR:
          stage.getLastActor().getLastBone().getLastAnim().rotate.setInterpolator(ary[0]);
          break;
        case ALPHA:
          anim = stage.getLastActor().getLastBone().getLastAnim();
          anim.alpha.set(Integer.parseInt(ary[0]), Integer.parseInt(ary[1]));
          break;
        case ALPHA_INTERPOLATOR:
          stage.getLastActor().getLastBone().getLastAnim().alpha.setInterpolator(ary[0]);
          break;
        default:
          break;
      }
    }
  }
}
