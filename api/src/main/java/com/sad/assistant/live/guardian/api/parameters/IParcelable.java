package com.sad.assistant.live.guardian.api.parameters;

import android.os.Parcel;
import android.os.Parcelable;
public interface IParcelable extends Parcelable {

    @Override
    default int describeContents(){return 0;}

    void readFromParcel(Parcel in);

}
