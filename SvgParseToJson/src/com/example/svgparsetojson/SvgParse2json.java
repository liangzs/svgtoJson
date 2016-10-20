package com.example.svgparsetojson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Decoder.BASE64Decoder;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class SvgParse2json {
	private final static String TAG = "SvgParse2json";
	private XmlPullParser xmlPullParser;

	public SvgParse2json() {
		// try {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// factory.setNamespaceAware(true);
		// xmlPullParser = factory.newPullParser();
		// } catch (XmlPullParserException e) {
		// // TODO Auto-generated catch block
		// Log.e(TAG, e.getMessage());
		// e.printStackTrace();
		// }

	}

	/**
	 * <g inkscape:groupmode="layer" id="layer3" inkscape:label="cornors"
	 * style="display:inline"> <rect style=
	 * "fill:#524200;fill-opacity:0.27964561;stroke:#534300;stroke-width:0.52657461;stroke-opacity:0.3744304"
	 * id="rect4170" width="159.57529" height="154.11032" x="396.07028"
	 * y="-252.33398" transform="scale(1,-1)"> <desc
	 * id="desc4188">minor:12,major:2015,height:13</desc> </rect> <g
	 * inkscape:groupmode="layer" id="layer2" inkscape:label="Beacons"
	 * style="display:none" />
	 * 
	 * @return
	 */
	public String toJsonString(Context mContext, String fileName) {
		int eventType;
		try {
			xmlPullParser.setInput(new InputStreamReader(mContext.getAssets()
					.open(fileName), "UTF-8"));
			eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					Log.i(TAG, "START_TAG:" + xmlPullParser.getName());
					if (xmlPullParser.getName().equals("g")
							&& xmlPullParser.getAttributeValue(
									xmlPullParser.getNamespace(),
									"inkscape:label").equals("cornors")) {
						Log.i(TAG, "cornors................");
					}

					break;
				case XmlPullParser.END_TAG:
					Log.i(TAG, "END_TAG:" + xmlPullParser.getName());
				default:
					break;
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String parseByDom4j(Context mContext, String fileName) {
		try {
			String fileString = getFromAssets(mContext, fileName).toString();
			Document document = DocumentHelper.parseText(fileString);
			// Element element=document.elementByID("g");
			Element root = document.getRootElement();
			List<Element> list = root.content();
			Floor floor = new Floor();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof Element &&list.get(i).getName().equals("g")) {
					Element e = list.get(i);
					String labelName=e.attributeValue("label");
					if (labelName.equals("zones")) {
						int index=1;
//						List<Element> listRect = e.selectNodes("rect");
						List<Element> listTest = e.elements("rect");
						List<CoordinateDomain> listCoordinate = new ArrayList<CoordinateDomain>();
						for (Element rect : listTest) {
							CoordinateDomain coord = new CoordinateDomain();
							coord.setX(Double.parseDouble(rect.attributeValue(
									"x").toString()));
							coord.setY(Double.parseDouble(rect.attributeValue(
									"y").toString()));
							coord.setId(index);
							listCoordinate.add(coord);
							index++;
						}
						floor.setCornors(listCoordinate);
					} else if (labelName.equals("image")) {
						Element imageElement = e.element("image");
						wirteJpg(imageElement.attributeValue("href"));
					} else if(labelName.equals("description")){
						Element eDescribe=e.element("//rect/desc");
						List<Element> l=e.getDocument().selectNodes("//rect/desc");
						Element t =(Element) e.getDocument().selectSingleNode("//rect/desc");
						List<Element>tt=e.elements("//rect/desc");
//						String str=eDescribe.getText();
//						setFilds(floor,str);
					}else if(labelName.equals("beacons")){
						
					}else if(labelName.equals("corners")){
						
					}
					// Log.i(TAG,"style:"+e.attributeValue("style"));
					// Log.i(TAG,"inkscape:label:"+e.attributeValue("label"));
					// Log.i(TAG,e.getName());
				}

			}
			Gson gson = new Gson();
			String jsonStr = gson.toJson(floor);
			Log.i(TAG, jsonStr);
			saveAsJson(jsonStr);
			Log.i(TAG, ".......");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public void wirteJpg(String imageStr) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				BASE64Decoder coder = new BASE64Decoder();
				imageStr = imageStr.substring(imageStr.indexOf(",") + 1);
				byte[] bytes = coder.decodeBuffer(imageStr);
				for (int i = 0; i < bytes.length; i++) {
					if (bytes[i] < 0) {// 调整异常数据
						bytes[i] += 256;
					}
				}
				String fileName = "test.jpg";
				File dir = new File(getLogPath());
				if (!dir.exists())
					dir.mkdir();
				File file = new File(dir, fileName);
				FileOutputStream out = new FileOutputStream(file);
				out.write(bytes);
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * assets下的文件读取
	 * 
	 * @param fileName
	 * @return
	 */
	public StringBuffer getFromAssets(Context mContext, String fileName) {
		StringBuffer sbResult = new StringBuffer();
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(mContext.getAssets().open(
					fileName), "UTF-8");
			bufReader = new BufferedReader(inputReader);
			String lineStr = "";
			while ((lineStr = bufReader.readLine()) != null)
				sbResult.append(lineStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sbResult;
	}

	public static void saveAsJson(String ExceptionStr) {
		String fileName = "test.json";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(getLogPath());
				if (!dir.exists())
					dir.mkdir();
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(ExceptionStr.trim().getBytes());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.getMessage();
			}
		}
	}

	public static String getLogPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator + "Tencent" + File.separator;
		}
		return "";// 没有外置sd卡
	}

	public static void setFilds(Floor floor,String str) {
		if (floor == null)
			floor = new Floor();
		try {
			Field[] fields = Floor.class.getDeclaredFields();
			String[] array = str.split(",");
			for (Field field : fields) {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				for (String s : array) {
					if (s.split(":")[0].equals(field.getName())) {
						field.set(floor, s.split(":")[1]);

					}
				}

			}
			Log.i(TAG, floor.toString());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
		}
	}

}
