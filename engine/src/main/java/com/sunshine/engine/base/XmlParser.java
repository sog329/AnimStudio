package com.sunshine.engine.base;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class XmlParser extends DefaultHandler {
  protected static final String NONE = "";
  public static final String WIDTH_HEIGHT = "width_height";
  public static final String LAYOUT_TYPE = "layout_type";
  public static final String DURATION = "duration";
  public static final String SRC_LTWH = "src_ltwh";
  public static final String EXTEND_Y = "extend_y";

  private StringBuilder sb = null;

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
    parseTag(tag, sb.toString().split(","), false);
  }

  public abstract void parseTag(String tag, String[] ary, boolean start);
}
