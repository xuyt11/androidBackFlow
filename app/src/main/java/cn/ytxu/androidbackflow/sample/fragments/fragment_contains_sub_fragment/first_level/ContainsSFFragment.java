package cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.first_level;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ytxu.androidbackflow.BaseFragment;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.second_level.FCSFSecondAFragment;
import cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.second_level.FCSFSecondBFragment;
import cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.second_level.FCSFSecondCFragment;
import cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.second_level.FCSFSecondDFragment;
import cn.ytxu.androidbackflow.sample.ContainerActivity;

public class ContainsSFFragment extends BaseFragment {
    private String[] mTitles = new String[]{"唐僧", "大师兄", "二师兄", "沙师弟"};

    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csf_fragment, container, false);
        setRoot(view);

        getActivity().setTitle(this.getClass().getSimpleName());

        tabLayout = $(R.id.fcsf_tab);
        viewPager = $(R.id.fcsf_viewPager);
        viewPager.setOffscreenPageLimit(mTitles.length);
        setPagerAdapter();

        initView();
        return view;
    }

    private void setPagerAdapter() {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new FCSFSecondAFragment();
                    case 1:
                        return new FCSFSecondBFragment();
                    case 2:
                        return new FCSFSecondCFragment();
                    case 3:
                        return new FCSFSecondDFragment();
                    default:
                        return new FCSFSecondAFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void initView() {
        $(R.id.fcsf_4_jump_2_next).setVisibility(View.INVISIBLE);
    }

    protected void jump2NextView(final Class<? extends Fragment> clazz) {
        $(R.id.fcsf_4_jump_2_next).setVisibility(View.VISIBLE);
        $(R.id.fcsf_4_jump_2_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContainsSFFragment.this.getActivity(), ContainerActivity.class);
                intent.putExtra(ContainerActivity.PARAM_CLASSNAME, clazz.getName());
                startActivity(intent);
            }
        });
    }

}
