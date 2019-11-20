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
	protected void loadData(List<String> lst) {
		lst.add("welcome_omi");
		lst.add("welcomeDemo");
		lst.add("qin");
		lst.add("sunglasses");
		lst.add("gear");
		lst.add("pass");
		lst.add("like");
		lst.add("match");
		lst.add("me");
		lst.add("set");
		lst.add("tab1");
		lst.add("tab2");
		lst.add("tab3");
		lst.add("tab4");
		lst.add("voice2");
		lst.add("arrow");
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
