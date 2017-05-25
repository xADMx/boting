/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbot;

import ChartDirector.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author ADM
 */
public class JBot {
      static Timer mTimer = new Timer();
      static GetData mMyTimerTask = new GetData();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
/*
        client.GetTicker();
        client.GetPairCurrencies();
        client.GetChartData("BTC_XRP", "1405699200", "9999999999", "14400");
        getChartViewer(40, "USDT_BTC", PeriodB.val14400).getChart().getDrawArea().outJPG("saved1.png", 90); 
        */

	mTimer.schedule(mMyTimerTask, 1000, 5000);
    }
           
   static class GetData extends TimerTask {
		@Override
		public void run() {
                   
		}
	}
    
}
