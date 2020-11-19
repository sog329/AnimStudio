package com.sunshine.engine.bone;

import android.content.Context;
import android.util.AttributeSet;

import com.sunshine.engine.base.AnimView;
import com.sunshine.engine.bone.logic.Stage;
import com.sunshine.engine.bone.logic.StageHelper;

public class StageView extends AnimView<StageHelper> {

    public StageView(Context context) {
        super(context);
    }

    public StageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public StageHelper buildHelper() {
        return new StageHelper();
    }

    public void canJump(boolean can) {
        helper.canJump(can);
    }

    /**
     * 在调用play(..)后生效
     *
     * @param toPercent 立即跳到指定percent
     */
    public void setPercent(float toPercent) {
        helper.setPercent(toPercent, toPercent, 0);
        invalidate();
    }

    /**
     * 在调用play(..)后生效
     *
     * @param toPercent
     * @param duration
     */
    public void setPercent(float toPercent, int duration) {
        helper.setPercent(toPercent, duration);
        invalidate();
    }

    /**
     * 在调用play(..)后生效
     *
     * @param fromPercent
     * @param toPercent
     * @param duration
     */
    public void setPercent(float fromPercent, float toPercent, int duration) {
        helper.setPercent(fromPercent, toPercent, duration);
        invalidate();
    }

    public void setCallback(Stage.Callback cb) {
        helper.setCallback(cb);
    }
}
