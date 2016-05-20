/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerup;

import pokerup.utils.GameConstants;
import pokerup.zone.TexassPokerZoneExtension;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

/**
 * @author Chirag
 */
public class PokerServerExtension extends BaseServerAdaptor implements GameConstants{
    /**
     * 	App Name : PokerGameZone
		AppKey : 6458abad-9ce4-4477-b
     */
    @Override
    public void onZoneCreated(IZone zone)
    {             
    	System.out.println();
    	System.out.print("Poker Server Extension : "+zone.getName());
    	if(zone.getAppKey().equals(TEXASS_APP_KEY) || zone.getAppKey().equals(TEXASS_APP_KEY_LOCAL)){
    		zone.setAdaptor(new TexassPokerZoneExtension(zone));
    	}
    }
}
