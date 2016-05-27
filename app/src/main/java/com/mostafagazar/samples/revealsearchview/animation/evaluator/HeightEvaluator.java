package com.mostafagazar.samples.revealsearchview.animation.evaluator;

import android.animation.IntEvaluator;
import android.view.View;
import android.view.ViewGroup;

public class HeightEvaluator extends IntEvaluator {

        private View view;

        public HeightEvaluator(View v) {
            this.view = v;
        }

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int newHeight = super.evaluate(fraction, startValue, endValue);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = newHeight;
            view.setLayoutParams(params);

            return newHeight;
        }

    }