package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;



public class MEMEServer {
	List<VideoFile> videoList;
	
	int port = 1138;
	ServerSocket serverSocket;
	Socket clientSocket;
	ObjectOutputStream outputToClient;
	public static String serverAddress = "127.0.0.1";
	String vlcLibraryPath = "C:/tmp/vlc-2.0.1";

	private void openSocket() throws IOException{
		try{
			serverSocket = new ServerSocket(port);
		} catch (IOException e){
			System.out.println("Could not listen on port: "+ port);
			System.exit(-1);
		}
		System.out.println("Opened socket on :" + port + ", waiting for client");
	
	
		try {
			clientSocket = serverSocket.accept();
		}catch(IOException e){
			System.out.println("Could not accept client.");
			System.exit(-1);
		}
		
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	
	
	
	private void writeListToSocket() throws IOException{
		outputToClient.writeObject(videoList);
	}
	
	public MEMEServer(){
		System.out.println("Building XML reader and getting list.");;
		XMLReader reader = new XMLReader();
		videoList = reader.getList("videoList.xml");
		for ( int i = 0; i < videoList.size()-1; i++){
			System.out.println(videoList.get(i).getID());
		}
		System.out.println("Finished XML reader and getting list.");
		
		Thread socketThread = new Thread("Socket"){
			public void run(){
				try {
					openSocket();
					writeListToSocket();
					clientSocket.close();
					serverSocket.close();
				}catch (IOException e){
					System.out.println("Error on socket connection.");
					e.printStackTrace();
				}
			}
		};
		socketThread.start();
		
		//vlc library coding
		String options = formatRtpStream(serverAddress,5555);
		String media = "monstersinc_high.mpg";
		
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
		
		try {
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n" + e);
	    }
	
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(media);
		HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		
		mediaPlayer.playMedia(media, options, ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all", ":sout-keep");
		
		try {
			Thread.currentThread().join();
		} catch(InterruptedException e){
			System.out.println("Exception thrown whilst streaming.");
			e.printStackTrace();
		}
	}

	public static String formatRtpStream(String serverAddress, int serverPort){
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		new MEMEServer();
	}
}
