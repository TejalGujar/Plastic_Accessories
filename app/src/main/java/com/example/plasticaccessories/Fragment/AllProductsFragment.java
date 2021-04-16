package com.example.plasticaccessories.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plasticaccessories.ProductDetailsAdapter;
import com.example.plasticaccessories.ProductsDetails;
import com.example.plasticaccessories.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllProductsFragment extends Fragment
{
    FirebaseFirestore firestoreDB;
    Context ctx;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_products_frag_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvpList = view.findViewById(R.id.rvpList);

        //initialization
        firestoreDB = FirebaseFirestore.getInstance();

        firestoreDB.collection("PRODUCT_DETAILS").orderBy("pName", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<ProductsDetails> pd = new ArrayList<>();

                    for (DocumentSnapshot doc:task.getResult()){
                        ProductsDetails pDetails = doc.toObject(ProductsDetails.class);
                        pDetails.setProductId(doc.getId());
                        pd.add(pDetails);
                    }

                    rvpList.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
                    rvpList.setAdapter(new ProductDetailsAdapter(pd,ctx));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx,"Data Show Fail...",Toast.LENGTH_LONG).show();
            }
        });
    }
}
