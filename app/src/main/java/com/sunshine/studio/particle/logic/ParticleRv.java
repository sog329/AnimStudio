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
	protected void loadData(List<String> lst) {
		lst.add("note");
		lst.add("heart");
		lst.add("welcome_omi_bg");
		lst.add("match_bg");
		lst.add("cry");
		lst.add("hi");
		lst.add("no");
		lst.add("smile");
		lst.add("what");
		lst.add("yeah");
		lst.add("singleDog");
		lst.add("snow");
		lst.add("christmas");
		lst.add("bell");
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
