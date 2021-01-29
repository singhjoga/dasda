package com.thetechnovator.common.java.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtil {
	private static final Logger LOG = LoggerFactory.getLogger(ZipUtil.class);

	public static void unzip(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				if (!folder.mkdir()) {
					throw new IllegalStateException("Cannot create output folder: " + outputFolder);
				}
			}

			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				// File name contains platform specific path separator. Change them to current
				// platform specific
				if (File.separator.equals("/")) {
					fileName = StringUtils.replace(fileName, "\\", "/");
				} else {
					fileName = StringUtils.replace(fileName, "/", "\\");
				}
				LOG.info("Extracting " + fileName);
				File newFile = new File(outputFolder + File.separator + fileName);
				File parentFolder = new File(newFile.getParent());

				// create sub folders
				if (!parentFolder.exists() && !parentFolder.mkdirs()) {
					throw new IllegalStateException("Could not create parent folder" + parentFolder.getAbsolutePath());
				}
				if (!ze.isDirectory()) {
					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

		} catch (IOException ex) {
			throw new IllegalStateException("Cannot unzip file: " + zipFile + " " + ex.getMessage());
		}
	}

	/**
	 * Zips a collection of files to a destination zip output stream.
	 *
	 * @param files
	 *            A collection of files and directories
	 * @param outputStream
	 *            The output stream of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void zipFiles(List<File> files, OutputStream outputStream) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(outputStream);

		for (File file : files) {
			if (file.isDirectory()) { // if it's a folder
				addFolderToZip("", file, zos);
			} else {
				addFileToZip("", file, zos);
			}
		}

		zos.finish();
	}

	/**
	 * Adds a directory to the current zip
	 *
	 * @param path
	 *            the path of the parent folder in the zip
	 * @param folder
	 *            the directory to be added
	 * @param zos
	 *            the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void addFolderToZip(String path, File folder, ZipOutputStream zos) throws IOException {
		String currentPath = StringUtils.isNotEmpty(path) ? path + "/" + folder.getName() : folder.getName();

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				addFolderToZip(currentPath, file, zos);
			} else {
				addFileToZip(currentPath, file, zos);
			}
		}
	}

	/**
	 * Adds a file to the current zip output stream
	 *
	 * @param path
	 *            the path of the parent folder in the zip
	 * @param file
	 *            the file to be added
	 * @param zos
	 *            the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void addFileToZip(String path, File file, ZipOutputStream zos) throws IOException {
		String currentPath = StringUtils.isNotEmpty(path) ? path + "/" + file.getName() : file.getName();

		zos.putNextEntry(new ZipEntry(currentPath));

		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			IOUtils.copy(is, zos);
		} finally {
			FileUtil.closeQuietly(is);
		}
		zos.closeEntry();
	}

	/**
	 * Zips a collection of files to a destination zip file.
	 *
	 * @param files
	 *            A collection of files and directories
	 * @param zipFile
	 *            The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void zipFiles(List<File> files, File zipFile) throws IOException {
		OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFile));
		try {
			zipFiles(files, os);
		} finally {
			FileUtil.closeQuietly(os);
		}
	}

	
	public static void zipFiles(File dir, File zipFile) throws IOException {
		OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFile));
		try {
			zipFiles(Arrays.asList(dir.listFiles()), os);
		} finally {
			FileUtil.closeQuietly(os);
		}
	}
	
	/**
	 * Unzips a zip from an input stream into an output folder.
	 *
	 * @param inputStream
	 *            the zip input stream
	 * @param outputFolder
	 *            the output folder where the files
	 * @throws IOException
	 */
	public static void unZipFiles(InputStream inputStream, File outputFolder) throws IOException {
		ZipInputStream zis = new ZipInputStream(inputStream);
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			File file = new File(outputFolder, ze.getName());
			OutputStream os = new BufferedOutputStream(FileUtils.openOutputStream(file));

			try {
				IOUtils.copy(zis, os);
			} finally {
				FileUtil.closeQuietly(os);
			}

			zis.closeEntry();
			ze = zis.getNextEntry();
		}
	}

	/**
	 * Unzips a zip file into an output folder.
	 *
	 * @param zipFile
	 *            the zip file
	 * @param outputFolder
	 *            the output folder where the files
	 * @throws IOException
	 */
	public static void unZipFiles(File zipFile, File outputFolder) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(zipFile));
		try {
			unZipFiles(is, outputFolder);
		} finally {
			FileUtil.closeQuietly(is);
		}
	}

}