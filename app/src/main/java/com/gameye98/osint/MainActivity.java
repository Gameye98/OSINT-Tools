package com.gameye98.osint;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.graphics.Color;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import android.widget.Toast;
import java.util.regex.Pattern;
import android.content.Intent;
import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.ColorDrawable;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.graphics.Typeface;
import android.text.Html;

public class MainActivity extends Activity {
	GridLayout grid;
	PermissionUtils permissions;
	List<String> sites = new ArrayList<String>();
	float density;
	SearchView searchData;
	AlertDialog dialog;
	
	public String readFromAssets(String pathFile) {
		try {
			InputStream stream = getAssets().open(pathFile);
			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			String content = new String(buffer);
			return content;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void toastCenter(String text) {
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public void filterSites(String query) {
		grid.removeAllViews();
		for(int x = 0;x < sites.size();x++) {
			String search = query.toLowerCase();
			String[] siteitem = sites.get(x).split(Pattern.quote("|"));
			final String siteurl = siteitem[0];
			final String siteinfo =  siteitem[1];
			final String siteicon = siteitem[2];
			if(!(siteinfo.toLowerCase().contains(search) || siteurl.toLowerCase().contains(search))) {
				continue;
			}
			// Create LinearLayout
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);
			layout.setBackgroundColor(Color.parseColor("#ff2f2f2f"));
			// GridLayout parameters
			GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
			layoutParams.width = 0;
			layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
			layoutParams.columnSpec = GridLayout.spec(
				GridLayout.UNDEFINED,
				1f // column weight
			);
			layout.setLayoutParams(layoutParams);
			// Create ImageButton
			ImageButton button = new ImageButton(this);
			// 64dp -> px
			int size = (int)(64 * density);
			LinearLayout.LayoutParams buttonParams =
				new LinearLayout.LayoutParams(size, size);
			button.setLayoutParams(buttonParams);
			// Remove all internal padding
			button.setPadding(0, 0, 0, 0); // remove internal spacing
			button.setMinimumWidth(0);
			button.setMinimumHeight(0);
			// Enforce zero extra padding layout behavior
			button.setAdjustViewBounds(true);
			// Remove default background
			button.setBackground(null);
			// Check logo icon && set image drawable
			if(!siteicon.equals("default")) {
				try {
					InputStream stream = getAssets().open("icons/"+siteicon);
					Drawable drawable = Drawable.createFromStream(stream, null);
					button.setImageDrawable(drawable);
					stream.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				button.setImageResource(R.drawable.ic_search_web);
			}
			// Scale type
			button.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// Content description
			button.setContentDescription("Button 1");
			// Optional ID
			button.setId(View.generateViewId());
			button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Set up action buttons
						LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
						view = layoutInflater.inflate(R.layout.open, null, false);
						ImageView openImage = view.findViewById(R.id.openImg);
						// Check logo icon && set image drawable
						if(!siteicon.equals("default")) {
							try {
								InputStream stream = getAssets().open("icons/"+siteicon);
								Drawable drawable = Drawable.createFromStream(stream, null);
								openImage.setImageDrawable(drawable);
								stream.close();
							} catch(Exception e) {
								e.printStackTrace();
							}
						} else {
							openImage.setImageResource(R.drawable.ic_search_web);
						}
						// Scale type
						//openImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
						// Content description
						TextView ooenTitle = view.findViewById(R.id.openTitle);
						ooenTitle.setText(siteinfo);
						Button openBrowser = view.findViewById(R.id.openBrowser);
						Button openHere = view.findViewById(R.id.openHere);
						openBrowser.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								try {
									dialog.dismiss();
									Intent intent = new Intent(Intent.ACTION_VIEW);
									intent.setData(Uri.parse(siteurl));
									startActivity(intent);
								} catch(Exception e) {
									e.printStackTrace();
								}
							}
						});
						openHere.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								try {
									dialog.dismiss();
									Intent intent = new Intent(MainActivity.this, Browser.class);
									intent.putExtra("url", siteurl);
									startActivity(intent);
								} catch(Exception e) {
									e.printStackTrace();
								}
							}
						});
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setView(view);
						dialog = builder.show();
					}
				});
			// Create TextView
			TextView text = new TextView(this);
			LinearLayout.LayoutParams textParams =
				new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
			);
			// Margin top = 4dp
			//textParams.topMargin = (int)(4 * density);
			text.setLayoutParams(textParams);
			text.setText(siteinfo);
			text.setMaxLines(3);
			text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
			text.setTextColor(Color.parseColor("#FFFFFF"));
			// Remove internal spacing
			text.setIncludeFontPadding(false); // critical
			text.setPadding(10,0,10,0);
			// =========================
			// Add views into layout
			// =========================
			layout.addView(button);
			layout.addView(text);
			// Insert into parent
			grid.addView(layout);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.information) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View parent = inflater.inflate(R.layout.about, null, false);
			TextView title = parent.findViewById(R.id.title);
			title.setText("OSINT Tools");
			TextView content = parent.findViewById(R.id.content);
			String[] contents = {
				"This application providing curated access to public data sources across multiple investigative categories. The tool aggregates external websites used for domain analysis, username lookup, email and phone tracing, IP and network inspection, metadata extraction, and public record correlation.",
				"Copyright (c) 2026 by Gameye98"
			};
			content.setText(String.join("\n", contents));
			String[] urls = {
				"https://github.com/Gameye98",
				"https://github.com/Gameye98/OSINT-Tools",
				"https://github.com/BlackHoleSecurity",
				"https://t.me/bhsecr",
				"https://t.me/schdenfreude"
			};
			String[] urlinfo = {
				"My GitHub",
				"Project Homepage",
				"BlackHole Security GitHub",
				"BlackHole Security Telegram",
				"Schadenfreude Telegram"
			};
			LinearLayout container = parent.findViewById(R.id.links);
			for (int i = 0; i < urls.length; i++) {
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams params =
					new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT
				);
				tv.setLayoutParams(params);
				tv.setText(Html.fromHtml("<a href=\""+urls[i]+"\">"+urlinfo[i]+"</a>"));
				tv.setTextAppearance(this, android.R.style.TextAppearance_Small);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(Color.WHITE);
				tv.setTypeface(null, Typeface.BOLD);
				final String url = urls[i];
				tv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(intent);
					}
				});
				//tv.setId(View.generateViewId());
				container.addView(tv);
			}
			builder.setView(parent);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return true;
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		permissions = new PermissionUtils(this);
		permissions.checkAllPermissions();
		getWindow().setStatusBarColor(Color.parseColor("#2C2C2C"));
		getWindow().setNavigationBarColor(Color.parseColor("#0077FF"));
		/*
		if(getSupportActionBar() != null) {
			getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(
					Color.parseColor("#0077FF")
				)
			);
		}*/
        grid = findViewById(R.id.grid);
		searchData = findViewById(R.id.searchData);
		int id = searchData.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		int btn = searchData.getContext().getResources().getIdentifier("android:id/search_button", null, null);
		int close = searchData.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
		TextView tv = findViewById(id);
		tv.setTextColor(Color.parseColor("#00FFFF"));
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER);
		ImageView searchBtn = findViewById(btn);
		searchBtn.setColorFilter(Color.parseColor("#0077FF"));
		ImageView searchClose = findViewById(close);
		searchClose.setColorFilter(Color.RED);
		searchData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				filterSites(query);
				return false;
			}
			@Override
			public boolean onQueryTextChange(String newText) {
				filterSites(newText);
				return false;
			}
		});
		density = getResources().getDisplayMetrics().density;
		try {
			String siteurl = readFromAssets("siteurl.dat");
			if(siteurl != null) {
				String[] siteurls = siteurl.split("\012");
				for(String url : siteurls) {
					sites.add(url);
				}
			} else {
				toastCenter("File 'siteurl.dat' is missing from assets/");
			}
			filterSites(".");
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
	
} 
