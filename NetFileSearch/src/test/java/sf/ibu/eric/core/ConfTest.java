package sf.ibu.eric.core;

import java.util.ArrayList;

import org.junit.Test;

import com.alibaba.fastjson.JSONException;

import junit.framework.Assert;
import sf.ibu.netFileSearch.core.Conf;

public class ConfTest {
	@Test
	public void test() throws JSONException, Exception {
	
		Assert.assertEquals(2000, Conf.getIns().getPort());
		ArrayList<String> paths=Conf.getIns().getPaths();
		Assert.assertEquals("/s",paths.get(0));
		Assert.assertEquals("/a",paths.get(1));
	}
}
