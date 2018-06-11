package com.sunshine.studio.bone.logic;

import android.graphics.Rect;

import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.engine.bone.logic.StageParser;
import com.sunshine.studio.base.XmlWriter;

import org.xmlpull.v1.XmlSerializer;

import static com.sunshine.studio.base.XmlWriter.addTag;

/** Created by songxiaoguang on 2017/12/3. */
public class StageWriter implements XmlWriter.Callback {
  private Stage stage = null;

  public StageWriter(Stage stage) {
    this.stage = stage;
  }

  @Override
  public void write(XmlSerializer xml) throws Exception {
    // stage
    xml.startTag(null, StageParser.STAGE);
    addTag(xml, StageParser.DURATION, Long.toString(stage.duration));
    addTag(xml, StageParser.WIDTH_HEIGHT, stage.scriptSize.toString());
    addTag(xml, StageParser.LAYOUT_TYPE, stage.layoutType);
    // actor
    for (Actor actor : stage.lstActor) {
      xml.startTag(null, StageParser.ACTOR);
      // bone
      for (Bone bone : actor.lstBone) {
        xml.startTag(null, StageParser.BONE);
        if (bone.externalBmpId == null) {
          for (Rect rc : bone.lstRect) {
            addTag(
                xml,
                StageParser.SRC_LTWH,
                rc.left + "," + rc.top + "," + rc.width() + "," + rc.height());
          }
        } else {
          addTag(
              xml,
              StageParser.SRC_ID_WH,
              bone.externalBmpId
                  + ","
                  + bone.lstRect.get(0).width()
                  + ","
                  + bone.lstRect.get(0).height());
        }
        // anim
        for (Anim anim : bone.lstAnim) {
          xml.startTag(null, StageParser.ANIM);
          addTag(xml, StageParser.RANGE, anim.duration.toString());
          addTag(
              xml,
              StageParser.MOVE,
              anim.centerX.getFrom()
                  + ","
                  + anim.centerY.getFrom()
                  + ","
                  + anim.centerX.getTo()
                  + ","
                  + anim.centerY.getTo());
          addTag(
              xml,
              StageParser.MOVE_INTERPOLATOR,
              anim.centerX.getInterpolatorName() + "," + anim.centerY.getInterpolatorName());
          addTag(
              xml,
              StageParser.SCALE,
              anim.scaleX.getFrom()
                  + ","
                  + anim.scaleY.getFrom()
                  + ","
                  + anim.scaleX.getTo()
                  + ","
                  + anim.scaleY.getTo());
          addTag(
              xml,
              StageParser.SCALE_INTERPOLATOR,
              anim.scaleX.getInterpolatorName() + "," + anim.scaleY.getInterpolatorName());
          addTag(
              xml,
              StageParser.ROTATE,
              anim.rotate.toString() + "," + anim.ptRotate.x + "," + anim.ptRotate.y);
          addTag(xml, StageParser.ROTATE_INTERPOLATOR, anim.rotate.getInterpolatorName());
          addTag(xml, StageParser.ALPHA, anim.alpha.toString());
          addTag(xml, StageParser.ALPHA_INTERPOLATOR, anim.alpha.getInterpolatorName());
          xml.endTag(null, StageParser.ANIM);
        }
        xml.endTag(null, StageParser.BONE);
      }
      xml.endTag(null, StageParser.ACTOR);
    }
    stage = null;
  }
}
