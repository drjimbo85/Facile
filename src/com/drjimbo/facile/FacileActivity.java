package com.drjimbo.facile;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.facebook.android.Util;

public class FacileActivity extends Activity implements OnItemClickListener {
    /** Called when the activity is first created. */
	
	private ImageView mUserPic;
	private TextView mUserName;
	
	Facebook facebook = new Facebook("108657026108");
	public AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    private SharedPreferences mPrefs;
    
    String aPIresponse = "No response";
    
	String baseUrl = "https://graph.facebook.com/me/";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mUserPic = (ImageView) FacileActivity.this.findViewById(R.id.profileImage);
        mUserName = (TextView) FacileActivity.this.findViewById(R.id.textView1);
        
        
        /*
         * Get existing access_token if any
         */
        
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
            
        }   
        if(expires != 0) {
           facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        
        do {   
        
        	facebook.authorize(this, new String[] { "read_stream" , 
            		"friends_photos" , "friends_status" }, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                }
    
                @Override
                public void onFacebookError(FacebookError error) {
                	
                	error.printStackTrace();
                }
    
                @Override
                public void onError(DialogError e) {
                	
                	e.printStackTrace();
                }
    
                @Override
                public void onCancel() {}
                
            });
            
        }  while (!facebook.isSessionValid());
        
        
        /*
         * Get user photo
         */
        
        
        
        /*
         * Get user name
         */
        
    	String response = null;
    	
		try {
			response = facebook.request("me");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
       	JSONObject obj = null;
       	
		try {
			obj = Util.parseJson(response);
			aPIresponse = obj.getString("name");
				
		} catch (FacebookError e1) {
			
			aPIresponse = ("Facebook Error: " + e1);
			e1.printStackTrace();
			
		} catch (JSONException e1) {
			
            aPIresponse = ("JSON Exception: " + e1);
			e1.printStackTrace();
		}
		
    	mUserName.setText(aPIresponse);
    	
    	
//    	 /*
//         * Create logout button
//         */
//        
//        final Button button = (Button) findViewById(R.id.logoutButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            	
//            	try {
//					facebook.logout(getBaseContext());
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            	
//            	
//            }
//            
//           
//    });
        
    }
    
    @Override
    public void onResume() {    
        super.onResume();
        
        facebook.extendAccessTokenIfNeeded(this, null);
      
        
        }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
        
        }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		}
}
