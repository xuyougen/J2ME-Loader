package javax.bluetooth;

import java.io.IOException;

import javax.microedition.io.Connection;
import android.bluetooth.BluetoothDevice;

public class RemoteDevice {
	BluetoothDevice dev;
	boolean btl2cap;

	RemoteDevice(BluetoothDevice dev, boolean btl2cap) {
		this.dev = dev;
		this.btl2cap = btl2cap;
	}

	static String javaToAndroidAddress(String addr) {
		StringBuilder sb = new StringBuilder(addr);
		for (int i = 2; i < sb.length(); i += 3)
			sb.insert(i, ':');
		return sb.toString();
	}

	protected RemoteDevice(String address) {
		if (address == null) {
			throw new NullPointerException("address is null");
		}

		dev = DiscoveryAgent.adapter.getRemoteDevice(javaToAndroidAddress(address));
	}

	public String getFriendlyName(boolean alwaysAsk) throws IOException {
		return dev.getName();
	}

	public final String getBluetoothAddress() {
		return dev.getAddress().replace(":", "");
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof RemoteDevice))
			return false;
		return dev.equals(((RemoteDevice)obj).dev);
	}

	public int hashCode() {
		return dev.hashCode();
	}

	public static RemoteDevice getRemoteDevice(Connection conn) throws IOException {
		if (conn == null)
			throw new NullPointerException("conn is null");
		if (!(conn instanceof org.microemu.cldc.btspp.Connection || conn instanceof org.microemu.cldc.btl2cap.Connection))
			throw new java.lang.IllegalArgumentException("not a RFCOMM connection");

		if (conn instanceof org.microemu.cldc.btspp.Connection) {
			org.microemu.cldc.btspp.Connection connection = (org.microemu.cldc.btspp.Connection) conn;
			if (connection.socket == null)
				throw new IOException("socket is null");
			return new RemoteDevice(connection.socket.getRemoteDevice(), false);
		} if (conn instanceof org.microemu.cldc.btl2cap.Connection) {
			org.microemu.cldc.btl2cap.Connection connection = (org.microemu.cldc.btl2cap.Connection) conn;
			if (connection.socket == null)
				throw new IOException("socket is null");
			return new RemoteDevice(connection.socket.getRemoteDevice(), true);
		} else
			throw new IllegalArgumentException("notifier is not BTSPP connection");
	}

	public boolean authenticate() throws IOException {
		return false;
	}

	public boolean authorize(javax.microedition.io.Connection conn) throws IOException {
		return false;
	}

	public boolean encrypt(javax.microedition.io.Connection conn, boolean on) throws IOException {
		return false;
	}

	public boolean isAuthenticated() {
		return false;
	}

	public boolean isAuthorized(javax.microedition.io.Connection conn) throws IOException {
		return false;
	}

	public boolean isEncrypted() {
		return false;
	}

	public boolean isTrustedDevice() {
		return false;
	}
}
