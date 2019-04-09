package com.tim.badrecord.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.tim.badrecord.Model.Crime;
import com.tim.badrecord.Model.CrimeLab;
import com.tim.badrecord.Activities.CrimePagerActivity;
import com.tim.badrecord.R;
import java.util.List;


public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mLastClickPostion = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = view.findViewById(R.id.rv_crime);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));    // must have layout manager or app will down

//        updateUI();

        return view;
    }

    // 保证Fragment视图得到刷新
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimeList = crimeLab.getCrimeList();
        if (mAdapter == null){
            mAdapter = new CrimeAdapter(crimeList);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            if (mLastClickPostion > -1){
                mAdapter.notifyItemChanged(mLastClickPostion);
                mLastClickPostion = -1;
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mPoliceImageView;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.tv_crime_title);
            mDateTextView = itemView.findViewById(R.id.tv_crime_date);
            mPoliceImageView = itemView.findViewById(R.id.img_police);
        }

        // 绑定一个view的数据
        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.format("EEEE, MMM dd yyyy hh:mm:ss", mCrime.getDate()));
            mPoliceImageView.setVisibility(mCrime.isSolved()? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            mLastClickPostion = this.getAdapterPosition();
            startActivity(CrimePagerActivity.newIntent(getActivity(),mCrime.getID()));
        }
    }



    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimeList;

        public CrimeAdapter(List<Crime> crimeList){
            mCrimeList = crimeList;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater,viewGroup);
        }

        // 将CrimeHolder的单一数据绑定到recycler view上
        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {
            Crime crime = mCrimeList.get(i);
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }
    }
}
