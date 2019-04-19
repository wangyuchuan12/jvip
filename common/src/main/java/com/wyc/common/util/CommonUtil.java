package com.wyc.common.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 *
 * @author wangyuchuan
 *
 */
public class CommonUtil {

	/*public static void main(String[]args){
	//	System.out.println(filterEmoji("sdfsdfdsf所属💗❤🐶🐈️️️🐔🐦⏰"));
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		System.out.println(list.subList(0, 6));


	}*/

    /**
     * 检测是否有emoji字符
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {

        if(source==null||source.equals("")){
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }


    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /***
     *  true:already in using  false:not using
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host,int port) throws UnknownHostException{
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress,port);
            return true;
        } catch (IOException e) {
            return false;
        }
    }



    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(url);
        if (m.find( )) {
            System.out.println(m.group(0));
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    public static File getPath(String mode,String name)throws Exception{
        if(mode.equals("classpath")){
            ClassPathResource classPathResource = new ClassPathResource(name);
            return classPathResource.getFile();
        }else if(mode.equals("path")){
            Resource resource = new FileSystemResource(new File(name));
            return resource.getFile();
        }else{
            Resource resource = new FileSystemResource(new File(name));
            return resource.getFile();
        }
    }


    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if(source==null||source.equals("")){
            return "";
        }
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }



    /**
     * 数据加密
     *
     * @param needHashCode
     * @return
     */
    public static String getHashCode(String needHashCode) {
        return needHashCode;
    }

    /**
     * 加密钱包私钥
     *
     * @param secret
     *            钱包私钥
     * @return
     */
    public static String aseSecret(String secret) {
        return secret;
    }

    /**
     * 判断数据是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object str) {
        return null == str || "".equals(str) || "undefined".equals(str)||"null".equals(str);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Timestamp getCurrent() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String timestamp2String8(Timestamp arg) {
        if (arg == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(arg);
    }

    public static Properties getProperties(){
        try {
            Properties prop = new Properties();
            prop.load(new FileReader(new File("application.properties")));
            return prop;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建井通流水号
     *
     * @return
     */

    /**
     * 获取WEB-INF的路径
     *
     * @return
     */
    public static String getXmlPath() {
        String path = Thread.currentThread().getContextClassLoader()
                .getResource("").toString();
        // String u=Class.class.getClass().getResource("/").getPath();
        path = path.replace("file:", "/"); // 去掉file:
        path = path.replace("classes/", ""); // 去掉class\
        path = path.substring(1); // 去掉第一个\,如 \D:\JavaWeb...
        return path;
    }

    /**
     * 实体转换json字符串
     *
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static String ObjToJson(Object obj) throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            ClassNotFoundException {
        StringBuilder build = new StringBuilder();
        build.append("{");
        @SuppressWarnings("rawtypes")
        // 反射加载类
                Class cla = Class.forName(obj.getClass().getName());

        StringBuffer methodname = new StringBuffer();
        // 获取java类的变量
        Field[] fields = cla.getDeclaredFields();
        String separate = "";
        for (Field temp : fields) {
            build.append(separate);
            build.append("\"");
            build.append(temp.getName());
            build.append("\":");

            methodname.append("get");
            methodname.append(temp.getName().substring(0, 1).toUpperCase());
            methodname.append(temp.getName().substring(1));

            build.append("\"");
            // 获取java的get方法
            Method method = cla.getMethod(methodname.toString());
            // 执行get方法，获取变量参数的直。
            build.append(method.invoke(obj));
            build.append("\"");
            methodname.setLength(0);
            separate = ",";
        }

        build.append("}");
        return build.toString();
    }

    /**
     * BigDecimal格式化
     *
     * @param T
     * @return
     */
    public static BigDecimal bigDecimalFormat(Object T) {

        BigDecimal amount1 = new BigDecimal("0.00");
        if (T == null) {
            amount1 = new BigDecimal("0");
            amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (T instanceof String) {
            // 判断金额是不是数字类型
            if (T.equals("")) {
                amount1 = new BigDecimal("0");
                amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                amount1 = new BigDecimal((String) T);
                amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        if (T instanceof Integer) {
            // 判断金额是不是数字类型
            amount1 = new BigDecimal((Integer) T);
            amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (T instanceof Double) {
            // 判断金额是不是数字类型
            amount1 = new BigDecimal((Double) T);
            amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (T instanceof Long) {
            // 判断金额是不是数字类型
            amount1 = new BigDecimal((Long) T);
            amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (T instanceof BigDecimal){
            // 判断金额是不是BigDecimal类型
            amount1 =  (BigDecimal) T ;
            amount1 = amount1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return amount1;
    }

    /**
     * 把long型时间值转换成yyyy-MM-dd HH:mm:ss的格式的字符串
     */
    public static String toDate(String timestamp){

        long date = Long.parseLong(timestamp);


        //Date date = new Date(_timestamp);
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        return sm.format(date);
    }
    /**
     *
     * @param i
     * @param j
     * @return
     */
    public static boolean checkBalance(int i, int j) {
        int c = i & j;
        return c == 0 ? false : true;
    }

    public static String getRandom(int digits) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < digits; i++) {
            int num = random.nextInt(9);
            sb.append(num);
        }
        return sb.toString();
    }




    /**
     * 获取出身年月
     * @param cardNo
     * @return
     */
    public static Integer getBirthdayByCardNo(String cardNo) {
        try {

            String str = cardNo.substring(6, 10);
            return Integer.valueOf(str);
        } catch (Exception e) {

        }

        return null;
    }


    public static Integer randomNum(Integer min,Integer max){
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;

        return s;
    }

    /**
     * 获取随机数，短信验证码
     * @param numberFlag 是否纯数字
     * @param length 长度
     * @return
     */
    public static String createRandom(boolean numberFlag, int length){
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }

    public static final void overlapHttpImage(String bigHttpPath, String smallHttpPath, String outFile) {
        try {
            URLConnection bigHttpURLConnection = new URL(bigHttpPath).openConnection();
            BufferedImage big = ImageIO.read(bigHttpURLConnection.getInputStream());
            URLConnection smallHttpURLConnection = new URL(smallHttpPath).openConnection();
            BufferedImage small = ImageIO.read(smallHttpURLConnection.getInputStream());
            Graphics2D g = big.createGraphics();
            g.drawImage(small, 10, 10,100,100, null);
            g.dispose();
            ImageIO.write(big, outFile.split("\\.")[1], new File(outFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}