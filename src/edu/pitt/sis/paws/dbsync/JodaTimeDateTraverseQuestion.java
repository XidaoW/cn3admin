package edu.pitt.sis.paws.dbsync;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 *
 * @author Jigar
 */
public class JodaTimeDateTraverseQuestion {
    public static void main(String[] args) {

        DateTime startDt = new DateTime(2013,1,1,0,0,0,0);//1st Dec 2010
        DateTime endDt = new DateTime(2013,1,31,0,0,0,0);//31st Dec 2010
        DateTime tempDate = new DateTime(2013,1,2,0,0,0,0);
        System.out.println(tempDate.getDayOfWeek());

        while(tempDate.compareTo(endDt) <=0 ){
            if(tempDate.getDayOfWeek() !=  DateTimeConstants.SATURDAY && tempDate.getDayOfWeek() !=  DateTimeConstants.SUNDAY){
                System.out.println(""+tempDate);
            }
            tempDate = tempDate.plusDays(1);

        }


    }
}