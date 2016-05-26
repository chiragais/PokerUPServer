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
     * App Name : WAPokerGameZone
AppKey : 4318ddad-038a-409d-8

App Name : PokerGameZone
AppKey : 3689654b-d64f-421e-8
     */
    @Override
    public void onZoneCreated(IZone zone)
    {             
//    	System.out.println();
//    	System.out.print("Poker Server Extension : "+zone.getName());
    	if(zone.getAppKey().equals(TAXESS_POKERUP)|| zone.getAppKey().equals(TAXESS_POKERUP_LIVE)){
    		zone.setAdaptor(new TexassPokerZoneExtension(zone));
    	}
    }
}
