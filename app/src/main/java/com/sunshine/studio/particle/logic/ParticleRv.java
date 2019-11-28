package com.sunshine.studio.particle.logic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.engine.particle.SceneView;
import com.sunshine.studio.R;
import com.sunshine.studio.base.DemoRv;

import java.util.List;

/**
 * Created by Jack on 2019-11-20.
 */
public class ParticleRv extends DemoRv<SceneView> {
	public ParticleRv(Context context) {
		super(context);
	}

	public ParticleRv(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ParticleRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void loadData(List<String> data, List<String> bg) {
		data.add("send_heart");
		data.add("note");
		data.add("heart");
		data.add("welcome_omi_bg");
		data.add("match_bg");
		data.add("cry");
		data.add("hi");
		data.add("no");
		data.add("smile");
		data.add("what");
		data.add("yeah");
		data.add("singleDog");
		data.add("snow");
		data.add("christmas");
		data.add("bell");
		data.add("gift");
	}

	@Override
	protected int layoutId() {
		return R.layout.item_particle_demo_rv;
	}

	@Override
	protected String getFolderName() {
		return "particle";
	}
}
