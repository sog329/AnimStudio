package com.sunshine.studio.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.sunshine.engine.base.Render2D;
import com.sunshine.engine.base.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import static com.sunshine.studio.base.StudioTool.PNG;

/** Created by Jack on 2019-11-11. */
public class Packer {
  public static int getArea(List<Cell> lstRc, Cell rcNew) {
    int[] ary = getWH(lstRc, rcNew);
    return ary[0] * ary[1];
  }

  public static int[] getWH(List<Cell> lstRc) {
    return getWH(lstRc, null);
  }

  public static int[] getWH(List<Cell> lstRc, Cell rcNew) {
    int w = rcNew == null ? 0 : rcNew.right();
    int h = rcNew == null ? 0 : rcNew.bottom();
    for (Cell rc : lstRc) {
      w = Math.max(w, rc.right());
      h = Math.max(h, rc.bottom());
    }
    return new int[] {w, h};
  }

  public static void buildPic(String folderPath, FilenameFilter filter) {
    List<Packer.Cell> lst = Packer.getLstBmpRc(folderPath, filter);
    if (lst.size() > 0) {
      lst = Packer.packer(lst);
      Packer.saveFiles(lst, folderPath);
    }
  }

  private static List<Cell> getLstBmpRc(String folderPath, FilenameFilter filter) {
    List<Cell> lst = new ArrayList<>();
    String[] aryFiles = new File(folderPath).list(filter);
    if (aryFiles != null && aryFiles.length > 0) {
      Arrays.sort(aryFiles, String.CASE_INSENSITIVE_ORDER);
      for (String name : aryFiles) {
        lst.add(new Cell(name, folderPath + File.separator + name, aryFiles.length == 1));
      }
    }
    return lst;
  }

  private static void saveFiles(List<Cell> lstRc, String folderPath) {
    // save cell
    List<Cell> lst = new ArrayList<>();
    for (Cell c : lstRc) {
      if (c.rcPic.width() < c.bmp.getWidth() || c.rcPic.height() < c.bmp.getHeight()) {
        lst.add(c);
        savePic(lst, folderPath + File.separator + c.name + PNG, false);
        lst.clear();
      }
    }
    // build pic
    savePic(lstRc, folderPath + File.separator + "pic", true);
    // build plist
    savePlist(lstRc, folderPath);
  }

  private static void savePic(List<Cell> lstRc, String picPath, boolean isFinal) {
    Bitmap bmp = null;
    if (isFinal) {
      int[] ary = Packer.getWH(lstRc);
      bmp = Bitmap.createBitmap(ary[0], ary[1], Bitmap.Config.ARGB_8888);
    } else {
      Rect r = lstRc.get(0).getRcPic();
      bmp = Bitmap.createBitmap(r.width(), r.height(), Bitmap.Config.ARGB_8888);
    }
    Canvas can = new Canvas(bmp);
    Render2D.setDrawFilter(can);
    for (Cell rc : lstRc) {
      Rect r = rc.getRcPic();
      can.drawBitmap(
          rc.bmp, r, isFinal ? rc.getDst() : new Rect(0, 0, r.width(), r.height()), Render2D.PAINT);
      if (isFinal) {
        rc.bmp.recycle();
      }
    }
    // save pic
    BufferedOutputStream bos = null;
    File file = new File(picPath);
    if (file.exists()) {
      file.delete();
    }
    try {
      bos = new BufferedOutputStream(new FileOutputStream(file));
      bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
      bos.flush();
    } catch (Exception e) {
      Tool.log(e.toString());
      return;
    } finally {
      try {
        if (bos != null) {
          bos.close();
        }
      } catch (IOException e) {
        Tool.log(e.toString());
      }
      bmp.recycle();
    }
    tinyPic(picPath);
  }

  public static void savePlist(List<Cell> lstRc, String folderPath) {
    String plistPath = folderPath + File.separator + "pic.plist";
    File file = new File(plistPath);
    if (file.exists()) {
      file.delete();
    }
    XmlWriter.save(plistPath, new PlistWriter(lstRc));
  }

