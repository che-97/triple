package com.eun.triple.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventAction {
    ADD, MOD, DELETE;

    public static boolean isAdd(String action) {
        return ADD.name().equals(action);
    }

    public static boolean isMod(String action) {
        return MOD.name().equals(action);
    }

    public static boolean isDelete(String action) {
        return DELETE.name().equals(action);
    }

}
