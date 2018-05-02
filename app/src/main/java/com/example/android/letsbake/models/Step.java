package com.example.android.letsbake.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zsolt on 03.04.2018.
 */

public class Step implements Parcelable {

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    // @SerializedName  is needed for GSON to map the JSON keys with the attributes of a {@link Step} object.
    @SerializedName("id")
    private int stepId;

    @SerializedName("shortDescription")
    private String stepShortDescription;

    @SerializedName("description")
    private String stepDescription;

    @SerializedName("videoURL")
    private String stepVideoUrl;

    @SerializedName("thumbnailUrl")
    private String stepThumbnailUrl;

    //Constructs a new {@link Step} object.
    public Step(int stepId, String stepShortDescription, String stepDescription, String stepVideoUrl, String stepThumbnailUrl) {
        this.stepId = stepId;
        this.stepShortDescription = stepShortDescription;
        this.stepDescription = stepDescription;
        this.stepVideoUrl = stepVideoUrl;
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    // Parcelling part
    public Step(Parcel in) {
        stepId = in.readInt();
        stepShortDescription = in.readString();
        stepDescription = in.readString();
        stepVideoUrl = in.readString();
        stepThumbnailUrl = in.readString();
    }

    public int getStepId() { return stepId; }

    public String getStepShortDescription() { return stepShortDescription; }

    public String getStepDescription() { return stepDescription; }

    public String getStepVideoUrl() { return stepVideoUrl; }

    public String getStepThumbnailUrl() { return stepThumbnailUrl; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepId);
        dest.writeString(stepShortDescription);
        dest.writeString(stepDescription);
        dest.writeString(stepVideoUrl);
        dest.writeString(stepThumbnailUrl);
    }
}
