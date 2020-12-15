
package com.jzz.learn;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jzz.learn.ApiAuthUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;


public class Test2 {

    @Resource
    private static RestTemplate restTemplate = new RestTemplate();
    public static final String url = "http://222.212.89.53:58080/18/sas-qhd/";

    public static  final int fileSize = 1024*1024*2;
    //切割文件
    public static void cutFile() throws Exception{
        //确定上传文件路径
        String filePath = "F:/wavfile/test.wav";
        File file = new File(filePath);
        FileInputStream inputStream = FileUtils.openInputStream(file);
        File temp = null;
        byte[] buffer = new byte[fileSize];
        int len = 0;
        String outputDir = "F:\\wavfile\\";
        //切分多个文件
        for (int i = 10000; (len = IOUtils.read(inputStream, buffer)) > 0; i++) {
            temp = FileUtils.getFile(outputDir, String.valueOf(i));
            FileUtils.writeByteArrayToFile(temp, buffer, 0, len);
        }
    }
    public static String encodeData(String salt, int index, boolean isFirst, String completeFilePath, String slicePath, boolean isLast, String taskId)
            throws Exception {
        //加密分片
        String[] pathArray = completeFilePath.split("/");
        JSONObject jsonObject = new JSONObject();
        if (!isFirst){
            jsonObject.put("taskId",taskId);
        }
        if (isLast){
            jsonObject.put("complete","true");
        }else {
            jsonObject.put("complete","false");
        }
        jsonObject.put("fileMd5",fileMD5(completeFilePath));
        //jsonObject.put("slice",fileSystemResource);
        System.out.println(pathArray);
        jsonObject.put("fileName",pathArray[pathArray.length-1]);
        jsonObject.put("sliceMd5",fileMD5(slicePath));
        jsonObject.put("sliceIndex",String.valueOf(index));
        jsonObject.put("sliceOffset",String.valueOf(fileSize*(index-1)));
        //Map<String, String> params = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>(){});
        //String sign = ApiAuthUtils.sign(params);
        String data = ApiAuthUtils.encode(salt,jsonObject.toString());
        System.out.println("data:"+data+"/nsalt:"+salt);
        //Map<String,String> map = new HashMap<String,String>();
        /*map.put("sign",sign);
        map.put("data",data);*/
        return data;
    }

    public static String fileMD5(String completeFilePath) throws FileNotFoundException {
        //String completeFilePath = "F:/wavfile/test.wav";
        File file = new File(completeFilePath);
        /*boolean ex = !file.exists();
        if(ex){
            throw new RuntimeException("file not exist");
        }*/
        String completeMd5 = DigestUtils.md5Hex(String.valueOf(new FileInputStream(completeFilePath)));
        // String completeMd5 = DigestUtils.md5Hex(String.valueOf(new FileInputStream(file)));
        return completeMd5;
    }

