package com.randioo.randioo_server_base.entity;

public class Verify {
    public int verifyId;
    public int useId;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Verify [verifyId=").append(verifyId).append(", useId=").append(useId).append("]");
        return builder.toString();
    }

}
