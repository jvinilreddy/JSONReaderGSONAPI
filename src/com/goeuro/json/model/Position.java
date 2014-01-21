package com.goeuro.json.model;

import com.google.gson.annotations.SerializedName;

public final class Position extends GeoPosition {

	@SerializedName("_id")
	private int _id;
	private String name;
	private String _type;
	private String type;
	private GeoPosition geo_position;

	public String getPosType() {
		return _type;
	}

	public void setPosType(String _type) {
		this._type = _type;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocType() {
		return type;
	}

	public void setLocType(String type) {
		this.type = type;
	}

	public GeoPosition getGeoPosition() {
		return geo_position;
	}

	public void setGeoPosition(GeoPosition geo_position) {
		this.geo_position = geo_position;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPosType() + "," + getId() + "," + getName() + ","
				+ getLocType() + "," + getGeoPosition() + "\n");
		return sb.toString();
	}

}
