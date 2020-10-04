package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BmpRect;
import com.sunshine.studio.bone.logic.BoneIv;

import java.util.ArrayList;
import java.util.List;

import static com.sunshine.studio.base.StudioTool.EXTERNAL;

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
    private Callback callback = null;

    public PicAdapter(Callback callback) {
      this.callback = callback;
    }

    public void loadData() {
      callback.buildLstBmpRc(lstData);
      BmpRect externalRect = new BmpRect();
      externalRect.lstRect.add(new Rect(0, 0, 1, 1));
      externalRect.name = EXTERNAL;
      lstData.add(externalRect);
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
      BoneIv iv = convertView.findViewById(R.id.iv);
      iv.setBackgroundColor(callback.getColor());
      iv.autoSize = false;
      ViewGroup.LayoutParams lp = iv.getLayoutParams();
      lp.width = (StudioTool.getDlgWidth() * 2 - 10) / 5 - 10;
      lp.height = lp.width;
      iv.setLayoutParams(lp);

      boolean isExternal = position == (getCount() - 1);
      BmpRect bmpRect = lstData.get(position);
      iv.setBmp(callback.getBmp(isExternal), bmpRect.lstRect.get(0));
      iv.setOnClickListener(v -> callback.onClick(bmpRect, isExternal));

      StudioTv tv = convertView.findViewById(R.id.tv);
      StudioTv.initSize(tv, .5f);
      tv.setText(bmpRect.name);

      return convertView;
    }

    public interface Callback {
      void onClick(BmpRect bmpRect, boolean isExternal);

      void buildLstBmpRc(List<BmpRect> lst);

      Bitmap getBmp(boolean isExternal);

      int getColor();
    }
  }
}
