package com.sunshine.studio.base;

import com.sunshine.engine.base.Tool;
import com.sunshine.engine.base.XmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/** Created by Jack on 2020/10/5. */
public class ConfigParser extends XmlParser {
  private List<Packer.Cell> lst = null;

  public ConfigParser(List<Packer.Cell> lst) {
    this.lst = lst;
  }

  public void parse(String path) {
    try {
      File f = new File(path);
      FileInputStream is = new FileInputStream(f);
      SAXParserFactory sf = SAXParserFactory.newInstance();
      SAXParser sp = sf.newSAXParser();
      sp.parse(is, this);
    } catch (Exception e) {
      Tool.log(e);
    }
  }

  @Override
  public void parseTag(String tag, String[] ary, boolean start) {
    if (!start && SRC_LTWH.equals(tag)) {
      int left = Integer.parseInt(ary[0]);
      int top = Integer.parseInt(ary[1]);
      int right = Integer.parseInt(ary[2]) + left;
      int bottom = Integer.parseInt(ary[3]) + top;
      Packer.Cell rc = new Packer.Cell(left, top, right, bottom);
      rc.setXY(left, top);
      boolean repeat = false;
      for (Packer.Cell c : lst) {
        if (c.getRcPic().equals(rc.getRcPic())) {
          repeat = true;
          break;
        }
      }
      if (!repeat) {
        lst.add(rc);
      }
    }
  }
}