  public static void tinyPic(String picPath) {
    Request request =
        new Request.Builder()
            .url("https://tinypng.com/web/shrink")
            .post(RequestBody.create(MediaType.parse("image/png"), new File(picPath)))
            .build();
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call = okHttpClient.newCall(request);
    call.enqueue(
        new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            Tool.log(e.getLocalizedMessage());
          }

          @Override
          public void onResponse(Call call, Response response) {
            if (response.isSuccessful()) {
              try {
                JSONObject body = new JSONObject(response.body().string());
                String picUrl = body.getJSONObject("output").getString("url");
                downloadPic(picUrl, picPath);
              } catch (JSONException e) {
                Tool.log(e.toString());
              } catch (IOException e) {
                Tool.log(e.toString());
              }
            } else {
              Tool.log(response.message());
            }
          }
        });
  }

  public static void downloadPic(String picUrl, String picPath) {
    Request request = new Request.Builder().url(picUrl).build();
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call = okHttpClient.newCall(request);
    call.enqueue(
        new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            Tool.log(e.getLocalizedMessage());
          }

          @Override
          public void onResponse(Call call, Response response) {
            if (response.isSuccessful()) {
              InputStream in = response.body().byteStream();
              FileOutputStream out = null;
              try {
                File file = new File(picPath);
                if (file.exists()) {
                  file.delete();
                }
                if (!file.exists()) {
                  out = new FileOutputStream(file);
                  byte[] buffer = new byte[1024];
                  int length;
                  while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                  }
                  out.flush();
                }
              } catch (IOException e) {
                Tool.log(e.toString());
              } finally {
                if (in != null) {
                  try {
                    in.close();
                  } catch (IOException e) {
                    Tool.log(e.toString());
                  }
                }
                if (out != null) {
                  try {
                    out.close();
                  } catch (IOException e) {
                    Tool.log(e.toString());
                  }
                }
              }
            } else {
              Tool.log(response.message());
            }
          }
        });
  }

  private static List<Cell> packer(List<Cell> lstRc) {
    // 找出最大边 & 排序
    for (Cell rc : lstRc) {
      rc.max = Math.max(rc.width(), rc.height());
    }
    Collections.sort(
        lstRc,
        (a, b) -> {
          if (a.max == b.max) {
            return b.width() * b.height() - a.width() * a.height();
          } else {
            return b.max - a.max;
          }
        });
    // 安置Rc
    List<Cell> lstResult = new ArrayList<>();
    for (Cell rc : lstRc) {
      if (lstResult.size() == 0) {
        lstResult.add(rc);
      } else {
        build(lstResult, rc);
      }
    }
    return lstResult;
  }

  private static void build(List<Cell> lstRc, Cell rcBmp) {
    // 收集可以作为新增矩形的坐标，即矩形左上角的顶点
    List<Anchor> lstPt = new ArrayList<>();
    for (Cell rc : lstRc) {
      // -lt
      lstPt.remove(new Anchor(rc.left(), rc.top()));
      // -rb
      lstPt.remove(new Anchor(rc.right(), rc.bottom()));

      int x, y;
      // +rt -> y
      x = rc.right();
      y = rc.top();
      int top = -1;
      for (Cell r : lstRc) {
        if (r.left() <= x && r.right() > x && r.bottom() <= y) {
          top = Math.max(top, r.bottom());
        }
      }
      if (top != -1) {
        lstPt.remove(new Anchor(x, y));
        y = Math.min(y, top);
      }
      lstPt.add(new Anchor(x, y));
      // +lb -> x
      x = rc.left();
      y = rc.bottom();
      int left = -1;
      for (Cell r : lstRc) {
        if (r.top() <= y && r.bottom() > y && x >= r.right()) {
          left = Math.max(left, r.right());
        }
      }
      if (left != -1) {
        lstPt.remove(new Anchor(x, y));
        x = Math.min(x, left);
      }
      lstPt.add(new Anchor(x, y));
    }
    // 计算这个点能放入的最大尺寸矩形 & 删除掉已被贴边的左边 & 如果是孔位则权值++
    Iterator<Anchor> iterator = lstPt.iterator();
    while (iterator.hasNext()) {
      Anchor pt = iterator.next();
      for (Cell rc : lstRc) {
        // pt在rc左边
        if (rc.top() <= pt.y && pt.y < rc.bottom()) {
          if (pt.x < rc.left()) {
            pt.maxW = Math.min(pt.maxW, rc.left() - pt.x);
            pt.weight++;
          } else if (pt.x == rc.left()) {
            iterator.remove();
            break;
          }
        }
        // pt在rc上边
        if (pt.x >= rc.left() && pt.x < rc.right()) {
          if (pt.y < rc.top()) {
            pt.maxH = Math.min(pt.maxH, rc.top() - pt.y);
            pt.weight++;
          } else if (pt.y == rc.top()) {
            iterator.remove();
            break;
          }
        }
      }
    }
    // 过滤出可以放入新rc的点 & 计算面积
    iterator = lstPt.iterator();
    while (iterator.hasNext()) {
      Anchor pt = iterator.next();
      if (pt.maxW < rcBmp.width() || pt.maxH < rcBmp.height()) {
        iterator.remove();
      } else {
        rcBmp.setXY(pt.x, pt.y);
        pt.area = getArea(lstRc, rcBmp);
      }
    }
    // 筛出来面积最小
    List<Anchor> lstMinArea = new ArrayList<>();
    for (Anchor pt : lstPt) {
      if (lstMinArea.size() == 0) {
        lstMinArea.add(pt);
      } else {
        if (pt.area == lstMinArea.get(0).area) {
          lstMinArea.add(pt);
        } else if (pt.area < lstMinArea.get(0).area) {
          lstMinArea.clear();
          lstMinArea.add(pt);
        }
      }
    }
    // 排序最优解，即面积最小 & 在宽高有限的区域填入新增矩形则认为最优
    Collections.sort(lstMinArea, (a, b) -> b.weight - a.weight);
    Anchor ptBest = lstMinArea.get(0);
    rcBmp.setXY(ptBest.x, ptBest.y);
    lstRc.add(rcBmp);
  }

  public static class Anchor {
    private int x = 0;
    private int y = 0;

    private int maxW = Integer.MAX_VALUE;
    private int maxH = Integer.MAX_VALUE;
    private int area = 0;
    private byte weight = 0;

    public Anchor(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Anchor) {
        Anchor pt = (Anchor) obj;
        return this.x == pt.x && this.y == pt.y;
      } else {
        return false;
      }
    }
  }

  public static class Cell {
    protected Bitmap bmp = null;
    protected String name = null;
    private Rect rcPic = new Rect(); // bmp内部非alpha部分的区域
    private Point pt = new Point();
    protected Integer extendY = null;

    private static final byte SPACE = 1;

    public Cell(String name, String path, boolean only) {
      this.name = StudioTool.getFileName(name);
      bmp = BitmapFactory.decodeFile(path);
      if (bmp == null) {
        Tool.log("bmp==null: path=" + path);
      }
      decodeBmp(only);
    }

    public Cell(int left, int top, int right, int bottom) {
      rcPic.set(left, top, right, bottom);
    }

    public void setXY(int x, int y) {
      pt.x = x;
      pt.y = y;
    }

    public void setExtendY(int y) {
      extendY = y;
    }

    private void decodeBmp(boolean only) {
      int x, y;
      boolean out = false;
      // 从上往下记录有颜色的起始点
      for (y = 0; y < bmp.getHeight(); y++) {
        for (x = 0; x < bmp.getWidth(); x++) {
          if (Color.alpha(bmp.getPixel(x, y)) > 0) {
            out = true;
            break;
          }
        }
        if (out) {
          out = false;
          break;
        }
      }
      rcPic.top = y;
      // 从下往上记录有颜色的起始点
      for (y = bmp.getHeight() - 1; y > rcPic.top; y--) {
        for (x = 0; x < bmp.getWidth(); x++) {
          if (Color.alpha(bmp.getPixel(x, y)) > 0) {
            out = true;
            break;
          }
        }
        if (out) {
          out = false;
          break;
        }
      }
      rcPic.bottom = y + (only ? 0 : 1);
      // 从左往右记录有颜色的起始点
      for (x = 0; x < bmp.getWidth(); x++) {
        for (y = rcPic.top; y < rcPic.bottom; y++) {
          if (Color.alpha(bmp.getPixel(x, y)) > 0) {
            out = true;
            break;
          }
        }
        if (out) {
          out = false;
          break;
        }
      }
      rcPic.left = x;
      // 从右往左记录有颜色的起始点
      for (x = bmp.getWidth() - 1; x > rcPic.left; x--) {
        for (y = rcPic.top; y < rcPic.bottom; y++) {
          if (Color.alpha(bmp.getPixel(x, y)) > 0) {
            out = true;
            break;
          }
        }
        if (out) {
          out = false;
          break;
        }
      }
      rcPic.right = x + (only ? 0 : 1);
    }

    public Rect getDst() {
      return new Rect(pt.x, pt.y, pt.x + rcPic.width(), pt.y + rcPic.height());
    }

    public Rect getRcPic() {
      return rcPic;
    }

    private int max = 0;

    public int left() {
      return pt.x;
    }

    public int right() {
      return pt.x + rcPic.width() + SPACE;
    }

    public int top() {
      return pt.y;
    }

    public int bottom() {
      return pt.y + rcPic.height() + SPACE;
    }

    public int width() {
      return rcPic.width() + SPACE;
    }

    public int height() {
      return rcPic.height() + SPACE;
    }

    public int picWidth() {
      return rcPic.width();
    }

    public int picHeight() {
      return rcPic.height();
    }
  }
}
