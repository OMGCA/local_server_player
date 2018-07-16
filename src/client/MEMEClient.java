package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import server.MEMEServer;
import server.VideoFile;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;

import com.sun.jna.*;

public class MEMEClient extends JFrame implements ActionListener {

	public List<VideoFile> videoList;
	static Container contentPane;
	String vlcLibraryPath = "C:/tmp/vlc-2.0.1";
	Font font = new Font("Microsoft YaHei", Font.PLAIN,14);
	String options = MEMEServer.formatRtpStream(MEMEServer.serverAddress,5555);
	String media = "monstersinc_high.mpg";
	String[] selectionListData;
	String[] selectionFilename;
	

	public MEMEClient() {
		try {
			openSocket();
			getListFromSocket();
			serverSocket.close();
		} catch (UnknownHostException e) {
			System.out.println("Don't know about host: " + host);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Couldn't open I/O connection : " + host + ":" + port);
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.out.println("Class definition not found for incoming object.");
			e.printStackTrace();
			System.exit(-1);
		}
		try {  
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
        }catch (Exception e) {  
        	e.printStackTrace();  
        }  
		JFrame mainFrame = new JFrame();
		setupGUI();
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		
		final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mainFrame.setContentPane(mediaPlayerComponent);

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.release();
			}
		});
		mediaPlayerComponent.setFont(font);

		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

		final EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
		PlayerControlsPanel controlsPanel = new PlayerControlsPanel(mediaPlayer);
		//JSlider Hue = new JSlider(0,100);
		mediaPlayer.setAdjustVideo(true);
		mediaPlayer.isSeekable();
		
		//mainFrame.add(Hue,  BorderLayout.EAST);

		mainFrame.add(controlsPanel, BorderLayout.SOUTH);

		String media = "rtp://@127.0.0.1:5555";
		mediaPlayer.playMedia(media);
		mediaPlayer.setHue(20);
	
		
	}

	// ----------------------------------------------------



	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1138;
	ObjectInputStream inputFromServer;
	public JComboBox selectionBox;

	private void openSocket() throws UnknownHostException, IOException {
		serverSocket = new Socket(host, port);
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
	}

	private void getListFromSocket() throws IOException, ClassNotFoundException {
		videoList = (List<VideoFile>) inputFromServer.readObject();
	}

	private void setupGUI() {
		setTitle("xTV Player");
		setSize(1366, 768);
		setVisible(true);

		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		selectionListData = new String[(videoList.size())];
		selectionFilename = new String[(videoList.size())];
		for (int i = 0; i < videoList.size(); i++) {
			selectionListData[i] = videoList.get(i).getTitle();
			selectionFilename[i] = videoList.get(i).getFilename();
			System.out.println(selectionListData[i]);
		}
		JComboBox selectionBox = new JComboBox(selectionListData);
		selectionBox.setSelectedIndex(0);
		add(selectionBox, BorderLayout.NORTH);
		selectionBox.addActionListener(this);

		validate();
		
	}

	public void actionPerformed(ActionEvent e) {
		JComboBox comboBox = (JComboBox)e.getSource();
		String selectedTitle = (String)comboBox.getSelectedItem();
		media = linearSearch(selectedTitle);
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(media);
		HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		
		mediaPlayer.playMedia(media, options, ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all", ":sout-keep");
		System.out.println("Selected filename: " + media);
	}
	
	public void hueChange(ChangeEvent e){
		JSlider source = (JSlider)e.getSource();
		
		
	}
	
	public String linearSearch(String videoTitle){
		int i = 0;
		do{
			i++;
		}while(videoTitle != selectionListData[i]);
		return selectionFilename[i];
	}
	
	public static void main(String[] args) {
		new MEMEClient();
		
	}

}
