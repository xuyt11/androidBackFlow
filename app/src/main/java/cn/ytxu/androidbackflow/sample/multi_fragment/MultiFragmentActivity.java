package cn.ytxu.androidbackflow.sample.multi_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterAFragment;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterBFragment;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterCFragment;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterDFragment;

public class MultiFragmentActivity extends BaseActivity {
    private String[] mTitles = new String[]{"唐僧", "大师兄", "二师兄", "沙师弟"};

    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_fragment_activity);

        $(R.id.multi_fragment_4_requedst_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiFragmentActivity.this, MFSecondActivity.class));
            }
        });

        tabLayout = $(R.id.multi_fragment_tab);
        viewPager = $(R.id.multi_fragment_viewPager);
        // TODO init all fragment or init only two
        viewPager.setOffscreenPageLimit(mTitles.length);
        setPagerAdapter();
    }

    private void setPagerAdapter() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new MFLetterAFragment();
                    case 1:
                        return new MFLetterBFragment();
                    case 2:
                        return new MFLetterCFragment();
                    case 3:
                        return new MFLetterDFragment();
                    default:
                        return new MFLetterAFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }


}
