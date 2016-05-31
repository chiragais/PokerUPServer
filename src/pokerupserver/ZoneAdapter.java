package pokerupserver;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;

public class ZoneAdapter extends BaseZoneAdaptor {
    

	@Override
	public void handleCreateRoomRequest(IUser user, IRoom room,
			HandlingResult result) {
		System.out.println("Room Creatd " + room.getName());
		room.setAdaptor(new RoomAdapter(room));
	}
    /*
     * handleAddUserRequest is called for every user join room request.
     */
    @Override
    public void handleAddUserRequest(IUser user, String authString, HandlingResult result)
    {
        System.out.println("UserRequest " + user.getName());
    }   

}
