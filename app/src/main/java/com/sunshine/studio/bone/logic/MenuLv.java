package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sunshine.studio.R;

import java.util.ArrayList;
import java.util.List;

/** Created by Jack on 12/3/20. */
public class MenuLv extends ListView {
  public MenuLv(Context context) {
    super(context);
  }

  public MenuLv(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MenuLv(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public static class Menu {
    private String name;

    private OnClickListener click;

    public Menu(String s, OnClickListener c) {
      name = s;
      click = c;
    }
  }

  public static class MenuAdapter extends BaseAdapter {
    private List<Menu> lstData = new ArrayList<>();

    public void loadData(List<Menu> lst) {
      lstData.clear();
      lstData.addAll(lst);
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return lstData.size();
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studio_menu_lv, null);
      }
      Menu m = lstData.get(position);
      TextView tv = convertView.findViewById(R.id.tv);
      tv.setText(m.name);
      tv.setOnClickListener(m.click);
      return convertView;
    }
  }
}
