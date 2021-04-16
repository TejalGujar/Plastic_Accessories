package com.example.plasticaccessories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ItemViewHolder>
{

    List<ProductsDetails> pList;
    Context ctx;

    FirebaseFirestore firestoreDB;

    //constructor
    public ProductDetailsAdapter(List<ProductsDetails> pList, Context ctx){
        this.pList = pList;
        this.ctx = ctx;
        this.firestoreDB = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new ItemViewHolder(LayoutInflater.from(ctx).inflate(R.layout.all_products_frag_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ProductsDetails pd = pList.get(position);

        holder.txtpName.setText(pd.getProductName());
        holder.txtpPrice.setText(String.valueOf(pd.getProductPrice()));

    }

    //return number of card items in recycler view
    @Override
    public int getItemCount() {
        return pList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView txtpName, txtpPrice;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);

            this.txtpName = itemView.findViewById(R.id.txtProductName);
            this.txtpPrice = itemView.findViewById(R.id.txtProductPrice);
        }
    }
}
