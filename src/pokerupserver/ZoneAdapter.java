package pokerupserver;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;

public class ZoneAdapter extends BaseZoneAdaptor {
	private IZone izone;
	public ZoneAdapter(IZone izone) {
		System.out.println();
    	System.out.print("Zone : "+izone.getName());
		this.izone = izone;
	}
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
