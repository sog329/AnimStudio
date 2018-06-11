package com.sunshine.engine.base;

/** Created by songxiaoguang on 2018/1/5. */
public enum LayoutType {
  center(
      "center",
      entity ->
          entity.drawArea.t = (entity.viewArea.h - entity.drawArea.h) / 2 + entity.viewArea.t),
  top("top", entity -> entity.drawArea.t = entity.viewArea.t),
  bottom(
      "bottom",
      entity -> entity.drawArea.t = entity.viewArea.h - entity.drawArea.h + entity.viewArea.t),
  match_height(
      "match_height",
      entity -> {
        entity.scale = 1f * entity.viewArea.h / entity.scriptSize.height;
        entity.drawArea.w = (int) (entity.scale * entity.scriptSize.width);
        entity.drawArea.h = (int) (entity.scale * entity.scriptSize.height);
        entity.drawArea.l = (entity.viewArea.w - entity.drawArea.w) / 2 + entity.viewArea.l;
        entity.drawArea.t = entity.viewArea.t;
      });
  private final String text;
  private final Callback callback;

  LayoutType(String text, Callback callback) {
    this.text = text;
    this.callback = callback;
  }

  @Override
  public String toString() {
    return text;
  }

  public void resize(Entity entity) {
    callback.resize(entity);
  }

  private interface Callback {
    void resize(Entity entity);
  }
}
