package com.example.emotioncomprehension;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.concurrent.ExecutionException;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
// ... other imports

public class verification extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private ImageAnalysis imageAnalysis;
    private TextView txtPrompt;
    private boolean faceProcessed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Button btnContinue=(Button)findViewById(R.id.btnContinue);
        previewView = findViewById(R.id.previewView);  // Assume you have a PreviewView in your layout

        txtPrompt=(TextView)findViewById(R.id.waitPrompt);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
//
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageAnalysis.clearAnalyzer();

            }
        });
    }
    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Handle any errors
                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageAnalysis = new ImageAnalysis.Builder().build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new YourAnalyzer());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with camera access
                startCamera();
            } else {
                // Permission denied, show an explanatory UI to the user
                Toast.makeText(this, "Camera permission is required for facial verification.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class YourAnalyzer implements ImageAnalysis.Analyzer {

        @Override
        public void analyze(ImageProxy imageProxy) {
            if (faceProcessed) {
                imageProxy.close();
                return;
            }
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                detectSmile(image, imageProxy);
            }
        }
    }

    private void detectSmile(InputImage image, ImageProxy imageProxy) {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        FaceDetector faceDetector = FaceDetection.getClient(options);

        faceDetector.process(image)
                .addOnSuccessListener(faces -> {
                    if (faces.size()==1) {
                        // Set the flag to true as a face is being processed.
                        faceProcessed = true;
                        float smileProb = faces.get(0).getSmilingProbability();
                        if (smileProb != android.media.FaceDetector.Face.CONFIDENCE_THRESHOLD) {
                            txtPrompt.setText("Analyzing....");
                            if (smileProb>0.65){
                                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
                                imageProxy.close();
                                openAccepted();
                            }else{
                                Toast.makeText(this, "Could not detect smile", Toast.LENGTH_SHORT).show();
                                imageProxy.close();
                                openRejected();
                            }
                        } else {
                            Toast.makeText(this, "Could not detect smile", Toast.LENGTH_SHORT).show();
                            imageProxy.close();
                            openRejected();
                        }
                    } else {
                        Toast.makeText(this, "Recalculating...", Toast.LENGTH_SHORT).show();
                    }
                    imageProxy.close();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
                    imageProxy.close();
                    openError();

                });
    }

    public void openAccepted(){
        Intent intent =new Intent(this,Success.class);
        startActivity(intent);
    }

    public void openRejected(){
        Intent intent =new Intent(this,denied.class);
        startActivity(intent);
    }

    public void openError(){
        Intent intent =new Intent(this,error.class);
        startActivity(intent);
    }


}
