package com.gf.BugManagerMobile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * bug注释的每一项
 * Created by GuLang on 2015-05-17.
 */
public class BugIntroduceItem implements Parcelable {
    private static final String TAG = "BugIntroduceItem";

    private String type;
    private String name;
    private String time;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeString(content);
    }

    public static Creator<BugIntroduceItem> CREATOR = new Creator<BugIntroduceItem>() {
        @Override
        public BugIntroduceItem createFromParcel(Parcel parcel) {
            BugIntroduceItem bugIntroduceItem = new BugIntroduceItem();
            bugIntroduceItem.setType(parcel.readString());
            bugIntroduceItem.setName(parcel.readString());
            bugIntroduceItem.setTime(parcel.readString());
            bugIntroduceItem.setContent(parcel.readString());
            return bugIntroduceItem;
        }

        @Override
        public BugIntroduceItem[] newArray(int size) {
            return new BugIntroduceItem[size];
        }
    };

    @Override
    public String toString() {
        return "BugIntroduceItem{" + "type='" + type + '\'' + ", name='" + name + '\'' + ", time='" + time + '\''
            + ", content='" + content + '\'' + '}';
    }
}
