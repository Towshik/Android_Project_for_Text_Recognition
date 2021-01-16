package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button captureImageBtn,detectTextBtn,historyBtn;
    private ImageView imageView;
    private TextView textView;
    private Bitmap imageBitmap;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captureImageBtn= findViewById(R.id.capture_image);
        detectTextBtn=findViewById(R.id.detect_text);
        historyBtn=findViewById(R.id.history);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dispatchTakePictureIntent();
            }
        });
        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE=1;

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebasevisionimage =FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebasevisiontextdetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebasevisiontextdetector.detectInImage(firebasevisionimage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
               displayTextFromImage(firebaseVisionText);
               //textView.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("Error: ",e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText){

        List<FirebaseVisionText.Block> blockList=firebaseVisionText.getBlocks();
        if(blockList.size()==0){
            Toast.makeText(this,"No Text Found in Image",Toast.LENGTH_SHORT).show();
        }
        else{
            for(FirebaseVisionText.Block block :firebaseVisionText.getBlocks()){
                text=block.getText();
                textView.setText(text);
            }
        }
    }
    public void History(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        intent.putExtra("message_key", text);
        startActivity(intent);

    }

}