package com.sad.assistant.live.guardian.api.parameters;

import android.os.Parcel;
import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public interface INotificationStyle extends IParcelable {

    String title();

    String description();

    String content();

    int iconRes();

    Creator creator();

    interface Creator{

        Creator title(String title);

        Creator description(String description);

        Creator content(String content);

        Creator iconRes(int iconRes);

        INotificationStyle create();
    }

    @Override
    default void writeToParcel(Parcel parcel, int i){
        parcel.writeString(title());
        parcel.writeString(description());
        parcel.writeString(content());
        parcel.writeInt(iconRes());
    }

    @Override
    default void readFromParcel(Parcel in){
        creator()
                .title(in.readString())
                .description(in.readString())
                .content(in.readString())
                .iconRes(in.readInt())
                .create();
    }
}
