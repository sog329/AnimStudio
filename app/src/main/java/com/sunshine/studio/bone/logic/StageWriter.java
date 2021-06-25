package com.sunshine.studio.bone.logic;

import static com.sunshine.studio.base.XmlWriter.addTag;

import android.graphics.Rect;
import com.sunshine.engine.base.XmlParser;
import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.engine.bone.logic.StageParser;
import com.sunshine.studio.base.XmlWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlSerializer;

/** Created by songxiaoguang on 2017/12/3. */
public class StageWriter implements XmlWriter.Callback {
  private Stage stage = null;

  public StageWriter(Stage stage) {
    this.stage = stage;
  }

  @Override
  public void write(XmlSerializer xml) throws Exception {
    Map<String, Rect> map = new HashMap<>(stage.mapRc);
    // stage
    xml.startTag(null, StageParser.STAGE);
    addTag(xml, StageParser.DURATION, Long.toString(stage.duration));
    addTag(xml, StageParser.WIDTH_HEIGHT, stage.scriptSize.toString());
    addTag(xml, StageParser.LAYOUT_TYPE, stage.layoutType);
    // actor
    for (Actor actor : stage.lstActor) {
      xml.startTag(null, StageParser.ACTOR);
      writeAnim(xml, actor.lstAnim);
      // bone
      for (Bone bone : actor.lstBone) {
        xml.startTag(null, StageParser.BONE);
        if (bone.name != null && !bone.name.isEmpty()) {
          addTag(xml, StageParser.NAME, bone.name);
          map.remove(bone.name);
        }
        if (bone.externalId == null) {
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
              bone.externalId
                  + ","
                  + bone.lstRect.get(0).width()
                  + ","
                  + bone.lstRect.get(0).height());
        }
        if (bone.extendY != null) {
          addTag(xml, StageParser.EXTEND_Y, bone.extendY.toString());
        }
        writeAnim(xml, bone.lstAnim);
        xml.endTag(null, StageParser.BONE);
      }
      xml.endTag(null, StageParser.ACTOR);
    }
    // src
    for (String k : map.keySet()) {
      Rect r = map.get(k);
      addTag(
          xml, XmlParser.SRC, k + "," + r.left + "," + r.top + "," + r.width() + "," + r.height());
    }
    stage = null;
  }

  private void writeAnim(XmlSerializer xml, List<Anim> lstAnim) throws Exception {
    // anim
    for (Anim anim : lstAnim) {
      xml.startTag(null, StageParser.ANIM);
      addTag(xml, StageParser.RANGE, anim.duration.toString());
      if (anim.alpha.getFrom() == anim.alpha.getTo() && anim.alpha.getFrom() == 0) {
        addTag(xml, StageParser.ALPHA, anim.alpha.toString());
      } else {
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
        if (anim.centerX.getFrom() == anim.centerX.getTo()
            && anim.centerY.getFrom() == anim.centerY.getTo()) {
          // do nothing
        } else {
          addTag(
              xml,
              StageParser.MOVE_INTERPOLATOR,
              anim.centerX.getInterpolatorName() + "," + anim.centerY.getInterpolatorName());
        }
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
        if (anim.scaleX.getFrom() == anim.scaleX.getTo()
            && anim.scaleY.getFrom() == anim.scaleY.getTo()) {
          // do nothing
        } else {
          addTag(
              xml,
              StageParser.SCALE_INTERPOLATOR,
              anim.scaleX.getInterpolatorName() + "," + anim.scaleY.getInterpolatorName());
        }
        addTag(
            xml,
            StageParser.ROTATE,
            anim.rotate.toString() + "," + anim.ptRotate.x + "," + anim.ptRotate.y);
        if (anim.rotate.getFrom() == anim.rotate.getTo()) {
          // do nothing
        } else {
          addTag(xml, StageParser.ROTATE_INTERPOLATOR, anim.rotate.getInterpolatorName());
        }
        addTag(xml, StageParser.ALPHA, anim.alpha.toString());
        if (anim.alpha.getFrom() == anim.alpha.getTo()) {
          // do nothing
        } else {
          addTag(xml, StageParser.ALPHA_INTERPOLATOR, anim.alpha.getInterpolatorName());
        }
      }
      xml.endTag(null, StageParser.ANIM);
    }
  }
}
