package com.github.s1maodyasz.machine.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstants {

    public static final String PREFIX = "prefix";

    public static final String PLACEHOLDER_PLAYER = "player";
    public static final String PLACEHOLDER_KEY = "key";
    public static final String PLACEHOLDER_AMOUNT = "amount";
    public static final String PLACEHOLDER_PERMISSION = "permission";

    public static final String NO_PERMISSION = "issue.noPermission";

    public static final String ISSUE_SUCCESS = "issue.success";
    public static final String ISSUE_INVALID_KEY = "issue.invalidKey";
    public static final String ISSUE_INVALID_AMOUNT = "issue.invalidAmount";
    public static final String ISSUE_INVENTORY_FULL = "issue.inventoryFull";

    public static final String PLACE_SUCCESS = "place.success";
    public static final String PLACE_INVALID_ITEM = "place.invalidItem";
    public static final String PLACE_INVALID_CONFIG = "place.invalidConfig";
    public static final String PLACE_TARGET_NOT_REPLACEABLE = "place.targetNotReplaceable";
    public static final String PLACE_WORLD_NULL = "place.worldNull";
    public static final String PLACE_DIFFERENT_TYPE_NEARBY = "place.differentTypeNearby";
    public static final String PLACE_STACK_NOT_ALLOWED = "place.stackNotAllowed";
    public static final String PLACE_SPAWN_DISPLAY_FAILED = "place.spawnDisplayFailed";

    public static final String BREAK_SUCCESS = "break.success";
    public static final String BREAK_NOT_A_MACHINE = "break.notAMachine";
    public static final String BREAK_NO_PERMISSION = "break.noPermission";

    public static final String OPEN_SUCCESS = "open.success";
    public static final String OPEN_NO_PERMISSION = "open.noPermission";

    public static final String ERROR_INTERNAL = "error.internal";
    public static final String ERROR_DATABASE = "error.database";
}