    public static String getToken(String phone,String pwd) throws Exception {
        String salt = String.valueOf((int)((Math.random()*9+1)*100000));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password",pwd);
        jsonObject.put("telephone",phone);
        /*Map<String, String> jsonObject  = new HashMap<String,String>();
        jsonObject.put("telephone",phone);
        jsonObject.put("password",pwd);*/
        //Map<String, String> params = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>(){});
        //System.out.println(params);
        //String sign = ApiAuthUtils.sign(params);
        String data = ApiAuthUtils.encode(salt,jsonObject.toString());
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        JSONObject form = new JSONObject();
        form.put("data",data);
        form.put("deviceNo","862312048758227");
        form.put("salt", salt);
        form.put("time",String.valueOf(System.currentTimeMillis()));
        form.put("version","1.0");
        Map<String, String> params = JSONObject.parseObject(form.toJSONString(), new TypeReference<Map<String, String>>(){});
        form.put("sign",ApiAuthUtils.sign(params));
        //HttpEntity<MultiValueMap<String, String>> files = new HttpEntity<>(form, headers);
        //System.out.println(form);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(form.toString(), headers);
        HttpHeaders result = new HttpHeaders();
        try {
            result = restTemplate.postForEntity(url+"u/login", formEntity, String.class).getHeaders();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(result);
        String re = String.valueOf(result.get("AUTH-TOKEN"));
        return re;
    }

    public static void uploadFile(String Token) throws Exception{
        String completeFilePath = "F:/wavfile/test.wav";
        File file = new File(completeFilePath);
        /*if(!file.exists()){
            throw new RuntimeException("file not exist");
        }
        String completeMd5 = DigestUtils.md5Hex(String.valueOf(new FileInputStream(file)));*/
        int fileCount = (int) Math.ceil(file.length() / fileSize);//分片数量


        int count = 0;
        String taskId = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("AUTH-TOKEN",Token);

        //上传文件
        for (int i = 10000;i<fileCount+10000;i++){
            String salt = String.valueOf((int)((Math.random()*9+1)*100000));
            count++;
            String tempFilePath = "F:/wavfile/"+i+".wav";
            FileSystemResource fileSystemResource = new FileSystemResource(tempFilePath);
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            Map<String,String> params = new HashMap<>();
            //JSONObject form = new JSONObject();
            boolean isFirst = false;
            boolean isLast = false;
            if (count==1){
                isFirst = true;
            }else if (count==fileCount){
                isLast = true;
            }
            String data = encodeData(salt,count,isFirst,completeFilePath,tempFilePath,isLast,taskId);
            form.add("data",data);
            form.add("deviceNo","862312048758227");
            form.add("salt", salt);
            form.add("time",String.valueOf(System.currentTimeMillis()));
            form.add("version","1.0");
            params.put("data",data);
            params.put("deviceNo","862312048758227");
            params.put("salt", salt);
            params.put("time",String.valueOf(System.currentTimeMillis()));
            params.put("version","1.0");
            //Map<String, String> params = JSONObject.parseObject(form.toJSONString(), new TypeReference<Map<String, String>>(){});

            form.add("sign", ApiAuthUtils.sign(params));
            form.add("slice",fileSystemResource);
            //System.out.println("---------data------------:/n"+form.get("data")+"/nsalt"+salt);
            //用HttpEntity封装整个请求报文
            MediaType type = MediaType.parseMediaType("multipart/form-data");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            //HttpEntity<String> formEntity = new HttpEntity<String>(form.toString(), headers);
            HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(form, headers);
            String result = "";
            System.out.println("headers:"+headers.toString());
            result = restTemplate.postForEntity(url+"upload/file/slice", formEntity, String.class).getBody();

            /*HttpEntity<String> formEntity = new HttpEntity<String>(form.toString(), headers);
            //HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
            Object result = restTemplate.postForEntity(url+"upload/file/slice", formEntity, Object.class);
            */
            if (count==1){
                //taskId = result.getString("taskId");
                //System.out.println(taskId);
                System.out.println(result);
            }


            System.out.println("tempFilePath:"+tempFilePath);
            System.out.println(result.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        //cutFile();
        //System.out.println(getToken("18010697718","123456"));
        /*String Token = getToken("18010697718","123456");
        String token = Token.substring(1,Token.length()-1);*/
        uploadFile();

    }
    public static void uploadFile() throws Exception{
        String Token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ" +
                "7XCJ1c2VySWRcIjo0LFwidGVsZXBob25lXCI6XCIxODAxMDY5NzcxOFwiLF" +
                "wiY2hhbm5lbFwiOlwicWhkXCIsXCJ1c2VyTnVtYmVyXCI6XCIxODAxMDY5" +
                "NzcxOFwifSIsImlzcyI6IlNBUy1RSEQiLCJleHAiOjE1NzI2MTIxNTIsIm" +
                "lhdCI6MTU3MjUyNTc1Mn0.mjUyZdSeUlb6Xud8B7qkGaHAcbshWwPmY_RYLHIsHe8";
        String completeFilePath = "F:/wavfile/test.wav";
        File file = new File(completeFilePath);
        int fileCount = (int) Math.ceil(file.length() / fileSize);//分片数量
        int count = 0;
        String taskId = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("AUTH-TOKEN",Token);
        //上传文件
        //for (int i = 10000;i<fileCount+10000;i++){
        String salt = String.valueOf((int)((Math.random()*9+1)*100000));
        count++;
        String tempFilePath = "F:/wavfile/"+10000+".wav";
        FileSystemResource fileSystemResource = new FileSystemResource(tempFilePath);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        Map<String,String> params = new HashMap<>();
        //JSONObject form = new JSONObject();
        boolean isFirst = false;
        boolean isLast = false;
        if (count==1){
            isFirst = true;
        }else if (count==fileCount){
            isLast = true;
        }
        String data = encodeData(salt,count,isFirst,completeFilePath,tempFilePath,isLast,taskId);
        form.add("data",data);
        form.add("deviceNo","862312048758227");
        form.add("salt", salt);
        form.add("time",String.valueOf(System.currentTimeMillis()));
        form.add("version","1.0");
        params.put("data",data);
        params.put("deviceNo","862312048758227");
        params.put("salt", salt);
        params.put("time",String.valueOf(System.currentTimeMillis()));
        params.put("version","1.0");
        //Map<String, String> params = JSONObject.parseObject(form.toJSONString(), new TypeReference<Map<String, String>>(){});

        form.add("sign",ApiAuthUtils.sign(params));
        form.add("slice",fileSystemResource);

        //用HttpEntity封装整个请求报文
        MediaType type = MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE);
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //HttpEntity<String> formEntity = new HttpEntity<String>(form.toString(), headers);
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(form, headers);
        String result = "";
        System.out.println("headers:"+headers.toString());

        result = restTemplate.postForEntity(url+"upload/file/slice", formEntity, String.class).getBody();
        if (count==1){
            //taskId = result.getString("taskId");
            //System.out.println(taskId);
            System.out.println(result);
        }


        System.out.println("tempFilePath:"+tempFilePath);
        System.out.println(result.toString());
        //}
    }
}
