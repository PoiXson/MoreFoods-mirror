package com.poixson.foodrot;

import org.bukkit.Material;


public class ItemRotDAO {

	public final Material type;
	public final String name;
	public final int model, next;
	public final int delay;



	public ItemRotDAO(final Material type, final String name,
			final int model, final int next, final int delay) {
		this.type  = type;
		this.name  = name;
		this.model = model;
		this.next  = next;
		this.delay = delay;
	}



}
