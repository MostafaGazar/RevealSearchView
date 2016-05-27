package com.mostafagazar.samples.revealsearchview.animation.evaluator;

import android.animation.IntEvaluator;
import android.view.View;
import android.view.ViewGroup;

public class WidthEvaluator extends IntEvaluator {

        private View view;

        public WidthEvaluator(View v) {
            this.view = v;
        }

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int newWidth = super.evaluate(fraction, startValue, endValue);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = newWidth;
            view.setLayoutParams(params);

            return newWidth;
        }
    }