package com.sunshine.studio.base;

import com.sunshine.engine.base.Tool;
import com.sunshine.engine.base.XmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/** Created by Jack on 2020/10/5. */
public class ConfigParser extends XmlParser {
  private List<Packer.Cell> lst = null;
  private String id = null;

  public ConfigParser(List<Packer.Cell> lst) {
    this.lst = lst;
  }

  public void parse(String path) {
    FileInputStream is = null;
    try {
      File f = new File(path);
      is = new FileInputStream(f);
      SAXParserFactory sf = SAXParserFactory.newInstance();
      SAXParser sp = sf.newSAXParser();
      sp.parse(is, this);
    } catch (Exception e) {
      Tool.log(e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          Tool.log(e);
        }
      }
    }
  }

  @Override
  public void parseTag(String tag, String[] ary, boolean start) {
    if (!start) {
      switch (tag) {
        case SRC_LTWH:
          int left = Integer.parseInt(ary[0]);
          int top = Integer.parseInt(ary[1]);
          int right = Integer.parseInt(ary[2]) + left;
          int bottom = Integer.parseInt(ary[3]) + top;
          Packer.Cell rc = new Packer.Cell(left, top, right, bottom);
          rc.name = id;
          id = null;
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
          break;
        case NAME:
          id = ary[0];
          break;
        case EXTEND_Y:
          int y = Integer.parseInt(ary[0]);
          lst.get(lst.size() - 1).setExtendY(y);
          break;
      }
    }
  }
}
