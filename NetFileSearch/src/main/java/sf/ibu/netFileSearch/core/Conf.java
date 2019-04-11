package sf.ibu.netFileSearch.core;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import sf.ibu.netFileSearch.common.Util;

public class Conf {
	private JSONObject confJson;
	private static Conf ins;
	public static Conf getIns() throws JSONException, Exception {
		if(ins==null) {
			ins=new Conf();
		}
		return ins;
	}
	private Conf() throws JSONException, Exception {
		refresh();
	}
	private void refresh() throws JSONException, Exception {
		String configureFile=Conf.class.getResource("/NetFileSearch.json").toURI().getPath();
		confJson=JSONObject.parseObject(Util.file2String(configureFile));
	}
	
	public JSONArray getIPs() {
		return confJson.getJSONArray("IPs");
	}
	public int getPort() {
		return confJson.getIntValue("port");
	}
	public long getScanServerIntervalms(){
		return confJson.getLongValue("scanServerIntervalms");
	}
	public ArrayList<String> getPaths(){
		ArrayList<String> rList=new ArrayList<String>();
		JSONArray jsonArray=confJson.getJSONArray("filePaths");
		if(jsonArray!=null) {
			for(int i=0;i<jsonArray.size();i++) {
				rList.add(jsonArray.getString(i));
			}
		}
		return rList;
	}
	public JSONObject getConfJson() {
		return confJson;
	}
	
}
