package com.tim.badrecord.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tim.badrecord.Model.Crime;
import com.tim.badrecord.Model.CrimeLab;
import com.tim.badrecord.Fragments.CrimeFragment;
import com.tim.badrecord.R;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimeList;
    private Button mFirstButton,mLastButton;
    public static final String EXTRA_CRIME_ID = "com.tim.badrecord.crime_id";

    public static Intent newIntent(Context context, UUID crimeID){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeID = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mCrimeList = CrimeLab.get(this).getCrimeList();

        mViewPager = findViewById(R.id.vp_crime_activity);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = mCrimeList.get(i);
                return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });

        mFirstButton = findViewById(R.id.btn_first);
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mLastButton = findViewById(R.id.btn_last);
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(Objects.requireNonNull(mViewPager.getAdapter()).getCount()-1);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (mViewPager.getCurrentItem() == 0){
                    mFirstButton.setEnabled(false);
                } else {
                    mFirstButton.setEnabled(true);
                }

                if (mViewPager.getCurrentItem() == Objects.requireNonNull(mViewPager.getAdapter()).getCount()-1){
                    mLastButton.setEnabled(false);
                } else {
                    mLastButton.setEnabled(true);
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 点哪个开哪个
        for (int i = 0; i < mCrimeList.size(); i++) {
            if (mCrimeList.get(i).getID().equals(crimeID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
