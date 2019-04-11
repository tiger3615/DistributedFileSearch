package sf.ibu.netFileSearch.core.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import sf.ibu.netFileSearch.common.OneOffConnector;
import sf.ibu.netFileSearch.core.server.ServerScanner;

public class CallRemoterThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(CallRemoterThread.class);
	private JSONObject serverInfo;
	private String searchInfo;
	private String result;
	public CallRemoterThread(JSONObject serverInfo, String searchInfo) {
		this.serverInfo = serverInfo;
		this.searchInfo = searchInfo;
	}

	@Override
	public void run(){
		try {
			OneOffConnector oneOffConnector = new OneOffConnector(serverInfo.getString("IP"),serverInfo.getIntValue("port"));
			oneOffConnector.init();
			/*
			 * message tpl
			 * {
			 * 		"type":"innerSearch",
			 *  	"info":"-2 key"
			 * }
			 */
			JSONObject requestJObj=new JSONObject();
			requestJObj.put("type", "innerSearch");
			requestJObj.put("info", searchInfo);
			oneOffConnector.write(requestJObj.toString());
			result = oneOffConnector.read();
			oneOffConnector.shutdown();
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	public String getResult() {
		return result;
	}

}
