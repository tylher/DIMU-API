package com.dimu.dimuapi.Enum;

import lombok.Getter;

@Getter
public enum AWSBucketList {
    DIIMU_USER_BUCKET("diiimuuserbucket"),
    DIIMU_GOOD_SERVICE_BUCKET("diimugoodservicebucket");

    private final String bucketName;

    AWSBucketList(String bucketName){
        this.bucketName = bucketName;
    }
}
