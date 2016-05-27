package com.mostafagazar.samples.revealsearchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.mostafagazar.samples.revealsearchview.animation.evaluator.HeightEvaluator;
import com.mostafagazar.samples.revealsearchview.animation.evaluator.WidthEvaluator;
import com.mostafagazar.samples.revealsearchview.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    public enum RevealAnimation {
        Circular, Classic
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrim)
    View scrim;

    @BindView(R.id.search_panel)
    View searchPanel;

    @BindView(R.id.search_view)
    SearchView searchView;

    private SearchActivity.RevealAnimation selectedRevealAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Intent i = getIntent();
        selectedRevealAnimation = (RevealAnimation) i.getSerializableExtra(Constants.EXTRA_SELECTED_REVEAL_ANIMATION);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupSearchView();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                dismiss();
            }
        });

        doEnterAnim();

        overridePendingTransition(0, 0);
    }

    private void setupSearchView() {
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.keyword));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            public boolean onClose() {
                SearchActivity.this.dismiss();
                return false;
            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    @OnClick(R.id.search_container)
    public void dismiss() {
        doExitAnim();
    }

    public void onBackPressed() {
        dismiss();
    }

    private void doEnterAnim() {
        switch (selectedRevealAnimation) {
            case Circular:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    doCircularRevealEnterAnim();
                } else {
                    Toast.makeText(this, "Circular Reveal animation is supported on Lollipop+", Toast.LENGTH_SHORT).show();
                }
                break;
            case Classic:
            default:
                doClassicRevealEnterAnim();
                break;
        }
    }

    private void doExitAnim() {
        switch (selectedRevealAnimation) {
            case Circular:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    doCircularRevealExitAnim();
                }
                break;
            case Classic:
            default:
                doClassicRevealExitAnim();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doCircularRevealEnterAnim() {
        scrim.animate()
                .alpha(1.0F)
                .setDuration(500L)
                .setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .start();

        if (searchPanel != null) {
            searchPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    searchPanel.getViewTreeObserver().removeOnPreDrawListener(this);
                    Animator animator = ViewAnimationUtils.createCircularReveal(searchPanel, searchPanel.getRight(), searchPanel.getTop(), 0.0F, searchPanel.getWidth());
                    animator.setDuration(200L);
                    animator.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this, android.R.interpolator.fast_out_slow_in));
                    animator.start();
                    return false;
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doCircularRevealExitAnim() {
        Animator animator = ViewAnimationUtils.createCircularReveal(searchPanel, searchPanel.getRight(), searchPanel.getTop(), searchPanel.getWidth(), 0.0F);
        animator.setDuration(200L);
        animator.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in));
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                searchPanel.setVisibility(View.INVISIBLE);
                ActivityCompat.finishAfterTransition(SearchActivity.this);
            }
        });
        animator.start();

        scrim.animate()
                .alpha(0.0F)
                .setDuration(300L)
                .setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .start();
    }

    public void doClassicRevealEnterAnim() {
        scrim.animate()
                .alpha(1.0F)
                .setDuration(500L)
                .setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .start();

        if (searchPanel != null) {
            searchPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    searchPanel.getViewTreeObserver().removeOnPreDrawListener(this);

                    ValueAnimator animatorWidth = ValueAnimator
                            .ofObject(new WidthEvaluator(searchPanel), 0, searchPanel.getWidth())
                            .setDuration(200L);
                    animatorWidth.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this, android.R.interpolator.fast_out_slow_in));

                    ViewGroup.LayoutParams params = searchPanel.getLayoutParams();
                    params.height = toolbar.getHeight();
                    searchPanel.setLayoutParams(params);
                    ValueAnimator animatorHeight = ValueAnimator
                            .ofObject(new HeightEvaluator(searchPanel), toolbar.getHeight(), searchPanel.getHeight())
                            .setDuration(300L);
                    animatorHeight.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this, android.R.interpolator.fast_out_slow_in));
                    animatorHeight.setStartDelay(animatorWidth.getDuration());

                    animatorWidth.start();
                    animatorHeight.start();
                    return false;
                }
            });
        }
    }

    private void doClassicRevealExitAnim() {
        ValueAnimator animatorHeight = ValueAnimator
                .ofObject(new HeightEvaluator(searchPanel), searchPanel.getHeight(), toolbar.getHeight())
                .setDuration(300L);
        animatorHeight.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_linear_in));

        ValueAnimator animatorWidth = ValueAnimator
                .ofObject(new WidthEvaluator(searchPanel), searchPanel.getWidth(), 0)
                .setDuration(200L);
        animatorWidth.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_linear_in));
        animatorWidth.setStartDelay(animatorWidth.getDuration());
        animatorWidth.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                searchPanel.setVisibility(View.INVISIBLE);
                ActivityCompat.finishAfterTransition(SearchActivity.this);
            }
        });

        animatorHeight.start();
        animatorWidth.start();

        scrim.animate()
                .alpha(0.0F)
                .setDuration(300L)
                .setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .start();
    }

}
