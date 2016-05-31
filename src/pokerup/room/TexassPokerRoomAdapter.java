package pokerup.room;

import pokerup.utils.GameConstants;
import pokerup.utils.LogUtils;

import com.shephertz.app42.server.idomain.BaseTurnRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.ITurnBasedRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;

/**
 * 
 * @author Chirag
 */
public class TexassPokerRoomAdapter extends BaseTurnRoomAdaptor implements
		GameConstants {

	private ITurnBasedRoom gameRoom;
	
	public TexassPokerRoomAdapter(IZone izone, ITurnBasedRoom room) {
		this.gameRoom = room;
}

	@Override
	public void onTimerTick(long time) {
	
	}
		/**
	 * Invoked when the timer expires for the current turn user.
	 * 
	 * By default, the turn user will be updated to the next user in order of
	 * joining and a move notification with empty data will be sent to all the
	 * subscribers of the room.
	 * 
	 * @param turn
	 *            the current turn user whose turn has expired.
	 * @param result
	 *            use this to override the default behavior
	 */
	@Override
	public void handleTurnExpired(IUser turn, HandlingResult result) {
		LogUtils.Log("onTurnExpired : Turn User : " + turn.getName());
	}
	/**
	 * Invoked when a move request is received from the client whose turn it is.
	 * 
	 * By default, the sender will be sent back a success response, the turn
	 * user will be updated to the next user in order of joining and a move
	 * notification will be sent to all the subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the move
	 * @param moveData
	 *            the move data sent by the user
	 * @param result
	 *            use this to override the default behavior
	 */
	public boolean isRoundCompelete = false;

		
	public void managePlayerAction(String sender, int playerAction,
			int betAmount) {
		}


	public void handleUserLeavingTurnRoom(IUser user, HandlingResult result) {
		
		
	}


	/**
	 * Invoked when a chat request is received from the client in the room.
	 * 
	 * By default this will trigger a success response back to the client and
	 * will broadcast a notification message to all the subscribers of the room.
	 * 
	 * 
	 * @param sender
	 *            the user who has sent the request
	 * @param message
	 *            the message that was sent
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleChatRequest(IUser sender, String message,
			HandlingResult result) {
		LogUtils.Log("ChatRequest :  User : " + sender.getName()
				+ " : Message : " + message);
		
	}
	/**
	 * Invoked when a join request is received by the room and the number of
	 * joined users is less than the maxUsers allowed.
	 * 
	 * By default this will result in a success response sent back the user, the
	 * user will be added to the list of joined users of the room and a user
	 * joined room notification will be sent back to all the subscribed users of
	 * the room.
	 * 
	 * @param user
	 *            the user who has sent the request
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleUserJoinRequest(IUser user, HandlingResult result) {
		LogUtils.Log(">>UserJoinRequest :  User : " + user.getName());
		user.SendChatNotification(TEXASS_SERVER_NAME, RESPONSE_FOR_PLAYERS_INFO
				+ "{\"Player_Status\":1,\"Game_Status\":71,\"Current_Round\":0,\"Card1\":\"eight_club\",\"Card2\":\"six_spade\",\"Player_Position\":1,\"Player_Name\":\"1775\",\"Player_Balance\":1000}", gameRoom);
		
//		// Handle player request
//		gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_PLAYERS_INFO
//				+ "{\"Player_Status\":1,\"Game_Status\":71,\"Current_Round\":0,\"Card1\":\"eight_club\",\"Card2\":\"six_spade\",\"Player_Position\":1,\"Player_Name\":\"1775\",\"Player_Balance\":1000}");

	}
	
	}
