 package com.coder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";
    private static final String PREFIX_POST = "post";

    // Entity's like
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // User's like
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // User's follow
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // Entity's follower
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // Kaptcha
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // Login ticket
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // User
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // Single day UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // Multiple days UV
    public static String getUVKey(String start, String end) {
        return PREFIX_UV + SPLIT + start + SPLIT + end;
    }

    // Single day DAU
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // Multiple days DAU
    public static String getDAUKey(String start, String end) {
        return PREFIX_UV + SPLIT + start + SPLIT + end;
    }

    // Post score
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}
