package com.gameye98.osint;

import android.os.*;
import android.app.*;
import android.webkit.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import android.print.*;
import java.text.*;
import java.util.*;
import java.io.*;
import android.net.Uri;
import android.content.Intent;
import androidx.annotation.RequiresApi;
import android.content.ActivityNotFoundException;

public class Browser extends Activity {
	Button dloadbtn,dsktpbtn;
	boolean isdesktop = false;
	WebView webview;
	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 1;
	String url;
	
	public void toast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	public String getUserAgent() {
		try {
			InputStream stream = getAssets().open("user_agent.txt");
			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			String content = new String(buffer);
			String[] contents = content.split("\012");
			return contents[(int)Math.floor(Math.random()*contents.length)];
		} catch(Exception e) {
			toast(e.toString());
			return "Mozilla/5.0 (Linux; U; Android 4.0.4; en-us; Glass 1 Build/IMM76L; XE16.2) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
		}
	}
	public void setDesktopMode(WebView webView,boolean enabled) {
		String newUserAgent = webView.getSettings().getUserAgentString();
		if (enabled) {
			try {
				String ua = webView.getSettings().getUserAgentString();
				String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
				newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
				Toast.makeText(getApplicationContext(), "Desktop Mode", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			newUserAgent = null;
		}
		webView.getSettings().setUserAgentString(newUserAgent);
		webView.getSettings().setUseWideViewPort(enabled);
		webView.getSettings().setLoadWithOverviewMode(enabled);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);
		webView.reload();
	}
	@Override
	public void onPause() {
		super.onPause();
		CookieManager.getInstance().flush();
	}
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.browser);
		dloadbtn = findViewById(R.id.wtpsave);
		dsktpbtn = findViewById(R.id.wtpdesktop);
		//dloadbtn.setOnClickListener(dloadbtnOnClick);
		url = getIntent().getStringExtra("url");
		webview = findViewById(R.id.webview);
		webview.getSettings().setUserAgentString(getUserAgent());
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLoadWithOverviewMode(false);
		webview.getSettings().setUseWideViewPort(false);
		webview.getSettings().setBuiltInZoomControls(false);
		webview.getSettings().setPluginState(WebSettings.PluginState.ON);
		webview.getSettings().setCacheMode(2);
		webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setDatabaseEnabled(true);
		webview.getSettings().setGeolocationEnabled(true);
		webview.getSettings().setAllowFileAccess(true);
		webview.getSettings().setAllowContentAccess(true);
		webview.getSettings().setAllowFileAccessFromFileURLs(true);
		webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
		webview.setLongClickable(true);
		webview.setFocusableInTouchMode(true);
		webview.setOnCreateContextMenuListener((View.OnCreateContextMenuListener)getParent());
		webview.setKeepScreenOn(true);
		webview.setSoundEffectsEnabled(true);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.setAcceptThirdPartyCookies(webview, true);
		webview.loadUrl(url);
		webview.setWebChromeClient(new WebChromeClient()
			{
// For 3.0+ Devices (Start)
// onActivityResult attached before constructor
				protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
				{
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setType("/*");
					startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
				}


// For Lollipop 5.0+ Devices
				@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
				public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
				{
					if (uploadMessage != null) {
						uploadMessage.onReceiveValue(null);
						uploadMessage = null;
					}

					uploadMessage = filePathCallback;

					Intent intent = fileChooserParams.createIntent();
					try
					{
						startActivityForResult(intent, REQUEST_SELECT_FILE);
					} catch (ActivityNotFoundException e)
					{
						uploadMessage = null;
						Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
						return false;
					}
					return true;
				}

//For Android 4.1 only
				protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
				{
					mUploadMessage = uploadMsg;
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("/*");
					startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
				}

				protected void openFileChooser(ValueCallback<Uri> uploadMsg)
				{
					mUploadMessage = uploadMsg;
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setType("/*");
					startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
				}
			});
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView p1, int p2, String p3, String p4) {
				Toast.makeText(getApplicationContext(), "NO CONNECTION!", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		dsktpbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isdesktop = !isdesktop;
				setDesktopMode(webview,isdesktop);
			}
		});
		dloadbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
					String jobName = url;
					if(jobName.contains("?")) {
						jobName = jobName.split("\\?")[0];
					}
					while(jobName.endsWith("/")) {
						jobName = jobName.substring(0,jobName.length()-1);
					}
					jobName = jobName.substring(jobName.lastIndexOf("/")+1);
					if(jobName.contains(".")) {
						jobName = jobName.split("\\.")[0];
					}
					if(jobName.isEmpty()) {
						jobName = new SimpleDateFormat("ddMMyyyy-hhmmss").format(new Date());
					} else {
						String datetime = new SimpleDateFormat("ddMMyyyy-hhmmss").format(new Date());
						jobName = jobName+"-"+datetime;
					}
					PrintDocumentAdapter adapter = webview.createPrintDocumentAdapter(jobName);
					PrintAttributes printAttributes = new PrintAttributes.Builder()
						.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
						.setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
						.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
						.build();
					//printManager.print(jobName, adapter, printAttributes);
					final String filename = jobName;
					final PrintJob printJob = printManager.print(jobName, adapter, printAttributes);
					// Observe the print job status
					final Handler handler = new Handler(Looper.getMainLooper());
					Runnable checkStatus = new Runnable() {
						@Override
						public void run() {
							if (printJob.isCompleted()) {
								Toast.makeText(getApplicationContext(), "File saved as "+filename+".pdf", Toast.LENGTH_SHORT).show();
							} else if (printJob.isFailed()) {
								Toast.makeText(getApplicationContext(), "Print failed", Toast.LENGTH_SHORT).show();
							} else if (printJob.isCancelled()) {
								Toast.makeText(getApplicationContext(), "Print cancelled", Toast.LENGTH_SHORT).show();
							} else {
								// Continue polling every 500ms
								handler.postDelayed(this, 500);
							}
						}
					};

					handler.post(checkStatus); // start polling
					
				} else {
					Toast.makeText(getApplicationContext(), "Your device is not supported!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (webview.canGoBack()) {
						webview.goBack();
					} else {
						finish();
					}
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
