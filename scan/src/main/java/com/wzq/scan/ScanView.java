package com.wzq.scan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ScanView extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    // param
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private ScanCallback mScanCallback;

    // view
    private TextureView mTextureView;

    // camera参数
    private boolean isFlashSupport;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        initView();
        initCamera();
        openCamera();
    }

    private void initView() {
        mTextureView = new TextureView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mTextureView, layoutParams);
    }

    private void initCamera() {
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            //获取可用摄像头列表
            for (String cameraId : mCameraManager.getCameraIdList()) {
                LogUtil.e(TAG, "cameraId:" + cameraId);

                //获取相机的相关参数
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                // 不使用前置摄像头。
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                LogUtil.e(TAG, "facing: " + facing.toString());

                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                }
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                LogUtil.e(TAG, map.toString());
                if (map == null) {
                }
                // 检查闪光灯是否支持。
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                isFlashSupport = available == null ? false : available;
            }
        } catch (NullPointerException | CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void openCamera() {
        try {
            //打开相机预览
            mCameraManager.openCamera("0", mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (null != mCameraDevice) {
            mCameraDevice.close();
        }
    }

    // 相机状态回调
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            mCameraDevice = null;
        }
    };

    // todo 开启session，预览
    private void createCameraPreviewSession() {
//        try {
//            //设置了一个具有输出Surface的CaptureRequest.Builder。
//            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            mCaptureRequestBuilder.addTarget(mSurfaceHolder.getSurface());
//            //创建一个CameraCaptureSession来进行相机预览。
//            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface()),
//                    new CameraCaptureSession.StateCallback() {
//
//                        @Override
//                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//                            // 相机已经关闭
//                            if (null == mCameraDevice) {
//                                return;
//                            }
//                            // 会话准备好后，我们开始显示预览
//                            mCaptureSession = cameraCaptureSession;
//                            try {
//                                // 自动对焦应
//                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//                                // 闪光灯
//                                setAutoFlash(mPreviewRequestBuilder);
//                                // 开启相机预览并添加事件
//                                mPreviewRequest = mPreviewRequestBuilder.build();
//                                //发送请求
//                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
//                                        null, mBackgroundHandler);
//                                Log.e(TAG, " 开启相机预览并添加事件");
//                            } catch (CameraAccessException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
//                            Log.e(TAG, " onConfigureFailed 开启预览失败");
//                        }
//                    }, null);
//        } catch (CameraAccessException e) {
//            Log.e(TAG, " CameraAccessException 开启预览失败");
//            e.printStackTrace();
//        }
    }

    // 设置回调
    private void setCallback(ScanCallback scanCallback) {
        this.mScanCallback = scanCallback;
    }

    // 返回错误
    private void backError(ScanErrorEnum scanErrorEnum) {
        if (null != mScanCallback) {
            mScanCallback.onFail(scanErrorEnum);
        }
    }

}
