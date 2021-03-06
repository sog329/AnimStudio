package com.sunshine.studio.base;

import android.graphics.Rect;

import com.sunshine.engine.base.Tool;
import com.sunshine.engine.base.XmlParser;
import com.sunshine.studio.bone.logic.BmpRect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/** Created by songxiaoguang on 2017/12/3. */
public class PlistParser extends XmlParser {
  private List<BmpRect> lst = null;
  private int depth = 0;
  private String name = null;
  private Type type = Type.none;

  public void parse(String path, List<BmpRect> lst) {
    this.lst = lst;
    FileInputStream is = null;
    try {
      File f = new File(path);
      is = new FileInputStream(f);
      SAXParserFactory sf = SAXParserFactory.newInstance();
      SAXParser sp = sf.newSAXParser();
      sp.parse(is, PlistParser.this);
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
    if (start) {
      if ("dict".equals(tag)) {
        depth++;
        if (depth == 3) {
          boolean inSameFrame = false;
          if (name != null) {
            for (BmpRect rc : lst) {
              if (BmpRect.inSameFrame(rc.name, name)) {
                inSameFrame = true;
                break;
              }
            }
          }
          if (!inSameFrame) {
            BmpRect bmpRect = new BmpRect();
            bmpRect.name = name;
            lst.add(bmpRect);
          }
        }
      }
    } else {
      switch (tag) {
        case "dict":
          depth--;
          type = Type.none;
          break;
        case "key":
          if (depth == 2) {
            name = ary[0];
          }
          if (depth == 3) {
            if ("x".equals(ary[0])) {
              type = Type.x;
            } else if ("y".equals(ary[0])) {
              type = Type.y;
            } else if ("w".equals(ary[0]) || "width".equals(ary[0])) {
              type = Type.w;
            } else if ("h".equals(ary[0]) || "height".equals(ary[0])) {
              type = Type.h;
            } else if ("extendY".equals(ary[0])) {
              type = Type.extendY;
            } else {
              type = Type.none;
            }
          }
          break;
        case "integer":
        case "real":
          if (depth == 3) {
            BmpRect bmpRect = lst.get(lst.size() - 1);
            Rect rc = bmpRect.getRect(BmpRect.getFrameIndex(name));
            switch (type) {
              case x:
                int left = Integer.parseInt(ary[0]);
                rc.right = left + rc.width();
                rc.left = left;
                break;
              case y:
                int top = Integer.parseInt(ary[0]);
                rc.bottom = top + rc.height();
                rc.top = top;
                break;
              case w:
                rc.right = rc.left + Integer.parseInt(ary[0]);
                break;
              case h:
                rc.bottom = rc.top + Integer.parseInt(ary[0]);
                break;
              case extendY:
                bmpRect.extendY = Integer.parseInt(ary[0]);
                break;
              default:
                break;
            }
          }
          break;
        default:
          break;
      }
    }
  }

  private enum Type {
    none,
    x,
    y,
    w,
    h,
    extendY
  }
}
