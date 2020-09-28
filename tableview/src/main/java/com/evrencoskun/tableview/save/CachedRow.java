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
import android.util.SparseArray;

/**
 * Created by evrencoskun on 4.03.2018.
 */

public class CachedRow implements Parcelable {

    private int row;
    private SparseArray<Object> cellRows;

    public CachedRow(int row, SparseArray<Object> cellRows) {
        this.row = row;
        this.cellRows = cellRows;
    }

    protected CachedRow(Parcel in) {
        row = in.readInt();
        cellRows = in.readSparseArray(CachedRow.class.getClassLoader());
    }

    public static final Creator<CachedRow> CREATOR = new Creator<CachedRow>() {
        @Override
        public CachedRow createFromParcel(Parcel in) {
            return new CachedRow(in);
        }

        @Override
        public CachedRow[] newArray(int size) {
            return new CachedRow[size];
        }
    };

    public int getRow() {
        return row;
    }

    public SparseArray<Object> getCellRows() {
        return cellRows;
    }

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
        dest.writeInt(row);
        dest.writeSparseArray(cellRows);
    }
}
