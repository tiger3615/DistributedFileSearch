package sf.ibu.netFileSearch.core.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import sf.ibu.netFileSearch.common.FixedMovingQueue;
import sf.ibu.netFileSearch.common.Util;
import sf.ibu.netFileSearch.core.Conf;

public class ServerHandler {

	public void handle(String message, Socket socket) throws Exception {
		/*
		 * message tpl { "type":"register", "info":{ "IP":"127.0.0.1", "port":2000,
		 * "filePaths":["path1","path2"] } }
		 */
		JSONObject json = JSONObject.parseObject(message);
		String type = json.getString("type");
		if ("register".equalsIgnoreCase(type)) {
			register(json.getJSONObject("info"));
		} else if ("innerSearch".equalsIgnoreCase(type)) {
			/*
			 * message tpl { "type":"innerSearch", "info":"-2 key" }
			 */
			String info = json.getString("info");
			String result = innerSearch(info);
			Util.write(socket, result);
		} else if ("getAllServers".equalsIgnoreCase(type)) {
			Util.write(socket, ServerCollection.getAllServers().toString());
		}
	}

	public void register(JSONObject json) {
		ServerCollection.put(json);
	}

	public static String innerSearch(String info) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------" + Util.getLocalIP() + "------------------\n");
		ArrayList<String> paths = Conf.getIns().getPaths();
		for (int i = 0; i < paths.size(); i++) {
			String path = paths.get(i);
			if ("list".equalsIgnoreCase(info)) {
				String r = allFiles(path);
				sb.append(r);
			} else {
				sb.append(searchInPath(path, info));
			}
		}
		String r = sb.toString().replaceAll("\\n", "<br/>");
		return r;
	}

	public static String allFiles(String path) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------" + path + "------------------\n");
		Process process = Runtime.getRuntime().exec("ls -lh", null, new File(path));
		process.waitFor();
		InputStream is = process.getInputStream();
		String r = Util.read(is);
		sb.append("\n" + r);
		return sb.toString();
	}

	public static String searchInPath(String path, String keys) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------" + path + "------------------\n");
		File pathO = new File(path);
		if (pathO.isDirectory()) {
			File logFiles[] = pathO.listFiles();
			if (logFiles != null) {
				for (int i = 0; i < logFiles.length; i++) {
					sb.append(searchInFile(logFiles[i], keys));
				}
			}

		} else {
			sb.append(searchInFile(pathO, keys));
		}
		return sb.toString();
	}

	public static String searchInFile(File file, String keys) throws Exception {
		StringBuilder sb = new StringBuilder();
		String filePath = file.getAbsolutePath();
		sb.append("----------------" + filePath + "------------------\n");
		if(keys.contains("-")) {
			String keyArr[]=keys.split("-");
			int lines=Integer.parseInt(keyArr[0]);
			String key=keys.substring(keys.indexOf("-")+1);
			sb.append(goThroughFile(lines,key,filePath));
		}else {
			sb.append(goThroughFile(1,keys,filePath));
		}
		
		return sb.toString();
	}

	public static String goThroughFile(int linesAround, String key, String filePath) throws Exception {
		StringBuilder sb = new StringBuilder();
		FixedMovingQueue<String> resultQueue = new FixedMovingQueue<String>(linesAround);
		Pattern p = Pattern.compile(key);
		int step = 0;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String temp = null;
		while ((temp = br.readLine()) != null) {
			Matcher m = p.matcher(temp);
			String newStr = temp;
			boolean found = false;
			while (m.find()) {
				found = true;
				String foundStr = m.group(0);
				newStr = newStr.replaceAll(foundStr, "-->" + foundStr + "<--");
			}
			if (found) {
				resultQueue.put(newStr);
				resultQueue.setSize(resultQueue.getSize() + linesAround);
				step=1;
			} else {
				resultQueue.put(temp);
			}
			if(step>0) {
				//continue to fetch couple lines
				if(++step>linesAround) {
					sb.append(queue2string(resultQueue)+"\n");
					resultQueue = new FixedMovingQueue<String>(linesAround);
					step=0;
				}
			}
		}
		return sb.toString();
	}
	private static String queue2string(FixedMovingQueue<String> resultQueue) {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> lines=resultQueue.toList();
		for(int i=0;i<lines.size();i++) {
			sb.append(lines.get(i)+"\n");
		}
		return sb.toString();
	}
}
