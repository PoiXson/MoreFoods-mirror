package com.poixson.morefoods;

import org.bukkit.Material;


public class CustomFoodDAO {

	public final Material type;
	public final String name;
	public final int model, next;
	public final int factor;



	public CustomFoodDAO(final Material type, final String name,
			final int model, final int next, final int factor) {
		this.type   = type;
		this.name   = name;
		this.model  = model;
		this.next   = next;
		this.factor = factor;
	}



}
