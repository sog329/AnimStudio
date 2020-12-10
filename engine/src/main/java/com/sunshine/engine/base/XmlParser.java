package com.sunshine.engine.base;

import android.graphics.Rect;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class XmlParser<T extends Entity> extends DefaultHandler {
  protected static final String NONE = "";
  public static final String WIDTH_HEIGHT = "width_height";
  public static final String LAYOUT_TYPE = "layout_type";
  public static final String DURATION = "duration";
  public static final String NAME = "name";
  public static final String SRC_LTWH = "src_ltwh";
  public static final String EXTEND_Y = "extend_y";
  public static final String SRC_ID_WH = "src_id_wh";
  public static final String SRC = "src_id_ltwh";

  private StringBuilder sb = null;
  protected T entity = null;

  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
    sb = new StringBuilder();
    sb.setLength(0);
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    super.characters(ch, start, length);
    sb.append(ch, start, length);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atr)
      throws SAXException {
    super.startElement(uri, localName, qName, atr);
    String tag = localName.length() != 0 ? localName : qName;
    parseTag(tag, sb.toString().split(","), true);
    sb.setLength(0);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);
    String tag = localName.length() != 0 ? localName : qName;
    String[] ary = sb.toString().split(",");
    if (SRC.equals(tag)) {
      Rect r = new Rect();
      r.left = Integer.valueOf(ary[1]);
      r.top = Integer.valueOf(ary[2]);
      r.right = r.left + Integer.valueOf(ary[3]);
      r.bottom = r.top + Integer.valueOf(ary[4]);
      entity.mapRc.put(ary[0], r);
    } else {
      parseTag(tag, ary, false);
    }
  }

  public abstract void parseTag(String tag, String[] ary, boolean start);
}
