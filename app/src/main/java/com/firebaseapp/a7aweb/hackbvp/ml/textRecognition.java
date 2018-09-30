package com.firebaseapp.a7aweb.hackbvp.ml;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.IOException;
import java.util.List;

import com.firebaseapp.a7aweb.hackbvp.R;

public class textRecognition extends AppCompatActivity {

    private Button snapBtn;
    private Button detectBtn;
    private ImageView imageView;
    private TextView txtView;
    private Bitmap imageBitmap;
    private String tex = " ";
    private Uri picUri;
    final int PIC_CROP = 2;
    final int CAMERA_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);
        snapBtn = findViewById(R.id.snapBtn);
        detectBtn = findViewById(R.id.detectBtn);
        imageView = findViewById(R.id.imageView);
        txtView = findViewById(R.id.txtView);
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    imageView.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                detectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                                .getOnDeviceTextRecognizer();

                        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);


                        textRecognizer.processImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText result) {
                                        // Task completed successfully
                                        // ...
                                        // progressDialog.dismiss();
                                        String resultText = result.getText();
                                        txtView.setText(resultText);
                                        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
                                            String blockText = block.getText();
                                            Toast.makeText(textRecognition.this, blockText, Toast.LENGTH_LONG).show();


                                            tex = tex.concat(blockText);



                                            Float blockConfidence = block.getConfidence();
                                            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                                            Point[] blockCornerPoints = block.getCornerPoints();
                                            Rect blockFrame = block.getBoundingBox();
                                            for (FirebaseVisionText.Line line : block.getLines()) {
                                                String lineText = line.getText();

                                                Float lineConfidence = line.getConfidence();
                                                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                                                Point[] lineCornerPoints = line.getCornerPoints();
                                                Rect lineFrame = line.getBoundingBox();
                                                for (FirebaseVisionText.Element element : line.getElements()) {
                                                    String elementText = element.getText();

                                                    //Toast.makeText(textRecognition.this, elementText, Toast.LENGTH_LONG).show();
                                                    Float elementConfidence = element.getConfidence();
                                                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                                                    Point[] elementCornerPoints = element.getCornerPoints();
                                                    Rect elementFrame = element.getBoundingBox();
                                                }
                                            }
                                        }

                                        txtView.setText(tex);

                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                Toast.makeText(textRecognition.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}







