package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReader extends DefaultHandler {
    // Instance variables
    String inputFile = "videoList.xml";
    VideoFile currentVideo;
    String currentSubElement;
    List<VideoFile> videoList;

    public XMLReader() {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputFile, this);
        }
        catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        catch (SAXException saxe) {
            saxe.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


    public void startDocument() throws SAXException {
        System.out.println("Started parsing: " + inputFile);
        videoList = new ArrayList<VideoFile>();
    }


    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {


        String elementName = localName;
        if ("".equals(elementName)) {
            elementName = qName;
        }


        switch (elementName) {
        case "video":
            currentVideo = new VideoFile();
            break;
        case "title":
            currentSubElement = "title";
            break;
        case "filename":
            currentSubElement = "filename";
            break;
        default:
            currentSubElement = "none";
            break;
        }


        if (attrs != null) {

            String attributeName = attrs.getLocalName(0);
            if ("".equals(attributeName)) {
                attributeName = attrs.getQName(0);
            }
            

            String attributeValue = attrs.getValue(0);
            switch (elementName) {
            case "video":
                currentVideo.setId(attributeValue);
                break;

            default:

                break;
            }
        }
    }


    public void characters(char ch[], int start, int length) throws SAXException {

        String newContent = new String(ch, start, length);
        
        switch (currentSubElement) {
        case "title":
            currentVideo.setTitle(newContent);
            break;
        case "filename":
            currentVideo.setFilename(newContent);
            break;
        default:

            break;
        }
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {


        currentSubElement = "none";


        String elementName = localName;
        if ("".equals(elementName)) {
            elementName = qName;
        }

        if (elementName.equals("video")) {
            videoList.add(currentVideo);

            currentVideo = null;
        }
    }


    public void endDocument() throws SAXException {
        System.out.println("Finished parsing, stored " + videoList.size() + " videos.");

    }

    
    public List getList(String filename){
    	
    	VideoFile videoFile = new VideoFile();
    	videoList.add(videoFile);
    	
    	return videoList;
    }
    public static void main(String[] args) {
        new XMLReader();
    }
}