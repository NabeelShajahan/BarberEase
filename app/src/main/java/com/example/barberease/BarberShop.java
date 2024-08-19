package com.example.barberease;
import android.os.Parcel;
import android.os.Parcelable;

public class BarberShop implements Parcelable{
    private String imageUrl;

    public BarberShop(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public BarberShop() {
        // Default constructor required for calls to DataSnapshot.getValue(BarberShop.class)
    }
    protected BarberShop(Parcel in) {
        imageUrl = in.readString();
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public static final Creator<BarberShop> CREATOR = new Creator<BarberShop>() {
        @Override
        public BarberShop createFromParcel(Parcel in) {
            return new BarberShop(in);
        }

        @Override
        public BarberShop[] newArray(int size) {
            return new BarberShop[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
    }
}
