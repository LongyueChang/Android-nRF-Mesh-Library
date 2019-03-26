/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.nrfmeshprovisioner.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.nordicsemi.android.meshprovisioner.Group;
import no.nordicsemi.android.meshprovisioner.MeshNetwork;
import no.nordicsemi.android.meshprovisioner.transport.MeshModel;
import no.nordicsemi.android.meshprovisioner.utils.MeshAddress;
import no.nordicsemi.android.nrfmeshprovisioner.R;
import no.nordicsemi.android.nrfmeshprovisioner.widgets.RemovableViewHolder;

public class GroupAddressAdapter extends RecyclerView.Adapter<GroupAddressAdapter.ViewHolder> {

    private final Context mContext;
    private final MeshNetwork network;
    private final ArrayList<Integer> mAddresses = new ArrayList<>();
    private MeshModel model;

    public GroupAddressAdapter(@NonNull final Context context, @NonNull final MeshNetwork network, @NonNull final LiveData<MeshModel> meshModelLiveData) {
        this.mContext = context;
        this.network = network;
        meshModelLiveData.observe((LifecycleOwner) context, meshModel -> {
            if(meshModel != null) {
                model = meshModel;
                final List<Integer> tempAddresses = meshModel.getSubscribedAddresses();
                if (tempAddresses != null) {
                    mAddresses.clear();
                    mAddresses.addAll(tempAddresses);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @NonNull
    @Override
    public GroupAddressAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View layoutView = LayoutInflater.from(mContext).inflate(R.layout.address_item, parent, false);
        return new GroupAddressAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAddressAdapter.ViewHolder holder, final int position) {
        if(mAddresses.size() > 0) {
            final int address = mAddresses.get(position);
            final Group group = network.getGroup(address);
            if(group != null) {
                holder.icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_group_work_black_alpha_24dp));
                holder.name.setText(group.getName());
                holder.address.setText(MeshAddress.formatAddress(address, true));
            } else {
                if(MeshAddress.isValidVirtualAddress(address)) {
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_label_outline_black_alpha_24dp));
                    holder.name.setText(R.string.label_uuid);
                    holder.address.setText(model.getLabelUUID(address).toString().toUpperCase(Locale.US));
                }
            }
        }
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public final class ViewHolder extends RemovableViewHolder {

        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.address_id)
        TextView name;
        @BindView(R.id.address)
        TextView address;

        private ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
