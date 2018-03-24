package com.github.on7labs.util;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by androidlover5842 on 24.3.2018.
 */

public class FirebaseDBInstance {
    private static FirebaseDatabase mData;

    public static FirebaseDatabase getInstance() {
        if (mData == null) {

            mData = FirebaseDatabase.getInstance();
            mData.setPersistenceEnabled(true);
        }
        return mData;
    }

}