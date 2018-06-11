package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sunshine.studio.R;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.XmlWriter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import static com.sunshine.studio.base.StudioTool.getFilePath;

/** Created by songxiaoguang on 2017/12/2. */
public class ProjectLv extends ListView {

  public ProjectLv(Context context) {
    super(context);
  }

  public ProjectLv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ProjectLv(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public static class FileAdapter extends BaseAdapter {

    private List<String> lstData = new ArrayList<>();
    private Callback callback = null;

    public FileAdapter(Callback callback) {
      this.callback = callback;
    }

    public void loadData() {
      lstData.clear();
      File[] ary = new File(getFilePath(callback.getFolderName())).listFiles();
      FilenameFilter filterConfig = (file, name) -> "config.xml".equals(name);
      FilenameFilter filterPic = (file, name) -> "pic".equals(name);
      FilenameFilter filterPlist = (file, name) -> "pic.plist".equals(name);
      for (File file : ary) {
        if (file.isDirectory()) {
          if (file.listFiles(filterPic).length == 1 && file.listFiles(filterPlist).length == 1) {
            String name = file.getName();
            if (file.listFiles(filterConfig).length == 0) {
              XmlWriter.save(
                  getFilePath(callback.getFolderName(), name, "config.xml"), callback.getWriter());
            }
            lstData.add(name);
          }
        }
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
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bone_studio_project_lv, null);
      }

      TextView tv = convertView.findViewById(R.id.tv);
      final String name = lstData.get(position);
      tv.setText(name);
      tv.setOnClickListener(v -> callback.onClick(name));

      View del = convertView.findViewById(R.id.del);
      del.setOnClickListener(
          v -> {
            StudioTool.deleteFile(new File(StudioTool.getFilePath("bone", name)));
            loadData();
          });

      return convertView;
    }

    public interface Callback {
      void onClick(String name);

      String getFolderName();

      XmlWriter.Callback getWriter();
    }
  }
}
