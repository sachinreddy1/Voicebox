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

package com.evrencoskun.tableview.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public interface ITableAdapter<T, CH, RH, C> {

    int getTimelineItemViewType(int position);

    int getColumnHeaderItemViewType(int position);

    int getRowHeaderItemViewType(int position);

    int getCellItemViewType(int position);

    View getCornerView();

    @NonNull
    AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable C cellItemModel, int columnPosition, int rowPosition);

    @NonNull
    AbstractViewHolder onCreateTimelineViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindTimelineViewHolder(@NonNull AbstractViewHolder holder, @Nullable T timelineItemModel, int columnPosition);

    @NonNull
    AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable CH columnHeaderItemModel, int columnPosition);

    @NonNull
    AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RH rowHeaderItemModel, int rowPosition);

    @NonNull
    View onCreateCornerView(@NonNull ViewGroup parent);

    ITableView getTableView();

    /**
     * Sets the listener for changes of data set on the TableView.
     *
     * @param listener The AdapterDataSetChangedListener listener.
     */
    void addAdapterDataSetChangedListener(@NonNull AdapterDataSetChangedListener<T, CH, RH, C> listener);
}
