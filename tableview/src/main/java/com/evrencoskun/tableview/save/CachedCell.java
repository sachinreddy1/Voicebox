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
 * Created by evrencoskun on 4.03.2018.
 * "Cell" represents the each of item of Column Header, Row Header and Cells.
 */

public class CachedCell implements Parcelable {
    private int column;
    private int width;
    private boolean constant;

    public CachedCell(int column, int width) {
        this.column = column;
        this.width = width;
    }

    public CachedCell(int column, int width, boolean constant) {
        this.column = column;
        this.width = width;
        this.constant = constant;
    }

    protected CachedCell(Parcel in) {
        column = in.readInt();
        width = in.readInt();
        constant = in.readInt() == 1;
    }

    public static final Creator<CachedCell> CREATOR = new Creator<CachedCell>() {
        @Override
        public CachedCell createFromParcel(Parcel in) {
            return new CachedCell(in);
        }

        @Override
        public CachedCell[] newArray(int size) {
            return new CachedCell[size];
        }
    };

    public int getColumn() {
        return column;
    }

    public int getWidth() {
        return width;
    }

    public boolean isConstant() { return constant;}

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
        dest.writeInt(column);
        dest.writeInt(width);
        dest.writeInt(constant ? 1 : 0);
    }
}
