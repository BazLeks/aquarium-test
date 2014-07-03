package ru.brostudios.aquarium_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

public class FishPredatory extends Fish {

	public FishPredatory(Context context, float x, float y) {
		super(context, x, y);
		//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.predatory);
		setBackgroundResource(R.drawable.predatory);	
		((AnimationDrawable)getBackground()).start();
	}

	@Override
	protected boolean cross(Fish fish) {
		int x1 = (int) (getX()+getWidth()/2);
		int y1 = (int) (getY()+getHeight()/2);
		int x2 = (int) (fish.getX()+fish.getWidth()/2);
		int y2 = (int) (fish.getY()+fish.getHeight()/2);
		int aveSize = (getWidth()+getHeight()+fish.getWidth()+getHeight())/6;
		int dx = x2-x1, dy = y2-y1;
		if(dy*dy+dx*dx<aveSize*aveSize) {
			if(getSize() >= fish.getSize()) return true;
			else {
				if(fish.getClass().equals(FishHerbivorous.class)) {
					if(getSize()+1 >= fish.getSize()) return true;
				}
			}
		}
		return false;
	}

}
