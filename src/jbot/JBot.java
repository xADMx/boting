/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbot;

import ChartDirector.ChartViewer;
import ChartDirector.RanTable;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javax.accessibility.AccessibleAction;
import javax.imageio.ImageIO;
import sun.rmi.server.UnicastRef;



/**
 *
 * @author ADM
 */
public class JBot {
      
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        HttpCilent client = new HttpCilent();
        client.GetTicker();
        client.GetPairCurrencies();
        client.GetChartData("BTC_XRP", "1405699200", "9999999999", "14400");

        
        getChartViewer(100, "BTC_XRP", PeriodB.val86400).getChart().getDrawArea().outJPG("saved1.png", 90);
    }
    
    static String getPeriod(PeriodB temp){
        String temps = "";
        switch (temp) {
            case val900:  
                temps = "900";
                break;
            case val1800:  
                temps = "1800";
                break;
            case val7200:  
                temps = "7200";
                break;
            case val14400:  
                temps = "14400"; 
                break;
            case val86400: 
                temps = "86400";
                break;
          }
        return temps;
    }
        
    static ChartViewer getChartViewer(int day, String pair, PeriodB period )
    {
        ChartViewer viewer = new ChartViewer();
        HttpCilent client = new HttpCilent();
        
        int start = 86400 *day;
        Map<String, double[]> temp = client.GetChartData(pair, Long.toString(System.currentTimeMillis()/1000 - start), "9999999999", getPeriod(period));
        // Create a FinanceChart object of width 640 pixels
        FinanceChart c = new FinanceChart(1500);
        // Add a title to the chart
        c.addTitle("График для пары " + pair);
        // Set the data into the finance chart object
        c.setData(temp.get("timeStamps"), 
                temp.get("highData"), 
                temp.get("lowData"), 
                temp.get("openData"), 
                temp.get("closeData"), 
                temp.get("volData"), day);

        // Add a slow stochastic chart (75 pixels high) with %K = 14 and %D = 3
        c.addSlowStochastic(75, 14, 3, 0x006060, 0x606000);
        // Add the main chart with 240 pixels in height
        c.addMainChart(600);
        
        c.addVolIndicator(75, 0x99ff99, 0xff9999, 0x808080);
        // Add a 10 period simple moving average to the main chart, using brown color
        c.addSimpleMovingAvg(10, 0x663300);
        // Add a 20 period simple moving average to the main chart, using purple color
        c.addSimpleMovingAvg(20, 0x9900ff);
        // Add candlestick symbols to the main chart, using green/red for up/down days
        c.addCandleStick(0x00ff00, 0xff0000);
        // Add 20 days donchian channel to the main chart, using light blue (9999ff) as the border
        // and semi-transparent blue (c06666ff) as the fill color
        c.addDonchianChannel(20, 0x9999ff, 0xc06666ff);
        // Add a 75 pixels volume bars sub-chart to the bottom of the main chart, using
        // green/red/grey for up/down/flat days
        c.addVolBars(75, 0x99ff99, 0xff9999, 0x808080);
        // Append a MACD(26, 12) indicator chart (75 pixels high) after the main chart, using 9 days
        // for computing divergence.
        c.addMACD(75, 26, 12, 9, 0x0000ff, 0xff00ff, 0x008000);

        // Output the chart
        viewer.setChart(c);
        return viewer;
    }
    
}
