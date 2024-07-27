package com.example.barberease;
import android.os.Parcel;
import android.os.Parcelable;


public class Barber implements Parcelable{
    private String userId;
    private String imageUrl;
    private String name;
    private String details;
    private String price;
    private String schedule;
    private String payments;
    private boolean haircut;
    private boolean shave;
    private boolean coloring;
    private boolean styling;

    public Barber() {
        // Default constructor required for calls to DataSnapshot.getValue(Barber.class)
    }

    public Barber(String userId, String imageUrl, String name, String details, String price, String schedule, String payments, boolean haircut, boolean shave, boolean coloring, boolean styling) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.details = details;
        this.price = price;
        this.schedule = schedule;
        this.payments = payments;
        this.haircut = haircut;
        this.shave = shave;
        this.coloring = coloring;
        this.styling = styling;
    }
    protected Barber(Parcel in) {
        userId = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        details = in.readString();
        price = in.readString();
        schedule = in.readString();
        payments = in.readString();
        haircut = in.readByte() != 0;
        shave = in.readByte() != 0;
        coloring = in.readByte() != 0;
        styling = in.readByte() != 0;
    }

    public static final Creator<Barber> CREATOR = new Creator<Barber>() {
        @Override
        public Barber createFromParcel(Parcel in) {
            return new Barber(in);
        }

        @Override
        public Barber[] newArray(int size) {
            return new Barber[size];
        }
    };

    // Getter and Setter methods for all fields

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public boolean isHaircut() {
        return haircut;
    }

    public void setHaircut(boolean haircut) {
        this.haircut = haircut;
    }

    public boolean isShave() {
        return shave;
    }

    public void setShave(boolean shave) {
        this.shave = shave;
    }

    public boolean isColoring() {
        return coloring;
    }

    public void setColoring(boolean coloring) {
        this.coloring = coloring;
    }

    public boolean isStyling() {
        return styling;
    }

    public void setStyling(boolean styling) {
        this.styling = styling;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(imageUrl);
        parcel.writeString(name);
        parcel.writeString(details);
        parcel.writeString(price);
        parcel.writeString(schedule);
        parcel.writeString(payments);
        parcel.writeByte((byte) (haircut ? 1 : 0));
        parcel.writeByte((byte) (shave ? 1 : 0));
        parcel.writeByte((byte) (coloring ? 1 : 0));
        parcel.writeByte((byte) (styling ? 1 : 0));
    }
}
