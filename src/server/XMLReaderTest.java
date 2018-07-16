package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XMLReaderTest {
	private XMLReader reader;
	private List<VideoFile> videoList;
	
	@Before
	public void setUp() throws Exception {
		reader = new XMLReader();
		videoList = reader.getList("videoList.xml");
	}

	@Test
	public void createListOfVideos() {
		assertTrue(videoList instanceof List);
	}

	@Test
	public void listContainsVideoFiles() {
		assertTrue(videoList.get(0) instanceof VideoFile);
	} 
	
	@Test
	public void videoFileReturnsCorrectFields(){
		assertNotNull(videoList);
		assertTrue(videoList.get(1) instanceof VideoFile); 
	}//videoList.size() clearly is 3 why it thros IndexOutOfBoundsException?

}
