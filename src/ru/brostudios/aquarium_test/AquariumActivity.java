package ru.brostudios.aquarium_test;

import java.util.ArrayList;
import java.util.List;

import ru.brostudios.aquarium_test.Fish.OnFishEatedListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class AquariumActivity extends Activity implements Runnable {
	
	private FrameLayout frameLayout;
	private volatile Thread thread;
	private volatile List<Fish> fishes;
	
	private int aquariumWidth, aquariumHeight;	// get in "onCreate"
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aquarium);
		fishes = new ArrayList<Fish>();
		
		frameLayout = (FrameLayout)findViewById(R.id.frame_container);
		frameLayout.post(new Runnable() {
			
			@Override
			public void run() {
				aquariumWidth = frameLayout.getWidth();
				aquariumHeight = frameLayout.getHeight();
				
				// adding 10 fishes
				// generate non-repeatable positions
				for(int i=0;i<10;i++) {
					Fish fish = addFish();
					frameLayout.addView(fish);
					fishes.add(fish);
					fish.setOnFishEatedListener(fishListener);
				}
			}
		});
		
	}
	
	
	private OnFishEatedListener fishListener = new OnFishEatedListener() {
		
		@Override
		public void onEated() {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Fish fish = addFish();
								frameLayout.addView(fish);
								fishes.add(fish);
								fish.setOnFishEatedListener(fishListener);		
							}
						});
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	};
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		if(thread == null) thread = new Thread(this);
		thread.start();
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.d("", "");
	}
	@Override
	public void onPause() {
		thread = null;
		super.onPause();
	}

	@Override
	public void run() {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<fishes.size();i++) {
					fishes.get(i).move();
				}
			}
		};
		
		while(thread!=null) { 
			for(int i=0;i<fishes.size();i++) {
				for(int j=0;j<fishes.size();j++) {
					if(i==j) continue;
					if(i==fishes.size()-1) break;	// fishes size can change by another thread
					Fish fish = fishes.get(i);
					if(fish.cross(fishes.get(j))) {
						final Fish eated = fishes.get(j);
						fishes.remove(eated);						
						eated.setEated();
						eated.post(new Runnable() {
							@Override
							public void run() {
								AlphaAnimation animation = new AlphaAnimation(1f,0f);
								animation.setDuration(1000);
								eated.startAnimation(animation);
								eated.setAlpha(0f);
							}
						});
					}
				}
			}
			runOnUiThread(runnable);
			try {
				Thread.sleep(41);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Fish addFish() {
		// sets non-collision position
		boolean cross = false;
		float x, y, size;
		do {
			cross = false;
			x = (int)(Math.random()*aquariumWidth);
			y = (int)(Math.random()*aquariumHeight);
			for(int i=0;i<fishes.size();i++) {
				if(fishes.get(i).getX() == x && fishes.get(i).getY() == y) {
					cross = false; break;
				}
			}
		} while(cross);
		Fish addingFish = null;		
		int type = (int)Math.round(Math.random());
		switch(type) {
		case 0: addingFish = new FishHerbivorous(this, x, y); break;
		case 1: addingFish = new FishPredatory(this, x, y); break;
		}
		
		float fromX = x<aquariumWidth/2?-2*x:2*x;
		float fromY = y<aquariumHeight/2?-2*y:2*y;
		
		TranslateAnimation animation = new TranslateAnimation(fromX, 0, fromY, 0);
		animation.setDuration(1000);
		addingFish.setAnimation(animation);
		
		return addingFish;
	}
}
