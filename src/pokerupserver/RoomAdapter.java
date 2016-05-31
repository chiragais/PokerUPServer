package pokerupserver;

import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;

public class RoomAdapter extends BaseRoomAdaptor{

    //Reference to current room
    private IRoom m_room;
    
    public RoomAdapter(IRoom room) {
        m_room = room;
    }
    
    /*
     * A user has joined a room
     */
    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        System.out.println("User Joined " + user.getName());
        m_room.BroadcastChat("PokerUP",user.getName()+ " join in room...");
    }
    
    /*
     * There is a new chat message in the room.
     */
    @Override
    public void handleChatRequest(IUser sender, String message, HandlingResult result)
    {
        System.out.println(sender.getName() + " says " + message);
    }
    
    /*
     * Timer
     * Called after particular interval defined in AppConfig.json
     */
    @Override
    public void onTimerTick(long time) {

    }

}
