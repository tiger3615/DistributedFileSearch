package sf.ibu.eric.common;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import sf.ibu.netFileSearch.common.FixedMovingQueue;
import sf.ibu.netFileSearch.common.Util;

public class UtilTest {
	@Test
	public void test_file2String() {
		try {
			String r=Util.file2String("src/test/resources/test.txt");
			Assert.assertEquals("12345\r\n", r);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test_UTC() {
		try {
			System.out.println(Util.UTC("yyyy-MM-dd HH:mm:ssZ"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test_UTCDate() {
		try {
			System.out.println(Util.UTCDateTime());
			System.out.println(Util.formatTime(Util.UTCDateTime(), "yyyy-MM-dd HH:mm:ssZ"));
			String s = "js{j}";
			System.out.println(s.matches("js\\{.+\\}"));
			System.out.println(s.substring(3, s.length() - 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test_str2DateTime() {
		try {
			System.out.println("ssss"+Util.str2DateTime("2018-03-01 07:49:38", "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_decodeEncode() {
		try {
			System.out.println(Util.encode("sf1234567", "123456"));
			System.out.println(Util.decode("sf1234567", "8C6F3A9BB06D9E12FFEF3F9E52248041"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test_FixedMovingQueue() {
		FixedMovingQueue<String> queue=new FixedMovingQueue<String>(3);
		queue.put("1");
		queue.put("2");
		queue.put("3");
		queue.put("4");
		queue.put("5");
		ArrayList<String> list=queue.toList();
		Assert.assertEquals("3", list.get(0));
		Assert.assertEquals("4", list.get(1));
		Assert.assertEquals("5", list.get(2));
	}
	public static void main(String []a) {
		String keys="2-lskd sdk sdf";
		String keyArr[]=keys.split("-");
		int lines=Integer.parseInt(keyArr[0]);
		String newStr=keys.substring(keys.indexOf("-")+1);
		
		System.out.println(newStr);
		
	}
}
