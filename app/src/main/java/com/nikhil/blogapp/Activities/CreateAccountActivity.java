package com.nikhil.blogapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nikhil.blogapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName,lastName,email,password;
    private Button createAccountBtn;
    private ImageButton profilePic;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_CODE=1;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MUsers");

        mAuth=FirebaseAuth.getInstance();

        mStorage= FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");

        mProgressDialog=new ProgressDialog(this);

        firstName=(EditText) findViewById(R.id.firstNameAct);
        lastName=(EditText) findViewById(R.id.lastNameAct);
        email=(EditText) findViewById(R.id.emailAct);
        password=(EditText) findViewById(R.id.passwodAct);
        createAccountBtn=(Button) findViewById(R.id.createAct);
        profilePic=(ImageButton) findViewById(R.id.profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {
        final String name=firstName.getText().toString().trim();
        final String lname=lastName.getText().toString().trim();
        String em=email.getText().toString().trim();
        String pwd=password.getText().toString().trim();

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(lname)&&!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(em)){
            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult!=null){
                        StorageReference  filepath=mStorage.child(resultUri.getLastPathSegment());
                        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String profileLink=uri.toString();

                                        String userid=mAuth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDb=mDatabaseReference.child(userid);

//                                        User user=new User(name,lname,profileLink);
//                                        currentUserDb.setValue(user);
                                        currentUserDb.child("firstname").setValue(name);
                                        currentUserDb.child("lastname").setValue(lname);
                                        currentUserDb.child("image").setValue(profileLink);

                                        mProgressDialog.dismiss();

                                        //send user to postlist
                                        Intent intent=new Intent(CreateAccountActivity.this,PostListActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //similar to finish()
                                        startActivity(intent);

                                    }
                                });

                            }
                        });

//                        String userid=mAuth.getCurrentUser().getUid();
//                        DatabaseReference currentUserDb=mDatabaseReference.child(userid);
//                        currentUserDb.child("firstname").setValue(name);
//                        currentUserDb.child("lastname").setValue(lname);
//                        currentUserDb.child("image").setValue("none");
//
//                        mProgressDialog.dismiss();
//
//                        //send user to postlist
//                        Intent intent=new Intent(CreateAccountActivity.this,PostListActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //similar to finish()
//                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_CODE&&resultCode==RESULT_OK){
            Uri mImageURI=data.getData();

            CropImage.activity(mImageURI)
                    .setAspectRatio(1,1)   //for square pic
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    profilePic.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();

            }
        }

    }
}