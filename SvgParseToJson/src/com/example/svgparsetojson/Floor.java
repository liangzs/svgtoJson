package com.example.svgparsetojson;

import java.util.List;

public class Floor {
	private String name;
	private String angle;
	private String height;
	private String level;
	private String usage;
	private String mapWidth;
	private String mapHeight;
	private String maxLength;
	private String projectId;
	
	private List<CoordinateDomain> cornors;
	public String toString(){
		return "name:"+this.name+"angle:"+this.angle+"height:"+this.height
				+"level:"+this.level+",projectId:"+this.projectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getMapWidth() {
		return mapWidth;
	}
	public void setMapWidth(String mapWidth) {
		this.mapWidth = mapWidth;
	}
	public String getMapHeight() {
		return mapHeight;
	}
	public void setMapHeight(String mapHeight) {
		this.mapHeight = mapHeight;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<CoordinateDomain> getCornors() {
		return cornors;
	}
	public void setCornors(List<CoordinateDomain> cornors) {
		this.cornors = cornors;
	}

	
	
}
