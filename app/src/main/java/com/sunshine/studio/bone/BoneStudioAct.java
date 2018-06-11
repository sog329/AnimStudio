package com.sunshine.studio.bone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BoneStudio;

/** Created by songxiaoguang on 2017/12/2. */
public class BoneStudioAct extends AppCompatActivity {
  private BoneStudio studio = new BoneStudio();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_bone_studio);
    studio.onCreate(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    studio.onDestroy();
  }

  @Override
  public void onBackPressed() {
    View editor = findViewById(R.id.win_editor);
    if (editor.getVisibility() == View.VISIBLE) {
      editor.setVisibility(View.GONE);
    } else {
      super.onBackPressed();
    }
  }
}
