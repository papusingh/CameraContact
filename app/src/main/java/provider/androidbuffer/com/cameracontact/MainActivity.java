package provider.androidbuffer.com.cameracontact;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mainPager = findViewById(R.id.main_pager);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mainPager.setAdapter(mainAdapter);
    }
}


