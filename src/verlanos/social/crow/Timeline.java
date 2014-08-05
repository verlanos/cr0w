package verlanos.social.crow;

import org.joda.time.*;

import java.io.Serializable;
import java.util.*;

/**
 * This class is a datastructure for representing a Timeline. A Timeline is a list of
 * messages posted by a User ordered by a timestamp.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */

public class Timeline implements Serializable{

    private SortedMap<Long,String> events;
    public Timeline(){
        this.events = new TreeMap<Long, String>();
    }

    /**
     * Adds an entry to this Timeline.
     * <p>
     *     This method creates a Timeline entry with a timestamp and the specified message.
     * </p>
     * @param message message to post to this Timeline
     */
    public void addEvent(String message)
    {
        long now = DateTimeUtils.currentTimeMillis();
        this.events.put(now, message);
    }

    /**
     * Returns a map of events in numerical order
     * @return SortedMap<Long,String>
     */
    public SortedMap<Long,String> getEvents()
    {
        return this.events;
    }

    /**
     * Returns a sorted map of events that are relative to current time and date
     * <p>
     *     This method creates a new Map containing Timeline entries that consist of the difference in time, when a
     *     message composed and when it was read (aka when this method is called) in milliseconds, and the message.
     *     The message will consists of the message body and an approximation of how long ago the message was posted
     *     relative to current time. e.g. "Hi everyone (10 seconds ago)".
     *     If a username is specified, the message is prefixed with the given username
     *     e.g. "John -> Hi everyone (10 seconds ago)".
     * </p>
     * @param username - (optional) if given, appends the string with a username otherwise leaves it blank
     * @return SortedMap<Long,String> chronologically sorted map of timeline entries
     */
    public SortedMap<Long,String> getEventsRelativeToCurrentTime(String username)
    {
        // String manipulation
        username = ((username == null) || (username.trim().equals(""))) ? " " : username+" -> ";
        StringBuilder timestring;

        // Date Time related
        DateTime current = DateTime.now(DateTimeZone.UTC);
        Interval interval;
        Period period;
        int years,months,days,hours,minutes,seconds;

        SortedMap<Long,String> relativeTimedEvents = new TreeMap<Long, String>();

        Iterator<Map.Entry<Long,String>> it = this.events.entrySet().iterator();

        for(Map.Entry<Long,String> entry : this.events.entrySet()){
            // Initialise string to build
            timestring = new StringBuilder(username);
            long millis = entry.getKey();

            // Append message to string
            timestring.append(entry.getValue()).append(" (");

            // Determine the difference between time message was composed and read (current time)
            interval = new Interval(new DateTime(millis),current);
            period = interval.toPeriod();

            // Append time units and values to the string
            if((years = period.getYears()) > 0){
                timestring.append(years).append(Utility.pluraliseAndPad(years,"year"));}
            if((months = period.getMonths()) > 0){
                timestring.append(months).append(Utility.pluraliseAndPad(months,"month"));}
            if((days = period.getDays()) > 0){
                timestring.append(days).append(Utility.pluraliseAndPad(days,"day"));}
            if((hours = period.getHours()) > 0){
                timestring.append(hours).append(Utility.pluraliseAndPad(hours,"hour"));}
            if((minutes = period.getMinutes()) > 0){
                timestring.append(minutes).append(Utility.pluraliseAndPad(minutes,"minute"));}
            if((seconds = period.getSeconds()) > 0){
                timestring.append(seconds).append(Utility.pluraliseAndPad(seconds,"second"));}

            timestring.append("ago)");
            relativeTimedEvents.put(interval.toDurationMillis(),timestring.toString());
            }
            return relativeTimedEvents;
        }

    /**
     * Returns a String representation of a Timeline
     * @param timeline a Timeline instance
     * @return Timeline String representation
     */
        public static String toText(Timeline timeline){
            StringBuilder text = new StringBuilder("Timeline:\n");
            for(Map.Entry<Long,String> event : timeline.getEvents().entrySet()){
                text.append(new Date(event.getKey())).append("    ").append(event.getValue());
            }
            return text.toString();
        }
    }
