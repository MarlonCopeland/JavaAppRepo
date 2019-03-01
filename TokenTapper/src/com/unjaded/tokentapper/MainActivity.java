/* extra code
 * Toasting --
 * Context context = getApplicationContext();
	    Toast toast = Toast.makeText(context, "OnStartEvent", toastDuration);
	    toast.show();
	    */


package com.unjaded.tokentapper;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;

public class MainActivity extends Activity {
	private boolean game = true;
	private float TPS = 0;
	private float TokenCount = 0;
	private float TapRate = 1;
	private long TokenDisplay = 0;
	private long MeterCost = 50;
	private long MeterCount = 0;
	private long ArcadeCost = 500;
	private long ArcadeCount = 0;
	private long SlotCost = 2000;
	private long SlotCount = 0;
	private long SubwayCost = 10000;
	private long SubwayCount = 0;
	private long QuarryCost = 1000000;
	private long QuarryCount = 0;
	private long TroveCost = 100000;
	private long TroveCount = 0;
	private long DeviceCount = 0;
	
	int toastDuration = Toast.LENGTH_SHORT;
	//final MediaPlayer coin1 = MediaPlayer.create(this, R.raw.coindrop1);
	private SoundManager mSoundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//***ADDDDSSADSADDSADDSADDSADSADSADSADS****///
		View v = inflater.inflate(R.layout.activity_main, container, false);
	    mAdStatus = (TextView) v.findViewById(R.id.status);
	    mAdView = (AdView) v.findViewById(R.id.ad);
	    mAdView.setAdListener(new MyAdListener());

	    AdRequest adRequest = new AdRequest();
	    adRequest.addKeyword("sporting goods");
	    mAdView.loadAd(adRequest);
	    return v;
		
		
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Log.w("TokenTapperState", "OnCreate");
		Log.w("TokentapperVariable", String.valueOf(game));
		
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(getBaseContext());
		
		mSoundManager.addSound(1, R.raw.coindrop1);
		mSoundManager.addSound(2, R.raw.coindrop2);
		mSoundManager.addSound(3, R.raw.coindrop3);
		
		
	    
        
		//check for existence of save file
		
		SharedPreferences mPrefs = getPreferences(Context.MODE_PRIVATE);
		
		if(mPrefs.contains("TPS")){
		//if Prefs exists, set variables
        TPS = mPrefs.getFloat("TPS", TPS);
        TokenCount = mPrefs.getFloat("TokenCount", TokenCount);
        TapRate = mPrefs.getFloat("TapRate", TapRate);
        TokenDisplay = mPrefs.getLong("TokenDisplay", TokenDisplay);
        MeterCount = mPrefs.getLong("MeterCount", MeterCount);
        ArcadeCount = mPrefs.getLong("ArcadeCount", ArcadeCount);
        SlotCount = mPrefs.getLong("SlotCount", SlotCount);
        QuarryCount = mPrefs.getLong("QuarryCount", QuarryCount);
        SubwayCount = mPrefs.getLong("SubwayCount", SubwayCount);
        TroveCount = mPrefs.getLong("TroveCount", TroveCount);
        DeviceCount = mPrefs.getLong("DeviceCount", DeviceCount);
		}
		
        // re-calculate cost based on count of Devices
        MeterCost = (50 + Math.round((MeterCount * .72)));
        ArcadeCost = 500;
        
        //new TokenCounter().execute();
		
		
        ImageButton ib = (ImageButton)findViewById(R.id.imgBigToken);
		ib.setSoundEffectsEnabled(false);
	    ib.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	//calculate taprate
	        	TapRate = (TPS/100) + (DeviceCount/100) + 1;
	        	
	        	TokenCount += TapRate;
	        	TextView t;

	        	    t=(TextView)findViewById(R.id.txtTokenCount); 
	        	    TokenDisplay = Math.round(TokenCount);
	        	    t.setText(String.valueOf(TokenDisplay));
	        	    
	        	 // coin1.start();
	        	    Random r = new Random();
	        	    int x = r.nextInt(3) + 1;
	        	   mSoundManager.playSound(x);
	        	   
