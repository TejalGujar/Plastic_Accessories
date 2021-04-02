package com.example.plasticaccessories.Fragment;

import android.Manifest;
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

import com.example.plasticaccessories.AddProduct;
import com.example.plasticaccessories.BottomNavigationActivity;
import com.example.plasticaccessories.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment
{

    Button btn_select, btn_upload,btn_add;
    ImageView image_show;
    EditText edt_ProductName, edt_ProductType, edt_Price;
    private Uri filePath;

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

        edt_ProductName = container.findViewById(R.id.edtProductName);
        edt_ProductType = container.findViewById(R.id.edtProductType);
        edt_Price = container.findViewById(R.id.edtPrice);


        btn_add = container.findViewById(R.id.btnAdd);

        //Initialization
        firestoreDB = FirebaseFirestore.getInstance();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strProductName = edt_ProductName.getText().toString();
                String  strProductType = edt_ProductType.getText().toString();
                Float fltPrice = Float.parseFloat(edt_Price.getText().toString());

                AddProduct ad = new AddProduct();

                //reset values on Edit Text
                edt_ProductName.setText("");
                edt_ProductType.setText("");
                edt_Price.setText("");

                //move cursor on First Edit Text
                edt_ProductName.requestFocus();

                //Insertion
                firestoreDB.collection("PATIENT_DETAILS").add(ad.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddProductFragment.this,"Data Added",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductFragment.this,"Fail...",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //get runtime permission from user
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        //storage initialization
        mStorage = FirebaseStorage.getInstance();
        rootReference = mStorage.getReference();

        btn_select = container.findViewById(R.id.btnSelectImage);
        btn_upload = container.findViewById(R.id.btnUploadImage);

        image_show = container.findViewById(R.id.imgShow);

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image from here..."),PICK_IMAGE_REQUEST);
    }

    private void uploadImage()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
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
                        Log.d("DOWNLOAD URL",uri.toString());
                    }
                });

                Toast.makeText(AddProductFragment.this,"Upload Successfully...",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddProductFragment.this,"Uploading Fail...",Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded" + " " +(int)progress + "%");
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                image_show.setImageBitmap(bitmap);
            }
            catch (IOException e){
                //log the exception
                e.printStackTrace();
            }
        }
    }
}
