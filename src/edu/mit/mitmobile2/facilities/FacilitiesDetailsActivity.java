package edu.mit.mitmobile2.facilities;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import edu.mit.mitmobile2.Global;
import edu.mit.mitmobile2.Module;
import edu.mit.mitmobile2.ModuleActivity;
import edu.mit.mitmobile2.R;
import edu.mit.mitmobile2.TwoLineActionRow;
import edu.mit.mitmobile2.objs.FacilitiesItem.LocationRecord;

//public class FacilitiesActivity extends ModuleActivity implements OnClickListener {
public class FacilitiesDetailsActivity extends ModuleActivity {

	public static final String TAG = "FacilitiesProblemTypeActivity";
	private Context mContext;	
	private TextView problemStringTextView;
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int PIC_SELECTION = 2;
    private TwoLineActionRow addAPhotoActionRow;
    private TwoLineActionRow takePhotoActionRow;
    private TwoLineActionRow chooseExistingPhotoActionRow;
    private TwoLineActionRow cancelActionRow;    
    private TwoLineActionRow submitActionRow;
	private View facilitiesCameraOptionsLayout;
	private ImageView selectedImage;
	private Uri mCapturedImageUri;
	private Uri mSelectedImageUri;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        this.mContext = this;
        createViews();
	}

	public void createViews() {
        setContentView(R.layout.facilities_details);        
        
    	facilitiesCameraOptionsLayout = (View)findViewById(R.id.facilitiesCameraOptionsLayout);
    	
    	// Set problem string
        problemStringTextView = (TextView)findViewById(R.id.facilitiesProblemString);
        String problemString = "I'm reporting a problem with the " + Global.sharedData.getFacilitiesData().getProblemType();
        
        if (Global.sharedData.getFacilitiesData().getBuildingRoomName().equalsIgnoreCase("INSIDE")) {
        	problemString += " inside " + Global.sharedData.getFacilitiesData().getLocationId();
        }
        else if (Global.sharedData.getFacilitiesData().getBuildingRoomName().equalsIgnoreCase("OUTSIDE")) {
        	problemString += " outside " + Global.sharedData.getFacilitiesData().getLocationId();
        }
        else {
        	problemString += " at " + Global.sharedData.getFacilitiesData().getBuildingNumber() + " in " + Global.sharedData.getFacilitiesData().getBuildingRoomName();        	
        }

        problemStringTextView.setText(problemString);
        
        // Add A Photo
    	addAPhotoActionRow = (TwoLineActionRow)findViewById(R.id.facilitiesAddAPhotoActionRow);
    	addAPhotoActionRow.setActionIconResource(R.drawable.photoopp);
	
    	addAPhotoActionRow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TwoLineActionRow addAPhotoActionRow = (TwoLineActionRow)findViewById(R.id.facilitiesAddAPhotoActionRow);
				addAPhotoActionRow.setVisibility(View.GONE);
	            selectedImage = (ImageView)findViewById(R.id.selectedImage);
	            selectedImage.setVisibility(View.GONE);	
				View facilitiesCameraOptionsLayout = findViewById(R.id.facilitiesCameraOptionsLayout);
				facilitiesCameraOptionsLayout.setVisibility(View.VISIBLE);
			}
		});
    	
    	// selected Image
    	selectedImage = (ImageView)findViewById(R.id.selectedImage);
    	
    	// Take Photo
    	takePhotoActionRow = (TwoLineActionRow)findViewById(R.id.facilitiesTakePhotoActionRow);

    	takePhotoActionRow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    ContentValues values = new ContentValues();   
			    mCapturedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			        
            	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Normally you would populate this with your custom intent.
            	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri);
            	startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);  
			}
		});

    	
    	// Use Existing Photo
    	chooseExistingPhotoActionRow = (TwoLineActionRow)findViewById(R.id.facilitiesChooseExistingPhotoActionRow);
    	chooseExistingPhotoActionRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent choosePhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(choosePhoto, PIC_SELECTION);
			}
    	});
    	
    	
    	// Cancel
    	cancelActionRow = (TwoLineActionRow)findViewById(R.id.facilitiesCancelActionRow);
    	cancelActionRow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addAPhotoActionRow.setVisibility(View.VISIBLE);				
	            selectedImage.setVisibility(View.VISIBLE);
				facilitiesCameraOptionsLayout.setVisibility(View.GONE);
			}
		});
    	
    	// Submit form
    	submitActionRow = (TwoLineActionRow)findViewById(R.id.facilitiesSubmitActionRow);
    	submitActionRow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitForm();
			}
		});
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == CAMERA_PIC_REQUEST) {
    			mSelectedImageUri = mCapturedImageUri;          
        	} else if(requestCode == PIC_SELECTION) {
        		mSelectedImageUri = data.getData();
        	}
			
	        selectedImage.setVisibility(View.VISIBLE);
	        long imageId = ContentUris.parseId(mSelectedImageUri);
	        Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
	        selectedImage.setImageBitmap(thumbnail);
	        facilitiesCameraOptionsLayout.setVisibility(View.GONE);
	        addAPhotoActionRow.setVisibility(View.VISIBLE);	 
    	} 
    }

	@Override
	protected Module getModule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isModuleHomeActivity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void prepareActivityOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	private void submitForm() {
		new Thread() {
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://" + Global.getMobileWebDomain() + "/api/?module=facilities&command=upload");
					InputStream imageStream;
					imageStream = getContentResolver().openInputStream(mSelectedImageUri);
					byte[] imageData;
					imageData = IOUtils.toByteArray(imageStream);
					InputStreamBody imageStreamBody = new InputStreamBody(new ByteArrayInputStream(imageData), "image");
					MultipartEntity multipartContent = new MultipartEntity();
					multipartContent.addPart("image", imageStreamBody);	
					multipartContent.addPart("email", new StringBody("example@example.com"));
					httpPost.setEntity(multipartContent);
					HttpResponse response;
					response = httpClient.execute(httpPost);
					response.getEntity().getContent().close();
				} catch (FileNotFoundException fileException)  {
					fileException.printStackTrace();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				} 
			}			
		}.start();
	}
}
	

	
	