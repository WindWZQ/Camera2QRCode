package com.wzq.scan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ScanView extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    // param
    private String mCameraId;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private CameraCaptureSession mCaptureSession;

    // 回调
    private ScanCallback mScanCallback;

    // view
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceViewHolder;

    // camera参数
    private boolean isFlashSupport;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
    }

    public void init() {
        initView();
        initCamera();
    }

    private void initView() {
        LogUtil.e(TAG, "initView");
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceViewHolder = mSurfaceView.getHolder();
        mSurfaceViewHolder.addCallback(surfaceCallback);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(640, 480);
        addView(mSurfaceView, layoutParams);
    }

    private void initCamera() {
        LogUtil.e(TAG, "initCamera");
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

        if (null != mCameraManager) {
            try {
                // 获取可用摄像头列表
                for (String cameraId : mCameraManager.getCameraIdList()) {
                    // 获取相机的相关参数
                    CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);

                    // 寻找后置摄像头。
                    Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        mCameraId = cameraId;

                        // 检查闪光灯是否支持
                        Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        isFlashSupport = available == null ? false : available;
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                backError(ScanErrorEnum.CAMERA_OPEN_FAIL);
            }
        } else {
            backError(ScanErrorEnum.CAMERA_NOT_SUPPORT);
        }
    }

    @SuppressLint("MissingPermission")
    public void openCamera() {
        LogUtil.e(TAG, "openCamera");

        try {
            //打开相机预览
            if (!TextUtils.isEmpty(mCameraId)) {
                mCameraManager.openCamera(mCameraId, mStateCallback, null);
            } else {
                backError(ScanErrorEnum.CAMERA_BACK_CAMERA_NOT_FOUND);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            backError(ScanErrorEnum.CAMERA_OPEN_FAIL);
        }
    }

    // surface回调
    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtil.e(TAG, "surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtil.e(TAG, "surfaceChanged");
            openCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtil.e(TAG, "surfaceDestroyed");
        }
    };

    // 相机状态回调
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            LogUtil.e(TAG, "StateCallback onOpened");
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            LogUtil.e(TAG, "StateCallback onDisconnected");
            cameraDevice.close();
            mCameraDevice = null;
            backError(ScanErrorEnum.CAMERA_CLOSE);
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            LogUtil.e(TAG, "StateCallback onError");
            cameraDevice.close();
            mCameraDevice = null;
            backError(ScanErrorEnum.CAMERA_OPEN_FAIL);
        }
    };

    // 开启session，预览
    private void createCameraPreviewSession() {
        LogUtil.e(TAG, "createCameraPreviewSession");
        try {
            //设置了一个具有输出Surface的CaptureRequest.Builder。
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(mSurfaceViewHolder.getSurface());

            List<Surface> surfaceList = new ArrayList<>();
            surfaceList.add(mSurfaceViewHolder.getSurface());

            //创建一个CameraCaptureSession来进行相机预览。
            mCameraDevice.createCaptureSession(surfaceList, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    LogUtil.e(TAG, "createCameraPreviewSession onConfigured");
                    // 会话准备好后，我们开始显示预览
                    mCaptureSession = cameraCaptureSession;
                    try {
                        // 自动对焦应
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 开启相机预览并添加事件
                        mPreviewRequest = mPreviewRequestBuilder.build();
                        //发送请求
                        mCaptureSession.setRepeatingRequest(mPreviewRequest, null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        backError(ScanErrorEnum.CAMERA_PREVIEW_FAIL);
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    LogUtil.e(TAG, "createCameraPreviewSession onConfigureFailed");
                    backError(ScanErrorEnum.CAMERA_PREVIEW_FAIL);
                }
            }, null);
        } catch (NullPointerException | CameraAccessException e) {
            e.printStackTrace();
            backError(ScanErrorEnum.CAMERA_PREVIEW_FAIL);
        }
    }

    // 设置回调
    public void setCallback(ScanCallback scanCallback) {
        this.mScanCallback = scanCallback;
    }

    // 返回错误
    private void backError(ScanErrorEnum scanErrorEnum) {
        if (null != mScanCallback) {
            mScanCallback.onFail(scanErrorEnum);
        }
    }

    // 关闭
    public void closeCamera() {
        if (null != mCameraDevice) {
            mCameraDevice.close();
        }
    }

}
