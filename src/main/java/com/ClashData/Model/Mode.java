package com.ClashData.Model;

/*
type : tournament + deckSelection : collection = CLASSIC_TOURNAMENT
type : challenge + isLadderTournament : true =  ROYALE_TOURNAMENT
type : pathOfLegend = PATH_OF_LEGEND
type : friendly + isHosted = true = CRL
type : PvP = TROPHY_MODE
 */

public enum Mode {
    CRL,
    ROYAL_TOURNAMENT,
    PATH_OF_LEGEND,
    TROPHY_MODE,
    CLASSIC_TOURNAMENT
}
