package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sunshine.engine.bone.logic.Actor;
import com.sunshine.engine.bone.logic.Anim;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.studio.R;
import com.sunshine.studio.base.StudioTool;
import com.sunshine.studio.base.StudioTv;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/** Created by songxiaoguang on 2017/12/2. */
public class AnimLv extends ListView {
  private AnimAdapter adapter = null;

  public AnimLv(Context context) {
    super(context);
  }

  public AnimLv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public AnimLv(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void loadData(BoneStudio studio) {
    if (adapter == null) {
      adapter = new AnimAdapter();
      setAdapter(adapter);
    }
    adapter.loadData(studio);
  }

  private static class AnimAdapter extends BaseAdapter {

    private BoneStudio studio = null;

    private List<Bone> lstData = new ArrayList<>();

    private int select = -1;

    public void loadData(BoneStudio studio) {
      this.studio = studio;
      if (studio.entity != null) {
        Actor actor = studio.entity.getLastActor();
        if (actor != null) {
          lstData = actor.lstBone;
        } else {
          lstData.clear();
        }
      }
      select = -1;
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
                .inflate(R.layout.item_bone_studio_anim_lv, null);
      }
      // change bg
      if (position == select) {
        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.btn_bg2));
      } else {
        convertView.setBackgroundDrawable(null);
      }
      Bone bone = lstData.get(position);
      // iv
      BoneIv iv = convertView.findViewById(R.id.iv);
      Rect rect = new Rect(lstData.get(position).lstRect.get(0));
      iv.setBmp(
          bone.externalBmpId == null
              ? studio.entity.bmp
              : studio.entity.mapBmp.get(bone.externalBmpId),
          rect);
      // up
      View up = convertView.findViewById(R.id.up);
      if (position == 0) {
        up.setVisibility(INVISIBLE);
        up.setOnClickListener(null);
      } else {
        up.setVisibility(VISIBLE);
        up.setOnClickListener(
            v -> {
              lstData.remove(bone);
              lstData.add(position - 1, bone);
              select = position - 1;
              notifyDataSetChanged();
            });
      }
      up.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
      // down
      View down = convertView.findViewById(R.id.down);
      if (position == getCount() - 1) {
        down.setVisibility(INVISIBLE);
        down.setOnClickListener(null);
      } else {
        down.setVisibility(VISIBLE);
        down.setOnClickListener(
            v -> {
              lstData.remove(bone);
              lstData.add(position + 1, bone);
              select = position + 1;
              notifyDataSetChanged();
            });
      }
      down.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
      // del
      View del = convertView.findViewById(R.id.del);
      del.setOnClickListener(
          v -> {
            lstData.remove(bone);
            notifyDataSetChanged();
            select = -1;
          });
      del.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
      // duration
      LinearLayout ln = convertView.findViewById(R.id.ln);
      addView(ln, bone, position);
      return convertView;
    }

    private void addView(LinearLayout ln, Bone bone, int position) {
      while (ln.getChildCount() > bone.lstAnim.size()) {
        ln.removeViewAt(0);
      }
      while (ln.getChildCount() < bone.lstAnim.size()) {
        ln.addView(
            LayoutInflater.from(ln.getContext())
                .inflate(R.layout.item_bone_studio_anim_duration, null));
      }
      for (int i = 0; i < bone.lstAnim.size(); i++) {
        View view = ln.getChildAt(i);
        Anim anim = bone.lstAnim.get(i);
        // from
        StudioTv from = view.findViewById(R.id.from);
        from.setText(String.valueOf(anim.duration.getFrom()));
        StudioTv.initSize(from, .5f);
        // to
        StudioTv to = view.findViewById(R.id.to);
        to.setText(String.valueOf(anim.duration.getTo()));
        StudioTv.initSize(to, .5f);
        // lp
        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(0, MATCH_PARENT, anim.duration.getDelta());
        view.setLayoutParams(lp);
        // alpha
        if (anim.alpha.getFrom() != 0 || anim.alpha.getTo() != 0) {
          if (i % 2 == 0) {
            view.setAlpha(1f);
          } else {
            view.setAlpha(.75f);
          }
        } else {
          view.setAlpha(.3f);
        }
        // click
        view.setOnClickListener(v -> {
          select = position;
          studio.onEditAnim(bone, anim);
          notifyDataSetChanged();
        });
      }
    }
  }
}
