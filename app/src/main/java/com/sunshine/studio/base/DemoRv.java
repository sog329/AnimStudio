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
import com.sunshine.engine.base.Function;
import com.sunshine.engine.base.Tool;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.R;
import com.sunshine.studio.base.DemoRv.Adapter.Holder;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by songxiaoguang on 2017/12/9. */
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

  public static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private List<Data> lstData = new ArrayList<>();

    public Adapter() {}

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
      // bind
      holder.bBg.setBackgroundDrawable(data.bg);

      Function<Boolean> func =
          click -> {
            // stage
            holder.bFront.stop();
            if (data.bone != null) {
              holder.bFront.play("bone" + File.separator + data.bone);
              holder.bFront.recycleBmp(false);
              if (!click) {
                holder.bFront.isRepeat(true);
              }
            }
            holder.bBack.stop();
            if (data.bone2 != null) {
              holder.bBack.play("bone" + File.separator + data.bone2);
              holder.bBack.recycleBmp(false);
              if (!click) {
                holder.bBack.isRepeat(true);
              }
            }
            holder.bBg.stop();
            if (data.bone3 != null) {
              holder.bBg.play("bone" + File.separator + data.bone3);
              holder.bBg.recycleBmp(false);
              if (!click) {
                holder.bBg.isRepeat(true);
              }
            }
            // scene
            holder.pBg.stop();
            if (data.particle != null) {
              holder.pBg.play("particle" + File.separator + data.particle);
              holder.pBg.recycleBmp(false);
              if (data.bone == null) {
                if (!click) {
                  holder.pBg.isRepeat(true);
                }
              }
            }
            if (data.bind != null) {
              data.bind.onBind(holder);
            }
          };
      func.call(false);
      holder.itemView.setOnClickListener(v -> func.call(true));
    }

    @Override
    public int getItemCount() {
      return lstData.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

      public StageView bFront = null;
      public StageView bBack = null;
      public StageView bBg = null;
      public SceneView pBg = null;
      StudioTv tv = null;
      TextView num = null;

      public Holder(View itemView) {
        super(itemView);
        bFront = itemView.findViewById(R.id.stage);
        bBack = itemView.findViewById(R.id.stage2);
        bBg = itemView.findViewById(R.id.bg);
        pBg = itemView.findViewById(R.id.scene);
        tv = itemView.findViewById(R.id.tv);
        num = itemView.findViewById(R.id.num);
        itemView.setClickable(true);
      }
    }
  }

  public static class Data {

    private String bone = null;
    private String bone2 = null;
    private String bone3 = null;
    private String particle = null;
    private Bind bind = null;
    private Drawable bg = null;

    public Data setBone(String s) {
      bone = s;
      return this;
    }

    public Data setBone2(String s) {
      bone2 = s;
      return this;
    }

    public Data setBone3(String s) {
      bone3 = s;
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

    public Data setBg(Drawable d) {
      bg = d;
      return this;
    }

    public interface Bind {

      void onBind(Holder h);
    }
  }
}
