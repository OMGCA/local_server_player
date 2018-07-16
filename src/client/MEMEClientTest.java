package client;

import static org.junit.Assert.*;

import javax.swing.JComboBox;

import org.junit.Before;
import org.junit.Test;

import server.VideoFile;

public class MEMEClientTest {
	private MEMEClient client;
	
	@Before
	public void setUp() throws Exception {
		server.MEMEServer.main(null);
		client = new MEMEClient();
	}

	@Test
	public void checkSelectedVideoInList(){
		JComboBox comboBox = client.selectionBox;
		comboBox.setSelectedIndex(2);
		assertEquals("Third Video", comboBox.getSelectedItem());
	}

}
