package com.auto.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import org.apache.logging.log4j.LogManager;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class IOUtils
{

	private IOUtils()
	{
		// empty
	}

	/**
	 * Creates a new directory in a directory
	 * @param strBaseDirectoryName The directory in which the new directory will be created
	 * @param strDirectoryName Name of the directory to be created
	 * @return <code>true</code> if the directory is successfully created else <code>false</code>
	 **/
	public static File createDirectory(String strBaseDirectoryName, String strDirectoryName)
	{
		File fBaseDirectory = new File(strBaseDirectoryName);
		File directory = null;
		if (fBaseDirectory.exists() && fBaseDirectory.isDirectory())
		{
			directory = new File(fBaseDirectory.getAbsolutePath() + File.separator + strDirectoryName);
			if (!directory.exists())
			{
				directory.mkdir();
			}
		}
		return directory;
	}

	/**
	 * Loads a properties file and returns properties object for the same
	 * @param propertiesFile Name of the file which is to be loaded
	 * @return Properties object
	 * @throws IOException if an error occurred when reading from the <code>propertiesFile</code>
	 */
	public static Properties loadProperties(String propertiesFile)
	{
		Properties properties = new Properties();
		try (FileInputStream fInputStream = new FileInputStream(propertiesFile))
		{
			properties.load(fInputStream);
		}
		catch (IOException e)
		{
			LogManager.getRootLogger().error("Unable to read poperty file " + ExceptionUtil.stackTraceToString(e));
		}
		return properties;
	}

	/**
	 * Loads a properties file from a xml file and returns properties object for the same
	 * @param propertiesFile Name of the file which is to be loaded
	 * @return Properties object
	 */
	public static Properties loadPropertiesFromXML(String propertiesFile)
	{
		Properties properties = new Properties();
		try
		{
			properties.loadFromXML(new FileInputStream(propertiesFile));
		}
		catch (IOException e)
		{
			LogManager.getRootLogger()
				.error("Unable to read property file " + ExceptionUtil.stackTraceToString(e));
		}
		return properties;
	}

	/**
	 * Read the passed file name line by line
	 * @param fileName - The name of file, which need to parsed
	 * @return - The <code>List of string</code>, in this each object will define one line of file.
	 */
	public static List<String> readTextLinesInFile(String fileName)
	{
		List<String> textLinesInFile = new ArrayList<String>();
		if (fileName != null && fileName.length() > 0)
		{
			File filetoBeRead = new File(fileName);
			if (!filetoBeRead.exists())
			{
				LogManager.getRootLogger().error("The file " + fileName + " can not be found. ");
			}

			try (BufferedReader reader = new BufferedReader(new FileReader(filetoBeRead)))
			{
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					if (line.trim().length() > 0)
					{
						textLinesInFile.add(line);
					}
				}
			}
			catch (IOException ex)
			{
				LogManager.getRootLogger()
					.error("Unable to read file " + filetoBeRead + " " + ExceptionUtil.stackTraceToString(ex));
			}
		}
		return textLinesInFile;
	}

	/**
	 * Read Rich Text Document (.rtf) and return the content of the document
	 * @param strAbsoluteFilePath
	 * @return String - content of file
	 */
	public static String readRTFFile(String strAbsoluteFilePath)
	{
		String strContent = null;
		try (FileInputStream stream = new FileInputStream(strAbsoluteFilePath))
		{
			RTFEditorKit kit = new RTFEditorKit();
			Document doc = kit.createDefaultDocument();
			kit.read(stream, doc, 0);
			strContent = doc.getText(0, doc.getLength());
		}
		catch (IOException | BadLocationException e)
		{
			LogManager.getRootLogger().error(ExceptionUtil.stackTraceToString(e));
		}
		return strContent;
	}

	/**
	 * Read PDF file and return the content of the specified page
	 * @param strAbsoluteFilePath
	 * @param nPageNumber
	 * @return String - content of the page
	 */
	public static String readPDFFile(String strAbsoluteFilePath, int nPageNumber)
	{
		if (!strAbsoluteFilePath.endsWith("pdf"))
		{
			throw new IllegalArgumentException(
				"The specified file '" + strAbsoluteFilePath + "' is not a PDF file");
		}

		PdfReader pdfReader;
		String strPageContent = "";
		try
		{
			pdfReader = new PdfReader(strAbsoluteFilePath);
			strPageContent = PdfTextExtractor.getTextFromPage(pdfReader, nPageNumber);
			pdfReader.close();
		}
		catch (IOException ex)
		{
			LogManager.getRootLogger().error("Unable to read pdf file " + strAbsoluteFilePath, ex);
		}
		return strPageContent;
	}
}
