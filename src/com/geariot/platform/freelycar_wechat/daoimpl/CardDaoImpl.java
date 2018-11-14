package com.geariot.platform.freelycar_wechat.daoimpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.CardDao;
import com.geariot.platform.freelycar_wechat.entities.Card;
import com.geariot.platform.freelycar_wechat.entities.CardProjectRemainingInfo;
import com.geariot.platform.freelycar_wechat.utils.Constants;

@Repository
public class CardDaoImpl implements CardDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public Card getCardById(int id) {
		String hql = "from Card where id = :id";
		return (Card) this.getSession().createQuery(hql).setInteger("id", id)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public CardProjectRemainingInfo getProjectRemainingInfo(int cardId, int projectId) {
		String hql = "from CardProjectRemainingInfo where cardId = :cardId and projectId = :projectId";
		return (CardProjectRemainingInfo) this.getSession().createQuery(hql).setInteger("cardId", cardId).setInteger("projectId", projectId)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Card> findByMakerAccount(String account) {
		String hql = "from Card where orderMaker.account = :account";
		return this.getSession().createQuery(hql).setString("account", account)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public long countProjectByIds(List<Integer> ids) {
		String hql = "select count(*) from CardProjectRemainingInfo where project.id in :list";
		return (long) this.getSession().createQuery(hql).setParameterList("list", ids)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAvailableCardId(int projectId) {
		String sql = "select cardId from cardprojectremaininginfo where remaining > 0 and projectId = :id";
		return this.getSession().createSQLQuery(sql).setInteger("id", projectId)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Card> getAvailableCard(int clientId, List<Integer> cardIds) {
		String sql = "select * from card where clientId = :clientId and id in :cardIds and expirationDate > :now";
		Date now = new Date(System.currentTimeMillis());
		return this.getSession().createSQLQuery(sql).setInteger("clientId", clientId).setParameterList("cardIds", cardIds)
				.setTime("now", now).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Card> listCardByClientId(int clientId) {
		String sql = "from Card where clientId = :clientId and expirationDate > :now";
		Date now = new Date(System.currentTimeMillis());
		return this.getSession().createQuery(sql).setInteger("clientId", clientId)
				.setTimestamp("now", now).setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public Card findByCardNumber(String cardNumber) {
		String hql = "from Card where cardNumber = :cardNumber";
		return (Card) this.getSession().createQuery(hql).setString("cardNumber", cardNumber).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}


	@Override
	public void updateCard(Card card) {
		getSession().update(card);
	}

	@Override
	public Card updateBalanceByCardId(int cardId, float cardMoney, int handlerId) {
		Card card = getSession().get(Card.class, cardId);
		card.setBalance(card.getBalance() + cardMoney);
		card.setHandlerId(handlerId);
		//有效期延长一年
		Calendar c = Calendar.getInstance();
		c.setTime(card.getExpirationDate());
		c.add(Calendar.YEAR, 1);
		card.setExpirationDate(c.getTime());

		updateCard(card);
		return card;
	}

}
