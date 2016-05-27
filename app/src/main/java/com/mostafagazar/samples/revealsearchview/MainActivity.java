package com.mostafagazar.samples.revealsearchview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.mostafagazar.samples.revealsearchview.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.revealAnimation_radiogroup)
    RadioGroup revealAnimationRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent i = new Intent(this, SearchActivity.class);

                SearchActivity.RevealAnimation selectedRevealAnimation;
                switch (revealAnimationRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.circularReveal_radiobutton:
                        selectedRevealAnimation = SearchActivity.RevealAnimation.Circular;
                        break;
                    case R.id.classicReveal_radiobutton:
                    default:
                        selectedRevealAnimation = SearchActivity.RevealAnimation.Classic;
                        break;
                }
                i.putExtra(Constants.EXTRA_SELECTED_REVEAL_ANIMATION, selectedRevealAnimation);

                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
