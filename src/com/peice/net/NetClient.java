package com.peice.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.util.Log;

public class NetClient {
	
	public final static boolean USE_FAKE = false;
	
	String mHost;
	String mCookie;
	
	private final static String CHARSET = HTTP.UTF_8;

	public interface Reciever{
		public void onStringReceived(String buffer);
	}
	
	public NetClient(String host) {
		mHost = host;
		if (USE_FAKE) {
			FakeServer.init(host);
		}
	}
	
	class HttpThread extends Thread {
		Reciever mReciever;
		String   mUrl;
		String   mParams;
		
		HttpThread(String url, String params, Reciever reciever) {
			mUrl = url;
			mParams = params;
			mReciever = reciever;
		}
		
		public void run() {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	        	Log.d("PEICE","URL Request:"+mUrl);
	        	Log.d("PEICE","Post:"+mParams);
	            URL realUrl = new URL(mUrl);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("Accept", "*/*");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("User-Agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            
	            if (mCookie != null && mCookie.length() > 0) {
	            	conn.setRequestProperty("Cookie", mCookie);
	            }
	            
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(mParams);
	            // flush输出流的缓冲
	            out.flush();
	            
	            // update cookie
	            String cookie = conn.getHeaderField("set-cookie");
	            if (cookie != null && cookie.length() > 0) {
	            	mCookie = cookie;
	            }
	            
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	            
	            Log.d("PEICE:","Recieved:"+result);
	            if (mReciever != null) {
	            	mReciever.onStringReceived(result);
	            }
	        } catch (Exception e) {
	            Log.i("NetClient","发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
		}
	}
	
	public void post(String url, String encodedParams, Reciever reciever) {
		if (USE_FAKE) {
			postFake(mHost + '/' + url, encodedParams, reciever);
		}
		else {
			postReal(mHost + '/' + url, encodedParams, reciever);
		}
	}
	
	public void post(String url, Map<String, String> params, Reciever reciever) {
		StringBuilder builder = new StringBuilder();
		try {
			boolean bfirst = true;
			for (String key : params.keySet()) {
				if (!bfirst) {
					builder.append("&");
				}
				else {
					bfirst = false;
				}
				builder.append(URLEncoder.encode(key, CHARSET));
				builder.append("=");
				builder.append(URLEncoder.encode(params.get(key), CHARSET));
			}
		}catch(Exception e) {
			//TODO
			e.printStackTrace();
		}
		post(url, builder.toString(), reciever);
	}
	
	//////////////////////////////////////////////////////////////
	//Real impl
	private void postReal(String url, String encodedParams, Reciever reciever) {
		final HttpThread httpThread = new HttpThread(url, encodedParams, reciever);
		httpThread.start();
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//Fake impl
	private void postFake(String url, String encodedParams, Reciever reciever) {
		final FakeThread fakeThread = new FakeThread(url, encodedParams, reciever);
		fakeThread.start();
	}
	
	class FakeThread extends Thread {
		Reciever mReciever;
		String   mUrl;
		String   mParams;
		
		FakeThread(String url, String params, Reciever reciever) {
			mUrl = url;
			mParams = params;
			mReciever = reciever;
		}
		
		public void run() {
			
			String buffer = FakeServer.getInstance().getBuffer(mUrl, mParams);
			try {
				sleep(1000);
			}catch(Exception e) {
			}
			
			if (mReciever != null) {
				mReciever.onStringReceived(buffer);
			}
		}
	}
	
	
}
