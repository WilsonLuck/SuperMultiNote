package org.wzg.multinotes.entity;

import java.io.Serializable;

public class NoteEntity implements Serializable {

	private String ID;
	private String TitleContent;
	private String Content;
	private String Time;
	private String Photo;
	private String Video;
	private String Voice;

	public NoteEntity(String ID, String TitleContent, String Content, String Time,
			String Photo, String Video, String Voice) {

		this.ID = ID;
		this.TitleContent = TitleContent;
		this.Content = Content;
		this.Time = Time;
		this.Photo = Photo;
		this.Video = Video;
		this.Voice = Voice;

	}

	public String getVoice() {
		return Voice;
	}

	public void setVoice(String voice) {
		Voice = voice;
	}

	public String getTitleContent() {
		return TitleContent;
	}

	public void setTitleContent(String titleContent) {
		TitleContent = titleContent;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String Content) {
		this.Content = Content;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String Time) {
		this.Time = Time;
	}

	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getVideo() {
		return Video;
	}

	public void setVideo(String video) {
		Video = video;
	}

}
