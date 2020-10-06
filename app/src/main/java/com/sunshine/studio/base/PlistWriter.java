package com.sunshine.studio.base;

import org.xmlpull.v1.XmlSerializer;

import java.util.List;

import static com.sunshine.studio.base.XmlWriter.addTag;

/** Created by Jack on 2019-11-20. */
public class PlistWriter implements XmlWriter.Callback {
  private List<Packer.Cell> lst = null;

  public PlistWriter(List<Packer.Cell> lst) {
    this.lst = lst;
  }

  @Override
  public void write(XmlSerializer xml) throws Exception {
    final String plist = "plist";
    final String dict = "dict";
    final String key = "key";
    final String real = "real";
    final String x = "x";
    final String y = "y";
    final String w = "w";
    final String h = "h";
    xml.startTag(null, plist);
    xml.startTag(null, dict);
    xml.startTag(null, dict);
    for (Packer.Cell rc : lst) {
      if (rc.name != null) {
        addTag(xml, key, rc.name);
      }
      xml.startTag(null, dict);
      addTag(xml, key, x);
      addTag(xml, real, Integer.toString(rc.left()));
      addTag(xml, key, y);
      addTag(xml, real, Integer.toString(rc.top()));
      addTag(xml, key, w);
      addTag(xml, real, Integer.toString(rc.picWidth()));
      addTag(xml, key, h);
      addTag(xml, real, Integer.toString(rc.picHeight()));
      if (rc.extendY != null) {
        addTag(xml, key, "extendY");
        addTag(xml, real, Integer.toString(rc.extendY));
      }
      xml.endTag(null, dict);
    }
    xml.endTag(null, dict);
    xml.endTag(null, dict);
    xml.endTag(null, plist);
    lst = null;
  }
}
