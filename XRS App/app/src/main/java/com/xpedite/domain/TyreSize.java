package com.xpedite.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhinkum on 4/14/18.
 *
 */

public class TyreSize implements Parcelable {

    private int smallTyreCount;
    private int mediumTyreCount;
    private int largeTyreCount;

    public int getSmallTyreCount() {
        return smallTyreCount;
    }

    public int getMediumTyreCount() {
        return mediumTyreCount;
    }

    public void setMediumTyreCount(int mediumTyreCount) {
        this.mediumTyreCount = mediumTyreCount;
    }

    public int getLargeTyreCount() {
        return largeTyreCount;
    }

    public void setLargeTyreCount(int largeTyreCount) {
        this.largeTyreCount = largeTyreCount;
    }

    public void setSmallTyreCount(int smallTyreCount) {
        this.smallTyreCount = smallTyreCount;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(smallTyreCount);
        out.writeInt(mediumTyreCount);
        out.writeInt(largeTyreCount);

    }

    public static final Parcelable.Creator<TyreSize> CREATOR
            = new Parcelable.Creator<TyreSize>() {
        public TyreSize createFromParcel(Parcel in) {
            return new TyreSize(in);
        }

        public TyreSize[] newArray(int size) {
            return new TyreSize[size];
        }
    };

    public TyreSize()
    {

    }

    private TyreSize(Parcel in) {

        smallTyreCount = in.readInt();
        mediumTyreCount = in.readInt();
        largeTyreCount = in.readInt();
    }


}
