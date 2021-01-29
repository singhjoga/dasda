package com.thetechnovator.common.java.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class XmlUtil {
	public static org.dom4j.Document getDom4jDocument(String filePath) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(filePath);
			return document;
		} catch (DocumentException e) {
			throw new IllegalStateException(e);
		}
	}

	public static List<Node> searchXPath(Document document, String searchPath, String... namespaces) {
		Map<String, String> namespaceUris = new HashMap<String, String>();
		int i = 0;
		if (namespaces != null) {
			for (String nsString : namespaces) {
				namespaceUris.put("ns" + i, nsString);
				i++;
			}
		}
		XPath xPath = DocumentHelper.createXPath(searchPath);
		xPath.setNamespaceURIs(namespaceUris);
		return xPath.selectNodes(document);
	}
}
