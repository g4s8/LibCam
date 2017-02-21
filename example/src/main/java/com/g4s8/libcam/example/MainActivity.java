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
import com.g4s8.libcam.Authority;
import com.g4s8.libcam.Camera;
import com.g4s8.libcam.IntentUri;
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
    private IntentUri intentUri;
    private ImageView imageView;

    private static final String KEY_URI = "uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imageView = ImageView.class.cast(findViewById(R.id.image));
        this.image = new File(getDir("photos", MODE_PRIVATE), "photo");

        if (savedInstanceState != null) {
            this.intentUri = IntentUri.class.cast(
                savedInstanceState.getParcelable(KEY_URI)
            );
        }

        setSupportActionBar(Toolbar.class.cast(findViewById(R.id.toolbar)));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("callback"));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Intent intent = new Camera(getApplicationContext())
                        .intent(new Authority("com.g4s8.libcam.example"));
                    intentUri = new IntentUri(intent);
                    startActivityForResult(intent, 42);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putParcelable(KEY_URI, this.intentUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == 42 && resultCode == RESULT_OK) {
            imageView.setImageURI(intentUri.uri());
        }
    }
}
