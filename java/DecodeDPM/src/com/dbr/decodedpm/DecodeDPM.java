package com.dbr.decodedpm;

import com.dynamsoft.barcode.BarcodeReader;
import com.dynamsoft.barcode.BarcodeReaderException;
import com.dynamsoft.barcode.EnumBarcodeFormat;
import com.dynamsoft.barcode.EnumDPMCodeReadingMode;
import com.dynamsoft.barcode.PublicRuntimeSettings;
import com.dynamsoft.barcode.TextResult;

public class DecodeDPM {
	
	public static String ToHexString(String string) {
	    String result = "";
	    for (int i = 0; i < string.length(); i++) {
	        int ch = (int) string.charAt(i);
	        String hex = Integer.toHexString(ch);
	        result = result + hex;
	    }
	    return result;
	}
	
	public static void OutputResult(TextResult[] result,long timeCost) {
		if(result.length==0) {
			System.out.println("No barcode found.Total time spent: "+timeCost+"  millisecond\n");
		}
		else {
			System.out.println("Total barcode(s) found:"+ result.length +" .Total time spent: "+timeCost+"  millisecond\n");
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
		System.out.println("*************************************************\r\n");
		System.out.println("Welcome to Dynamsoft Barcode Reader Demo\r\n");
		System.out.println("*************************************************\r\n");
		System.out.println("Decode DPM.\r\n");
		try {
			BarcodeReader reader = new BarcodeReader("t0068MgAAAKh+3x+Y3EFBqOMBGnRw9mTIc0hZOnBzJdCzjaUPgEAXqffqQhW5179bBIImwWKqpHhU0Gz1OM1fOAYCq32DT1Y=");			
			String projectRoot = System.getProperty("user.dir");
			String pImagePath = projectRoot + "\\resource\\DPM-Sample-1.png";
			
			//DPM settings with json string
			//reader.initRuntimeSettingsWithString("{\"ImageParameter\":{\"Name\":\"DMPSetting\",\"DPMCodeReadingModes\":[\"DPMCRM_GENERAL\"]}}", EnumConflictMode.CM_OVERWRITE);
			
			//DPM settings with runtimeSetting
			PublicRuntimeSettings runtimeSettings =  reader.getRuntimeSettings();
			runtimeSettings.barcodeFormatIds = EnumBarcodeFormat.BF_DATAMATRIX;
			runtimeSettings.furtherModes.dpmCodeReadingModes[0] = EnumDPMCodeReadingMode.DPMCRM_GENERAL;
			reader.updateRuntimeSettings(runtimeSettings);
			long startTime = System.currentTimeMillis();
			TextResult[] result =  reader.decodeFile(pImagePath, "");
			long endTime  = System.currentTimeMillis();
			long diffTime = endTime - startTime;
			OutputResult(result,diffTime);
		} catch (BarcodeReaderException e) {
			System.out.println("Error code: "+e.getErrorCode());
		}
	}

}
