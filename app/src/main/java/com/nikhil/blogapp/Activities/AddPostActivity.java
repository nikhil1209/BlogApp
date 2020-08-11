package com.nikhil.blogapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nikhil.blogapp.R;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private ImageButton mPostImage;
    private EditText mPostTitle,mPostdec;
    private Button mSubmitButton;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorage;
    private ProgressDialog mprogress;
    private Uri mImageUri;
    private static final int GALERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mprogress=new ProgressDialog(this);

        mStorage= FirebaseStorage.getInstance().getReference();

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        mPostDatabase=FirebaseDatabase.getInstance().getReference().child("Mblog");

        mPostImage=(ImageButton) findViewById(R.id.profilePic);
        mPostTitle=(EditText) findViewById(R.id.postTitleET);
        mPostdec=(EditText)findViewById(R.id.postDescriptionET);
        mSubmitButton=(Button) findViewById(R.id.submitPost);

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALERY_CODE);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //posting to database
                startPosting();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALERY_CODE&&resultCode==RESULT_OK){
            mImageUri=data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }

    private void startPosting() {

        mprogress.setMessage("Posting to blog...");
        mprogress.show();

//         String dpurl,first="k";
//
//        DatabaseReference path=FirebaseDatabase.getInstance().getReference().child("MUsers").child(mUser.getUid()).child("image");
//        path.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dpurl=snapshot.getValue(String.class);
//                Log.d("fname",dpurl);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        DatabaseReference path1=FirebaseDatabase.getInstance().getReference().child("MUsers").child(mUser.getUid()).child("firstname");
//        path1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                final String first=snapshot.getValue(String.class);
//                Log.d("fname",first);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        final String titleVal=mPostTitle.getText().toString().trim();
        final String descVal=mPostdec.getText().toString().trim();

        if(!TextUtils.isEmpty(titleVal)&&!TextUtils.isEmpty(descVal)&&mImageUri!=null){
            //start the uploading..

            StorageReference filepath=mStorage.child("MBlog_images").child(mImageUri.getLastPathSegment());
//           //mImageUri.getLastPathSegment()== /image/abc.jpg
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    /** check ths **/

//                    String downloadurl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    // for image link
                    Log.d("STRING",taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //this uri contains image link



                            String downloadurl=uri.toString();
                            Log.d("STRINGG",downloadurl);
                            DatabaseReference newPost=mPostDatabase.push();

                            Map<String,String> dataToSave=new HashMap<>();
                            dataToSave.put("title",titleVal);
                            dataToSave.put("desc",descVal);
                            dataToSave.put("image", downloadurl);
                            dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                            dataToSave.put("userid",mUser.getUid());
//                            dataToSave.put("fname",first);
//                            dataToSave.put("dpimgurl",dpurl);

                            newPost.setValue(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"item added",Toast.LENGTH_LONG).show();
                                    mprogress.dismiss();
                                }
                            });

                            startActivity(new Intent(AddPostActivity.this,PostListActivity.class));

                            Log.d("status",uri.toString());
                        }
                    });



//
//                    mprogress.dismiss();

                }
            });


//            Blog blog=new Blog("title","description","userid","time","img");
//            mPostDatabase.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(getApplicationContext(),"item added",Toast.LENGTH_LONG).show();
//                    mprogress.dismiss();
//                }
//            });
        }
    }
}