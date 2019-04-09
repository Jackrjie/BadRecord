package com.tim.badrecord.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Map<UUID,Crime> mCrimeList;

    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimeList = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 ==  0);
            mCrimeList.put(crime.getID(),crime);
        }
    }

    public List<Crime> getCrimeList() {
        return new ArrayList<>(mCrimeList.values());
    }

    public Crime getCrime(UUID id){
        return mCrimeList.get(id);
    }
}
