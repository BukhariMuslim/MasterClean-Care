package com.TA.MVP.appmobilemember.Model.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.TA.MVP.appmobilemember.View.Activity.WalletActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Zackzack on 08/07/2017.
 */

public class RecyclerAdapterWallet extends RecyclerView.Adapter<RecyclerAdapterWallet.ViewHolder> {
    private List<Wallet> wallets = new ArrayList<>();
    protected AlertDialog.Builder abuilder;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemnominal;
        public String nominal;

        public ViewHolder(final View itemview){
            super(itemview);
            itemnominal = (TextView) itemview.findViewById(R.id.card_wallet_nominal);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    nominal = setRP(wallets.get(position).getAmt());
                    abuildermessage("Request pembelian wallet "+nominal,"Konfirmasi Request", itemview.getContext());
                    abuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do request transaction trs type = 3
                            //admin nnti ubah 3 ke 1
                        }
                    });
                    abuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    showalertdialog();
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
        holder.itemnominal.setText(setRP(wallets.get(position).getAmt()));
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
    public void abuildermessage(String msg, String title, Context context){
        abuilder = new AlertDialog.Builder(context);
        abuilder.setMessage(msg).setTitle(title);
    }
    public void showalertdialog(){
        AlertDialog dialog = abuilder.create();
        if (abuilder == null)
            throw new NullPointerException("null builder");
        else
            dialog.show();
    }
    public String setRP(Integer number){
        String tempp = "Rp. ";
        tempp = tempp + numberFormat.format(number) + ".00";
        return tempp;
    }
}
