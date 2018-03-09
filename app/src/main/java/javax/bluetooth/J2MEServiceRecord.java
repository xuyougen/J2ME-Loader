package javax.bluetooth;

import java.util.HashMap;

class J2MEServiceRecord implements ServiceRecord {
	private RemoteDevice dev;
	private UUID uuid;
	private boolean skipAfterWrite;
	private HashMap<Integer, DataElement> dataElements = new HashMap<>();

	public J2MEServiceRecord(RemoteDevice dev, UUID uuid, boolean skipAfterWrite) {
		this.dev = dev;
		this.uuid = uuid;
		this.skipAfterWrite = skipAfterWrite;
	}

	public RemoteDevice getHostDevice() {
		return dev;
	}

	public String getConnectionURL(int requiredSecurity, boolean mustBeMaster) {
		StringBuilder sb;
		if (dev.btl2cap)
			sb = new StringBuilder("btl2cap://");
		else
			sb = new StringBuilder("btspp://");
		if (dev != null)
			sb.append(dev.getBluetoothAddress());
		else
			sb.append("localhost");
		sb.append(":");
		sb.append(uuid.toString());

		switch (requiredSecurity) {
			case NOAUTHENTICATE_NOENCRYPT:
				sb.append(";authenticate=false;encrypt=false");
				break;
			case AUTHENTICATE_NOENCRYPT:
				sb.append(";authenticate=true;encrypt=false");
				break;
			case AUTHENTICATE_ENCRYPT:
				sb.append(";authenticate=true;encrypt=true");
				break;
			default:
				throw new IllegalArgumentException();
		}

		if (mustBeMaster)
			sb.append(";master=true");
		else
			sb.append(";master=false");

		if (skipAfterWrite)
			sb.append(";skipAfterWrite=true");

		return sb.toString();
	}

	public boolean setAttributeValue(int attrID, DataElement attrValue) {
		if (attrID == 0)
			throw new IllegalArgumentException("attrID is ServiceRecordHandle (0x0000)");
		if (attrValue == null)
			return false;
		dataElements.put(attrID, attrValue);
		return true;
	}

	public DataElement getAttributeValue(int attrID) {
		return dataElements.get(attrID);
	}

	public int[] getAttributeIDs() {
		int[] arr = new int[dataElements.size()];
		int i = 0;
		for (Integer val : dataElements.keySet()) arr[i++] = val;
		return arr;
	}

	public void setDeviceServiceClasses(int classes) {
	}

	public boolean populateRecord(int[] attrIDs) {
		if (attrIDs == null)
			throw new NullPointerException();
		for (int val : attrIDs) {
			if (dataElements.containsKey(val)) return true;
		}
		return false;
	}

}
