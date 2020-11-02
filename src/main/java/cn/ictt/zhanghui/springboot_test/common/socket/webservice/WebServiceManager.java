package cn.ictt.zhanghui.springboot_test.common.socket.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.stereotype.Component;

/**
 * webservice调用工具类
 * 
 * @author Zhanghui
 */
@Component
public class WebServiceManager {
//	@Value("${pplatform.webservice.wsdl}")
	private String WSDL;

//	@Value("${pplatform.webservice.methodname}")
	private String METHODNAME;

//	@Value("${pplatform.webservice.namespaceurl}")
	private String NAMESPACEURL;
	
	public String callWebService(String inMsg) throws MalformedURLException, ServiceException, RemoteException {
		inMsg = inMsg == null ? "" : inMsg;
		URL url = null;
		String resp = "";
		
		url = new URL(WSDL);
		Service service = new Service();
		QName qName = new QName(NAMESPACEURL, METHODNAME);
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(url);
		call.setOperationName(qName);
		Object respObject = call.invoke(new Object[] { inMsg });
		if (respObject != null) {
			resp = (String) respObject;
		}
		return resp;
	}
}
