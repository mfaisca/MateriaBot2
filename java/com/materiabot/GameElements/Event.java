package com.materiabot.GameElements;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.BotException;

public class Event {
	public static class EventLink{
		public long eventId, linkId, order;
		public String name;
		public String url;
		
		public long getEventId() {
			return eventId;
		}
		public void setEventId(long eventId) {
			this.eventId = eventId;
		}
		public long getLinkId() {
			return linkId;
		}
		public void setLinkId(long linkId) {
			this.linkId = linkId;
		}
		public long getOrder() {
			return order;
		}
		public void setOrder(long order) {
			this.order = order;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		public int saveEventLink() throws BotException {
			try {
				ResultSet result = SQLAccess.executeSelect("SELECT * FROM current_events_links WHERE eventId = ? AND linkId = ? AND name = ?", getEventId(), getLinkId(), getName());
				if(result.next()) {
					if(getUrl().equalsIgnoreCase("delete")) {
						SQLAccess.executeInsert("DELETE FROM current_events_links WHERE name = ? AND eventId = ? AND linkId = ?", 
								getName(), getEventId(), getLinkId());
						return 2;
					}else {
						SQLAccess.executeInsert("UPDATE current_events_links SET url = ? WHERE eventId = ? AND linkId = ? AND name = ?", 
							getUrl(), getEventId(), getLinkId(), getName());
						return 1;
					}
				}else {
					if(getName().equalsIgnoreCase("delete")) {
						SQLAccess.executeInsert("DELETE FROM current_events_links WHERE eventId = ? AND linkId = ?", 
								getEventId(), getLinkId());
						return 4;
					}
					else if(!getUrl().equalsIgnoreCase("delete")) {
						SQLAccess.executeInsert("INSERT INTO current_events_links(eventId, linkId, `order`, name, url) VALUES(?, ?, ?, ?, ?)", 
								getEventId(), getLinkId(), getOrder(), getName(), getUrl());
						return 0;
					}else
						return 3;
				}
			} catch (BotException | SQLException e) {
				throw new BotException(e);
			}
		}
	}
	
	public long id;
	public String name;
	public ZonedDateTime startDate, endDate;
	public LinkedList<EventLink> links = new LinkedList<EventLink>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}
	public ZonedDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}
	public LinkedList<EventLink> getLinks() {
		return links;
	}
	
	public int saveEvent() throws BotException {
		ResultSet result = SQLAccess.executeSelect("SELECT * FROM current_events WHERE eventName = ? AND endDate >= ?", getName(), ZonedDateTime.now(ZoneId.of("UTC")).getLong(ChronoField.INSTANT_SECONDS));
		try {
			if(result.next()) 
				return 1;
			SQLAccess.executeInsert("INSERT INTO current_events(eventName, startDate, endDate) VALUES(?, ?, ?)", 
					getName(), getStartDate().getLong(ChronoField.INSTANT_SECONDS), getEndDate().getLong(ChronoField.INSTANT_SECONDS));
			return 0;
		} catch (SQLException | BotException e) {
			throw new BotException(e);
		}
	}
	
	public int updateEvent() throws BotException {
		ResultSet result = SQLAccess.executeSelect("SELECT * FROM current_events WHERE eventName = ? AND endDate >= ?", getName(), ZonedDateTime.now(ZoneId.of("UTC")).getLong(ChronoField.INSTANT_SECONDS));
		try {
			if(!result.next())
				return 1;
			SQLAccess.executeInsert("UPDATE current_events SET startDate = ?, endDate = ? WHERE eventId = ?", 
					getStartDate().getLong(ChronoField.INSTANT_SECONDS), getEndDate().getLong(ChronoField.INSTANT_SECONDS), result.getInt(1));
			return 0;
		} catch (SQLException | BotException e) {
			throw new BotException(e);
		}
	}
	
	public static int deleteEvent(String name) throws BotException {
		ResultSet result = SQLAccess.executeSelect("SELECT * FROM current_events WHERE eventName = ? AND endDate >= ?", 
				name, ZonedDateTime.now(ZoneId.of("UTC")).toEpochSecond());
		try {
			if(result.next()) {
				ResultSet result2 = SQLAccess.executeSelect("SELECT * FROM current_events_links WHERE eventId = ?", result.getInt(1));
				if(result2.next())
					return 2;
				SQLAccess.executeInsert("DELETE FROM current_events WHERE eventId = ?", 
						result.getInt(1));
				return 0;
			}
			else 
				return 1;
		} catch (SQLException | BotException e) {
			throw new BotException(e);
		}
	}
	
	public static List<Event> getCurrentAndFutureEvents(){
		try {
			ResultSet r = SQLAccess.executeSelect("SELECT * FROM current_events WHERE endDate > ? ORDER BY startDate ASC", ZonedDateTime.now(ZoneId.of("UTC")).toEpochSecond());
			List<Event> list = new LinkedList<Event>();
			while(r.next()) {
				Event e = new Event();
				e.setId(r.getInt(1));
				e.setName(r.getString(2));
				e.setStartDate(ZonedDateTime.ofInstant(Instant.ofEpochSecond(r.getLong(3)), ZoneOffset.UTC));
				e.setEndDate(ZonedDateTime.ofInstant(Instant.ofEpochSecond(r.getLong(4)), ZoneOffset.UTC));
				ResultSet rr = SQLAccess.executeSelect("SELECT * FROM current_events_links WHERE eventId = ? ORDER BY linkId DESC, `order` ASC", e.getId());
				while(rr.next()) {
					EventLink ev = new EventLink();
					ev.setEventId(rr.getInt(1));
					ev.setLinkId(rr.getLong(2));
					ev.setOrder(rr.getLong(3));
					ev.setName(rr.getString(4));
					ev.setUrl(rr.getString(5));
					e.getLinks().add(ev);
				}
				list.add(e);
			}
			Event maintenance = list.stream().filter(e -> e.getName().contains("Maintenance")).findAny().orElse(null);
			if(maintenance != null) {
				list.remove(maintenance);
				list.add(0, maintenance);
			}
			Event stream = list.stream().filter(e -> e.getName().contains("Stream")).findAny().orElse(null);
			if(stream != null) {
				list.remove(stream);
				list.add(0, stream);
			}
			return list;
		} catch (BotException | SQLException e) {
			e.printStackTrace();
			return new LinkedList<Event>();
		}
	}
}