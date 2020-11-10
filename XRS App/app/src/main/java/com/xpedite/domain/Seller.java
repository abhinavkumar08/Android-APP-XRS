package com.xpedite.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinkum on 9/28/17.
 */

public class Seller implements Parcelable {

    private String name;
    private String emailId;
    private String phoneNumber;
    private String password;
    private List<Address> listOfAddresses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Address> getListOfAddresses() {
        return listOfAddresses;
    }

    public void setListOfAddresses(List<Address> listOfAddresses) {
        this.listOfAddresses = listOfAddresses;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(listOfAddresses);
        out.writeString(name);
        out.writeString(emailId);
        out.writeString(phoneNumber);
        out.writeString(password);
    }

    public static final Parcelable.Creator<Seller> CREATOR
            = new Parcelable.Creator<Seller>() {
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };

    public Seller()
    {

    }

    private Seller(Parcel in) {
        listOfAddresses = new ArrayList<Address>();
        in.readTypedList(listOfAddresses, Address.CREATOR);
        name = in.readString();
        emailId = in.readString();
        phoneNumber = in.readString();
        password = in.readString();

    }

}

