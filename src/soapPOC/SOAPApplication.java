package soapPOC;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;

public class SOAPApplication {


public static void main(String args[]) {
    try {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://www.webservicex.net/ConvertTemperature.asmx";
        
        //createSOAPRequest();
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

        // Process the SOAP Response
        printSOAPResponse(soapResponse);
        System.out.println(soapResponse.getSOAPBody().getTextContent());

        soapConnection.close();
    } catch (Exception e) {
        System.err.println("Error occurred while sending SOAP Request to Server");
        e.printStackTrace();
    }
}

private static SOAPMessage createSOAPRequest() throws Exception {
    MessageFactory messageFactory = MessageFactory.newInstance();
    SOAPMessage soapMessage = messageFactory.createMessage();
    SOAPPart soapPart = soapMessage.getSOAPPart();
    
 

    String actionName = "\"http://www.webserviceX.NET/ConvertTemp\"";
   


    // SOAP Envelope
    SOAPEnvelope envelope = soapPart.getEnvelope();
    



    // SOAP Body
    SOAPBody soapBody = envelope.getBody();
    
    SOAPElement soapBodyElem = soapBody.addChildElement("ConvertTemp","","http://www.webserviceX.NET/");

    SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("Temperature");
    SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("FromUnit");
    SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("ToUnit");
    soapBodyElem1.setTextContent("15.0");
    soapBodyElem2.setTextContent("degreeCelsius");
    soapBodyElem3.setTextContent("kelvin");
    
  
   

    MimeHeaders headers = soapMessage.getMimeHeaders();
    headers.addHeader("SOAPAction", actionName);

    soapMessage.saveChanges();
    
    ByteArrayOutputStream baa = new ByteArrayOutputStream();

    soapMessage.writeTo(baa);
    /* Print the request message */
    System.out.print("Request SOAP Message = ");
   // soapMessage.writeTo(System.out);
    String msg = new String(baa.toByteArray());
    System.out.println(StringEscapeUtils.unescapeXml(msg));

    return soapMessage;
}

/**
 * Method used to print the SOAP Response
 */
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