package pokerupserver.winner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pokerupserver.cards.Card;
import pokerupserver.handrank.GeneralHandManager;
import pokerupserver.payout.PayoutManager;
import pokerupserver.players.AllInPlayer;
import pokerupserver.players.PlayerBean;
import pokerupserver.players.PlayersManager;
import pokerupserver.utils.GameConstants;
import pokerupserver.utils.LogUtils;

public class WinnerManager implements GameConstants{

	PlayersManager playerManager;
	ArrayList<Winner> listWinners;
	ArrayList<AllInPlayer> listAllinPotAmounts;
	int totalTableAmount = 0;
	int remainingAmount = 0;
	
	

	GeneralHandManager generalHandManager ;
	public WinnerManager(PlayersManager playerMgr,GeneralHandManager generalHandManager ) {
		this.playerManager = playerMgr;
		this.generalHandManager = generalHandManager;
		listWinners = new ArrayList<Winner>();
		listAllinPotAmounts = new ArrayList<AllInPlayer>();
	}

	public void addWinner(Winner winner) {
		this.listWinners.add(winner);
	}

	public void addAllInTotalPotAmount(AllInPlayer allInPlayer) {
		this.listAllinPotAmounts.add(allInPlayer);
	}
	
	public int getPlayerWinningAmount(String playerName) {
		int winningAmount = 0;
		for (Winner winner : listWinners) {
			if (winner.getPlayer().getPlayerName().equals(playerName)) {
				winningAmount = winner.getWinningAmount();

			}
		}
		return winningAmount;
	}

	public int getAllInPotAmount(String playerName) {
		int allInPotAmount = 0;
		for (AllInPlayer allInplayer : listAllinPotAmounts) {
			if (allInplayer.getPlayerName().equals(playerName)) {
				allInPotAmount = allInplayer.getTotalAllInPotAmount();
			}
		}
		return allInPotAmount;
	}

	public void setTotalTableAmount(int amount) {
		this.totalTableAmount = amount;
	}

	public void setPlayerWinningAmount(String playerName, int winningAmount) {

		for (Winner winner : listWinners) {
			if (winner.getPlayer().getPlayerName().equals(playerName)) {
				winner.setWinningAmount(winningAmount);
				break;
			}
		}

	}

