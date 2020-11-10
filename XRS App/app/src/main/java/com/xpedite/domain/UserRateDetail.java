package com.xpedite.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by abhinkum on 10/2/18.
 */

public class UserRateDetail implements Parcelable {




    private String primaryUserMobileNumber;

    private int smallTyrePrice;

    private int mediumTyrePrice;

    private int largeTyrePrice;

    private int otherTyrePrice;

    public int getSmallTyrePrice() {
        return smallTyrePrice;
    }

    public void setSmallTyrePrice(int smallTyrePrice) {
        this.smallTyrePrice = smallTyrePrice;
    }

    public int getMediumTyrePrice() {
        return mediumTyrePrice;
    }

    public void setMediumTyrePrice(int mediumTyrePrice) {
        this.mediumTyrePrice = mediumTyrePrice;
    }

    public int getLargeTyrePrice() {
        return largeTyrePrice;
    }

    public void setLargeTyrePrice(int largeTyrePrice) {
        this.largeTyrePrice = largeTyrePrice;
    }

    public String getPrimaryUserMobileNumber() {
        return primaryUserMobileNumber;
    }

    public void setPrimaryUserMobileNumber(String primaryUserMobileNumber) {
        this.primaryUserMobileNumber = primaryUserMobileNumber;
    }

    public int getOtherTyrePrice() {
        return otherTyrePrice;
    }

    public void setOtherTyrePrice(int otherTyrePrice) {
        this.otherTyrePrice = otherTyrePrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(primaryUserMobileNumber);
        parcel.writeInt(smallTyrePrice);
        parcel.writeInt(mediumTyrePrice);
        parcel.writeInt(largeTyrePrice);
    }

    public static final Parcelable.Creator<UserRateDetail> CREATOR
            = new Parcelable.Creator<UserRateDetail>() {
        public UserRateDetail createFromParcel(Parcel in) {
            return new UserRateDetail(in);
        }

        public UserRateDetail[] newArray(int size) {
            return new UserRateDetail[size];
        }
    };

    public UserRateDetail()
    {

    }

    private UserRateDetail(Parcel in) {

        primaryUserMobileNumber = in.readString();
        smallTyrePrice = in.readInt();
        mediumTyrePrice = in.readInt();
        largeTyrePrice = in.readInt();

    }
}
