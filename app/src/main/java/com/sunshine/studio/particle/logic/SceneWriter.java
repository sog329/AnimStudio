package com.sunshine.studio.particle.logic;

import com.sunshine.engine.base.XmlParser;
import com.sunshine.engine.particle.logic.ParticleModel;
import com.sunshine.engine.particle.logic.Scene;
import com.sunshine.engine.particle.logic.SceneParser;
import com.sunshine.studio.base.XmlWriter;

import org.xmlpull.v1.XmlSerializer;

import static com.sunshine.studio.base.XmlWriter.addTag;

/** Created by songxiaoguang on 2017/12/3. */
public class SceneWriter implements XmlWriter.Callback {
  private Scene scene = null;

  public SceneWriter(Scene scene) {
    this.scene = scene;
  }

  @Override
  public void write(XmlSerializer xml) throws Exception {
    // scene
    xml.startTag(null, SceneParser.SCENE);
    addTag(xml, SceneParser.DURATION, Long.toString(scene.duration));
    addTag(xml, SceneParser.WIDTH_HEIGHT, scene.scriptSize.toString());
    addTag(xml, SceneParser.LAYOUT_TYPE, scene.layoutType);
    addTag(xml, SceneParser.MAX, String.valueOf(scene.maxParticle));
    // model
    for (ParticleModel model : scene.lstParticleModel) {
      xml.startTag(null, SceneParser.MODEL);
      if (model.name != null && !model.name.isEmpty()) {
        addTag(xml, XmlParser.NAME, model.name);
      }
      addTag(xml, SceneParser.CHANCE_RANGE, model.chanceRange.toString());
      addTag(xml, SceneParser.ACTIVE_TIME, model.activeTime.toString());
      addTag(
          xml,
          SceneParser.SRC_LTWH,
          model.rcBmp.left
              + ","
              + model.rcBmp.top
              + ","
              + model.rcBmp.width()
              + ","
              + model.rcBmp.height());
      // move
      addTag(xml, SceneParser.MOVE_FROM, model.areaFrom.toString());
      if (model.areaTo.isRotate) {
        addTag(xml, SceneParser.MOVE_ROTATE_TO, model.areaTo.toString());
      } else {
        addTag(xml, SceneParser.MOVE_TO, model.areaTo.toString());
      }
      addTag(
          xml,
          SceneParser.MOVE_INTERPOLATOR,
          model.interpolatorMove[0] + "," + model.interpolatorMove[1]);
      // rotate
      addTag(
          xml,
          SceneParser.ROTATE,
          model.rotateBegin.toString()
              + ","
              + (model.rotateEnd == null
                  ? model.ptRotate.toString()
                  : (model.rotateEnd.toString() + "," + model.ptRotate.toString())));
      addTag(xml, SceneParser.ROTATE_INTERPOLATOR, model.interpolatorRotate);
      // alpha
      addTag(
          xml,
          SceneParser.ALPHA,
          model.alphaBegin.toString()
              + (model.alphaEnd == null ? "" : ("," + model.alphaEnd.toString())));
      addTag(xml, SceneParser.ALPHA_INTERPOLATOR, model.interpolatorAlpha);
      // scale
      addTag(
          xml,
          SceneParser.SCALE,
          model.scaleBegin.toString()
              + (model.scaleEnd == null ? "" : ("," + model.scaleEnd.toString())));
      addTag(xml, SceneParser.SCALE_INTERPOLATOR, model.interpolatorScale);

      xml.endTag(null, SceneParser.MODEL);
    }
    scene = null;
  }
}
