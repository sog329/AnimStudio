package com.sunshine.studio.base;

import android.util.Xml;

import com.sunshine.engine.base.Tool;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/** Created by songxiaoguang on 2017/12/3. */
public class XmlWriter {
  public static void save(String path, Callback callback) {
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
    FileOutputStream out = null;
    try {
      file.createNewFile();
      out = new FileOutputStream(file);
      XmlSerializer xml = Xml.newSerializer();
      xml.setOutput(out, "utf-8");
      xml.startDocument(null, null);
      callback.write(xml);
      xml.endDocument();
      xml.flush();
    } catch (Exception e) {
      Tool.log(e);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception e) {
          Tool.log(e);
        }
      }
    }
  }

  public static void addTag(XmlSerializer xml, String tag, String value) throws Exception {
    xml.startTag(null, tag).text(value).endTag(null, tag);
  }

  public interface Callback {
    void write(XmlSerializer xml) throws Exception;
  }
}
