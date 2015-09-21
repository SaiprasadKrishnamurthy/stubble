package main;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sai on 21/09/2015.
 */
public class Sample {

    public static void main(String[] args) {
        String apiDef = "/:customer/:phone/registration";
        String real = "/saiprasad/099882/registration";
        System.out.println(StringUtils.difference(real, apiDef));
    }
}
