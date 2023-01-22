import common.ConnUtil;
import common.DemoException;
import common.TokenHolder;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

public class TtsMain {

    //  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
    private static String appKey;

    // 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
    private static String secretKey;

    // text 的内容为"欢迎使用百度语音合成"的urlencode,utf-8 编码
    // 可以百度搜索"urlencode"

    // 发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
    // 精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
    private static final int per = 0;
    // 语速，取值0-15，默认为5中语速
    private static final int spd = 5;
    // 音调，取值0-15，默认为5中语调
    private static final int pit = 5;
    // 音量，取值0-9，默认为5中音量
    private static final int vol = 5;

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private static final int aue = 6;

    private static final String url = "https://tsn.baidu.com/text2audio"; // 可以使用https

    private static final String cuid = "JAVA";

    public TtsMain() {
        try {
            InputStream inputStream = new FileInputStream("/home/key.yml");
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> data = yaml.load(inputStream);
            appKey = data.get("baiduTTS").get("appKey");
            secretKey = data.get("baiduTTS").get("secretKey");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String getReversedAudio(String prefix, String suffix, String text, String filename)
            throws IOException, DemoException, InterruptedException {
        TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
        holder.refresh();
        String token = holder.getToken();

        // 此处2次urlencode， 确保特殊字符被正确编码
        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(text));
        params += "&per=" + per;
        params += "&spd=" + spd;
        params += "&pit=" + pit;
        params += "&vol=" + vol;
        params += "&cuid=" + cuid;
        params += "&tok=" + token;
        params += "&aue=" + aue;
        params += "&lan=zh&ctp=1";
        // System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            savaAudioFile(bytes, prefix+filename+"."+suffix);
            return this.reverseAudio(prefix, filename, suffix);
        } else {
            System.err.println("ERROR: content-type= " + contentType);
            String res = ConnUtil.getResponseString(conn);
            System.err.println(res);
            return null;
        }
    }

    public String reverseAudioByBase64(String prefix, String suffix, String filename, String base64Str)
            throws IOException, InterruptedException {
        byte[] bytes = Base64.getDecoder().decode(base64Str);
        this.savaAudioFile(bytes, prefix+filename+"."+suffix);
        return reverseAudio(prefix, filename, suffix);
    }


    private void savaAudioFile(byte[] bytes, String filename) throws IOException{
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file);
        os.write(bytes);
        os.close();
    }

    private byte[] loadAudioFile(String filepath) throws IOException{
        File file = new File(filepath);
        FileInputStream is = new FileInputStream(file);
        byte[] audioBytes = is.readAllBytes();
        is.close();
        return audioBytes;
    }

    /* return Base64 */
    private String reverseAudio(String prefix, String filename, String suffix)
            throws InterruptedException, IOException {
        //use pydup, only support wav
        String cmd = "python /home/AudioReverser.py --prefix " + prefix +
                " --input_file "+filename+"."+suffix+" --output_file "+filename+"."+suffix;
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();

        byte[] reversedByteArray = loadAudioFile(prefix+filename+"."+suffix);
        Files.delete(Paths.get(prefix+filename+"."+suffix));

        return Base64.getEncoder().encodeToString(reversedByteArray);
    }

    // reverse manually aborted. developing...
//    private short[] reverseArray(short[] arr){
//        for(int i=0, j=arr.length-1; i < j; ++i,--j){
//            arr[i]^=arr[j];arr[j]^=arr[i];arr[i]^=arr[j];
//        }
//        return arr;
//    }

//    private byte[] reverseAudioBytes(byte[] bytes){
//        /*
//         The bytes length is diff with AudioSegment.from_wav()
//         So convert failed. Use pydup as alternative
//         */
//        short[] bufferArr = new short[bytes.length/2];
//        ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(bufferArr);
//
//        short[] reversedBufferArr = reverseArray(bufferArr);
//
//        byte[] reversedByteArray = new byte[bufferArr.length * 2];
//        ByteBuffer.wrap(reversedByteArray).order(ByteOrder.BIG_ENDIAN).asShortBuffer().put(reversedBufferArr);
//        return reversedByteArray;
//    }

}
