package sf.ibu.netFileSearch.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class OneOffConnector {
	private String monitorCenter;

	private int monitorPort;

	private PrintWriter pw;
	private BufferedReader br;
	private Socket socket;
	private InputStream iStream;

	public OneOffConnector(String monitorCenter, int monitorPort) {
		this.monitorCenter = monitorCenter;
		this.monitorPort = monitorPort;
	}

	public void init() throws Exception {
		socket = new Socket(monitorCenter, monitorPort);
		OutputStream os = socket.getOutputStream();
		pw = new PrintWriter(os);
		iStream = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(iStream));
	}

	public void write(String v) {
		synchronized ("write" + this.hashCode()) {
			Util.write(pw, v);
		}
	}

	public InputStream getIStream() {
		return iStream;
	}

	public String read() throws Exception {
		synchronized ("read" + this.hashCode()) {
			try {
				return Util.read(br);
			} catch (SocketException e) {
				Thread.sleep(1000 * 10);
				throw e;
			}
		}

	}

	public void shutdown() throws Exception {
		socket.shutdownOutput();
		br.close();
		pw.close();
		socket.close();
	}
}
