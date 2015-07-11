package com.esolz.fitnessapp.customcameranew;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * This class assumes the parent layout is RelativeLayout.LayoutParams.
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private static boolean DEBUGGING = true;
	private static final String LOG_TAG = "CameraPreviewSample";
	private static final String CAMERA_PARAM_ORIENTATION = "orientation";
	private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
	private static final String CAMERA_PARAM_PORTRAIT = "portrait";
	protected Activity mActivity;
	private SurfaceHolder mHolder;

	protected Camera mCamera;

	protected List<Camera.Size> mPreviewSizeList;
	protected List<Camera.Size> mPictureSizeList;
	protected Camera.Size mPreviewSize;
	protected Camera.Size mPictureSize;
	private int mSurfaceChangedCallDepth = 0;
	private int mCameraId;
	private LayoutMode mLayoutMode;
	private int mCenterPosX = -1;
	private int mCenterPosY;

	PreviewReadyCallback mPreviewReadyCallback = null;

	PictureCallback rawCallback;
	ShutterCallback shutterCallback;
	PictureCallback jpegCallback;

	public static enum LayoutMode {
		FitToParent, // Scale to the size that no side is larger than the parent
		NoBlank // Scale to the size that no side is smaller than the parent
	};

	public interface PreviewReadyCallback {
		public void onPreviewReady();
	}
	protected boolean mSurfaceConfiguring = false;
	Activity activity;
	byte[] IMAGEB;
	String result = "";
	LinearLayout progrssBar;

	public CameraPreview(final Activity activity, int cameraId,
			LayoutMode mode, LinearLayout progrssBar) {
		super(activity); // Always necessary
		mActivity = activity;
		mLayoutMode = mode;
		this.progrssBar = progrssBar;
		this.activity = activity;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream outStream = null;
				try {
					mCamera.stopPreview();
					IMAGEB = data;
					(new SaveImage()).execute();
					Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			if (Camera.getNumberOfCameras() > cameraId) {
				mCameraId = cameraId;
			} else {
				mCameraId = 0;
			}
		} else {
			mCameraId = 0;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(mCameraId);
		} else {
			mCamera = Camera.open();
		}
		Camera.Parameters cameraParams = mCamera.getParameters();
		mPreviewSizeList = cameraParams.getSupportedPreviewSizes();
		mPictureSizeList = cameraParams.getSupportedPictureSizes();

	}

	public void captureImage() throws IOException {
		// take the picture
		mCamera.takePicture(null, null, jpegCallback);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}

	public String addImage(ContentResolver resolver, int orientation,
			byte[] jpeg, double lati, double longi) {
		String path = String
				.format(Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
						+ "/%d.jpg", System.currentTimeMillis());
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			out.write(jpeg);
			Bitmap bm = BitmapFactory.decodeFile(path);
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, false);
			FileOutputStream fos2 = new FileOutputStream(path);
			rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
			fos2.close();
		} catch (Exception e) {
			Log.e("SAIKAT", "Failed to write image", e);
			return null;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		ContentValues values = new ContentValues(9);
		values.put(ImageColumns.MIME_TYPE, "image/jpeg");
		values.put(ImageColumns.ORIENTATION, orientation);
		values.put(ImageColumns.LATITUDE, lati);
		values.put(ImageColumns.LONGITUDE, longi);
		values.put(ImageColumns.DATA, path);
		values.put(ImageColumns.SIZE, jpeg.length);
		Uri uri = null;
		try {
			uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		} catch (Throwable th) {
			Log.e("SAIKAT", "Failed to write MediaStore" + th);
		}
		return path;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mSurfaceChangedCallDepth++;
		doSurfaceChanged(width, height);
		mSurfaceChangedCallDepth--;
	}

	private void doSurfaceChanged(int width, int height) {
		mCamera.stopPreview();

		Camera.Parameters cameraParams = mCamera.getParameters();
		boolean portrait = isPortrait();
		if (!mSurfaceConfiguring) {
			Camera.Size previewSize = determinePreviewSize(portrait, width,
					height);
			Camera.Size pictureSize = determinePictureSize(previewSize);
			if (DEBUGGING) {
				Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: "
						+ height);
			}
			mPreviewSize = previewSize;
			mPictureSize = pictureSize;
			if (mSurfaceConfiguring && (mSurfaceChangedCallDepth <= 1)) {
				return;
			}
		}

		configureCameraParameters(cameraParams, portrait);
		mSurfaceConfiguring = false;

		try {
			mCamera.startPreview();
		} catch (Exception e) {
			Log.w(LOG_TAG, "Failed to start preview: " + e.getMessage());

			// Remove failed size
			mPreviewSizeList.remove(mPreviewSize);
			mPreviewSize = null;

			// Reconfigure
			if (mPreviewSizeList.size() > 0) {
				surfaceChanged(null, 0, width, height);
			} else {
				Toast.makeText(mActivity, "Can't start preview",
						Toast.LENGTH_LONG).show();
				Log.w(LOG_TAG, "Gave up starting preview");
			}
		}

		if (null != mPreviewReadyCallback) {
			mPreviewReadyCallback.onPreviewReady();
		}
	}

	/**
	 *
	 * @param portrait
	 * @param reqWidth
	 *            must be the value of the parameter passed in surfaceChanged
	 * @param reqHeight
	 *            must be the value of the parameter passed in surfaceChanged
	 * @return Camera.Size object that is an element of the list returned from
	 *         Camera.Parameters.getSupportedPreviewSizes.
	 */
	protected Camera.Size determinePreviewSize(boolean portrait, int reqWidth,
			int reqHeight) {
		int reqPreviewWidth; // requested width in terms of camera hardware
		int reqPreviewHeight; // requested height in terms of camera hardware
		if (portrait) {
			reqPreviewWidth = reqHeight;
			reqPreviewHeight = reqWidth;
		} else {
			reqPreviewWidth = reqWidth;
			reqPreviewHeight = reqHeight;
		}

		if (DEBUGGING) {
			Log.v(LOG_TAG, "Listing all supported preview sizes");
			for (Camera.Size size : mPreviewSizeList) {
				Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
			}
			Log.v(LOG_TAG, "Listing all supported picture sizes");
			for (Camera.Size size : mPictureSizeList) {
				Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
			}
		}

		// Adjust surface size with the closest aspect-ratio
		float reqRatio = ((float) reqPreviewWidth) / reqPreviewHeight;
		float curRatio, deltaRatio;
		float deltaRatioMin = Float.MAX_VALUE;
		Camera.Size retSize = null;
		for (Camera.Size size : mPreviewSizeList) {
			curRatio = ((float) size.width) / size.height;
			deltaRatio = Math.abs(reqRatio - curRatio);
			if (deltaRatio < deltaRatioMin) {
				deltaRatioMin = deltaRatio;
				retSize = size;
			}
		}

		return retSize;
	}

	protected Camera.Size determinePictureSize(Camera.Size previewSize) {
		Camera.Size retSize = null;
		for (Camera.Size size : mPictureSizeList) {
			if (size.equals(previewSize)) {
				return size;
			}
		}

		if (DEBUGGING) {
			Log.v(LOG_TAG, "Same picture size not found.");
		}

		// if the preview size is not supported as a picture size
		float reqRatio = ((float) previewSize.width) / previewSize.height;
		float curRatio, deltaRatio;
		float deltaRatioMin = Float.MAX_VALUE;
		for (Camera.Size size : mPictureSizeList) {
			curRatio = ((float) size.width) / size.height;
			deltaRatio = Math.abs(reqRatio - curRatio);
			if (deltaRatio < deltaRatioMin) {
				deltaRatioMin = deltaRatio;
				retSize = size;
			}
		}

		return retSize;
	}

	protected boolean adjustSurfaceLayoutSize(Camera.Size previewSize,
			boolean portrait, int availableWidth, int availableHeight) {
		float tmpLayoutHeight, tmpLayoutWidth;
		if (portrait) {
			tmpLayoutHeight = previewSize.width;
			tmpLayoutWidth = previewSize.height;
		} else {
			tmpLayoutHeight = previewSize.height;
			tmpLayoutWidth = previewSize.width;
		}

		float factH, factW, fact;
		factH = availableHeight / tmpLayoutHeight;
		factW = availableWidth / tmpLayoutWidth;
		if (mLayoutMode == LayoutMode.FitToParent) {
			// Select smaller factor, because the surface cannot be set to the
			// size larger than display metrics.
			if (factH < factW) {
				fact = factH;
			} else {
				fact = factW;
			}
		} else {
			if (factH < factW) {
				fact = factW;
			} else {
				fact = factH;
			}
		}

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this
				.getLayoutParams();

		int layoutHeight = (int) (tmpLayoutHeight * fact);
		int layoutWidth = (int) (tmpLayoutWidth * fact);
		if (DEBUGGING) {
			Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutWidth + ", h: "
					+ layoutHeight);
			Log.v(LOG_TAG, "Scale factor: " + fact);
		}

		boolean layoutChanged;
		if ((layoutWidth != this.getWidth())
				|| (layoutHeight != this.getHeight())) {
			layoutParams.height = layoutHeight;
			layoutParams.width = layoutWidth;
			if (mCenterPosX >= 0) {
				layoutParams.topMargin = mCenterPosY - (layoutHeight / 2);
				layoutParams.leftMargin = mCenterPosX - (layoutWidth / 2);
			}
			this.setLayoutParams(layoutParams); // this will trigger another
												// surfaceChanged invocation.
			layoutChanged = true;
		} else {
			layoutChanged = false;
		}

		return layoutChanged;
	}

	/**
	 * @param x
	 *            X coordinate of center position on the screen. Set to negative
	 *            value to unset.
	 * @param y
	 *            Y coordinate of center position on the screen.
	 */
	public void setCenterPosition(int x, int y) {
		mCenterPosX = x;
		mCenterPosY = y;
	}

	protected void configureCameraParameters(Camera.Parameters cameraParams,
			boolean portrait) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			if (portrait) {
				cameraParams.set(CAMERA_PARAM_ORIENTATION,
						CAMERA_PARAM_PORTRAIT);
			} else {
				cameraParams.set(CAMERA_PARAM_ORIENTATION,
						CAMERA_PARAM_LANDSCAPE);
			}
		} else { // for 2.2 and later
			int angle;
			Display display = mActivity.getWindowManager().getDefaultDisplay();
			switch (display.getRotation()) {
			case Surface.ROTATION_0: // This is display orientation
				angle = 90; // This is camera orientation
				break;
			case Surface.ROTATION_90:
				angle = 0;
				break;
			case Surface.ROTATION_180:
				angle = 270;
				break;
			case Surface.ROTATION_270:
				angle = 180;
				break;
			default:
				angle = 90;
				break;
			}
			Log.v(LOG_TAG, "angle: " + angle);
			mCamera.setDisplayOrientation(angle);
		}

		cameraParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		if (DEBUGGING) {
			Log.v(LOG_TAG, "Preview Actual Size - w: " + mPreviewSize.width
					+ ", h: " + mPreviewSize.height);
			Log.v(LOG_TAG, "Picture Actual Size - w: " + mPictureSize.width
					+ ", h: " + mPictureSize.height);
		}

		mCamera.setParameters(cameraParams);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stop();
	}

	public void stop() {
		if (null == mCamera) {
			return;
		}
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	public boolean isPortrait() {
		return (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
	}

	public void setOneShotPreviewCallback(PreviewCallback callback) {
		if (null == mCamera) {
			return;
		}
		mCamera.setOneShotPreviewCallback(callback);
	}

	public void setPreviewCallback(PreviewCallback callback) {
		if (null == mCamera) {
			return;
		}
		mCamera.setPreviewCallback(callback);
	}

	public Camera.Size getPreviewSize() {
		return mPreviewSize;
	}

	public void setOnPreviewReady(PreviewReadyCallback cb) {
		mPreviewReadyCallback = cb;
	}

	public class SaveImage extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progrssBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void resultt) {
			// TODO Auto-generated method stub
			super.onPostExecute(resultt);
			// *********Fire Intent
			progrssBar.setVisibility(View.GONE);
			Intent returnIntent = new Intent();
			returnIntent.putExtra("Path", result);
			activity.setResult(6000, returnIntent);
			activity.finish();
			Toast.makeText(activity.getApplicationContext(), "" + result,
					Toast.LENGTH_LONG).show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			result = addImage(activity.getContentResolver(), 1, IMAGEB,
					23.2155, 23.25147).toString();
			return null;
		}

	}

}
