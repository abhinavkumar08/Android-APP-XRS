package com.xpedite.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abhinkum on 9/18/18.
 */

public class UserRequest implements Parcelable {


    /** The request id. */
    private String requestId;

    /** The pick up user mobile number. */
    private long pickupUserMobileNumber;

    /** The primary user mobile number. */
    private long primaryUserMobileNumber;

    /** The pickup user. */
    private String pickupUser;

    /** The address. */
    private String address;

    /** The pickup request date. */
    private Date pickupRequestDate;

    /** The pickup collected date. */
    private Date pickupCollectedDate;

    /** The small tyre count. */
    private int smallTyreCount;

    /** The medium tyre count. */
    private int mediumTyreCount;

    /** The large tyre count. */
    private int largeTyreCount;

    /** The amount paid. */
    private float amountPaid;

    /** The payment status. */
    private String paymentStatus;

    /**
     * Gets the request id.
     *
     * @return the request id
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the request id.
     *
     * @param requestId the new request id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the pickup request date.
     *
     * @return the pickup request date
     */
    public Date getPickupRequestDate() {
        return pickupRequestDate;
    }

    /**
     * Sets the pickup request date.
     *
     * @param pickupRequestDate the new pickup request date
     */
    public void setPickupRequestDate(Date pickupRequestDate) {
        this.pickupRequestDate = pickupRequestDate;
    }

    /**
     * Gets the pickup user mobile number.
     *
     * @return the pickup user mobile number
     */
    public long getPickupUserMobileNumber() {
        return pickupUserMobileNumber;
    }

    /**
     * Sets the pickup user mobile number.
     *
     * @param pickupUserMobileNumber the new pickup user mobile number
     */
    public void setPickupUserMobileNumber(long pickupUserMobileNumber) {
        this.pickupUserMobileNumber = pickupUserMobileNumber;
    }

    /**
     * Gets the pickup user.
     *
     * @return the pickup user
     */
    public String getPickupUser() {
        return pickupUser;
    }

    /**
     * Sets the pickup user.
     *
     * @param pickupUser the new pickup user
     */
    public void setPickupUser(String pickupUser) {
        this.pickupUser = pickupUser;
    }

    /**
     * Gets the pickup collected date.
     *
     * @return the pickup collected date
     */
    public Date getPickupCollectedDate() {
        return pickupCollectedDate;
    }

    /**
     * Sets the pickup collected date.
     *
     * @param pickupCollectedDate the new pickup collected date
     */
    public void setPickupCollectedDate(Date pickupCollectedDate) {
        this.pickupCollectedDate = pickupCollectedDate;
    }

    /**
     * Gets the small tyre count.
     *
     * @return the small tyre count
     */
    public int getSmallTyreCount() {
        return smallTyreCount;
    }

    /**
     * Sets the small tyre count.
     *
     * @param smallTyreCount the new small tyre count
     */
    public void setSmallTyreCount(int smallTyreCount) {
        this.smallTyreCount = smallTyreCount;
    }

    /**
     * Gets the medium tyre count.
     *
     * @return the medium tyre count
     */
    public int getMediumTyreCount() {
        return mediumTyreCount;
    }

    /**
     * Sets the medium tyre count.
     *
     * @param mediumTyreCount the new medium tyre count
     */
    public void setMediumTyreCount(int mediumTyreCount) {
        this.mediumTyreCount = mediumTyreCount;
    }

    /**
     * Gets the large tyre count.
     *
     * @return the large tyre count
     */
    public int getLargeTyreCount() {
        return largeTyreCount;
    }

    /**
     * Sets the large tyre count.
     *
     * @param largeTyreCount the new large tyre count
     */
    public void setLargeTyreCount(int largeTyreCount) {
        this.largeTyreCount = largeTyreCount;
    }

    /**
     * Gets the amount paid.
     *
     * @return the amount paid
     */
    public float getAmountPaid() {
        return amountPaid;
    }

    /**
     * Sets the amount paid.
     *
     * @param amountPaid the new amount paid
     */
    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * Gets the payment status.
     *
     * @return the payment status
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the payment status.
     *
     * @param paymentStatus the new payment status
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the primary user mobile number.
     *
     * @return the primary user mobile number
     */
    public long getPrimaryUserMobileNumber() {
        return primaryUserMobileNumber;
    }

    /**
     * Sets the primary user mobile number.
     *
     * @param primaryUserMobileNumber the new primary user mobile number
     */
    public void setPrimaryUserMobileNumber(long primaryUserMobileNumber) {
        this.primaryUserMobileNumber = primaryUserMobileNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(requestId);
        parcel.writeLong(pickupUserMobileNumber);
        parcel.writeString(pickupUser);
        parcel.writeString(address);
        parcel.writeSerializable(pickupRequestDate);
        parcel.writeSerializable(pickupCollectedDate);
        parcel.writeInt(smallTyreCount);
        parcel.writeInt(mediumTyreCount);
        parcel.writeInt(largeTyreCount);
        parcel.writeFloat(amountPaid);
        parcel.writeString(paymentStatus);

    }

    public static final Parcelable.Creator<UserRequest> CREATOR
            = new Parcelable.Creator<UserRequest>() {
        public UserRequest createFromParcel(Parcel in) {
            return new UserRequest(in);
        }

        public UserRequest[] newArray(int size) {
            return new UserRequest[size];
        }
    };

    public UserRequest()
    {

    }


    private UserRequest(Parcel in) {

        requestId = in.readString();
        pickupUserMobileNumber = in.readLong();
        pickupUser = in.readString();
        address = in.readString();
        pickupRequestDate = (Date) in.readSerializable();
        pickupCollectedDate = (Date) in.readSerializable();
        smallTyreCount = in.readInt();
        mediumTyreCount = in.readInt();
        largeTyreCount = in.readInt();
        amountPaid = in.readFloat();
        paymentStatus = in.readString();


    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "requestId='" + requestId + '\'' +
                ", pickupUserMobileNumber=" + pickupUserMobileNumber +
                ", pickupUser='" + pickupUser + '\'' +
                ", address='" + address + '\'' +
                ", pickupRequestDate=" + pickupRequestDate +
                ", pickupCollectedDate=" + pickupCollectedDate +
                ", smallTyreCount=" + smallTyreCount +
                ", mediumTyreCount=" + mediumTyreCount +
                ", largeTyreCount=" + largeTyreCount +
                ", amountPaid=" + amountPaid +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
