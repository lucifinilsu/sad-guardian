package com.sad.assistant.live.guardian.api.parameters;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceAidlParameters implements IParcelable{

    private INotificationStyle notificationStyle;

    public INotificationStyle getNotificationStyle() {
        return notificationStyle;
    }

    public void setNotificationStyle(INotificationStyle notificationStyle) {
        this.notificationStyle = notificationStyle;
    }

    public static final Parcelable.Creator<ServiceAidlParameters> CREATOR = new
            Parcelable.Creator<ServiceAidlParameters>() {
                public ServiceAidlParameters createFromParcel(Parcel in) {
                    ServiceAidlParameters parameters=new ServiceAidlParameters();
                    parameters.readFromParcel(in);
                    return parameters;
                }

                public ServiceAidlParameters[] newArray(int size) {
                    return new ServiceAidlParameters[size];
                }
            };

    @Override
    public void readFromParcel(Parcel in) {
        setNotificationStyle(in.readParcelable(getClass().getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(getNotificationStyle(),i);
    }
}
