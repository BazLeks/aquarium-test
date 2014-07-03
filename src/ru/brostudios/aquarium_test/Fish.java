package ru.brostudios.aquarium_test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;

public abstract class Fish extends View {

	private final int SIZE_FACTOR = 20;
	private ViewGroup parent;
	private boolean isEated; 
	
	public interface OnFishEatedListener { public void onEated(); }
	
	private int speed;
	private float direction;
	private OnFishEatedListener eatedListener;
	
	public Fish(Context context, float x, float y) {
		super(context);
		speed = (int)(Math.random()*4)+1;
		direction = (float)(Math.random()*2*Math.PI);
		setX(x); setY(y);
		setLayoutParams(new LayoutParams((int)(SIZE_FACTOR*(5-speed)*2), SIZE_FACTOR*(5-speed)));
		
		setRotation((float)Math.toDegrees(direction));
		
	}
	
	public void setOnFishEatedListener(OnFishEatedListener listener) {
		this.eatedListener = listener;
	}
	
	public void move() {
		parent = (ViewGroup) getParent();
		float newX = (float)(getX()+speed*Math.cos(direction));
		float newY = (float)(getY()+speed*Math.sin(direction));
		
		if(newX<0) { 
			newX = 0; 
			float oldDirection = direction;
			direction = (float)(Math.random()*Math.PI-Math.PI/2);
			setRotation((float)Math.toDegrees(direction));
			this.startAnimation(setWallRotate((float)Math.toDegrees(oldDirection-direction)));
		}
		if(newX+getWidth()>parent.getWidth()) { 
			newX = parent.getWidth()-getWidth(); 
			float oldDirection = direction;
			direction = (float)(Math.random()*Math.PI+Math.PI/2);
			setRotation((float)Math.toDegrees(direction));
			this.startAnimation(setWallRotate((float)Math.toDegrees(oldDirection-direction)));
		}
		
		if(newY<0) { 
			newY = 0;
			float oldDirection = direction;
			direction = (float)(Math.random()*Math.PI);
			setRotation((float)Math.toDegrees(direction));
			this.startAnimation(setWallRotate((float)Math.toDegrees(oldDirection-direction)));
		}
		if(newY+getHeight()>parent.getHeight()) { 
			newY = parent.getHeight()-getHeight();
			float oldDirection = direction;
			direction = -(float)(Math.random()*Math.PI);
			setRotation((float)Math.toDegrees(direction));
			this.startAnimation(setWallRotate((float)Math.toDegrees(oldDirection-direction)));
		}
		setX(newX); setY(newY);
	}
	
	protected abstract boolean cross(Fish fish);
	
	public void setEated() { isEated = true; if(eatedListener!=null) eatedListener.onEated(); }
	
	public final int getSize() { return 5-speed; }
	public final int getSpeed() { return speed; }
	
	private RotateAnimation setWallRotate(float from) {
		RotateAnimation animation = new RotateAnimation(from, 0, getX()+getWidth()/2, getY()+getHeight()/2);
		animation.setDuration(1000);
		return animation;
		
	}
}
