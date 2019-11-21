package com.sunshine.studio.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.engine.base.AnimView;
import com.sunshine.studio.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/9. */
public abstract class DemoRv<T extends AnimView> extends RecyclerView {
  public DemoRv(Context context) {
    super(context);
    init();
  }

  public DemoRv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DemoRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public void init() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    setLayoutManager(linearLayoutManager);
    List<String> lst = new ArrayList<>();
    loadData(lst);
    setAdapter(new Adapter(lst));
  }

  protected abstract void loadData(List<String> lst);

  protected abstract int layoutId();

  protected abstract String getFolderName();

  private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private List<String> lstData = new ArrayList<>();

    public Adapter(List<String> lst) {
      lstData.addAll(lst);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new Holder(
          LayoutInflater.from(parent.getContext())
              .inflate(layoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
      String name = lstData.get(position);
      holder.tv.setText(name);
      holder.animView.stop();
      holder.animView.play(getFolderName() + File.separator + name);
      holder.animView.autoStop(false);
      holder.animView.isRepeat(true);
    }

    @Override
    public int getItemCount() {
      return lstData.size();
    }

    class Holder extends RecyclerView.ViewHolder {
      T animView = null;
      StudioTv tv = null;

      public Holder(View itemView) {
        super(itemView);
        animView = itemView.findViewById(R.id.anim);
        tv = itemView.findViewById(R.id.tv);
      }
    }
  }
}