	public List<PlayerBean> generateWinnerPlayers() {
		List<PlayerBean> listWinnerPlayer = new ArrayList<PlayerBean>();
		List<PlayerBean> listAllActivePlayers = playerManager.getAllAactivePlayersForWinning(); 
		// Sort winner players
		Collections.sort(listAllActivePlayers,
				new Comparator<PlayerBean>() {
					@Override
					public int compare(PlayerBean player1, PlayerBean player2) {
						return player1.getHandRank().compareTo(
								player2.getHandRank());
					}
				});
//		LogUtils.Log("Before sort...........");
//		for (PlayerBean playerBean : listAllActivePlayers) {
//			LogUtils.Log("Current Pla : " + playerBean.getPlayerName()
//					+ " >> " + playerBean.getBestHandRankTotal() + " >> "
//					+ playerBean.getHandRank());
//		}
//		LogUtils.Log("Before sort...........");
		for (int i = 0; i < listAllActivePlayers.size(); i++) {
			List<PlayerBean> sameRankPlayer = new ArrayList<PlayerBean>();
			PlayerBean currentPlayer = listAllActivePlayers
					.get(i);
			if (!listWinnerPlayer.contains(currentPlayer)) {
				sameRankPlayer.add(currentPlayer);
				for (int j = i + 1; j < listAllActivePlayers
						.size(); j++) {
					PlayerBean nextPlayer = listAllActivePlayers.get(j);
					if (currentPlayer.getHandRank() == nextPlayer.getHandRank()) {
						sameRankPlayer.add(nextPlayer);
					}
				}
				Collections.sort(sameRankPlayer, new Comparator<PlayerBean>() {
					@Override
					public int compare(PlayerBean player1, PlayerBean player2) {
						return getBestHandPlayer(player1,player2);
					}
				});
				listWinnerPlayer.addAll(sameRankPlayer);
			}
		}

		for (PlayerBean playerBean : listWinnerPlayer) {
			LogUtils.Log("Current Pla : " + playerBean.getPlayerName()
					+ " >> " + playerBean.getBestHandRankTotal() + " >> "
					+ playerBean.getHandRank());
		}
		return listWinnerPlayer;
	}

	
	public void findWinnerPlayers(int gameType) {
//		LogUtils.Log("\n Find Winner Player ------------"+gameType);
		List<PlayerBean> listAscWinningPlayers = generateWinnerPlayers(); 

		if(gameType==GAME_TYPE_TOURNAMENT_SIT_N_GO || gameType==GAME_TYPE_TOURNAMENT_REGULAR){
			PayoutManager payoutManager = new PayoutManager(9);
			for (int i =0;i<listAscWinningPlayers.size();i++) {
				PlayerBean player = listAscWinningPlayers.get(i);
				float winningPercentage = payoutManager.getWinnigPercentage(i);
				if (winningPercentage > 0f) {
					float winningAmt = (totalTableAmount * winningPercentage) / 100;
					Winner winner = new Winner(player,(int) winningAmt);
					winner.getPlayer().setBalance(
							winner.getPlayer().getBalance()
									+ winner.getWinningAmount());
					listWinners.add(winner);
					LogUtils.Log("SNG : Player : "
							+ player.getPlayerName() + " > Rank Index : " + i
							+ " > Payout : " + winningPercentage
							+ " > Winning Amt : " + winningAmt
							+ " > Pot Amt : " + totalTableAmount);
				}
			}
			totalTableAmount = 0;
		}
		if (gameType == GAME_TYPE_REGULAR) {
			// For Regular game
			for (PlayerBean player : listAscWinningPlayers) {
				if (!player.isFolded()) {
					if (!player.isAllIn()) {
						Winner winner = new Winner(player, totalTableAmount);
						winner.getPlayer().setBalance(
								winner.getPlayer().getBalance()
										+ winner.getWinningAmount());
						totalTableAmount = 0;
						listWinners.add(winner);
						break;
					} else {
						if (getAllInPotAmount(player.getPlayerName()) < totalTableAmount) {
							Winner winner = new Winner(player,
									getAllInPotAmount(player.getPlayerName()));
							winner.getPlayer().setBalance(
									winner.getPlayer().getBalance()
											+ winner.getWinningAmount());
							totalTableAmount -= getAllInPotAmount(player
									.getPlayerName());
							listWinners.add(winner);
						} else {
							Winner winner = new Winner(player, totalTableAmount);
							winner.getPlayer().setBalance(
									winner.getPlayer().getBalance()
											+ winner.getWinningAmount());
							totalTableAmount = 0;
							listWinners.add(winner);
							break;
						}
					}
				}
			}
		}
//		else
		
		if (totalTableAmount != 0) {
			remainingAmount = totalTableAmount;
		}
		LogUtils.Log("\n ---------------------------------");
		for (Winner player : listWinners) {
			LogUtils.Log("\n Winner Player =  "
					+ player.getPlayer().getPlayerName() +" :: Amount : "+player.getWinningAmount());
		}

	}
public  int getBestHandPlayer(PlayerBean playerBean1,PlayerBean playerBean2){
		
		if(playerBean1.getHandRank().equals(playerBean2.getHandRank())){
			
			// If Pair is Full House
			if (playerBean1.getHandRank().equals(HAND_RANK.FULL_HOUSE)) {
				List<List<Card>> listOfListSameRankCardsForPlayer1 = generalHandManager.listAllSameRankCards(playerBean1.getMainHandCards());
				List<List<Card>> listOfListSameRankCardsForPlayer2 = generalHandManager.listAllSameRankCards(playerBean2.getMainHandCards());
				List<Card> listPlayer1Cards = new ArrayList<Card>();
				List<Card> listPlayer2Cards = new ArrayList<Card>();
				
				if(listOfListSameRankCardsForPlayer1.size()==2 && listOfListSameRankCardsForPlayer2.size()==2){
					if(listOfListSameRankCardsForPlayer1.get(0).size()==3){
						listPlayer1Cards.addAll(listOfListSameRankCardsForPlayer1.get(0));
						listPlayer1Cards.addAll(listOfListSameRankCardsForPlayer1.get(1));
					}else{
						listPlayer1Cards.addAll(listOfListSameRankCardsForPlayer1.get(1));
						listPlayer1Cards.addAll(listOfListSameRankCardsForPlayer1.get(0));
					}
					
					if(listOfListSameRankCardsForPlayer2.get(0).size()==3){
						listPlayer2Cards.addAll(listOfListSameRankCardsForPlayer2.get(0));
						listPlayer2Cards.addAll(listOfListSameRankCardsForPlayer2.get(1));
					}else{
						listPlayer2Cards.addAll(listOfListSameRankCardsForPlayer2.get(1));
						listPlayer2Cards.addAll(listOfListSameRankCardsForPlayer2.get(0));
					}
					for(int i =0 ;i<listPlayer1Cards.size();i++){
						if (listPlayer1Cards.get(i).getValue() == listPlayer2Cards.get(i).getValue()) {

						} else if (listPlayer1Cards.get(i).getValue() > listPlayer2Cards.get(i).getValue()) {
							return -1;
						} else if (listPlayer1Cards.get(i).getValue() < listPlayer2Cards.get(i).getValue()) {
							return 1;
						}
						
					}
				}
			} else {
				for (int i = 0; i < playerBean1.getMainHandCards().size(); i++) {
					if (playerBean1.getMainHandCards().get(i).getValue() == playerBean2
							.getMainHandCards().get(i).getValue()) {

					} else if (playerBean1.getMainHandCards().get(i).getValue() > playerBean2
							.getMainHandCards().get(i).getValue()) {
						return -1;
					} else if (playerBean1.getMainHandCards().get(i).getValue() < playerBean2
							.getMainHandCards().get(i).getValue()) {
						return 1;
					}
				}
			}
			for (int i = 0; i < playerBean1.getBestHandCards().size(); i++) {
				if (playerBean1.getBestHandCards().get(i).getValue() == playerBean2
						.getBestHandCards().get(i).getValue()) {

				} else if (playerBean1.getBestHandCards().get(i).getValue() > playerBean2
						.getBestHandCards().get(i).getValue()) {
					return -1;
				} else if (playerBean1.getBestHandCards().get(i).getValue() < playerBean2
						.getBestHandCards().get(i).getValue()) {
					return 1;
				}
			}
		}
		return 0;
	}
	public List<AllInPlayer> getAllInPlayers(){
		return listAllinPotAmounts;
	}
	public ArrayList<Winner> getWinnerList() {
		return listWinners;
	}

	public Winner getTopWinner() {
		return listWinners.get(0);
	}

}
