package sf.ibu.netFileSearch.core;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import sf.ibu.netFileSearch.common.MySocketServer;
import sf.ibu.netFileSearch.common.OneOffConnector;
import sf.ibu.netFileSearch.common.Util;
import sf.ibu.netFileSearch.core.local.LocalHandler;
import sf.ibu.netFileSearch.core.server.ServerHandler;
import sf.ibu.netFileSearch.core.server.ServerScanner;

public class NetFileSearch {
	private static boolean inited = false;
	private static LocalHandler localHandler = new LocalHandler();
	private static final Logger logger = LoggerFactory.getLogger(NetFileSearch.class);
	public static void init() {
		if (!inited) {
			inited = true;
			Thread connectMonitorThread = new Thread() {
				public void run() {
					try {
						//start receive server just once
						startInnerServer();
					} catch (Throwable e1) {
						logger.error("",e1);
					}
					while (true) {
						try {
							//register every 30s to make sure server always has terminal info.
							register();
						} catch (Throwable e) {
							logger.error("",e);
						}
						Util.sleep(30 * 1000);
					}
				}
			};
			connectMonitorThread.setName("connectMonitorThread");
			connectMonitorThread.start();
		}
	}

	public static String search(String key) {
		try {
			return localHandler.search(key);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String exceptionStack = sw.toString();
			return exceptionStack;
		}
	}

	public static void register() throws Exception {
		OneOffConnector oneOffConnector = new OneOffConnector(ServerScanner.getAvailableIP(), Conf.getIns().getPort());
		/*
		 * message tpl { "type":"register", "info":{ "IP":"127.0.0.1", "port":2000,
		 * "filePaths":["path1","path2"] } }
		 */
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "register");
		JSONObject infoJObj=Conf.getIns().getConfJson();
		//clone one to avoid update orignal configure
		JSONObject infoJObjSending=new JSONObject();
		infoJObjSending.put("IP", Util.getLocalIP());
		infoJObjSending.put("port", infoJObj.getIntValue("port"));
		infoJObjSending.put("filePaths", infoJObj.getJSONArray("filePaths"));
		jsonObject.put("info", infoJObjSending);
		oneOffConnector.init();
		oneOffConnector.write(jsonObject.toString());
		oneOffConnector.shutdown();
	}


	public static void startInnerServer() throws Exception {
		MySocketServer mySocketServer = new MySocketServer(Conf.getIns().getPort(), "net file content search server");
		mySocketServer.setOneOffConnectionHandler(new ServerHandler());
		mySocketServer.startListener();

	}
}
