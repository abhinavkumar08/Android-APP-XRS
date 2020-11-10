package com.xpedite.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhinkum on 9/20/17.
 *
 */

public class Address implements Parcelable{

    private String name;
    private long mobileNumber;
    private String plotNumber;
    private String area;
    private String street;
    private String landmark;
    private String city;
    private long pinCode;
    private String state;
    private String isPrimaryUser;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPlotNumber() {
        return plotNumber;
    }
    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public long getPinCode() {
        return pinCode;
    }
    public void setPinCode(long pinCode) {
        this.pinCode = pinCode;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public long getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public String getStreet() { return street;}
    public void setStreet(String street) { this.street = street;}
    public String getLandmark() { return landmark;}
    public void setLandmark(String landmark) { this.landmark = landmark;}
    public String getIsPrimaryUser() {
        return isPrimaryUser;
    }
    public void setIsPrimaryUser(String isPrimaryUser) {
        this.isPrimaryUser = isPrimaryUser;
    }

    public String toString()
    {
        return name+  ",\n" +
                mobileNumber + ",\n" +
                plotNumber + ", " + street+ ", " + area + ", " +
                landmark+ ", "+ "\n" +
                city + ", "+ state + ", "+ "\n" +
                pinCode;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeLong(mobileNumber);
        out.writeString(plotNumber);
        out.writeString(street);
        out.writeString(landmark);
        out.writeString(area);
        out.writeString(city);
        out.writeLong(pinCode);
        out.writeString(state);
    }

    public static final Parcelable.Creator<Address> CREATOR
            = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address()
    {

    }
    public Address(Parcel in) {
        name = in.readString();
        mobileNumber = in.readLong();
        plotNumber = in.readString();
        area = in.readString();
        street = in.readString();
        landmark = in.readString();
        city = in.readString();
        pinCode = in.readLong();
        state = in.readString();
    }


}
