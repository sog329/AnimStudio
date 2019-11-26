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

  public void selectBone(int index){
//    adapter.select = index;
    adapter.notifyDataSetChanged();
  }

  private static class AnimAdapter extends BaseAdapter {

    private BoneStudio studio = null;

    private List<Actor> lstData = new ArrayList<>();

    private Anim animSelect = null;

    public void loadData(BoneStudio studio) {
      this.studio = studio;
      if (studio.entity != null) {
        lstData = studio.entity.lstActor;
        } else {
          lstData.clear();
      }
//      select = -1;
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      int total = 0;
      for (Actor a : lstData) {
        total += a.lstBone.size() + 1;
      }
      return total;
    }

    @Override
    public Object getItem(int position) {
      int p = 0;
      for (Actor a : lstData) {
        if (p == position) {
          return a;
        } else {
          p++;
          for (Bone b : a.lstBone) {
            if (p == position) {
              return b;
            } else {
              p++;
            }
          }
        }
      }
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
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bone_studio_anim_lv, null);
      }
      // change bg
//      if (position == select) {
//        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.btn_bg2));
//      } else {
//        convertView.setBackgroundDrawable(null);
//      }
      Object obj = getItem(position);
      if (obj instanceof Actor) {
        Actor actor = (Actor) obj;
        // add
        View add = convertView.findViewById(R.id.add);
        add.setVisibility(VISIBLE);
        add.setBackgroundResource(R.drawable.bg_btn2);
        add.setOnClickListener(v -> {
          studio.actor = actor;
          studio.dlgPic.show();
        });
        // iv
        convertView.findViewById(R.id.iv).setVisibility(GONE);
        // up
        int index = studio.entity.lstActor.indexOf(actor);
        View up = convertView.findViewById(R.id.up);
        up.setBackgroundResource(R.drawable.bg_btn);
        if (index == 0) {
          up.setVisibility(INVISIBLE);
          up.setOnClickListener(null);
        } else {
          up.setVisibility(VISIBLE);
          up.setOnClickListener(
              v -> {
                studio.entity.lstActor.remove(actor);
                studio.entity.lstActor.add(index - 1, actor);
//                select = position - 1;
                notifyDataSetChanged();
              });
        }
        up.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
        // down
        View down = convertView.findViewById(R.id.down);
        down.setBackgroundResource(R.drawable.bg_btn);
        if (index == studio.entity.lstActor.size() - 1) {
          down.setVisibility(INVISIBLE);
          down.setOnClickListener(null);
        } else {
          down.setVisibility(VISIBLE);
          down.setOnClickListener(
              v -> {
                studio.entity.lstActor.remove(actor);
                studio.entity.lstActor.add(index + 1, actor);
//                select = position + 1;
                notifyDataSetChanged();
              });
        }
        down.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
        // del
        View del = convertView.findViewById(R.id.del);
        del.setBackgroundResource(R.drawable.bg_btn);
        del.setOnClickListener(
            v -> {
              studio.entity.lstActor.remove(actor);
              notifyDataSetChanged();
//              select = -1;
            });
        del.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
        // duration
        LinearLayout ln = convertView.findViewById(R.id.ln);
        addView(ln, actor, position);
      } else {
        Bone bone = (Bone) obj;
        // add
        convertView.findViewById(R.id.add).setVisibility(GONE);
        // iv
        BoneIv iv = convertView.findViewById(R.id.iv);
        iv.setVisibility(VISIBLE);
        Rect rect = new Rect(bone.lstRect.get(0));
        iv.setBmp(
            bone.externalBmpId == null
                ? studio.entity.bmp
                : studio.entity.mapBmp.get(bone.externalBmpId),
            rect);
        // up
        int index = bone.actor.lstBone.indexOf(bone);
        View up = convertView.findViewById(R.id.up);
        up.setBackgroundResource(R.drawable.bg_btn2);
        if (index == 0) {
          up.setVisibility(INVISIBLE);
          up.setOnClickListener(null);
        } else {
          up.setVisibility(VISIBLE);
          up.setOnClickListener(
              v -> {
                bone.actor.lstBone.remove(bone);
                bone.actor.lstBone.add(index - 1, bone);
//                select = position - 1;
                notifyDataSetChanged();
              });
        }
        up.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
        // down
        View down = convertView.findViewById(R.id.down);
        down.setBackgroundResource(R.drawable.bg_btn2);
        if (index == bone.actor.lstBone.size() - 1) {
          down.setVisibility(INVISIBLE);
          down.setOnClickListener(null);
        } else {
          down.setVisibility(VISIBLE);
          down.setOnClickListener(
              v -> {
                bone.actor.lstBone.remove(bone);
                bone.actor.lstBone.add(index + 1, bone);
//                select = position + 1;
                notifyDataSetChanged();
              });
        }
        down.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
        // del
        View del = convertView.findViewById(R.id.del);
        del.setBackgroundResource(R.drawable.bg_btn2);
        del.setOnClickListener(
            v -> {
              bone.actor.lstBone.remove(bone);
              notifyDataSetChanged();
//              select = -1;
            });
        del.setMinimumWidth((int) (StudioTool.getBtnHeight() * 1.2f));
        // duration
        LinearLayout ln = convertView.findViewById(R.id.ln);
        addView(ln, bone, position);
      }
      return convertView;
    }

    private void addView(LinearLayout ln, Anim.Helper helper, int position) {
      while (ln.getChildCount() > helper.lstAnim.size()) {
        ln.removeViewAt(0);
      }
      while (ln.getChildCount() < helper.lstAnim.size()) {
        ln.addView(
            LayoutInflater.from(ln.getContext())
                .inflate(R.layout.item_bone_studio_anim_duration, null));
      }
      for (int i = 0; i < helper.lstAnim.size(); i++) {
        View view = ln.getChildAt(i);
        Anim anim = helper.lstAnim.get(i);
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
            view.setAlpha(.8f);
          } else {
            view.setAlpha(.6f);
          }
        } else {
          view.setAlpha(.3f);
        }
        // click
        view.setOnClickListener(v -> {
//          select = position;
          studio.onEditAnim(helper, anim);
          notifyDataSetChanged();
        });
      }
    }
  }
}
