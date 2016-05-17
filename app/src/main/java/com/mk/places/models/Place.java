package com.mk.places.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mk.places.utilities.Utils;

public class Place implements Parcelable {

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    private String
            id,
            location,
            sight,
            continent,
            infoTitle,
            info,
            creditsTitle,
            creditsDesc,
            credits,
            description,
            url;


    protected Place(Parcel in) {

        id = in.readString();
        location = in.readString();
        sight = in.readString();
        continent = in.readString();
        infoTitle = in.readString();
        info = in.readString();
        creditsTitle = in.readString();
        creditsDesc = in.readString();
        credits = in.readString();
        description = in.readString();
        url = in.readString();


    }

    public Place(String id, String location, String sight, String continent, String infoTitle, String info, String creditsTitle, String creditsDesc, String credits, String description, String url) {
        this.id = Utils.convertEntitiesCharsHTML(id);
        this.location = Utils.convertEntitiesCharsHTML(location);
        this.sight = Utils.convertEntitiesCharsHTML(sight);
        this.continent = Utils.convertEntitiesCharsHTML(continent);
        this.infoTitle = Utils.convertEntitiesCharsHTML(infoTitle);
        this.info = Utils.convertEntitiesCharsHTML(info);
        this.creditsTitle = Utils.convertEntitiesCharsHTML(creditsTitle);
        this.creditsDesc = Utils.convertEntitiesCharsHTML(creditsDesc);
        this.credits = credits.replace(" ", "");
        this.description = Utils.convertEntitiesCharsHTML(description);
        this.url = url.replace(" ", "");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(continent);
        dest.writeString(infoTitle);
        dest.writeString(info);
        dest.writeString(creditsTitle);
        dest.writeString(creditsDesc);
        dest.writeString(credits);
        dest.writeString(description);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return Places.getPlacesList().size();
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public String getSight() {
        return sight;
    }

    public String getContinent() {
        return continent;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public String getCreditsTitle() {
        return creditsTitle;
    }

    public String getCredits() {
        return credits;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public String getCreditsDesc() {
        return creditsDesc;
    }

}