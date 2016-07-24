package com.example.xueyuanzhang.weibo;

import android.util.Log;

import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by xueyuanzhang on 16/5/1.
 */
public class HttpUtils {
    public static HttpClient httpClient = new DefaultHttpClient();
    public static HttpClient httpClient1 = new DefaultHttpClient();
    public static HttpClient httpClient3=new DefaultHttpClient();
    public static String boundary="---------------------------7d33a816d302b6";
    public interface OnResponseListener {
        void onResponse(String accessToken);

    }

    public interface OnOutOfDateListener {
        void onCheck();
    }

    public interface OnShowWeiboListener {
        void onFriendWeibo(List<ListEntry> weibo);
    }

    public interface OnGetUserInfoListener {
        void onUserInfo(User user);
    }

    public interface OnGetCommentsListener {
        void onComments(List<Comments> comments);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(List<ListEntry> weibo);
    }

    public interface OnUnConnectListener {
        void onUnConnect();
    }

    public interface OnMyCommentsListener{
        void onMyComments(List<MyComments> list);
    }

    public interface OnSendSuccessListener{
        void onSuccess();
    }


    public static void getAccessToken(final String url, final OnResponseListener onResponseListener) {

        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {

                try {
                    HttpPost post = new HttpPost(url);
                    System.out.println(url);
                    HttpResponse response = httpClient.execute(post);

                    String result = EntityUtils.toString(response.getEntity());
                    System.out.println(result);

                    if (onResponseListener != null) {
                        onResponseListener.onResponse(result);
                    }
                } catch (Exception e) {
                    System.out.println("cannot get accessToken");
                }
                return null;
            }
        });
        new Thread(task).start();

    }

    //**********************发送微博
    public static void sendWeibo(final String url, final String accessToken, final String content) {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    HttpPost post = new HttpPost(url);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("access_token", accessToken));
                    params.add(new BasicNameValuePair("status", content));
                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    HttpResponse response = httpClient.execute(post);


                } catch (Exception e) {
                    System.out.println("send Error");
                }
                return null;
            }
        });
        new Thread(task).start();
    }

    //*********************读取首页微博
    public static void readFrendsWeibo(final String accessToken, final String weiboID, final OnOutOfDateListener onOutOfDateListener, final OnUnConnectListener onUnConnectListener, final OnShowWeiboListener onShowWeiboListener) {
        final String urlGetHome;
        if (weiboID == null) {
            urlGetHome = UrlManage.showHomeWeiboUrl + "?access_token=" + accessToken + "&count=20";
        } else {
            urlGetHome = UrlManage.showHomeWeiboUrl + "?access_token=" + accessToken + "&max_id=" + weiboID + "&count=20";
        }

        final FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {

                String result = "";
                try {

                    HttpGet getHomeWeibo = new HttpGet(urlGetHome);
                    HttpResponse response = httpClient.execute(getHomeWeibo);
                    result = EntityUtils.toString(response.getEntity());

                }catch (IOException e){
                    e.printStackTrace();
                    if(onUnConnectListener!=null){
                        onUnConnectListener.onUnConnect();
                    }
                }


                JSONObject jsonObject = new JSONObject(result);
                try {
                    String error_code = jsonObject.get("error_code").toString();
                    if (error_code != null) {
                        if (onOutOfDateListener != null) {
                            onOutOfDateListener.onCheck();
                        }
                    }
                } catch (Exception e) {

                }
                JSONArray array = jsonObject.getJSONArray("statuses");
                List<ListEntry> InfoToShow = new ArrayList<>();
                System.out.println("长度" + array.length());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject user = array.getJSONObject(i);
                    System.out.println(user);
                    String Name = user.getJSONObject("user").get("screen_name").toString();
                    String Text = user.get("text").toString();
                    String Header = user.getJSONObject("user").get("avatar_hd").toString();
                    String uid=user.getJSONObject("user").get("id").toString();
                    String repost = user.get("reposts_count").toString();
                    String comment = user.get("comments_count").toString();
                    String praise = user.get("attitudes_count").toString();
                    String createTime = user.get("created_at").toString();
                    String weiboID = user.get("id").toString();
                    List<String> AllImageUrl = new ArrayList<>();
                    String bmiddle_pic = "";
                    try {
                        bmiddle_pic = user.get("bmiddle_pic").toString();
                        JSONArray pic_urls = user.getJSONArray("pic_urls");
                        //System.out.println(pic_urls);
                        for (int j = 0; j < pic_urls.length(); j++) {
                            JSONObject imageId = pic_urls.getJSONObject(j);
                            String imageUrl = imageId.get("thumbnail_pic").toString();
                            //System.out.println(imageUrl);
                            AllImageUrl.add(imageUrl);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    String sourceName = "";
                    String sourceText = "";
                    String source_bmiddle_pic = "";
                    List<String> sourceImageUrl = new ArrayList<>();
                    try {
                        JSONObject source = user.getJSONObject("retweeted_status");
                        System.out.println(source);
                        sourceName = source.getJSONObject("user").get("screen_name").toString();
                        sourceText = source.get("text").toString();
                        try {
                            source_bmiddle_pic = source.get("bmiddle_pic").toString();
                            JSONArray sourceImage = source.getJSONArray("pic_urls");
                            for (int j = 0; j < sourceImage.length(); j++) {
                                String imageUrl = sourceImage.getJSONObject(j).get("thumbnail_pic").toString();
                                sourceImageUrl.add(imageUrl);
                            }
                        } catch (Exception d) {
                        }
                    } catch (Exception e) {
                    }


                    Weibo weibo = new Weibo();
                    weibo.setWeiboID(weiboID);
                    weibo.setUid(uid);
                    weibo.setUserName(Name);
                    weibo.setWeiboContent(Text);
                    weibo.setHeader(Header);
                    weibo.setRepost(repost);
                    weibo.setComment(comment);
                    weibo.setPraise(praise);
                    weibo.setImage(AllImageUrl);
                    weibo.setCreateTime(createTime);
                    weibo.setBmiddle_pic(bmiddle_pic);
                    weibo.setSource_userName(sourceName);
                    weibo.setSource_text(sourceText);
                    weibo.setSourceImageUrl(sourceImageUrl);
                    weibo.setSource_bmiddle_pic(source_bmiddle_pic);
                    InfoToShow.add(weibo);

                }
                InfoToShow.add(new LoadMoreModel());


                if (onShowWeiboListener != null) {
                    onShowWeiboListener.onFriendWeibo(InfoToShow);
                }


                return null;
            }
        });
        new Thread(task).start();
    }

    //*********************读取用户信息
    public static void getUserInfo(final String accessToken, final String uId,final OnGetUserInfoListener onGetUserInfoListener, final OnUnConnectListener onUnConnectListener, final OnOutOfDateListener onOutOfDateListener) {
        final String getUserInfoUrlBase = UrlManage.getUserInfoUrl;
        final String getUidUrl = UrlManage.getUid + "?access_token=" + accessToken;

        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result="";
                String uid=uId;
                if(uId==null) {

                    try {
                        HttpGet getUid = new HttpGet(getUidUrl);    //获取uid
                        HttpResponse response = httpClient1.execute(getUid);
                        uid = EntityUtils.toString(response.getEntity());

                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onUnConnectListener != null) {
                            onUnConnectListener.onUnConnect();
                        }
                    }


                    JSONObject uidObject = new JSONObject(uid);
                    try {
                        String error_code = uidObject.get("error_code").toString();
                        if (error_code != null) {
                            if (onOutOfDateListener != null) {
                                onOutOfDateListener.onCheck();
                            }
                        }
                    } catch (Exception e) {
                    }
                    uid = uidObject.get("uid").toString();
                }


                    String getUserInfoUrl = getUserInfoUrlBase + "?access_token=" + accessToken + "&uid=" + uid;
                try {
                    HttpGet getUserInfo = new HttpGet(getUserInfoUrl);     //获取用户信息
                    HttpResponse response1 = httpClient1.execute(getUserInfo);
                    result = EntityUtils.toString(response1.getEntity());
                }catch (Exception e){
                    e.printStackTrace();
                    if (onUnConnectListener != null) {
                        onUnConnectListener.onUnConnect();
                    }
                }

                JSONObject userInfo = new JSONObject(result);

                try {
                    String error_code = userInfo.get("error_code").toString();
                    if (error_code != null) {
                        if (onOutOfDateListener != null) {
                            onOutOfDateListener.onCheck();
                        }
                    }
                } catch (Exception e) {
                }


                    String screen_name = userInfo.get("screen_name").toString();
                    String followers_count = userInfo.get("followers_count").toString();
                    String friends_count = userInfo.get("friends_count").toString();
                    String description=userInfo.get("description").toString();
                    String statuses_count = userInfo.get("statuses_count").toString();
                    String header = userInfo.get("avatar_hd").toString();
                    User user = new User();
                    user.setScreen_name(screen_name);
                    user.setFollowers_count(followers_count);
                    user.setFriends_count(friends_count);
                    user.setDescription(description);
                    user.setStatuses_count(statuses_count);
                    user.setHeader(header);

                    if (onGetUserInfoListener != null) {
                        onGetUserInfoListener.onUserInfo(user);
                    }

                return null;
            }
        });
        new Thread(task).start();
    }

    public static void getComments(final String accessToken, final String weiboID, final OnGetCommentsListener onGetCommentsListener) {

        final String url = UrlManage.getCommentsUrl + "?access_token=" + accessToken + "&id=" + weiboID;
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result;
                synchronized (httpClient) {
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = httpClient.execute(get);
                    result = EntityUtils.toString(response.getEntity());
                }
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("comments");
                List<Comments> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Comments comment = new Comments();
                    JSONObject commentObject = jsonArray.getJSONObject(i);
                    String text = commentObject.get("text").toString();
                    String time = commentObject.get("created_at").toString();
                    JSONObject user = commentObject.getJSONObject("user");
                    String header = user.get("avatar_hd").toString();
                    String name = user.get("screen_name").toString();
                    comment.setHeader(header);
                    comment.setName(name);
                    comment.setText(text);
                    comment.setTime(time);
                    list.add(comment);

                }
                if (onGetCommentsListener != null) {
                    onGetCommentsListener.onComments(list);
                }

                return null;
            }
        });
        new Thread(task).start();
    }

    public static void sendWeiboWithPic(final String accessToken,final String picPath,final String text,final OnUnConnectListener onUnConnectListener,final OnOutOfDateListener onOutOfDateListener){
        final String postUrl=UrlManage.SEND_WEIBO_WITH_PIC;
        FutureTask<String> task=new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    HttpPost post = new HttpPost(postUrl);
                    FileBody fileBody = new FileBody(new File(picPath));
                    StringBody stringBody = new StringBody(text);
                    StringBody stringBody1 = new StringBody(accessToken);
                    MultipartEntity entity = new MultipartEntity();
                    entity.addPart("pic", fileBody);
                    entity.addPart("status", stringBody);
                    entity.addPart("access_token", stringBody1);
                    post.setEntity(entity);
                    HttpResponse response = httpClient3.execute(post);
                    String result=EntityUtils.toString(response.getEntity());
                }catch (Exception e){
                    e.printStackTrace();
                }


                return null;
            }
        });
        new Thread(task).start();
    }

    public static void getMyComments(String accessToken,final OnUnConnectListener onUnConnectListener,final OnOutOfDateListener onOutOfDateListener,final OnMyCommentsListener onMyCommentsListener){
        final String url=UrlManage.GET_COMMENTS_TO_ME+"?access_token="+accessToken;
        FutureTask<String> task=new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result="";
                List<MyComments> list=new ArrayList<>();
                try {
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = httpClient.execute(get);
                    result=EntityUtils.toString(response.getEntity());
                }catch (Exception e){
                    if(onUnConnectListener!=null){
                        onUnConnectListener.onUnConnect();
                    }
                }
                JSONObject jsonObject=new JSONObject(result);
                try {
                    String error_code = jsonObject.get("error_code").toString();
                    if (error_code != null) {
                        if (onOutOfDateListener != null) {
                            onOutOfDateListener.onCheck();
                        }
                    }
                } catch (Exception e) {
                }
                JSONArray jsonArray=jsonObject.getJSONArray("comments");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject comment=jsonArray.getJSONObject(i);
                       MyComments comments=new MyComments();
                        comments.setTextOfComment(comment.get("text").toString());
                        comments.setTimeOfComment(comment.get("created_at").toString());
                        comments.setHeaderOfComment(comment.getJSONObject("user").get("profile_image_url").toString());
                        comments.setNameOfComment(comment.getJSONObject("user").get("screen_name").toString());
                        comments.setTextOfSource(comment.getJSONObject("status").get("text").toString());
                        comments.setNameOfAuther(comment.getJSONObject("status").getJSONObject("user").get("screen_name").toString());
                        comments.setHeaderOfAuther(comment.getJSONObject("status").getJSONObject("user").get("avatar_hd").toString());
                    list.add(comments);
                }
                if(onMyCommentsListener!=null){
                    onMyCommentsListener.onMyComments(list);
                }

                return null;
            }
        });
           new Thread(task).start();

      }

    public static void sendJust(final String accessToken, final String text, final String picPath, final OnUnConnectListener onUnConnectListener
                                 , final OnOutOfDateListener onOutOfDateListener,final OnSendSuccessListener onSendSuccessListener){
        final String postUrl=UrlManage.SEND_WEIBO_WITH_PIC;
        FutureTask<String> task=new FutureTask<String>(new Callable<String>() {
            private void writeStringParams(OutputStream out,Map<String,String> textParams) throws Exception {
                Set<String> keySet = textParams.keySet();
                for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                    String name = it.next();
                    String value = textParams.get(name);

                    out.write(("--" + boundary + "\r\n").getBytes());
                    out.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n")
                            .getBytes());
                    out.write(("\r\n").getBytes());
                    out.write((encode(value) + "\r\n").getBytes());
                }
            }

            // 文件数据
            private void writeFileParams(OutputStream out,Map<String,File> fileparams) throws Exception {
                Set<String> keySet = fileparams.keySet();
                for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                    String name = it.next();
                    File value = fileparams.get(name);

                    out.write(("--" + boundary + "\r\n").getBytes());
                    out.write(("Content-Disposition: form-data; name=\"" + name
                            + "\"; filename=\"" + encode(value.getName()) + "\"\r\n")
                            .getBytes());
                    out.write(("Content-Type: " + getContentType(value) + "\r\n")
                            .getBytes());
                    out.write(("Content-Transfer-Encoding: " + "binary" + "\r\n")
                            .getBytes());

                    out.write(("\r\n").getBytes());

                    FileInputStream inStream = new FileInputStream(value);
                    int bytes = 0;
                    byte[] bufferByte = new byte[1024];
                    while ((bytes = inStream.read(bufferByte)) != -1) {
                        out.write(bufferByte, 0, bytes);
                    }
                    inStream.close();

                    out.write(("\r\n").getBytes());
                }
            }

            // 添加结尾数据
            private void writesEnd(OutputStream out) throws Exception {
                out.write(("--" + boundary + "--" + "\r\n").getBytes());
                out.write(("\r\n").getBytes());
            }

            // 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
            private String getContentType(File f) throws Exception {
                String fileName = f.getName();
                if (fileName.endsWith(".jpg")) {
                    return "image/jpeg";
                } else if (fileName.endsWith(".png")) {
                    return "image/png";
                }
                return "application/octet-stream";
            }

            // 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
            private String encode(String value) throws Exception {
                return URLEncoder.encode(value, "UTF-8");
            }
            @Override
            public String call() throws Exception {
                Map<String,String> textParams=new HashMap<String,String>();
                Map<String,File> fileParams=new HashMap<String,File>();
                try{
                    URL url=new URL(postUrl);
                    textParams=new HashMap<String,String>();
                    fileParams=new HashMap<String, File>();
                    File file=new File(picPath);
                    fileParams.put("pic",file);
                    textParams.put("status",text);
                    textParams.put("access_token",accessToken);
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Charset","UTF-8");
                    conn.setRequestProperty("ser-Agent","Fiddler");
                    conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);

                    writeFileParams(ds,fileParams);
                    writeStringParams(ds,textParams);
                    writesEnd(ds);
                    InputStream responseInStream = conn.getInputStream();
                    ByteArrayOutputStream responseOutStream = new ByteArrayOutputStream();
                    int len;
                    byte[] bufferByte = new byte[1024];
                    while ((len = responseInStream.read(bufferByte)) != -1) {
                        responseOutStream.write(bufferByte, 0, len);
                    }
                    responseInStream.close();
                    ds.close();

                    conn.disconnect();
                    byte[] resultByte = responseOutStream.toByteArray();
                    responseOutStream.close();
                    String convert=new String(resultByte,"UTF-8");
                    JSONObject jsonObject=new JSONObject(convert);
                    try{
                        String error_code=jsonObject.get("error_code").toString();
                        if(error_code!=null){
                            if(onOutOfDateListener!=null){
                                onOutOfDateListener.onCheck();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(onSendSuccessListener!=null){
                        onSendSuccessListener.onSuccess();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    if(onUnConnectListener!=null){
                        onUnConnectListener.onUnConnect();
                    }
                }
                return null;
            }
        });
        new Thread(task).start();
    }

    public static void getUidByToken(final String accessToken){
        final String getUidUrl = UrlManage.getUid + "?access_token=" + accessToken;
        FutureTask<String> task=new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {


                return null;
            }
        });
        new Thread(task).start();
    }

//    public static void getMyWeibo(final String)
}

