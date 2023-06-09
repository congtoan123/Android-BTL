package com.example.BTL.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailShowTime {

    @SerializedName("time")
    private String mTime;
    @SerializedName("room")
    private int mRoom;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("seats")
    private ArrayList<Boolean> mSeats;
    @SerializedName("seatRowNumber")
    private int mSeatRowNumber;

    @Override
    public String toString() {
        return "DetailShowTime{" +
                "mTime='" + mTime + '\'' +
                ", mRoom=" + mRoom +
                ", mPrice=" + mPrice +
                ", mSeats=" + mSeats +
                ", mSeatRowNumber=" + mSeatRowNumber +
                ", mSeatColumnNumber=" + mSeatColumnNumber +
                '}';
    }

    public int getSeatRowNumber() {
        return mSeatRowNumber;
    }

    public void setSeatRowNumber(int mSeatRowNumber) {
        this.mSeatRowNumber = mSeatRowNumber;
    }

    public int getSeatColumnNumber() {
        return mSeatColumnNumber;
    }

    public void setSeatColumnNumber(int mSeatColumnNumber) {
        this.mSeatColumnNumber = mSeatColumnNumber;
    }

    @SerializedName("seatColumnNumber")
    private int mSeatColumnNumber;

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public int getRoom() {
        return mRoom;
    }

    public void setRoom(int mRoom) {
        this.mRoom = mRoom;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public ArrayList<Boolean> getSeats() {
        return mSeats;
    }

    public void setSeats(ArrayList<Boolean> mSeats) {
        this.mSeats = mSeats;
    }


}
