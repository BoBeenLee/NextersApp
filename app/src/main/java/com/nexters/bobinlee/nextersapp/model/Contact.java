package com.nexters.bobinlee.nextersapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by BoBinLee on 2014-08-10.
 */
public class Contact implements Serializable {
    public String name;
    public int rank;
    public String phone;
    public String email;
    public boolean selected = false;
}
