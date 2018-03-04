package soapPOC;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class SOAPUsingDocumentBuilder {
	
	public static final String XML_PATH = "request.xml";
	public static final String SOAP_URL ="http://www.webservicex.net/ConvertTemperature.asmx";
	public static final String SOAP_ACTION = "http://www.webserviceX.NET/ConvertTemp";

public static void main(String[] args) throws Exception {
	
	String xmlPayload = FileUtils.readFileToString(new File(XML_PATH));
	
	SOAPMessage request = buildSOAPRequest(xmlPayload, SOAP_ACTION);
	
	SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    
    SOAPMessage soapResponse = soapConnection.call(request, SOAP_URL);
    
    printSOAPResponse(soapResponse);
    
    System.out.println(soapResponse.getSOAPBody().getTextContent());
    	
}

private static Document  buildata(String xmlString) throws Exception{
	
	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	docBuilderFactory.setNamespaceAware(true);
	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	return docBuilder.parse(new InputSource(new StringReader(xmlString)));
}

private static SOAPMessage buildSOAPRequest(String payload,String soapaction) throws Exception{
	MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
	SOAPMessage msg = mf.createMessage();
	msg.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
	//SOAPPart soapPart = msg.getSOAPPart();
	SOAPBody soapBody = msg.getSOAPBody();
	//SOAPEnvelope envelope = soapPart.getEnvelope();   
	SOAPHeader header = msg.getSOAPHeader();   
	//header.detachNode(); 
	soapBody.addDocument(buildata(payload));
	msg.saveChanges();   
	msg.getMimeHeaders().addHeader("SOAPAction", soapaction);
	
	return msg;
}

private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    Source sourceContent = soapResponse.getSOAPPart().getContent();
    System.out.print("\nResponse SOAP Message = ");
    StreamResult result = new StreamResult(System.out);
    transformer.transform(sourceContent, result);
}
}