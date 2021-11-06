package com.thknight.torch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {
    private ImageView button, on;
    private CameraManager cameraManager;
    private String id;
    private final int REAR=0;
    private final int FRONT=1;
    private boolean state = false;
    private SwitchMaterial switchMaterial;
    private TextView textView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        on = findViewById(R.id.on);
        switchMaterial = findViewById(R.id.front);
        textView = findViewById(R.id.textView);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            id = cameraManager.getCameraIdList()[FRONT];
            cameraManager.setTorchMode(id, false);


            System.out.println(id);
        }
        catch (IllegalArgumentException | CameraAccessException f){
            switchMaterial.setClickable(false);
            textView.setText(R.string.front_not_available);


        }




        button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (switchMaterial.isChecked()){
                    try {
                        id = cameraManager.getCameraIdList()[FRONT];


                    }
                    catch (Exception e){
                        e.printStackTrace();


                    }

                }
                else{
                    try {
                        id = cameraManager.getCameraIdList()[REAR];

                    }
                    catch (CameraAccessException e){
                        e.printStackTrace();
                    }


                }





                if (state == true) {
                    try {

                        cameraManager.setTorchMode(id, false);
                        button.setImageResource(R.drawable.onflash);
                        on.setImageResource(0);
                        switchMaterial.setChecked(false);

                        state = false;

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                else try {
                    if (id.equals("0")){
                        switchMaterial.setClickable(false);

                    }
                    else if(isFrontAvailable()) {
                        switchMaterial.setClickable(true);


                    }

                    cameraManager.setTorchMode(id, true);
                    button.setImageResource(R.drawable.offflash);
                    on.setImageResource(R.drawable.on);




                    state = true;
                    switchMaterial.setClickable(true);



                } catch (IllegalArgumentException | CameraAccessException e) {

                    e.printStackTrace();
                }


            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isFrontAvailable(){
        try {
            id = cameraManager.getCameraIdList()[FRONT];
            cameraManager.setTorchMode(id, false);
            return true;


        }
        catch (IllegalArgumentException | CameraAccessException f){
            switchMaterial.setClickable(false);
            textView.setText(R.string.front_not_available);
            return false;


        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void finish() {
        super.finish();
        try {
            cameraManager.setTorchMode(id, false);
            state = false;
        } catch ( CameraAccessException e) {
            e.printStackTrace();
        }
    }
}