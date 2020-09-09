package com.dbr.decodevideoframe;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import com.dynamsoft.barcode.BarcodeReader;
import com.dynamsoft.barcode.BarcodeReaderException;
import com.dynamsoft.barcode.EnumConflictMode;
import com.dynamsoft.barcode.EnumImagePixelFormat;
import com.dynamsoft.barcode.ErrorCallback;
import com.dynamsoft.barcode.FrameDecodingParameters;
import com.dynamsoft.barcode.TextResult;
import com.dynamsoft.barcode.TextResultCallback;



public class DecodeVideoFrame {
	static{
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    }
	
	public static String ToHexString(String string) {
	    String result = "";
	    for (int i = 0; i < string.length(); i++) {
	        int ch = (int) string.charAt(i);
	        String hex = Integer.toHexString(ch);
	        result = result + hex;
	    }
	    return result;
	}
	
	public static void OutputResult(TextResult[] result) {
		if(result.length==0) {
			System.out.println("No barcode found.");
		}
		else {
			System.out.println("Total barcode(s) found:"+ result.length);
			for(int iIndex = 0; iIndex <result.length;iIndex++) {
				System.out.println("Barcode "+(iIndex+1)+":\n");
				if(result[iIndex].barcodeFormat !=0 ) {
					System.out.println("    Type: "+result[iIndex].barcodeFormatString);
				}
				else {
					System.out.println("    Type: "+result[iIndex].barcodeFormatString_2);
				}
				System.out.println("    Value: "+result[iIndex].barcodeText);
				String hexString =  ToHexString(result[iIndex].barcodeText);
				System.out.println("    Hex Value: "+hexString+"\n");
			}
		}
		return;
	}
	
	public static void main(String[] args) {
		Mat frame = new Mat();
		System.out.println("Opening camera...");
		VideoCapture capture = new VideoCapture();
		try {
			capture.open(0);
		} catch (Exception e) {
			System.out.println("ERROR: Can't initialize camera capture");
			return;
		}
		
		if(!capture.isOpened()) {
			System.out.println("ERROR: Can't initialize camera capture");
			return;
		}
		try {
			BarcodeReader reader = new BarcodeReader("t0068MgAAAKh+3x+Y3EFBqOMBGnRw9mTIc0hZOnBzJdCzjaUPgEAXqffqQhW5179bBIImwWKqpHhU0Gz1OM1fOAYCq32DT1Y=");
			reader.initRuntimeSettingsWithString("{\"ImageParameter\":{\"Name\":\"Balance\",\"DeblurLevel\":5,\"ExpectedBarcodesCount\":512,\"LocalizationModes\":[{\"Mode\":\"LM_CONNECTED_BLOCKS\"},{\"Mode\":\"LM_STATISTICS\"}]}}",EnumConflictMode.CM_OVERWRITE);
			reader.setTextResultCallback(new TextResultCallback() {
			    @Override
			    public void textResultCallback(int frameId, TextResult[] results, Object userData) {
			    	System.out.println("frame:  "+frameId);
					OutputResult(results);
			    }
			}, null);
			reader.setErrorCallback(new ErrorCallback() {
			    @Override
			    public void errorCallback(int frameId, int errorCode, Object userData) {
			    	System.out.println("frame:  "+frameId);
			    	if(errorCode!=0) {
			    		System.out.println("error code:  "+errorCode);
			    	}
			    }
			}, null);
			capture.read(frame);
			FrameDecodingParameters parameters;
			parameters = reader.initFrameDecodingParameters();
			parameters.width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
			parameters.height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
			parameters.maxQueueLength = 30;
			parameters.maxResultQueueLength = 30;
			parameters.stride = (int) frame.step1(0);
			parameters.imagePixelFormat = EnumImagePixelFormat.IPF_RGB_888;
			parameters.region.regionMeasuredByPercentage = 1;
			parameters.region.regionTop = 0;
			parameters.region.regionBottom = 100;
			parameters.region.regionLeft = 0;
			parameters.region.regionRight = 100;
			parameters.threshold =(float) 0.01;
			parameters.fps = 0;
			reader.startFrameDecodingEx(parameters, "");
			ImageGui gui = new ImageGui(frame,"Image");
			gui.imshow();
			for(;;) {
				capture.read(frame);
				if(frame.empty()) {
					System.out.println("ERROR: Can't grab camera frame.");
					break;
				}
				int length = (int)(frame.total()*frame.elemSize());
				byte data[] = new byte[length];
				frame.get(0, 0,data);
				reader.appendFrame(data);
				gui.Changeframe(frame);
			}
			reader.stopFrameDecoding();
		} catch (BarcodeReaderException e) {
			System.out.println("init barcode reader failed, error code: "+e.getErrorCode());
			return;
		}
	}

	

}
