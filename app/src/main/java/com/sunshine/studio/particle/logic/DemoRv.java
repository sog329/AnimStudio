package com.sunshine.studio.particle.logic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.R;
import com.sunshine.studio.base.StudioTv;

import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/9. */
public class DemoRv extends RecyclerView {
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

    setAdapter(
        new Adapter()
            .add("heart")
            .add("welcome_omi_bg")
            .add("match_bg")
            .add("cry")
            .add("hi")
            .add("no")
            .add("smile")
            .add("what")
            .add("yeah")
            .add("singleDog")
            .add("snow")
            .add("christmas")
            .add("bell"));
  }

  private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private List<String> lstData = new ArrayList<>();

    public Adapter add(String name) {
      lstData.add(name);
      return this;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new Holder(
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_particle_demo_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
      String name = lstData.get(position);

      holder.tv.setText(name);

      holder.sceneView.stop();
      holder.sceneView.play("particle/" + name);
      holder.sceneView.autoStop(false);
      holder.sceneView.isRepeat(true);
    }

    @Override
    public int getItemCount() {
      return lstData.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
      SceneView sceneView = null;
      StudioTv tv = null;

      public Holder(View itemView) {
        super(itemView);
        sceneView = itemView.findViewById(R.id.anim);
        tv = itemView.findViewById(R.id.tv);
      }
    }
  }
}
