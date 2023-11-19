import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class IPGeoMain {
    private static IPGeolocationAPI api;

    public IPGeoMain() {
        try {
            InputStream inputStream = new FileInputStream("/home/key.yml");
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> data = yaml.load(inputStream);
            api = new IPGeolocationAPI(data.get("ipgeo").get("apiKey"));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String getIPInfo(String ipAddr){
        GeolocationParams geoParams = new GeolocationParams();
        geoParams.setIPAddress(ipAddr);
        geoParams.setLang("en");

//        var fields = new String[]{"geo",
//                "isp",
//                "time_zone"};
//        geoParams.setFields(StringUtils.join(fields, ","));

        Geolocation geolocation = api.getGeolocation(geoParams);

        JSONObject ipInfo = new JSONObject();
        // Check if geolocation lookup was successful
        if(geolocation.getStatus() == 200) {
            ipInfo.put("ipaddr", geolocation.getIPAddress());
            ipInfo.put("hostname", geolocation.getHostname());
            ipInfo.put("countryName", geolocation.getCountryName());
            ipInfo.put("countryCode", geolocation.getCountryCode2());
            ipInfo.put("countryFlag", geolocation.getCountryFlag());
            ipInfo.put("city", geolocation.getCity());
            ipInfo.put("district", geolocation.getDistrict());
            ipInfo.put("isp", geolocation.getISP());
            ipInfo.put("organization", geolocation.getOrganization());
            ipInfo.put("timeOffset", geolocation.getTimezone().getOffset());
            ipInfo.put("code", geolocation.getStatus()+"");
        }
        ipInfo.put("code", geolocation.getStatus()+"");
        return ipInfo.toString();
    }
}
