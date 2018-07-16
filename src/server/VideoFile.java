package server;

import java.io.Serializable;

public class VideoFile implements Serializable {
	String videoID, title, filename;
	
	public VideoFile(){
		
	}

	public String getID() {
		return this.videoID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getFilename() {
		return this.filename;
	}

	public void setId(String videoID) {
		this.videoID = videoID;
		
	}
	public void setTitle(String title) {
		this.title = title;
		
	}
	
	public void setFilename(String filename){
		this.filename = filename;
	}


}
