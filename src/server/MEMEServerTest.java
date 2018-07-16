package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MEMEServerTest {
	private MEMEServer server;

	@Before
	public void setUp() throws Exception {
		server = new MEMEServer();
	}

	@Test
	public void presenceOfVideoList() {
		assertTrue(server.videoList instanceof List);
	}

}
