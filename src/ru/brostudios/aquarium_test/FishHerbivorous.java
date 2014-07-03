package ru.brostudios.aquarium_test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;

public class FishHerbivorous extends Fish {

	public FishHerbivorous(Context context, float x, float y) {
		super(context, x, y);
		setBackgroundResource(R.drawable.herbivolous);
		((AnimationDrawable)getBackground()).start();
	}

	@Override
	protected boolean cross(Fish fish) {
		return false;
	}

}
