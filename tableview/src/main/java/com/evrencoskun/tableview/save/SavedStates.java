/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.save;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by evrencoskun on 3.03.2018.
 */

public class SavedStates implements Parcelable {
    public Parcelable cellLayoutManager;
    public Parcelable columnLayoutManager;
    public Parcelable rowHeaderLayoutManager;
    public Parcelable[] cellRowLayoutManagers;
    public Parcelable[] cellRowCachedWidthList;
    public Parcelable[] columnHeaderCachedWidthList;

    protected SavedStates(Parcel in) {
        cellLayoutManager = in.readParcelable(SavedStates.class.getClassLoader());
        columnLayoutManager = in.readParcelable(SavedStates.class.getClassLoader());
        rowHeaderLayoutManager = in.readParcelable(SavedStates.class.getClassLoader());
        cellRowLayoutManagers = in.readParcelableArray(SavedStates.class.getClassLoader());
        cellRowCachedWidthList = in.readParcelableArray(SavedStates.class.getClassLoader());
        columnHeaderCachedWidthList = in.readParcelableArray(SavedStates.class.getClassLoader());
    }

    public static final Creator<SavedStates> CREATOR = new Creator<SavedStates>() {
        @Override
        public SavedStates createFromParcel(Parcel in) {
            return new SavedStates(in);
        }

        @Override
        public SavedStates[] newArray(int size) {
            return new SavedStates[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable
     * object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link
     *              #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cellLayoutManager, flags);
        dest.writeParcelable(columnLayoutManager, flags);
        dest.writeParcelable(rowHeaderLayoutManager, flags);
        dest.writeParcelableArray(cellRowLayoutManagers, flags);
        dest.writeParcelableArray(cellRowCachedWidthList, flags);
        dest.writeParcelableArray(columnHeaderCachedWidthList, flags);
    }
}
