package ws.cccm.reg.service;

import java.io.Serializable;

public class NameTag implements Serializable {

	private static final long serialVersionUID = 2310622123753972442L;

	private String conferenceName = "二零一八基督徒大會";

	private String graceRow = "GRACE 2018";

	private boolean isGrace = false;

	private String chineseFullName;

	private String englishFullName;

	private String chruchName;

	private String centerId;

	private String address;

	private String barcodeId;

	private String groupId;

	private String topic;

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public String getGraceRow() {
		return graceRow;
	}

	public void setGraceRow(String graceRow) {
		this.graceRow = graceRow;
	}

	public boolean isGrace() {
		return isGrace;
	}

	public void setGrace(boolean isGrace) {
		this.isGrace = isGrace;
	}

	public String getChineseFullName() {
		return chineseFullName;
	}

	public void setChineseFullName(String chineseFullName) {
		this.chineseFullName = chineseFullName;
	}

	public String getEnglishFullName() {
		return englishFullName;
	}

	public void setEnglishFullName(String englishFullName) {
		this.englishFullName = englishFullName;
	}

	public String getChruchName() {
		return chruchName;
	}

	public void setChruchName(String chruchName) {
		this.chruchName = chruchName;
	}

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBarcodeId() {
		return barcodeId;
	}

	public void setBarcodeId(String barcodeId) {
		this.barcodeId = barcodeId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
