package com.g4s8.libcam.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.g4s8.libcam.Camera;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final BroadcastReceiver receiver =
        new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                MainActivity.this.imageView.setImageBitmap(
                    BitmapFactory.decodeFile(
                        MainActivity.this.image.getAbsolutePath()
                    )
                );
            }
        };

    private File image;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imageView = ImageView.class.cast(findViewById(R.id.image));
        this.image = new File(getDir("photos", MODE_PRIVATE), "photo");

        setSupportActionBar(Toolbar.class.cast(findViewById(R.id.toolbar)));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("callback"));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new Camera(MainActivity.this, "com.g4s8.libcam.example")
                        .takePhoto(
                            new Intent("callback"),
                            MainActivity.this.image
                        );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
