package com.thetechnovator.common.java.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thetechnovator.common.java.StringProperties;
import com.thetechnovator.common.java.exceptions.BusinessException;

public class FileUtil {
	private static Logger LOG = LoggerFactory.getLogger(FileUtil.class);
	private static String VERSION_REGEX = "-\\d+\\.\\d+\\.\\d+.*";
	private static Pattern VERSION_PATTERN = Pattern.compile(VERSION_REGEX);
	public static FileFilter SOURCE_ZIP_FILE_FILTER = new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			String name = file.getName();
			if (name.startsWith(".") || name.startsWith("target") || name.endsWith(".zip") || name.endsWith(".gz")) {
				return false;
			}
			return true;
		}
	};
	public static String findVersionInFileName(String fileName) {
		// First find a starting with -<digits>.<digits>.<digits>

		// Remove extension
		String namePart = fileName.substring(0, fileName.lastIndexOf("."));
		Matcher matcher = VERSION_PATTERN.matcher(namePart);
		if (matcher.find()) {
			return namePart.substring(matcher.start() + 1);
		}

		return null;
	}

	public static Properties readPropertyFile(String file) {
		return readPropertyFile(file, new Properties());
	}

	public static Properties readPropertyFile(String file, Properties props) {
		File propFile = new File(file);
		if (!propFile.exists()) {
			return null;
		}
		FileInputStream fis;
		try {
			if (props == null) {
				props = new Properties();
			}
			fis = new FileInputStream(propFile);
			props.load(fis);
			fis.close();
			return props;
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Reads a key-value file where the values are separated with '='. Value is the
	 * part after the first '=' As compared to readPropertyFile method, it allows
	 * white space in the key part.
	 * 
	 * @param file
	 * @return
	 */
	public static Properties readKeyValueFile(File file) {
		Properties props = new Properties();
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			String str;

			while ((str = in.readLine()) != null) {
				String key = StringUtils.substringBefore(str, "=");
				String value = StringUtils.substringAfter(str, "=");
				props.setProperty(key, value);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		return props;
	}

	public static void makeOrCleanDir(File dir) throws BusinessException {
		try {
			if (dir.exists()) {
				FileUtils.cleanDirectory(dir);
			}
			FileUtils.forceMkdir(dir);
			if (!dir.exists()) {
				throw new BusinessException("Could not create working directory: " + dir.getAbsolutePath());
			}

		} catch (IOException e) {
			throw new BusinessException("Error cleaning directory: " + dir.getAbsolutePath());
		}
	}

	public static List<String> readLinesFromClasspathResource(String fileName) {
		return readLinesFromClasspathResource(fileName, true);
	}

	public static List<String> readLinesFromClasspathResource(String fileName, boolean skipCommentLines) {
		BufferedReader in;
		try {
			in = getInputStemFromClasspathResource(fileName);
			String str;
			List<String> lines = new ArrayList<String>();
			while ((str = in.readLine()) != null) {
				if (skipCommentLines) {
					if (str.trim().startsWith("#")) {
						continue;
					}
				}
				lines.add(str);
			}
			in.close();
			return lines;
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	public static String readFileAsStringFromClasspath(String fileName) throws IOException{
		BufferedReader br = getInputStemFromClasspathResource(fileName);
		StringWriter writer = new StringWriter();
		IOUtils.copy(br, writer);
		closeQuietly(br);
		return writer.toString();
	}
	public static BufferedReader getInputStemFromClasspathResource(String fileName) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			throw new IOException("Resource file " + fileName + " could not be loaded");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
		return br;
	}
	public static InputStream getInputStreamFromClasspathResource(String fileName) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			throw new IOException("Resource file " + fileName + " could not be loaded");
		}
		
		return is;
	}
	public static int compareVersions(String version1, String version2) {
		Long v1 = toNumericVersion(version1);
		Long v2 = toNumericVersion(version2);

		return v1.compareTo(v2);
	}

	public static long toNumericVersion(String versionStr) {
		StringBuffer sf = new StringBuffer();
		for (String s : StringUtils.split(versionStr, ".")) {
			if (StringUtils.isNumeric(s)) {
				sf.append(StringUtils.leftPad(s, 4, "0"));
			} else {
				LOG.error("Nnn numeric version found in " + versionStr);
			}
		}
		String newStr = sf.toString();
		if (newStr.length() > 0) {
			return Long.parseLong(newStr);
		} else {
			LOG.error("Not valid numeric version found in " + versionStr);
			return 0;
		}
	}

	public static boolean replacePlaceholders(File file, StringProperties properties) {
		StringBuilder missingRefs = new StringBuilder();
		return replacePlaceholders(file, properties, missingRefs);
	}

	public static boolean replacePlaceholders(File file, StringProperties properties, StringBuilder missingRefs) {
		try {
			File temp = File.createTempFile("placeholder", "tmp");
			boolean retValue = replacePlaceholders(file, properties, temp, missingRefs);
			FileUtils.copyFile(temp, file);
			temp.delete();

			return retValue;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public static boolean replacePlaceholders(File file, StringProperties properties, File outputFile,
			StringBuilder missingRefs) {
		BufferedReader in = null;
		BufferedWriter out = null;
		boolean retvalue = true;
		try {
			out = new BufferedWriter(new FileWriter(outputFile));
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				StringBuilder sb = new StringBuilder();
				boolean ok = PropertyUtil.replacePlaceholders(str, properties, sb, missingRefs);
				if (!ok) {
					retvalue = false;
				}
				out.append(sb.toString() + System.lineSeparator());
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			closeQuietly(in);
			closeQuietly(out);
		}
		return retvalue;
	}

	public static void closeQuietly(Closeable closable) {
		if (closable == null)
			return;
		try {
			closable.close();
		} catch (IOException e) {
			// Nothing. this method is mean to suppress the exception
		}
	}

	public static File copyFileFromClassPathToTemp(Class<?> cls, String classPath) throws IOException {
		URL inputUrl = cls.getResource(classPath);
		if (inputUrl == null) {
			throw new IOException("Classpath resource not found: " + classPath);
		}
		File dest = File.createTempFile("classpathfile", ".tmp");
		FileUtils.copyURLToFile(inputUrl, dest);

		return dest;
	}

	public static File[] findFiles(File dir, String nameStartsWith, String ext) {
		File[] files = dir.listFiles((folder, name) -> name.startsWith(nameStartsWith) && name.endsWith("." + ext));
		return files;
	}

	public static String readFileAsString(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static void setFilesAsExecutable(File dir) {
		File[] files = dir.listFiles();
		for (File f: files) {
			f.setExecutable(true);
		}
	}
}
