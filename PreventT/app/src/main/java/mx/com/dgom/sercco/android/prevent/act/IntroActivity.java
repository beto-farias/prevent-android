package mx.com.dgom.sercco.android.prevent.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.act.frament.IntroScreenSlidePageFragment;

public class IntroActivity extends AppCompatActivity {


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }



    /**
     * A simple pager adapter that represents 5 {@link IntroScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return IntroScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void entiendoAction(View v){
        Controller controller = new Controller(this);
        controller.hideIntro();


        Intent i = new Intent(IntroActivity.this, HomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
        finish();
    }
}
