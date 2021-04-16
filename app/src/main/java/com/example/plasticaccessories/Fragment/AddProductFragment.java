package com.example.plasticaccessories.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plasticaccessories.ProductsDetails;
import com.example.plasticaccessories.BottomNavigationActivity;
//import com.example.plasticaccessories.ProductsDetails;
import com.example.plasticaccessories.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment
{

    Button btn_select, btn_upload,btn_add;
    ImageView image_show;
    EditText edt_ProductName, edt_ProductType, edt_Price;
    String doc_id;
    private Uri filePath;
    Context ctx;

    int PICK_IMAGE_REQUEST = 10;

    FirebaseFirestore firestoreDB;
    FirebaseStorage mStorage;
    StorageReference rootReference;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_product_frag_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_ProductName = view.findViewById(R.id.edtProductName);
        edt_ProductType = view.findViewById(R.id.edtProductType);
        edt_Price = view.findViewById(R.id.edtPrice);
        btn_add = view.findViewById(R.id.btnAdd);
        btn_select = view.findViewById(R.id.btnSelectImage);
        image_show = view.findViewById(R.id.imgShow);

        //Initialization
        firestoreDB = FirebaseFirestore.getInstance();

        //storage initialization
        mStorage = FirebaseStorage.getInstance();

        rootReference = mStorage.getReference();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strProductName = edt_ProductName.getText().toString();
                String  strProductType = edt_ProductType.getText().toString();
                Float fltPrice = Float.parseFloat(edt_Price.getText().toString());

                ProductsDetails ad = new ProductsDetails(strProductName,strProductType,fltPrice);

                //reset values on Edit Text
                edt_ProductName.setText("");
                edt_ProductType.setText("");
                edt_Price.setText("");

                //move cursor on First Edit Text
                edt_ProductName.requestFocus();

                //Insertion
                firestoreDB.collection("PRODUCT_DETAILS").add(ad.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ctx,"Data Added",Toast.LENGTH_LONG).show();
                        doc_id = documentReference.getId();
                        uploadImage();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx,"Fail...",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            //get the Uri of data
            filePath = data.getData();
            try {
                //setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(),filePath);
                image_show.setImageBitmap(bitmap);
            }
            catch (IOException e){
                //log the exception
                e.printStackTrace();
            }
        }
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image from here..."),PICK_IMAGE_REQUEST);
    }

    private void uploadImage()
    {
        ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference fileRef = rootReference.child(UUID.randomUUID().toString() + ".jpg");

        fileRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                //file download url get
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String get_url;
                        get_url = String.valueOf(Log.d("DOWNLOAD URL : ",uri.toString()));
                        firestoreDB.collection(get_url).get();
                    }
                });

                Toast.makeText(ctx,"Upload Successfully...",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ctx,"Uploading Fail...",Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded" + " " +(int)progress + "%");
            }
        });
    }
}
