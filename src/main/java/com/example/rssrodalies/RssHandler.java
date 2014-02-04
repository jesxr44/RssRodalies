package com.example.rssrodalies;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by jesus on 29/01/14.
 */
public class RssHandler extends DefaultHandler {
    // Feed and Article objects to use for temporary storage

    private String estado;

    Boolean inTitle = false;
    Boolean inItem = false;

    //Current characters being accumulated
    StringBuffer chars = new StringBuffer();

    /*
     * This method is called everytime a start element is found (an opening XML marker)
     * here we always reset the characters StringBuffer as we are only currently interested
     * in the the text values stored at leaf nodes
     *
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes atts) {

        if(localName.equals("item"))
        {
            inItem = true;
        }
        if (inItem)
        {
            if (localName.equals("title"))
            {
                inTitle = true;
                chars = new StringBuffer();
            }
        }
    }

    /*
     * This method is called everytime an end element is found (a closing XML marker)
     * here we check what element is being closed, if it is a relevant leaf node that we are
     * checking, such as Title, then we get the characters we have accumulated in the StringBuffer
     * and set the current "Estado"
     *
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(inItem && inTitle)
        {
            Log.d("Titulo", "titulo: " + chars.toString());
            inTitle = false;
            inItem = false;

            estado = chars.toString();
        }
    }

    /*
     * This method is called when characters are found in between XML markers, however, there is no
     * guarante that this will be called at the end of the node, or that it will be called only once
     * , so we just accumulate these and then deal with them in endElement() to be sure we have all the
     * text
     *
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public void characters(char ch[], int start, int length) {
        chars.append(new String(ch, start, length));
    }

    /**
     * This is the entry point to the parser and creates the feed to be parsed
     *
     * @param feedUrl
     * @return
     */

    /* Esta es la función mas importante de la clase, el resto son funciones auxiliares de parseo.
    * A la función le llega la url de la linea como string, descarga los datos y los parsea en busca
    * del tag "item" que es el que contiene una incidencia cuando existe. */
    public String getLatestArticles(String feedUrl) {
        URL url;
        try {

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            url = new URL(feedUrl);

            xr.setContentHandler(this);
            xr.parse(new InputSource(url.openStream()));

        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e("RSS Handler SAX", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("RSS Handler Parser Config", e.toString());
        }
        return estado;
    }
}
