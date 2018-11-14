package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.Card;
import com.geariot.platform.freelycar_wechat.entities.CardProjectRemainingInfo;

public interface CardDao {
	
	Card getCardById(int id);
	
	CardProjectRemainingInfo getProjectRemainingInfo(int cardId, int projectId);
	
	List<Card> findByMakerAccount(String account);
	
	long countProjectByIds(List<Integer> ids);
	
	List<Integer> getAvailableCardId(int projectId);
	
	List<Card> getAvailableCard(int clientId, List<Integer> cardIds);

	List<Card> listCardByClientId(int clientId);
	
	Card findByCardNumber(String cardNumber);

    void updateCard(Card card);

	Card updateBalanceByCardId(int cardId, float cardMoney, int handlerId);
}
