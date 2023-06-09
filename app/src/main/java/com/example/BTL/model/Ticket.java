package com.example.BTL.model;

import com.google.gson.annotations.SerializedName;

public class Ticket {
    @SerializedName("id")
    private int mID;
    @SerializedName("movieName")
    private String mMovieName;
    @SerializedName("cinemaName")
    private String mCinemaName;
    @SerializedName("time")
    private String mTime;
    @SerializedName("date")
    private String mDate;
    @SerializedName("seat")
    private String mSeat;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("userUID")
    private String mUserUID;
    @SerializedName("room")
    private int mRoom;
    @SerializedName("cinemaID")
    private int mCinemaID;
    @SerializedName("movieID")
    private int mMovieID;

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmMovieName() {
        return mMovieName;
    }

    public void setmMovieName(String mMovieName) {
        this.mMovieName = mMovieName;
    }

    public String getmCinemaName() {
        return mCinemaName;
    }

    public void setmCinemaName(String mCinemaName) {
        this.mCinemaName = mCinemaName;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmSeat() {
        return mSeat;
    }

    public void setmSeat(String mSeat) {
        this.mSeat = mSeat;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public String getmUserUID() {
        return mUserUID;
    }

    public void setmUserUID(String mUserUID) {
        this.mUserUID = mUserUID;
    }

    public int getmRoom() {
        return mRoom;
    }

    public void setmRoom(int mRoom) {
        this.mRoom = mRoom;
    }

    public int getmCinemaID() {
        return mCinemaID;
    }

    public void setmCinemaID(int mCinemaID) {
        this.mCinemaID = mCinemaID;
    }

    public int getmMovieID() {
        return mMovieID;
    }

    public void setmMovieID(int mMovieID) {
        this.mMovieID = mMovieID;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "mID=" + mID +
                ", mMovieName='" + mMovieName + '\'' +
                ", mCinemaName='" + mCinemaName + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mSeat='" + mSeat + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mUserUID='" + mUserUID + '\'' +
                ", mRoom=" + mRoom +
                ", mCinemaID=" + mCinemaID +
                ", mMovieID=" + mMovieID +
                '}';
    }
}
