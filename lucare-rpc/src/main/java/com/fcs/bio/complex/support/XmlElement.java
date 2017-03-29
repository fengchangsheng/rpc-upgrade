package com.fcs.bio.complex.support;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class XmlElement {

    private static final String DEFAULT_ENCODING_CHARSET = "UTF-8";

    private Element e;

    private XmlElement(Element element) {
        this.e = element;
    }

    /**
     * 创建xml
     *
     * @param rootName
     * @return
     */
    public static XmlElement newXml(String rootName) {
        try {
            DocumentBuilder dombuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dombuilder.newDocument();
            doc.setXmlStandalone(true);
            Element root = doc.createElement(rootName);
            doc.appendChild(root);
            return new XmlElement(root);
        } catch (Exception e) {
            throw new XmlException(e.getMessage(), e);
        }
    }

    /**
     * 读取xml
     *
     * @param is
     * @return 根元素
     */
    public static XmlElement read(InputStream is) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            docBuilder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    if (systemId.contains("http://java.sun.com/dtd/web-app_2_3.dtd")) {
                        return new InputSource(new StringReader(""));
                    } else {
                        return null;
                    }
                }
            });
            Document doc = docBuilder.parse(is);
            Element root = doc.getDocumentElement();
            return new XmlElement(root);
        } catch (Exception e) {
            throw new XmlException(e.getMessage(), e);
        }
    }

    /**
     * 读取xml
     *
     * @param xmlFile 文件绝对路径
     * @return 根元素
     */
    public static XmlElement read(String xmlFile) {
        InputStream is = null;
        try {
            return read(new FileInputStream(xmlFile));
        } catch (Exception e) {
            throw new XmlException(e.getMessage(), e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * 获取当前元素的Element, 返回的是 org.w3c.dom.Element, 不推荐使用
     *
     * @return org.w3c.dom.Element
     */
    @Deprecated
    public Element getElement() {
        return e;
    }

    /**
     * 获取当前元素的标识名
     *
     * @return
     */
    public String getTagName() {
        return e.getTagName();
    }

    /**
     * @return
     */
    public String getText() {
        return e.getTextContent();
    }

    /**
     * @return
     */
    public int getInt() {
        return getInt(0);
    }

    /**
     * @return
     */
    public int getInt(int def) {
        return CommonUtils.isEmpty(e.getTextContent()) ? def : Integer.valueOf(e.getTextContent().trim());
    }

    /**
     * 获取当前元素的属性值
     *
     * @param attributeName 属性名
     * @return
     */
    public String attributeValue(String attributeName) {
        return e.getAttribute(attributeName);
    }


    /**
     * 获取子元素Element, 返回的是 org.w3c.dom.Element, 不推荐使用
     *
     * @param subName
     * @return org.w3c.dom.Element
     */
    @Deprecated
    public Element getChildElement(String subName) {
        NodeList nodeList = e.getElementsByTagName(subName);
        if ((nodeList == null) || (nodeList.getLength() < 1)) {
            return null;
        }
        return (Element) nodeList.item(0);
    }


    /**
     * 是否存子元素
     *
     * @param subName 元素名
     * @return
     */
    public boolean existChildElement(String subName) {
        return getChildElement(subName) != null;
    }


    /**
     * 获取子元素
     *
     * @param subName
     * @return
     */
    public XmlElement element(String subName) {
        Element element = getChildElement(subName);
        return new XmlElement(element);
    }


    /**
     * 获取子元素的text内容
     *
     * @param subName 子元素名
     * @return
     */
    public String elementText(String subName) {
        Element element = getChildElement(subName);
        return element == null ? null : element.getTextContent().trim();
    }


    /**
     * 获取子元素的text内容
     *
     * @param subName
     * @param def     如果不存在该子节点, 则返回def
     * @return
     */
    public String elementText(String subName, String def) {
        Element element = getChildElement(subName);
        return element == null ? def : element.getTextContent().trim();
    }


    /**
     * @param subName
     * @return
     */
    public int elementInt(String subName) {
        return elementInt(subName, 0);
    }


    /**
     * @param subName
     * @param def
     * @return
     */
    public int elementInt(String subName, int def) {
        String text = elementText(subName);
        return CommonUtils.isEmpty(text) ? def : Integer.valueOf(text.trim());
    }


    /**
     * @return
     */
    public List<XmlElement> getChildNodes() {
        List<XmlElement> list = new ArrayList<XmlElement>();
        NodeList nodeList = e.getChildNodes();
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    list.add(new XmlElement(element));
                }
            }
        }
        return list;
    }


    /**
     * @param subName
     * @return List<XmlElement> 不会为null
     */
    public List<XmlElement> elements(String subName) {
        List<XmlElement> list = new ArrayList<XmlElement>();
        NodeList nodeList = e.getElementsByTagName(subName);
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    list.add(new XmlElement(element));
                }
            }
        }
        return list;
    }


    /**
     * @param name
     * @return
     */
    public XmlElement addElement(String name) {
        Document document = e.getOwnerDocument();
        Element element = document.createElement(name);
        e.appendChild(element);
        return new XmlElement(element);
    }


    /**
     * @param name
     * @param value
     * @return
     */
    public XmlElement addElement(String name, String value) {
        Document document = e.getOwnerDocument();
        Element element = document.createElement(name);
        e.appendChild(element);
        Text text = document.createTextNode(value);
        element.appendChild(text);
        return new XmlElement(element);
    }


    /**
     * 添加或修改属性
     *
     * @param name
     * @param value
     * @return
     */
    public XmlElement setAttribute(String name, String value) {
        e.setAttribute(name, value);
        return this;
    }


    /**
     * @param subNode
     */
    public void remove(XmlElement subNode) {
        e.removeChild(subNode.getElement());
    }


    /**
     * @param name
     */
    public void removeElement(String name) {
        NodeList nodeList = e.getElementsByTagName(name);
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                e.removeChild(nodeList.item(i));
            }
        }
    }


    /**
     * @param name
     */
    public void removeAttribute(String name) {
        e.removeAttribute(name);
    }


    /**
     * @param name
     * @param value
     * @return
     */
    public XmlElement updateElementText(String name, String value) {
        Element element = (Element) e.getElementsByTagName(name).item(0);
        Node textNode = element.getFirstChild();
        textNode.setNodeValue(value);
        return new XmlElement(element);
    }


    /**
     * @param value
     * @return
     */
    public XmlElement updateElementText(String value) {
        Node textNode = e.getFirstChild();
        textNode.setNodeValue(value);
        return this;
    }


    /**
     * @return
     */
    public String getElementText() {
        Node textNode = e.getFirstChild();
        return textNode == null ? null : textNode.getNodeValue();
    }


    /**
     * 获取节点的int值, 如果不存在节点值, 返回0
     *
     * @return
     */
    public int getElementInt() {
        return getElementInt(0);
    }


    /**
     * 获取节点的int值, 如果不存在节点值, 返回def
     *
     * @param def
     * @return
     */
    public int getElementInt(int def) {
        String text = getElementText();
        return text == null ? def : Integer.valueOf(text);
    }


    /**
     * @param os
     */
    public void write(OutputStream os) {
        write(os, DEFAULT_ENCODING_CHARSET);
    }


    /**
     * @param xmlFile
     * @throws XmlException
     */
    public void write(String xmlFile) throws XmlException {
        write(xmlFile, DEFAULT_ENCODING_CHARSET);
    }


    /**
     * @param xmlFile
     * @param encoding
     * @throws XmlException
     */
    public void write(String xmlFile, String encoding) throws XmlException {
        try {
            OutputStream os = new FileOutputStream(xmlFile);
            write(os, encoding);
            os.close();
        } catch (Exception e) {
            throw new XmlException(e.getMessage(), e);
        }
    }


    /**
     * @param os
     * @param encoding
     */
    public void write(OutputStream os, String encoding) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setAttribute("indent-number", 2);
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(new DOMSource(e.getOwnerDocument()), new StreamResult(new OutputStreamWriter(os)));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     */
    public void printNodes() {
        NodeList nodeList = e.getChildNodes();
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println("节点名: " + node.getNodeName() + ", 节点值: " + node.getNodeValue() + ", 节点类型: " + node.getNodeType());
            }
        }
    }

}


class XmlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public XmlException(String message){
        super(message);
    }

    public XmlException(String message, Throwable cause){
        super(message, cause);
    }
}