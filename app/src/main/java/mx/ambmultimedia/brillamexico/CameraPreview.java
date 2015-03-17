package mx.ambmultimedia.brillamexico;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Activity atx;

    private Boolean inPreview = false;
    private int currentCameraId;

    public CameraPreview (Activity activity, Context context, Camera camera) {
        super(context);
                atx = activity;
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            //mCamera.setDisplayOrientation(90);
            setCameraDisplayOrientation(atx, currentCameraId, mCamera);
            inPreview = true;
        } catch (IOException e) { }

        currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    public void surfaceDestroyed (SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){ return; }
        try {
            mCamera.stopPreview();
            inPreview = false;
        } catch (Exception e){ }

        /**
         * Aquí las modificaciones pertinentes
         * para cambiar el preview de la cámara
         */

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            inPreview = true;
        } catch (Exception e){ }
    }

    public void PausePreview () {
        mCamera.stopPreview();
        inPreview = false;
    }

    public void ResumePreview () {
        mCamera.startPreview();
        inPreview = true;
    }

    public void flipCamera () {
        if (inPreview) {
            mCamera.stopPreview();
            inPreview = false;
        }
        mCamera.release();

        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mCamera = Camera.open(currentCameraId);

        setCameraDisplayOrientation(atx, currentCameraId, mCamera);
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        inPreview = true;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
