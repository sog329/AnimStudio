package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.sunshine.engine.base.ViewHelper;
import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BmpRect;
import com.sunshine.studio.bone.logic.SkinIv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.sunshine.studio.base.StudioTool.EXTERNAL;
import static com.sunshine.studio.base.StudioTool.PNG;

/** Created by songxiaoguang on 2017/12/2. */
public class PicGv extends GridView {

  public PicGv(Context context) {
    super(context);
  }

  public PicGv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public PicGv(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public static class PicAdapter extends BaseAdapter {

    private List<BmpRect> lstData = new ArrayList<>();
    public List<String> lstCheck = new ArrayList<>();
    private Callback callback = null;
    private String projectPath = null;
    private Map<String, Bitmap> mapBmp = new HashMap<>();

    public PicAdapter(Callback callback) {
      this.callback = callback;
    }

    public void loadData(String path) {
      projectPath = path;
      if (projectPath != null) {
        File[] ary = new File(projectPath).listFiles();
        lstData.clear();
        for (File f : ary) {
          String n = f.getName();
          if (n.endsWith(StudioTool.PNG)) {
            BmpRect r = new BmpRect();
            r.name = StudioTool.getFileName(n);
            lstData.add(r);
          }
        }
        List<BmpRect> lstTmp = new ArrayList<>();
        callback.buildLstBmpRc(lstTmp);
        lstCheck.clear();
        for (BmpRect r : lstTmp) {
          if (r.name != null && !lstCheck.contains(r.name)) {
            boolean in = false;
            for (BmpRect d : lstData) {
              if (r.name.equals(d.name)) {
                in = true;
                break;
              }
            }
            if (in) {
              lstCheck.add(r.name);
            }
          }
        }
        notifyDataSetChanged();
        new Thread(
                () -> {
                  Runnable post = () -> notifyDataSetChanged();
                  for (BmpRect r : lstData) {
                    String n = r.name;
                    if (path == projectPath) {
                      mapBmp.put(
                          n, BitmapFactory.decodeFile(projectPath + File.separator + n + PNG));
                      ViewHelper.handler.removeCallbacks(post);
                      ViewHelper.handler.postDelayed(post, 300);
                    }
                  }
                })
            .start();

      } else {
        callback.buildLstBmpRc(lstData);
        BmpRect externalRect = new BmpRect();
        externalRect.lstRect.add(new Rect(0, 0, 1, 1));
        externalRect.name = EXTERNAL;
        lstData.add(externalRect);
      }
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return lstData.size();
    }

    @Override
    public Object getItem(int position) {
      return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.gv_item_studio_pic, null);
      }
      SkinIv iv = convertView.findViewById(R.id.iv);
      iv.setBackgroundColor(callback.getColor());
      iv.autoSize = false;
      ViewGroup.LayoutParams lp = iv.getLayoutParams();
      PicGv gv = (PicGv) parent;
      lp.width = (StudioTool.getPicDlgWidth() - 10) / gv.getNumColumns() - 10;
      lp.height = lp.width;
      iv.setLayoutParams(lp);
      BmpRect bmpRect = lstData.get(position);
      StudioTv tv = convertView.findViewById(R.id.tv);
      StudioTv.initSize(tv, .5f);
      tv.setText(bmpRect.name);
      StudioCb cb = convertView.findViewById(R.id.cb);
      if (projectPath != null) {
        cb.setVisibility(VISIBLE);
        iv.setBmp(mapBmp.get(bmpRect.name), null);
        iv.setOnClickListener(null);
        cb.mapValue(
            lstCheck.contains(bmpRect.name),
            b -> {
              lstCheck.remove(bmpRect.name);
              if (b) {
                lstCheck.add(bmpRect.name);
                cb.setBackgroundColor(Color.argb(150, 0, 0, 0));
              } else {
                cb.setBackgroundColor(0);
              }
            });
      } else {
        Pic pic = (Pic) callback;
        cb.setVisibility(GONE);
        boolean isExternal = position == (getCount() - 1);
        iv.setBmp(pic.getBmp(isExternal), bmpRect.lstRect);
        iv.setOnClickListener(v -> pic.onClick(bmpRect, isExternal));
      }

      return convertView;
    }

    public interface Callback {

      void buildLstBmpRc(List<BmpRect> lst);

      int getColor();
    }

    public interface Pic extends Callback {
      void onClick(BmpRect bmpRect, boolean isExternal);

      Bitmap getBmp(boolean isExternal);
    }

    public interface Packer extends Callback {}
  }
}
