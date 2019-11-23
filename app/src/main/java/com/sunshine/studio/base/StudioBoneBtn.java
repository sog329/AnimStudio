package com.sunshine.studio.base;

import android.content.Context;
import android.util.AttributeSet;

import com.sunshine.engine.bone.StageView;

/**
 * Created by Jack on 2019-11-23.
 */
public class StudioBoneBtn extends StageView {
	public StudioBoneBtn(Context context) {
		super(context);
	}

	public StudioBoneBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StudioBoneBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		StudioTool.square(this, w, h, h);
	}
}
