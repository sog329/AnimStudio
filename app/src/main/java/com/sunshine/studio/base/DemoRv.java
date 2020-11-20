package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sunshine.engine.base.Tool;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songxiaoguang on 2017/12/9.
 */
public abstract class DemoRv extends RecyclerView {

  private Map<String, Bitmap> map = new HashMap<>();

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
    setAdapter(new Adapter());
    loadData();
  }

  public Data addData() {
    Data data = new Data();
    Adapter adapter = (Adapter) getAdapter();
    adapter.lstData.add(data);
    return data;
  }

  public Bitmap getBmp(String s) {
    Bitmap bmp = map.get(s);
    if (bmp == null) {
      bmp = Tool.getBmpByAssets(getContext(), s);
      map.put(s, bmp);
    }
    return bmp;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    for (Bitmap bmp : map.values()) {
      if (bmp != null) {
        bmp.recycle();
      }
    }
    map.clear();
  }

  protected abstract void loadData();

  private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private List<Data> lstData = new ArrayList<>();

    public Adapter() {
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new Holder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
      Data data = lstData.get(position);
      // num
      holder.num.setText(String.valueOf(position + 1));
      // name
      String name = data.bone == null ? data.particle : data.bone;
      holder.tv.setText(name);
      // stage
      holder.stageView.stop();
      if (data.bone != null) {
        holder.stageView.play("bone" + File.separator + data.bone);
        holder.stageView.recycleBmp(false);
        holder.stageView.isRepeat(true);
      }
      // scene
      holder.sceneView.stop();
      if (data.particle != null) {
        holder.sceneView.play("particle" + File.separator + data.particle);
        holder.sceneView.recycleBmp(false);
        if (data.bone == null) {
          holder.sceneView.isRepeat(true);
        }
      }
      // bind
      holder.sceneView.setBackgroundDrawable(data.bg);
      data.onBind(holder.stageView, holder.sceneView);
    }

    @Override
    public int getItemCount() {
      return lstData.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

      StageView stageView = null;
      SceneView sceneView = null;
      StudioTv tv = null;
      TextView num = null;

      public Holder(View itemView) {
        super(itemView);
        stageView = itemView.findViewById(R.id.stage);
        sceneView = itemView.findViewById(R.id.scene);
        tv = itemView.findViewById(R.id.tv);
        num = itemView.findViewById(R.id.num);
        itemView.setOnClickListener(v -> {
        });
      }
    }
  }

  public static class Data {

    private String bone = null;
    private String particle = null;
    private Bind bind = (a, b) -> {
    };
    private Drawable bg = null;

    public Data setBone(String s) {
      bone = s;
      return this;
    }

    public Data setParticle(String s) {
      particle = s;
      return this;
    }

    public Data setBind(Bind b) {
      bind = b;
      return this;
    }

    private void onBind(StageView stageView, SceneView sceneView) {
      bind.onBind(stageView, sceneView);
    }

    public Data setBg(Drawable d) {
      bg = d;
      return this;
    }

    public interface Bind {

      void onBind(StageView stageView, SceneView sceneView);
    }
  }
}
