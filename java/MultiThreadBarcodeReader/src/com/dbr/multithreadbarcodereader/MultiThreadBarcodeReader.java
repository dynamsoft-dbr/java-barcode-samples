package com.dbr.multithreadbarcodereader;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import com.dynamsoft.barcode.BarcodeReader;
import com.dynamsoft.barcode.BarcodeReaderException;
import com.dynamsoft.barcode.TextResult;





class BarcodeReaderThread implements Runnable{
	public static ArrayList<String> imageList;
	public static ArrayList<Boolean> DecodeOrNot;
	public static int totalImageNum;
	public int currentNum;
	public BarcodeReader reader = null;
	public boolean Init() {
		try {
			reader = new BarcodeReader("t0068MgAAAKh+3x+Y3EFBqOMBGnRw9mTIc0hZOnBzJdCzjaUPgEAXqffqQhW5179bBIImwWKqpHhU0Gz1OM1fOAYCq32DT1Y=");
		} catch (Exception ex) {
			return false;
		}
		return true;
	};
	
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
	
	public void run() {
		boolean init = Init();
		if(init) {
			System.out.println("init barcode reader successful\r\n");
			while(true) {
				int currentIdx =  currentNum;
				synchronized(this) {
					if(currentIdx>=totalImageNum) {
						break;
					}
					if(DecodeOrNot.get(currentIdx)) {
						currentNum ++;
						continue;
					}
					else {
						DecodeOrNot.set(currentIdx, true);
					}
				}
				
				try {
					long startTime = System.currentTimeMillis();			
					TextResult[] result = reader.decodeFile(imageList.get(currentIdx), "");
					long endTime  = System.currentTimeMillis();
					long diffTime = endTime - startTime;
					synchronized(this) {
						System.out.println("Current  image number: "+currentIdx);
						System.out.println("Current  image name: "+imageList.get(currentIdx));
						OutputResult(result,diffTime);
					}
				} catch (BarcodeReaderException e) {
					currentNum++;
					System.out.println("Error code: "+e.getErrorCode());
					continue;
				}
			}
		}
		else {
			System.out.println("init barcode reader failed\r\n");
		}
		System.out.println("thread finish");
	}
}

public class MultiThreadBarcodeReader {
	public static String GetSlash() {
		String Slash;
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win"))
			Slash = "\\";
		else
			Slash = "/";
		return Slash;
	}
	
	public static void GetFiles(String filepath, String extension, ArrayList<String> listname) {
		File file = new File(filepath);
		if (!file.isDirectory()) {
			int pos = file.getName().lastIndexOf(".");
			String ext = file.getName().substring(pos).toLowerCase();
			if (extension.indexOf(ext) >= 0)
				listname.add(file.getAbsolutePath());
		} else {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath);
				if (!readfile.isDirectory()) {
					listname.add(readfile.getName());
				} else if (readfile.isDirectory()) {
					GetFiles(filepath + GetSlash() + filelist[i], extension, listname);// recursive
				}
			}
		}
	}
	
	public static String GetImageFolderPath() {
		String sCommand = "";
		int iLen = 0;
		while(true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("\r\n>> Step 1: Input your image folder's full path:\r\n");
			sCommand = scanner.nextLine();
			iLen = sCommand.length();
			if(iLen > 0) {
				if(iLen == 1 && (sCommand.charAt(0) == 'q' || sCommand.charAt(0) == 'Q')) {
					break;
				}
				File file = new File(sCommand);
				if(file.isDirectory()) {
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
		
		while(true) {
			int iThreadCount = 4;
			String imageFolder = "";
			imageFolder = GetImageFolderPath();
			if(imageFolder.equals("Q")||imageFolder.equals("q")) {
				break;
			}
			ArrayList<String> imageList= new ArrayList<String>();
			ArrayList<Boolean> DecodeOrNot= new ArrayList<Boolean>();
			GetFiles(imageFolder, ".bmp .png .jpg .jpeg .pdf .tif .tiff .gif", imageList);		
			BarcodeReaderThread.totalImageNum = imageList.size();
			BarcodeReaderThread.imageList = imageList;
			BarcodeReaderThread.DecodeOrNot = DecodeOrNot;
			for(int i = 0;i<imageList.size();i++) {
				DecodeOrNot.add(false);
			}
			Vector<Thread> threadVector = new Vector<Thread>();
			for(int i = 0;i<iThreadCount;i++) {
				BarcodeReaderThread barcodeReaderThread = new BarcodeReaderThread();
				barcodeReaderThread.currentNum = 0;
				Thread thread = new Thread(barcodeReaderThread);
				threadVector.add(thread);
				thread.start();
			}
			for(Thread thread:threadVector) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

}
