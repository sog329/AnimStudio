package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.engine.bone.StageView;
import com.sunshine.studio.R;
import com.sunshine.studio.base.DemoRv;

import java.util.List;

/**
 * Created by Jack on 2019-11-20.
 */
public class BoneRv extends DemoRv<StageView> {
	public BoneRv(Context context) {
		super(context);
	}

	public BoneRv(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public BoneRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void loadData(List<String> data, List<String> bg) {
		data.add("welcome_omi");
		data.add("card");
		data.add("welcomeDemo");
		data.add("qin");
		data.add("sunglasses");
		data.add("gear");
		data.add("match");
		data.add("pass");
		data.add("like");
		data.add("me");
		data.add("set");
		data.add("tab1");
		data.add("tab2");
		data.add("tab3");
		data.add("tab4");
		data.add("voice2");
		data.add("arrow");

		bg.add("card");
		bg.add("welcomeDemo");
	}

	@Override
	protected int layoutId() {
		return R.layout.item_bone_demo_rv;
	}

	@Override
	protected String getFolderName() {
		return "bone";
	}
}
