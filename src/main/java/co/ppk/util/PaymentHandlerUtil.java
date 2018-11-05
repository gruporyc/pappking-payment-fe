package co.ppk.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PaymentHandlerUtil {

    private static final List<String> PAYMENTS_REPORT_HEADER = Arrays.asList("DRIVER NAME", "DATE", "STATUS", "BANK", "CLABE", "RIDE EARNING COUNT",
            "RIDE EARNING AMMOUNT", "REFERRAL EARNING COUNT", "REFERRAL EARNING AMMOUNT", "TOTAL EARNING");

    public static String s3StorageUrl(String fileName) {
        return "https://s3." + Constant.REGION + ".amazonaws.com/" + Constant.BUCKET + "/" + fileName;
    }

    public static String getVersion() {
        return String.valueOf(new Date().getTime());
    }

    public static List<String> getPaymentsReportHeader() { return PAYMENTS_REPORT_HEADER; }
}
