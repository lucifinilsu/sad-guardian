package com.sad.assistant.live.guardian.api.parameters;

import android.os.Parcel;
import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public interface INotificationStyle extends IParcelable {

    String title();

    String description();

    int iconRes();

    Creator creator();

    interface Creator{

        Creator title(String title);

        Creator description(String description);

        Creator iconRes(int iconRes);

        INotificationStyle create();
    }

    @Override
    default void writeToParcel(Parcel parcel, int i){
        parcel.writeString(title());
        parcel.writeString(description());
        parcel.writeInt(iconRes());
    }

    @Override
    default void readFromParcel(Parcel in){
        creator()
                .title(in.readString())
                .description(in.readString())
                .iconRes(in.readInt())
                .create();
    }
}
