/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class DateUtil {

    public static SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final Date formatDate(Date dt) {
        try {
            String strDt = null;
            strDt = dtFormat.format(dt);
            System.out.println("dateFormat: "+strDt);
            return dtFormat.parse(strDt);
        } catch (ParseException ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
            return dt;
        }
    }
}
