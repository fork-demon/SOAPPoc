package soapPOC;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class SoapPOC {
	
	public static final String ENVELOPE_PATH = "soapEnvelop.xml";
	public static final String SOAP_URL ="http://www.webservicex.net/ConvertTemperature.asmx";
	public static final String SOAP_ACTION = "http://www.webserviceX.NET/ConvertTemp";
	public static final String BODY_NODE = "</Body>";
	
	public static void invokeInternal(final String xmlPayload) throws Exception{
		
		URL url = new URL(SOAP_URL);

	    String soapEnvString = FileUtils.readFileToString(new File(ENVELOPE_PATH));
	    StringBuilder sb = new StringBuilder(soapEnvString);
	    sb.insert(sb.toString().indexOf(BODY_NODE),xmlPayload);

	    HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
	   
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "text/xml");

	    conn.setRequestProperty("Content-Length", Integer.toString(sb.toString().getBytes().length));
	    conn.setRequestProperty("SOAPAction",SOAP_ACTION);

	    conn.setUseCaches (false);
	    conn.setDoOutput(true);
	    conn.setDoInput(true);

	    DataOutputStream wr = new DataOutputStream (
	            conn.getOutputStream ());
	    wr.writeBytes(sb.toString());
	    wr.flush ();
	    wr.close ();

	    final char[] buffer = new char[0x10000];
	    StringBuilder out = new StringBuilder();
	    Reader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
	    int read;
	    do {
	      read = in.read(buffer, 0, buffer.length);
	      if (read>0) {
	        out.append(buffer, 0, read);
	      }
	    } while (read>=0);
	    System.out.println(out);

	}
	
	public static void main(String[] args) throws Exception {
		String xmlPayload = FileUtils.readFileToString(new File("request.xml"));
		invokeInternal(xmlPayload);
		
	}

}
