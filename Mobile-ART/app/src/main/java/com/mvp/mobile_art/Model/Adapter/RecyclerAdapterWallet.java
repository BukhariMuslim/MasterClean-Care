package com.TA.MVP.appmobilemember.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.TA.MVP.appmobilemember.MasterCleanApplication;
import com.TA.MVP.appmobilemember.Model.Array.ArrayWallet;
import com.TA.MVP.appmobilemember.Model.Basic.Wallet;
import com.TA.MVP.appmobilemember.R;
import com.TA.MVP.appmobilemember.View.Activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zackzack on 08/07/2017.
 */

public class RecyclerAdapterWallet extends RecyclerView.Adapter<RecyclerAdapterWallet.ViewHolder> {
    private List<Wallet> wallets = new ArrayList<>();
//    private ArrayWallet wallets = new ArrayWallet();

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemnominal, itemprice;

        public ViewHolder(final View itemview){
            super(itemview);
            itemnominal = (TextView) itemview.findViewById(R.id.card_wallet_nominal);
            itemprice = (TextView) itemview.findViewById(R.id.card_wallet_price);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Toast.makeText(itemview.getContext(),"Clicking card number " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_wallet,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemnominal.setText(String.format(Locale.getDefault(),"Rp. %d", wallets.get(position).getAmt()));
        holder.itemprice.setText(String.format(Locale.getDefault(),"Rp. %d", wallets.get(position).getPrice()));
//        holder.itemnominal.setText(String.format(Locale.getDefault(),"Rp. %d",wallets.getWallets().get(position).getAmt()));
//        holder.itemprice.setText(String.format(Locale.getDefault(),"Rp. %d",wallets.getWallets().get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        if (wallets == null)
            return 0;
        else
            return wallets.size();
    }

    public void setWallets(List<Wallet> wallets){
        this.wallets = wallets;
        notifyDataSetChanged();
    }
}