	        	  t=(TextView)findViewById(R.id.txtTapRate); 
	        	  t.setText(String.valueOf(TapRate));
	        	  t.setVisibility(0);
	        	  
	        	    
	        }
	        
	});
	    
	    
	    ImageButton meterBuy = (ImageButton)findViewById(R.id.imgMeterBuy);
	    meterBuy.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	if(TokenCount >= MeterCost){
	        		TokenCount -= MeterCost;
	        		MeterCount += 1;
	        		TPS += .8;
	        		
	        		//update meter
	        		UpdateDevicesAndTokens();
	        	    
	        	   
	        	}
	        	    
	        }
	        
	});
	    
	    ImageButton arcadeBuy = (ImageButton)findViewById(R.id.imgArcadeBuy);
	    arcadeBuy.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	if(TokenCount >= ArcadeCost){
	        		TokenCount -= ArcadeCost;
	        		ArcadeCount += 1;
	        		TPS += 5.5;
	        		
	        		//update meter
	        		UpdateDevicesAndTokens();
	        	    
	        	    //update tokens
	        	    
	        	}
	        	    
	        }
	        
	});
	    
	    ImageButton slotBuy = (ImageButton)findViewById(R.id.imgSlotBuy);
	    slotBuy.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	if(TokenCount >= SlotCost){
	        		TokenCount -= SlotCost;
	        		SlotCount += 1;
	        		TPS += 22.4;
	        		
	        		//update meter
	        		UpdateDevicesAndTokens();
	      
	        	}
	        	    
	        }
	        
	});
	    
	    ImageButton subwayBuy = (ImageButton)findViewById(R.id.imgSubwayBuy);
	    subwayBuy.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	if(TokenCount >= SubwayCost){
	        		TokenCount -= SubwayCost;
	        		SubwayCount += 1;
	        		TPS += 53.7;
	        		
	        		//update meter
	        		UpdateDevicesAndTokens();
	      
	        	}
	        	    
	        }
	        
	});
	    
	    ImageButton quarryBuy = (ImageButton)findViewById(R.id.imgQuarryBuy);
	    quarryBuy.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	if(TokenCount >= QuarryCost){
	        		TokenCount -= QuarryCost;
	        		QuarryCount += 1;
	        		TPS += 99.1;
	        		
	        		//update meter
	        		UpdateDevicesAndTokens();
	      
	        	}
	        	    
	        }
	        
	});
	    
	    ImageButton troveBuy = (ImageButton)findViewById(R.id.imgTroveBuy);
	    troveBuy.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	if(TokenCount >= TroveCost){
	        		TokenCount -= TroveCost;
	        		TroveCount += 1;
	        		TPS += 75.3;
	        		
	        		//update meter
	        		UpdateDevicesAndTokens();
	      
	        	}
	        	    
	        }
	        
	});
	   
	   
	    
	    runThreadForUI();
	   
	    runThreadForLogic();
	}
	
	 //Add reset menu option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset_game:
                resetGame();
                return true;
            /*case R.id.help:
                showHelp();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
	private void resetGame() {
		TPS = 0;
        TokenCount = 0;
        TapRate = 1;
        TokenDisplay = 0;
        MeterCount = 0;
        ArcadeCount = 0;
        SlotCount = 0;
        QuarryCount = 0;
        SubwayCount = 0;
        TroveCount = 0;
        DeviceCount = 0;
        
        MeterCost = 50;
        ArcadeCost = 500;
        SlotCost = 2000;
        QuarryCost = 1000000;
        TroveCost = 100000;
        SubwayCost = 10000;
        
		
	}

	private void runThreadForUI() {

	    new Thread() {
	        @Override
			public void run() {
	            while (game) {
	                try {
	                    runOnUiThread(new Runnable() {

	                        @Override
	                        public void run() {
	                        	TextView t;
	                    		//update TPS
	                    	    t=(TextView)findViewById(R.id.txtTPS); 
	                    	    t.setText(String.valueOf(TPS));
	                    	    
	                    	    //update tokens
	                    	    t=(TextView)findViewById(R.id.txtTokenCount); 
	                    	    TokenDisplay = Math.round(TokenCount);
	        	        	    t.setText(String.valueOf(TokenDisplay));
	                    	    
	                    	    ImageView i;
	                    	    //check for available upgrades
	                    	    if (TokenCount >= MeterCost){
	                    	    	//u can buy meters
	                    	    	
	                    	    	i=(ImageView)findViewById(R.id.imgMeterBuy);
	                    	    	i.setImageResource(R.drawable.meter);
	                    	    	
	                    	    }
	                    	    else
	                    	    {
	                    	    	// can't buy meters fade them
	                    	    	i=(ImageView)findViewById(R.id.imgMeterBuy);
	                    	    	i.setImageResource(R.drawable.meter60x60faded);
	                    	    }
	                    	    if (TokenCount >= ArcadeCost){
	                    	    	//u can buy arcades
	                    	    	
	                    	    	i=(ImageView)findViewById(R.id.imgArcadeBuy);
	                    	    	i.setImageResource(R.drawable.arcadegame100x100);
	                    	    	
	                    	    }
	                    	    else{
	                    	    	i=(ImageView)findViewById(R.id.imgArcadeBuy);
	                    	    	i.setImageResource(R.drawable.arcadegame100x100faded);
	                    	    }
	                    	    if (TokenCount >= SlotCost){
	                    	    	//u can buy slots
	                    	    	
	                    	    	i=(ImageView)findViewById(R.id.imgSlotBuy);
	                    	    	i.setImageResource(R.drawable.slotmachine100x100);
	                    	    	
	                    	    }
	                    	    else
	                    	    {
	                    	    	i=(ImageView)findViewById(R.id.imgSlotBuy);
	                    	    	i.setImageResource(R.drawable.slotmachine100x100faded);
	                    	    }
	                    	    if (TokenCount >= SubwayCost){
	                    	    	//u can buy subways
	                    	    	
	                    	    	i=(ImageView)findViewById(R.id.imgSubwayBuy);
	                    	    	i.setImageResource(R.drawable.subway100x100);
	                    	    	
	                    	    }
	                    	    else
	                    	    {
	                    	    	i=(ImageView)findViewById(R.id.imgSubwayBuy);
	                    	    	i.setImageResource(R.drawable.subway100x100faded);
	                    	    }
	                    	    if (TokenCount >= QuarryCost){
	                    	    	//u can buy quarrys
	                    	    	
	                    	    	i=(ImageView)findViewById(R.id.imgQuarryBuy);
	                    	    	i.setImageResource(R.drawable.quarry100x100);
	                    	    	
	                    	    }
	                    	    else
	                    	    {
	                    	    	i=(ImageView)findViewById(R.id.imgQuarryBuy);
	                    	    	i.setImageResource(R.drawable.quarry100x100faded);
	                    	    }
	                    	    if (TokenCount >= TroveCost){
	                    	    	//u can buy troves
	                    	    	
	                    	    	i=(ImageView)findViewById(R.id.imgTroveBuy);
	                    	    	i.setImageResource(R.drawable.trove100x100);
	                    	    	
	                    	    }
	                    	    else
	                    	    {
	                    	    	i=(ImageView)findViewById(R.id.imgTroveBuy);
	                    	    	i.setImageResource(R.drawable.trove100x100faded);
	                    	    }
	                        }
	                    });
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }.start();
	}
	

	private void runThreadForLogic() {

	    new Thread() {
	        @Override
			public void run() {   
	                try {
	                    while(game){
	                    	
	                    	Thread.sleep(50);
	        				TokenCount += (TPS/20);
	                    }
	                        
	                    }
	                    catch (InterruptedException e) {
	                    e.printStackTrace();
	                    }
	            
	        }
	    }.start();
	}
	
	/*start junk code
	private class TokenCounter extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			while(game == true){
			try {
				Thread.sleep(100);
				TokenCount += (TPS/10);
				
				//update number of tokens        	    
        	    //UpdateTextViews();
        	    
        	    //update tokens per second
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			return null;
		}		
	}*/
