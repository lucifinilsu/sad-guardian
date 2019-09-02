package com.sad.assistant.live.guardian.api.parameters;

import android.os.Parcel;
import android.os.Parcelable;

public class GuardiaTaskParameters implements IParcelable{

    public static final int NO_SINGLETARGET=996;

    private INotificationStyle notificationStyle;
    private int singleTarget=NO_SINGLETARGET;
    private ActionSource source=ActionSource.LOCAL_WAKE;

    public ActionSource getSource() {
        return source;
    }

    public void setSource(ActionSource source) {
        this.source = source;
    }

    public int getSingleTarget() {
        return singleTarget;
    }

    public void setSingleTarget(int singleTarget) {
        this.singleTarget = singleTarget;
    }

    public INotificationStyle getNotificationStyle() {
        return notificationStyle;
    }

    public void setNotificationStyle(INotificationStyle notificationStyle) {
        this.notificationStyle = notificationStyle;
    }

    public static final Parcelable.Creator<GuardiaTaskParameters> CREATOR = new
            Parcelable.Creator<GuardiaTaskParameters>() {
                public GuardiaTaskParameters createFromParcel(Parcel in) {
                    GuardiaTaskParameters parameters=new GuardiaTaskParameters();
                    parameters.readFromParcel(in);
                    return parameters;
                }

                public GuardiaTaskParameters[] newArray(int size) {
                    return new GuardiaTaskParameters[size];
                }
            };

    @Override
    public void readFromParcel(Parcel in) {
        setNotificationStyle(in.readParcelable(getClass().getClassLoader()));
        setSingleTarget(in.readInt());
        setSource((ActionSource) in.readSerializable());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(getNotificationStyle(),i);
        parcel.writeInt(getSingleTarget());
        parcel.writeSerializable(getSource());
    }
}
