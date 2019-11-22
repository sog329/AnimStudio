package com.sunshine.studio.base;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sunshine.engine.base.InterpolatorType;
import com.sunshine.engine.base.LayoutType;
import com.sunshine.engine.base.Tool;
import com.sunshine.studio.R;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/5. */
public class StudioSpinner extends AppCompatSpinner {
  private SpAdapter adapter = null;

  public StudioSpinner(Context context) {
    super(context);
  }

  public StudioSpinner(Context context, int mode) {
    super(context, mode);
  }

  public StudioSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
  }

  public void layoutType(String now, Callback callback) {
    if (adapter == null) {
      adapter = new SpAdapter(false);
      setAdapter(adapter);
    }
    loadData(now, callback, Tool.aryKey(LayoutType.values()));
  }

  public void interpolator(String now, Callback callback) {
    if (adapter == null) {
      adapter = new SpAdapter(true);
      setAdapter(adapter);
    }
    loadData(now, callback, Tool.aryKey(InterpolatorType.values()));
  }

  private void loadData(String now, Callback callback, String[] ary) {
    adapter.lstData.clear();
    for (String str : ary) {
      adapter.lstData.add(str);
    }
    setOnItemSelectedListener(null);
    adapter.notifyDataSetChanged();
    setSelection(adapter.lstData.indexOf(now));
    setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            callback.onChoice(adapter.lstData.get(i));
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {}
        });
  }

  private static class SpAdapter extends BaseAdapter {
    private List<String> lstData = new ArrayList<>();
    private boolean isInterpolator = false;

    public SpAdapter(boolean is) {
      isInterpolator = is;
    }

    @Override
    public int getCount() {
      return lstData.size();
    }

    @Override
    public Object getItem(int i) {
      return lstData.get(i);
    }

    @Override
    public long getItemId(int i) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner, null);
      }
      String name = lstData.get(position);
      InterpolatorView tv = convertView.findViewById(R.id.tv);
      tv.setText(name);
      if (isInterpolator) {
        tv.setInterpolator(name);
        tv.setTextColor(convertView.getResources().getColor(R.color.tv_color2));
        StudioTv.initSize(tv, .7f);
      } else {
        tv.setInterpolator(null);
      }
      return convertView;
    }
  }

  public interface Callback {
    void onChoice(String str);
  }
}
