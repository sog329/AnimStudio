package com.sunshine.studio.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.engine.base.InterpolatorType;
import com.sunshine.engine.base.Tool;
import com.sunshine.studio.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Created by Jack on 12/5/20. */
public class InterpolatorRv extends RecyclerView {
  public InterpolatorRv(@NonNull Context context) {
    super(context);
    init();
  }

  public InterpolatorRv(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public InterpolatorRv(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public String select() {
    Adapter a = (Adapter) getAdapter();
    return a.select.now;
  }

  private void init() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    setLayoutManager(linearLayoutManager);
    setAdapter(new Adapter());
  }

  public void load(String select, Runnable onSelect, int selectBg) {
    String[] ary = Tool.aryKey(InterpolatorType.values());
    Adapter a = (Adapter) getAdapter();
    a.load(Arrays.asList(ary), select, onSelect, selectBg);
    scrollToPosition(a.lstData.indexOf(select));
  }

  private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private List<String> lstData = new ArrayList<>();
    private Select select = new Select();
    private int selectBg = 0;
    private Runnable onSelect = null;

    public void load(List<String> lst, String select, Runnable onSelect, int selectBg) {
      lstData.clear();
      lstData.addAll(lst);
      this.select.old = null;
      this.selectBg = selectBg;
      this.onSelect = onSelect;
      select(select);
    }

    private void select(String s) {
      select.now = s;
      if (select.old == null) {
        select.old = s;
      }
      onSelect.run();
      notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
      return new Adapter.Holder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
      String str = lstData.get(i);
      holder.tv.setText(str);
      holder.tv.setInterpolator(str);
      holder.tv.setOnClickListener(v -> select(str));
      holder.tv.setTextColor(holder.itemView.getResources().getColor(R.color.tv_color2));
      if (str.equals(select.now)) {
        holder.tv.setBackgroundResource(selectBg);
        holder.tv.setAlpha(.7f);
      } else if (str.equals(select.old)) {
        holder.tv.setBackgroundResource(R.drawable.bg_btn_menu);
        holder.tv.setAlpha(.5f);
      } else {
        holder.tv.setBackgroundResource(0);
        holder.tv.setAlpha(.5f);
      }
    }

    @Override
    public int getItemCount() {
      return lstData.size();
    }

    private static class Holder extends RecyclerView.ViewHolder {
      private InterpolatorView tv = null;

      public Holder(@NonNull View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.tv);
      }
    }

    private static class Select {
      public String old = null;
      public String now = null;
    }
  }
}
