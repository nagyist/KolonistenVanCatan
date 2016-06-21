package nl.groep4.kvc.server.model;

import nl.groep4.kvc.common.enumeration.CardType;
import nl.groep4.kvc.common.enumeration.VictoryCardType;
import nl.groep4.kvc.common.interfaces.VictoryCard;

public class ServerVictoryCard extends ServerCard implements VictoryCard {

    private static final long serialVersionUID = 666L;

    private VictoryCardType subType;

    public ServerVictoryCard(CardType type, VictoryCardType subType) {
	super(type);
	this.subType = subType;
    }

    @Override
    public VictoryCardType getVictoryType() {
	return subType;
    }

}