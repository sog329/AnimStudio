package com.sunshine.engine.base;

/** Created by songxiaoguang on 2017/12/8. */
public class LayoutHelper {
  public static void resize(Entity entity) {
    if (entity == null
        || entity.scriptSize.width == 0
        || entity.scriptSize.height == 0
        || entity.viewArea.w == 0
        || entity.viewArea.h == 0) {
      return;
    }
    if (LayoutType.match_height.toString().equals(entity.layoutType)) {
      LayoutType.match_height.resize(entity);
    } else {
      float sc = 1f * entity.viewArea.w / entity.scriptSize.width;
      if (sc * entity.scriptSize.height > entity.viewArea.h) {
        sc = 1f * entity.viewArea.h / entity.scriptSize.height;
      } else {
        float tmp = sc;
        sc = 1f * entity.viewArea.h / entity.scriptSize.height;
        if (sc * entity.scriptSize.width > entity.viewArea.w) {
          sc = tmp;
        } else {
          sc = Math.max(sc, tmp);
        }
      }
      entity.scale = sc;
      entity.drawArea.w = (int) (entity.scale * entity.scriptSize.width);
      entity.drawArea.h = (int) (entity.scale * entity.scriptSize.height);
      entity.drawArea.l = (entity.viewArea.w - entity.drawArea.w) / 2 + entity.viewArea.l;
      try {
        LayoutType.valueOf(entity.layoutType).resize(entity);
      } catch (Exception e) {
        LayoutType.center.resize(entity);
      }
    }
  }
}
