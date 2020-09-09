package com.dbr.simplebarcodereader;
import java.io.*;
import java.util.Scanner;
import com.dynamsoft.barcode.*;

class BarcodeFormatSet{
	int barcodeFormatIds;
	int barcodeFormatIds_2;
	public BarcodeFormatSet(int barcodeFormatIds,int barcodeFormatIds_2) {
		this.barcodeFormatIds = barcodeFormatIds;
		this.barcodeFormatIds_2 = barcodeFormatIds_2;
	}
}


public class SimpleBarcodeReader {
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
	
	public static BarcodeFormatSet SetBarcodeFormat() {
		int index = 0;
		int ilen = 0;
		BarcodeFormatSet bfSet = null;
		while(true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("\r\n>> Step 2: Choose a number for the format(s) of your barcode image:\r\n");
			System.out.println("   1: All");
			System.out.println("   2: OneD");
			System.out.println("   3: QR Code");
			System.out.println("   4: Code 39");
			System.out.println("   5: Code 128");
			System.out.println("   6: Code 93");
			System.out.println("   7: Codabar");
			System.out.println("   8: Interleaved 2 of 5");
			System.out.println("   9: Industrial 2 of 5");
			System.out.println("   10: EAN-13");
			System.out.println("   11: EAN-8");
			System.out.println("   12: UPC-A");
			System.out.println("   13: UPC-E");
			System.out.println("   14: PDF417");
			System.out.println("   15: DATAMATRIX");
			System.out.println("   16: AZTEC");
			System.out.println("   17: Code 39 Extended");
			System.out.println("   18: Maxicode");
			System.out.println("   19: GS1 Databar");
			System.out.println("   20: PatchCode");
			System.out.println("   21: GS1 Composite");
			System.out.println("   22: Postal  Code");
			System.out.println("   23: DotCode");
			String command = scanner.nextLine();
			ilen = command.length();
			if(ilen>0) {
				if(ilen == 1 &&(command.charAt(0)=='q' || command.charAt(0)=='Q')) {
					break;
				}
				index = Integer.parseInt(command);
				if(index>0 && index<24) {
					bfSet = GetBarcodeFormat(index);
					break;
				}				
			}
			scanner.close();
			System.out.println("Please input a valid number.\r\n");
		}
		return bfSet;
	}
	
	public static BarcodeFormatSet GetBarcodeFormat(int iIndex) {
		BarcodeFormatSet bfSet = null;
		switch (iIndex) {
		case 1: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_ALL,EnumBarcodeFormat_2.BF2_POSTALCODE | EnumBarcodeFormat_2.BF2_DOTCODE);
			break;
		}
		case 2: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_ONED,0);
			break;
		}
		case 3: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_QR_CODE,0);
			break;
		}
		case 4: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_CODE_39,0);
			break;
		}
		case 5: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_CODE_128,0);
			break;
		}
		case 6: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_CODE_93,0);
			break;
		}
		case 7: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_CODABAR,0);
			break;
		}
		case 8: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_ITF,0);
			break;
		}
		case 9: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_INDUSTRIAL_25,0);
			break;
		}
		case 10: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_EAN_13,0);
			break;
		}
		case 11: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_EAN_8,0);
			break;
		}
		case 12: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_UPC_A,0);
			break;
		}
		case 13: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_UPC_E,0);
			break;
		}
		case 14: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_PDF417,0);
			break;
		}
		case 15: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_DATAMATRIX,0);
			break;
		}
		case 16: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_AZTEC,0);
			break;
		}
		case 17: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_CODE_39_EXTENDED,0);
			break;
		}
		case 18: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_MAXICODE,0);
			break;
		}
		case 19: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_GS1_DATABAR,0);
			break;
		}
		case 20: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_PATCHCODE,0);
			break;
		}
		case 21: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat.BF_GS1_COMPOSITE,0);
			break;
		}
		case 22: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat_2.BF2_POSTALCODE,0);
			break;
		}
		case 23: {
			bfSet =new BarcodeFormatSet(EnumBarcodeFormat_2.BF2_DOTCODE,0);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + iIndex);
		}
		return bfSet;
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
		System.out.println("Hints: Please input 'Q'or 'q' to quit the application.\r\n");
		BarcodeFormatSet iBarcodeFormatId = new BarcodeFormatSet(0,0);
		String pImagePath = "";
		BarcodeReader reader = null;
		try {
			reader = new BarcodeReader();
			reader.initLicense("t0068MgAAALgMCB1ptfnl2upO5r/MTbeK5Up/OjRJrAAJyHsow0McTfev+uPY3e3fT1APfeTeVbzkgb0lN9Xj7Wvc+hD7W8c=");
		}
		catch(Exception ex) {
			System.out.println(ex.toString());
		}
		while(true) {
			pImagePath = GetImagePath();
			if (pImagePath.equals("q")||pImagePath.equals("Q")) {
				break;
			}				
//			iBarcodeFormatId = SetBarcodeFormat();
//			if(iBarcodeFormatId == null) {
//				break;
//			}
			try {
				PublicRuntimeSettings runtimeSettings =  reader.getRuntimeSettings();
				runtimeSettings.expectedBarcodesCount = 1;
//				runtimeSettings.intermediateResultSavingMode = EnumIntermediateResultSavingMode.IRSM_BOTH;
//				runtimeSettings.intermediateResultTypes = 16383;
//				runtimeSettings.barcodeFormatIds = iBarcodeFormatId.barcodeFormatIds;
//				runtimeSettings.barcodeFormatIds_2 = iBarcodeFormatId.barcodeFormatIds_2;
				reader.updateRuntimeSettings(runtimeSettings);
				long startTime = System.currentTimeMillis();
				TextResult[] result =  reader.decodeFile(pImagePath, "");
				long endTime  = System.currentTimeMillis();
				long diffTime = endTime - startTime;
				OutputResult(result,diffTime);
			} catch (BarcodeReaderException e) {
				int errorCode = e.getErrorCode();
				System.out.println("failed to read barcode  errorCode:" +errorCode+"\n");
			}
		}
	}

}
