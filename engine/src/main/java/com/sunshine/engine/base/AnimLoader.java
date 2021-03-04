package com.sunshine.engine.base;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AnimLoader {
  private static List<Entity> queue = new ArrayList<>();
  private static boolean isRun = false;

  public static synchronized void load(Entity entity) {
    if (entity != null) {
      queue.add(entity);
      if (!isRun) {
        isRun = true;
        new Thread(new ParseRunnable()).start();
      }
    }
  }

  public static synchronized void stop(Entity entity) {
    queue.remove(entity);
  }

  private static synchronized Entity getEntity() {
    if (queue.size() > 0) {
      return queue.remove(0);
    } else {
      isRun = false;
      return null;
    }
  }

  private static void parse(InputStream is, Entity entity) throws Exception {
    SAXParserFactory sf = SAXParserFactory.newInstance();
    SAXParser sp = sf.newSAXParser();
    sp.parse(is, entity.getParser());
  }

  private static class ParseRunnable implements Runnable {
    @Override
    public void run() {
      android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
      Entity entity = getEntity();
      while (entity != null) {
        InputStream is = null;
        boolean success = false;
        try {
          boolean inAsset = !new File(entity.configPath).exists();
          if (inAsset) {
            Context context = entity.helper.getContext();
            is = context.getResources().getAssets().open(entity.configPath);
          } else {
            File f = new File(entity.configPath);
            is = new FileInputStream(f);
          }
          parse(is, entity);
          entity.parsed = true;
          LayoutHelper.resize(entity);
          Bitmap bmp = null;
          if (inAsset) {
            bmp = Tool.getBmpByAssets(entity.helper.getContext(), entity.picPath);
          } else {
            bmp = BitmapFactory.decodeFile(entity.picPath);
          }
          MediaPlayer sound = null;
          if (entity.soundPath != null) {
            if (inAsset) {
              AssetFileDescriptor fd =
                  Tool.getAssetFileDescriptor(entity.helper.getContext(), entity.soundPath);
              if (fd != null) {
                sound = new MediaPlayer();
                sound.reset();
                sound.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
              }
            } else {
              sound = new MediaPlayer();
              sound.reset();
              sound.setDataSource(entity.soundPath);
            }
          }
          if (bmp != null) {
            entity
                .helper
                .addLog("in parse")
                .addLog("   entity.hashCode()=" + entity.hashCode())
                .addLog("   bmp.hashCode()=" + bmp.hashCode())
                .addLog("   entity.configPath=" + entity.configPath)
                .addLog("   entity.picPath=" + entity.picPath)
                .addLog("   entity.inAsset=" + inAsset);
            if (bmp.isRecycled()) {
              entity.helper.addLog("   bmp.isRecycled() in parse").onError();
            } else {
              if (sound != null) {
                sound.prepare();
              }
              entity.setSrcAsync(bmp, sound);
              success = true;
            }
          }
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
          if (!success) {
            entity.helper.stopAsync(entity);
          }
        }
        entity = getEntity();
      }
    }
  }
}
