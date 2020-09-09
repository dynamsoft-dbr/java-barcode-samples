package com.dbr.fastreadbarcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.dynamsoft.barcode.BarcodeReader;
import com.dynamsoft.barcode.BarcodeReaderException;
import com.dynamsoft.barcode.EnumBarcodeFormat;
import com.dynamsoft.barcode.EnumConflictMode;
import com.dynamsoft.barcode.EnumDPMCodeReadingMode;
import com.dynamsoft.barcode.PublicRuntimeSettings;
import com.dynamsoft.barcode.TextResult;

public class FastReadBarcode {
	
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
	
	public static String GetImagePath() {
		String sCommand = "";
		int iLen = 0;
		while(true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("\r\n>> Step 1: Input your image file's full path:\r\n");
			sCommand = scanner.nextLine();
			iLen = sCommand.length();
			if(iLen > 0) {
				if(iLen == 1 && (sCommand.charAt(0) == 'q' || sCommand.charAt(0) == 'Q')) {
					break;
				}
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(sCommand);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Please input a valid path.\r\n");
					continue;
				}
				
				int iAvail = 0;
	            try {
	            	iAvail = inputStream.available();
	            	inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            if(iAvail != 0) {
	            	break;
	            }
			}
			System.out.println("Please input a valid path.\r\n");
		}
		return sCommand;
	}
	
	public static void main(String[] args) {
		System.out.println("*************************************************\r\n");
		System.out.println("Welcome to Dynamsoft Barcode Reader Demo\r\n");
		System.out.println("*************************************************\r\n");
		System.out.println("Hints: Please input 'Q'or 'q' to quit the application.\r\n");
		try {
			BarcodeReader reader = new BarcodeReader("t0068MgAAAKh+3x+Y3EFBqOMBGnRw9mTIc0hZOnBzJdCzjaUPgEAXqffqQhW5179bBIImwWKqpHhU0Gz1OM1fOAYCq32DT1Y=");
			while(true) {
				String pImagePath = GetImagePath();
				if (pImagePath.equals("q")||pImagePath.equals("Q")) {
					break;
				}
				//DPM settings with json file
				String projectRoot = System.getProperty("user.dir");
				String templatePath = projectRoot + "\\resource\\FastReadBarcode.json";
				reader.initRuntimeSettingsWithFile(templatePath, EnumConflictMode.CM_OVERWRITE);
				
				long startTime = System.currentTimeMillis();
				TextResult[] result =  reader.decodeFile(pImagePath, "");
				long endTime  = System.currentTimeMillis();
				long diffTime = endTime - startTime;
				OutputResult(result,diffTime);
			}		
		} catch (BarcodeReaderException e) {
			System.out.println("Error code: "+e.getErrorCode());
		}
	}

}
