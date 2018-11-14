package com.geariot.platform.freelycar_wechat.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


/**
 * @author javebean
 */
public class HttpRequest {
	
	private static Logger log = LogManager.getLogger(HttpRequest.class);
	
	private static SSLConnectionSocketFactory sslsf;
	
	/*
	 *	文件的初始化，如果需要读取证书，将证书放在src根目录，设置证书的文件名及密码(必须同时设置)；
	 *	如果不需要证书，保持CERT_FILE_NAME为null。
	 */
	private static final String CERT_FILE_NAME = null; 
	private static final String CERT_PASSWORD = null;
	private static String CERT_LOCAL_PATH = null;
	static {
		KeyStore keyStore = null;
		if(CERT_FILE_NAME != null && !CERT_FILE_NAME.isEmpty()){
			CERT_LOCAL_PATH = HttpRequest.class.getClassLoader().getResource(CERT_FILE_NAME).getPath();
			FileInputStream instream = null;
			try {
				instream = new FileInputStream(new File(CERT_LOCAL_PATH));//加载本地的证书进行https加密传输
				keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(instream, CERT_PASSWORD.toCharArray());//设置证书密码
			} catch (CertificateException | NoSuchAlgorithmException | IOException | KeyStoreException e) {
				e.printStackTrace();
			} finally {
				try {
					if(instream != null){
						instream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = null;
		try {
			if(keyStore != null){
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, CERT_PASSWORD.toCharArray()).build();
			}
			else{
				sslcontext = SSLContexts.custom().build();
			}
		} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
		// Allow TLSv1 protocol only
		sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
				null,
				new HostnameVerifier(){
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});
	}
	
	private static CloseableHttpClient getHttpClient(){
		return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(setTimeOut()).build();
	}
	
	/*设置超时*/
	private static RequestConfig setTimeOut(){
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(5000)
				.setConnectTimeout(5000)
				.setSocketTimeout(5000)
				.build();
		return requestConfig;
	}
	
	public static String postCall(String interfaceName,HttpEntity entity,Map<String,Object> head){
		log.debug("post调用远程接口， 接口名："+interfaceName);
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(interfaceName);
		if(entity!=null){
			httpPost.setEntity(entity);
		}
		//设置请求头
		if(head!=null){
			for(Map.Entry<String, Object> h :head.entrySet()){
				httpPost.setHeader(h.getKey(), h.getValue().toString());
			}
		}
		String outResult = null;
//		httpPost.setConfig(setTimeOut());
		try {
			response = httpClient.execute(httpPost);
			outResult = getOutResult(response);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(httpClient != null){
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(response != null){
			    try {
		            response.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
			}
		}
		log.debug("post调用结果：" + outResult);
		return outResult;
		
	}
	
	/**
	 * 处理输出结果，如果是图片单独处理
	 */
	private static String getOutResult(CloseableHttpResponse response){
		StringBuilder out = new StringBuilder();
		Header firstHeader = response.getFirstHeader("Content-Type");
		List<String> arr = new ArrayList<String>(4);
		arr.add("image/bmp");
		arr.add("image/gif");
		arr.add("image/jpeg");
		arr.add("image/png");
		
		
		try{
			InputStream body = response.getEntity().getContent();
			
			//如果是图片
			if(arr.contains(firstHeader.getValue())){
				/*disposition  --》attachment; filename="wqTrmcVTWn3N-OSixFlHb56LxUrkcFe7L2Hzt3KZLoIIiSPkF-67BZy2mu
						ksN14u.jpg"
						*/	
				String disposition = response.getFirstHeader("Content-disposition").getValue();
				String filename = disposition.substring(22, disposition.length()-1);
				saveMediaImg(body,filename);
				return filename;
			}
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(body,"utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			body.close();
			reader.close();
		}catch(IOException  e){
			e.printStackTrace();
		}
		
		return out.toString();	
	}
	
	

	/**
	 * 请求头里面是图片的单独处理
	 */
	//http://stackoverflow.com/questions/43157/easy-way-to-write-contents-of-a-java-inputstream-to-an-outputstream
	private static void saveMediaImg(InputStream input,String filename){
		Path target = Paths.get(filename);
		/**
		 * 每个用户一个单独的文件夹
		 */
		
		try {
			if(Files.notExists(target)){
				Files.createDirectories(target);
			}
			Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getCall(String interfaceName,Map<String,Object> param,Map<String,Object> head){
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		if(param!=null){
			String paramString  ="";
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String, Object> map : param.entrySet()){
				nvps.add(new BasicNameValuePair(map.getKey(), map.getValue().toString()));
			}
			paramString = URLEncodedUtils.format(nvps, "utf-8");
			interfaceName = interfaceName+"?"+paramString;
		}
		HttpGet httpGet = new HttpGet(interfaceName);
//		httpGet.setConfig(setTimeOut());
		
		//设置请求头
		if(head!=null){
			for(Map.Entry<String, Object> h :head.entrySet()){
				httpGet.setHeader(h.getKey(), h.getValue().toString());
			}
		}
		log.debug("发起get请求 请求路径：" + interfaceName);
		String outResult = null;
		try {
			response = httpClient.execute(httpGet);
			outResult = getOutResult(response);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(httpClient != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(response != null){
	            try {
	                response.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
			}
        }
		log.debug("get请求结果：" + outResult);
		return outResult;
	}
	
	
	/**
	 * 在post请求中，需要提交参数，参数 可以为map jsonobject 以及str
	 * @param param
	 * @return
	 */
	public static HttpEntity getEntity(Map<String,Object> param){
		HttpEntity entity = null;
		if(param!=null && !param.isEmpty()){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String, Object> map : param.entrySet()){
				nvps.add(new BasicNameValuePair(map.getKey(), map.getValue().toString()));
			}
			try {
				entity =  new UrlEncodedFormEntity(nvps, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}
	
	/**
	 * 在post请求中，需要提交参数，参数 可以为map jsonobject 以及str
	 * @param param
	 * @return
	 */
	public static HttpEntity getEntity(JSONObject obj){
		StringEntity entity = null;
		if(obj!=null){
			 entity = new StringEntity(obj.toString(),"utf-8"); //解决中文乱码问题    
		}
		return entity;
	}
	
	/**
	 * 在post请求中，需要提交参数，参数 可以为map jsonobject 以及str
	 * @param param
	 * @return
	 */
	public static HttpEntity getEntity(String str){
		StringEntity entity = null;
		if(str!=null){
			entity = new StringEntity(str,"utf-8"); //解决中文乱码问题    
		}
		return entity;
	}
	
}