/*end junk code*/

	/* ACTIVITY EVENTS  */
	@Override
	protected void onStart() {
	    super.onStart();  // Always call the superclass method first
	    
	   //update devices
	    UpdateDevicesAndTokens();
	    
	    Log.w("TokenTapperState", "OnStart");
	}
	
	@Override
	protected void onRestart() {
	    super.onStart();  // Always call the superclass method first    
	    
	    Log.w("TokenTapperState", "OnRestart");
	}
	
	@Override
	public void onPause() {
	    super.onPause();  // Always call the superclass method first
	    Log.w("TokenTapperState", "OnPause");
	    SharedPreferences mPrefs = getPreferences(Context.MODE_PRIVATE);
	    
	    SharedPreferences.Editor ed = mPrefs.edit();
        ed.putFloat("TPS", TPS);
        ed.putFloat("TokenCount",TokenCount);
        ed.putFloat("TapRate", TapRate);
        ed.putLong("TokenDisplay", TokenDisplay);
        ed.putLong("ArcadeCount", ArcadeCount);
        ed.putLong("MeterCount", MeterCount);
        ed.putLong("SlotCount", SlotCount);
        ed.putLong("SubwayCount", SubwayCount);
        ed.putLong("QuarryCount", QuarryCount);
        ed.putLong("TroveCount", SlotCount);
        ed.putLong("DeviceCount", DeviceCount);
        
        ed.commit();
       
	}
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    Log.w("TokenTapperState", "OnResume");
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    
	    Log.w("TokenTapperState", "OnStop");
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();  // Always call the superclass method first
	   
	    Log.w("TokenTapperState", "OnDestroy");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void UpdateDevicesAndTokens(){
		TextView t;
		
		t=(TextView)findViewById(R.id.txtArcadeCount); 
	    t.setText(String.valueOf(ArcadeCount));
	    
	    t=(TextView)findViewById(R.id.txtMeterCount); 
	    t.setText(String.valueOf(MeterCount));
	    
	    t=(TextView)findViewById(R.id.txtSlotCount); 
	    t.setText(String.valueOf(SlotCount));
	    
	    t=(TextView)findViewById(R.id.txtSubwayCount); 
	    t.setText(String.valueOf(SubwayCount));
	    
	    t=(TextView)findViewById(R.id.txtQuarryCount); 
	    t.setText(String.valueOf(QuarryCount));
	    
	    t=(TextView)findViewById(R.id.txtTroveCount); 
	    t.setText(String.valueOf(TroveCount));
	    
	    t=(TextView)findViewById(R.id.txtTokenCount); 
	    TokenDisplay = Math.round(TokenCount);
	    t.setText(String.valueOf(TokenDisplay));
	}
}
