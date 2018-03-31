package com.example.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Andres on 3/14/2018.
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        // Initializing the ArrayList
        this.applications = new ArrayList<>();
    }

    // Getter for the ArrayList
    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        // Proccessing an entry true or false (initialized to false)
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            // new PullParser object
            XmlPullParser xpp = factory.newPullParser();
            // A StringReader reads Strings as a stream
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        // Checks if the current parsed TAG is "entry" which is the one we need to find
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        // Sets the current TAG being parsed to the textValue variable
                        textValue = xpp.getText();
                        break;

                    // Stores the Text when we get to the End Tag
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        // Checks for inEntry being True
                        if (inEntry) {
                            // Check if the the current tagName is "Entry" to start getting what we need
                            if ("entry".equalsIgnoreCase(tagName)) {
                                applications.add(currentRecord);
                                // We reached the end of the "entry" TAG
                                inEntry = false;
                              // Checks the tagName for the exact info we want about the app
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                // Sets the values for the FeedEntry class
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;

                    default:
                        // Nothing else to do
                }
                eventType = xpp.next();
            }
            for (FeedEntry app : applications) {
                Log.d(TAG, "*****************************");
                Log.d(TAG, app.toString());
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
