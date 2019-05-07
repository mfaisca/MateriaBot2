package com.materia.materiabot.GameElements;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Event {
	private Date startDate, endDate;
	private String eventImage;
	
	private List<Unit> bannerCharacters = new LinkedList<Unit>();

	public Date getStartDate() { return startDate; }
	public void setStartDate(Date startDate) { this.startDate = startDate; }
	public Date getEndDate() { return endDate; }
	public void setEndDate(Date endDate) { this.endDate = endDate; }
	public String getEventImage() { return eventImage; }
	public void setEventImage(String eventImage) { this.eventImage = eventImage; }
	public List<Unit> getBannerCharacters() { return bannerCharacters; }
	public void setBannerCharacters(List<Unit> bannerCharacters) { this.bannerCharacters = bannerCharacters; }
}