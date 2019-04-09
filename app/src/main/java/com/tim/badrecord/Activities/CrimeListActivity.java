package com.tim.badrecord.Activities;

import com.tim.badrecord.Fragments.CrimeListFragment;

public class CrimeListActivity extends AbSingleFragmentActivity {

    @Override
    protected CrimeListFragment createFragment() {
        return new CrimeListFragment();
    }
}
