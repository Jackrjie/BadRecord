package com.tim.badrecord.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.tim.badrecord.Model.Crime;
import com.tim.badrecord.Model.CrimeLab;
import com.tim.badrecord.R;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton,mTimeButton;
    private CheckBox mSolvedCheckBox;

    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    /**
     * 将CrimeID存在CrimeFragment
     * @param crimeID crimeID
     * @return CrimeFragment
     */
    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeID);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 方法1：使用getActivity()方法获取CrimeActivity的intent，这样子就能依据ID来启动相对的画面。
        // 但此方法使得CrimeFragment不再是可复用的建构单元
//        UUID crimeID = (UUID)getActivity().getIntent().getSerializableExtra(CrimePagerActivity.EXTRA_CRIME_ID);

        // 方法2：调用getArguments方法再调用Bundle限定类型的get方法,如getSerializable
        UUID crimeID = (UUID) (getArguments() != null ? getArguments().getSerializable(ARG_CRIME_ID) : null);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField = view.findViewById(R.id.et_crime_title);
        mTitleField.setText(mCrime.getTitle()); // update crime title after got ID at CrimeListFragment.java
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // left blank
            }
        });

        mDateButton = view.findViewById(R.id.btn_crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                // 返回数据给crimeFragment,step 1 设置目标fragment
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                if (fm != null)
                    dialog.show(fm,DIALOG_DATE);
            }
        });

        mTimeButton = view.findViewById(R.id.btn_crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                if (fm != null){
                    dialog.show(fm,DIALOG_TIME);
                }
            }
        });
        updateTime();

        mSolvedCheckBox = view.findViewById(R.id.chk_crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_TIME){
            Date time = (Date)data.getSerializableExtra(TimePickerFragment.ARG_TIME);
            mCrime.setDate(time);
            updateTime();
        }
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM dd yyyy", mCrime.getDate()));
    }

    private void updateTime() {
        mTimeButton.setText(DateFormat.format("kk:mm:ss",mCrime.getDate()));
    }
}
