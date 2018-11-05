package co.ppk.util;

import java.util.Optional;

public interface Constant {

    String BUCKET = Optional.ofNullable(System.getenv("AWS_S3_REPORTS_BUCKET")).orElse("nekso2-test");
    String REGION = Optional.ofNullable(System.getenv("AWS_REGION")).orElse("us-east-2");
    int PAGE_LIMIT = 2;
}