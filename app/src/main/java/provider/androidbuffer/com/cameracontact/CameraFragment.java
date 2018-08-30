package provider.androidbuffer.com.cameracontact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by incred-dev
 * on 29/8/18.
 */

public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";
    private static final int MY_CAMERA_PERMISSION_CODE = 1000;
    private static final int CAMERA_REQUEST = 100;

    private ImageView camImage;

    static CameraFragment newInstance() {

        return new CameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        camImage = rootView.findViewById(R.id.cam_image);

        Button openCam = rootView.findViewById(R.id.open_cam);
        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndOpenCamera();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap pic = (Bitmap) data.getExtras().get("data");
            camImage.setImageBitmap(pic);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraApp();
            } else {
                Toast.makeText(this.getActivity(), R.string.cam_perm_denied,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkAndOpenCamera() {

        Log.e(TAG, "open camera");

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "permission not granted");

            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE
            );
        } else {
            Log.e(TAG, "permission granted");
            openCameraApp();
        }
    }

    private void openCameraApp() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}
