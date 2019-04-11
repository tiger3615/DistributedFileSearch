package sf.ibu.netFileSearch.core.server;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;

import sf.ibu.netFileSearch.common.Util;
import sf.ibu.netFileSearch.core.Conf;

public class ServerScanner {
	private static final Logger logger = LoggerFactory.getLogger(ServerScanner.class);
	private static volatile String availableIP;

	public static String getAvailableIP() {
		return availableIP;
	}

	public static Thread scannerThread = new Thread() {
		public void run() {
			while (true) {
				try {
					Conf conf = Conf.getIns();
					long scanServerIntervalms = conf.getScanServerIntervalms();
					availableIP = findAvailableIP();
					Thread.sleep(scanServerIntervalms);
				} catch (Throwable e) {
					Util.sleep(1000*60);
					logger.error("", e);
				}
			}

		}
	};
	
	static {
		try {
			availableIP = findAvailableIP();
			scannerThread.setName("server scanner");
			scannerThread.start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	public static String findAvailableIP() throws Exception {
		Conf conf = Conf.getIns();
		int port = conf.getPort();
		JSONArray ipArr = conf.getIPs();
		for (int i = 0; i < ipArr.size(); i++) {
			String ip = ipArr.getString(i);
			if (ping(ip, port)) {
				return ip;
			}
		}
		return ipArr.getString(0);
	}
	public static boolean ping(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			socket.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
