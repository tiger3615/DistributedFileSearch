package sf.ibu.netFileSearch.core.local;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import sf.ibu.netFileSearch.common.OneOffConnector;
import sf.ibu.netFileSearch.common.Util;
import sf.ibu.netFileSearch.core.Conf;
import sf.ibu.netFileSearch.core.server.ServerHandler;
import sf.ibu.netFileSearch.core.server.ServerScanner;

public class LocalHandler {
	public String search(String info) throws Exception {
		// get all server info
		OneOffConnector oneOffConnector = new OneOffConnector(ServerScanner.getAvailableIP(), Conf.getIns().getPort());
		oneOffConnector.init();
		/*
		 * message tpl { "type":"getAllServers" }
		 */
		JSONObject cmdJObj = new JSONObject();
		cmdJObj.put("type", "getAllServers");
		oneOffConnector.write(cmdJObj.toString());
		String allServers = oneOffConnector.read();
		JSONArray serversJarr = JSONArray.parseArray(allServers);
		removeSelf(serversJarr);
		StringBuilder sb=new StringBuilder();
		// call remote server
		if(serversJarr.size()>0) {
			ArrayList<CallRemoterThread> tasks = new ArrayList<CallRemoterThread>();
			ExecutorService  executor = Executors.newFixedThreadPool(serversJarr.size());
			for (int i = 0; i < serversJarr.size(); i++) {
				CallRemoterThread callRemoterThread=new CallRemoterThread(serversJarr.getJSONObject(i), info);
				executor.execute(callRemoterThread);
				tasks.add(callRemoterThread);
			}
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.MINUTES);
			// merge
			for(int i=0;i<tasks.size();i++) {
				String result= tasks.get(i).getResult();
				sb.append(result);
			}
		}
		
		
		// call local
		String localResult = ServerHandler.innerSearch(info);
		sb.append(localResult);
		
		return sb.toString();
	}

	public void removeSelf(JSONArray serversJarr) {
		for (int i = 0; i < serversJarr.size(); i++) {
			JSONObject oneServerInfo = serversJarr.getJSONObject(i);
			String localIP = Util.getLocalIP();
			if (localIP.equals(oneServerInfo.getString("IP"))) {
				serversJarr.remove(i);
				break;
			}
		}
	}

}
