package com.loopedlabs.util;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

public class FileUtils {
	public static String readAssetFileAsString(Context context,
                                               String filename) {
		String sFile = "";
		if (filename != null) {
			try {
				InputStream is = context.getAssets().open(filename);
				 sFile = IOUtils.toString(is,"UTF-8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sFile;
	}

	public static byte[] readAssetFileAsByteArray(Context context,
											   String filename) {
		byte[] b = null;
		if (filename != null) {
			try {
				InputStream is = context.getAssets().open(filename);
				b = IOUtils.toByteArray(is);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return b;
	}

	public static String readFile(File f) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		byte[] bytes = new byte[(int) raf.length()];
		raf.readFully(bytes);
		raf.close();
		return new String(bytes);
	}

	public static byte[] readFileAsByteArray(File f) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		byte[] bytes = new byte[(int) raf.length()];
		raf.readFully(bytes);
		raf.close();
		return bytes;
	}

	public static void writeFile(File installation) throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString().replace("-", "");
		out.write(id.getBytes());
		out.close();
	}
}
