package com.randioo.randioo_server_base.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GlobleXmlLoader implements XmlReader {
	/**
	 * 初始化全局变量
	 * 
	 * @param fileName
	 */
	public static void init(String fileName) {
		GlobleXmlLoader loader = new GlobleXmlLoader();
		loader.readXml(fileName);
	}

	/**
	 * 读xml文件
	 * 
	 * @param fileName
	 */
	public void readXml(String fileName) {
		try (FileInputStream inputStream = new FileInputStream(fileName)) {
			readXml(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readXml(InputStream inputStream) {
		SAXReader saxReader = new SAXReader();
		try {
			Document doc = saxReader.read(inputStream);
			Element serverElement = doc.getRootElement();
			Iterator<?> paramIterator = serverElement.elementIterator(ConfigXmlConstant.ELEMENT_PRARM);

			GlobleParameter param = new GlobleParameter();
			while (paramIterator.hasNext()) {
				Element paramElement = (Element) paramIterator.next();
				String type = this.getAttributeValue(paramElement, ConfigXmlConstant.ATTRIBUTE_TYPE);
				String key = this.getAttributeValue(paramElement, ConfigXmlConstant.ATTRIBUTE_KEY);
				String value = this.getElementValue(paramElement);

				param.key = key;
				param.type = GlobleTypeEnum.getGlobleType(type);
				param.value = value;

				GlobleMap.putParam(param);
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private String getAttributeValue(Element element, String attributeName) {
		Attribute attribute = element.attribute(attributeName);
		String value = attribute.getValue();
		return value;
	}

	private String getElementValue(Element element) {
		return element.getStringValue();
	}

}
