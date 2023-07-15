package com.poixson.morefoods;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class FoodBuilder {

	public final LinkedList<Map<String, Object>> stages = new LinkedList<Map<String, Object>>();



	public FoodBuilder() {
	}



	public FoodBuilder addStage(final String title,
			final int model, final int next, final int factor) {
		final HashMap<String, Object> food = new HashMap<String, Object>();
		food.put("name", title);
		food.put("model", Integer.valueOf(model));
		if (next > 0)
			food.put("next",  Integer.valueOf(next ));
		if (factor > 0)
			food.put("delay", Integer.valueOf(factor));
		this.stages.addLast(food);
		return this;
	}
	public FoodBuilder addStage(final String title, final int model) {
		return this.addStage(title, model, 0, 0);
	}



	public List<Map<String, Object>> build() {
		return this.stages;
	}



}
