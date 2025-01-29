package com.poixson.morefoods;

import static com.poixson.utils.Utils.IsEmpty;

import com.poixson.tools.localization.LangShelfPaper;


public enum MoreFoodsDefines {



	// -------------------------------------------------------------------------------
	// commands

	CMD_NAMESPACE("morefoods"),

	// /food
	CMD_LABELS_FOOD("morefoods", "food"),
	CMD_DESC_FOOD,

	// /food info
	CMD_LABELS_FOOD_INFO("info"),
	CMD_DESC_FOOD_INFO,

	// /food fresh
	CMD_LABELS_FOOD_FRESH("fresh"),
	CMD_DESC_FOOD_FRESH,

	// /food rot
	CMD_LABELS_FOOD_ROT("rot"),
	CMD_DESC_FOOD_ROT,



	// -------------------------------------------------------------------------------
	// permissions

	PERM_CMD_FOOD_INFO ("morefoods.cmd.info" ),
	PERM_CMD_FOOD_FRESH("morefoods.cmd.fresh"),
	PERM_CMD_FOOD_ROT  ("morefoods.cmd.rot"  ),

	PERM_FOOD_DETAILED("morefoods.detailed"),



	// -------------------------------------------------------------------------------
;
	public static final LangShelfPaper Lang = new LangShelfPaper();

	public final String   NODE;
	public final String[] NODES;



	MoreFoodsDefines() {
		this(null);
	}
	MoreFoodsDefines(final String node, final String...nodes) {
		this.NODE = (IsEmpty(node) ? this.name() : node);
		this.NODES = nodes;
	}



	@Override
	public String toString() {
		return this.NODE;
	}
	public String[] toStrings() {
		return this.NODES;
	}



}
