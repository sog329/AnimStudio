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

import com.sunshine.engine.base.ViewHelper;
import com.sunshine.studio.R;
import com.sunshine.studio.base.ConfigParser;
import com.sunshine.studio.base.Packer;
import com.sunshine.studio.base.StudioImageBtn;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.XmlWriter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import static com.sunshine.studio.base.StudioTool.PNG;
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
    private int sequence = 0;

    public FileAdapter(Callback callback) {
      this.callback = callback;
    }

    public void loadData() {
      lstData.clear();
      notifyDataSetChanged();
      callback.onLoading(true);
      int s = ++sequence;
      new Thread(
              () -> {
                List<String> lstResult = new ArrayList<>();
                File[] ary = new File(getFilePath(callback.getFolderName())).listFiles();
                // 文件夹按修改时间降序
                Arrays.sort(
                    ary,
                    new Comparator<File>() {
                      public int compare(File f1, File f2) {
                        long diff = f1.lastModified() - f2.lastModified();
                        if (diff > 0) return -1;
                        else if (diff == 0) return 0;
                        else return 1;
                      }

                      public boolean equals(Object obj) {
                        return true;
                      }
                    });
                FilenameFilter filterConfig = (file, name) -> "config.xml".equals(name);
                FilenameFilter filterPic = (file, name) -> "pic".equals(name);
                FilenameFilter filterPlist = (file, name) -> "pic.plist".equals(name);
                for (File file : ary) {
                  if (file.isDirectory()) {
                    // use pics to build pic & plist
                    if (file.listFiles(filterPic).length != 1
                        || file.listFiles(filterPlist).length != 1) {
                      Packer.buildPic(
                          file.getPath(),
                          (dir, name) -> {
                            name = name.toLowerCase();
                            return name.endsWith(PNG);
                          });
                    }
                    // use config to build plist
                    if (file.listFiles(filterConfig).length == 1
                        && file.listFiles(filterPlist).length == 0) {
                      List<Packer.Cell> lst = new ArrayList<>();
                      new ConfigParser(lst).parse(file.getPath() + File.separator + "config.xml");
                      Packer.savePlist(lst, file.getPath());
                    }
                    // ensure pic & plist exist
                    if (file.listFiles(filterPic).length == 1
                        && file.listFiles(filterPlist).length == 1) {
                      String name = file.getName();
                      if (file.listFiles(filterConfig).length == 0) {
                        XmlWriter.save(
                            getFilePath(callback.getFolderName(), name, "config.xml"),
                            callback.getWriter(name));
                      }
                      lstResult.add(name);
                    }
                  }
                }
                ViewHelper.handler.post(() -> render(s, lstResult));
              })
          .start();
    }

    private void render(int sequence, List<String> lst) {
      if (this.sequence == sequence) {
        callback.onLoading(false);
        lstData.addAll(lst);
        notifyDataSetChanged();
      }
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
      tv.setBackgroundColor(callback.getColor());
      if (name.equals(callback.getProjectName())) {
        convertView.setAlpha(1f);
      } else {
        convertView.setAlpha(.6f);
      }

      StudioImageBtn del = convertView.findViewById(R.id.del);
      del.setBackgroundColor(callback.getColor());
      del.setOnClickListener(
          v -> {
            callback.onDel(name);
            StudioTool.deleteFile(new File(StudioTool.getFilePath(callback.getFolderName(), name)));
            StudioTool.showToast(parent.getContext(), name + " deleted");
            loadData();
          });
      tv.getLayoutParams().height = del.getSize();

      return convertView;
    }

    public interface Callback {
      void onClick(String name);

      void onDel(String name);

      String getFolderName();

      String getProjectName();

      XmlWriter.Callback getWriter(String name);

      int getColor();

      void onLoading(boolean b);
    }
  }
}
