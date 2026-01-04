package com.github.s1maodyasz.machine.message;

import com.github.s1maodyasz.machine.MachinePlugin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstants {
    public static final String UNAUTHORIZED =
        MachinePlugin.getInstance().getConfig().getString("messages.unauthorized", "");
    public static final String ISSUE_SUCCESS =
        MachinePlugin.getInstance().getConfig().getString("messages.issue.success", "");
    public static final String ISSUE_INVALID_KEY =
        MachinePlugin.getInstance().getConfig().getString("messages.issue.invalidKey", "");
    public static final String ISSUE_INVALID_AMOUNT =
        MachinePlugin.getInstance().getConfig().getString("messages.issue.invalidAmount", "");
    public static final String ISSUE_INVENTORY_FULL =
        MachinePlugin.getInstance().getConfig().getString("messages.issue.inventoryFull", "");
    public static final String PLACE_SUCCESS =
        MachinePlugin.getInstance().getConfig().getString("messages.place.success", "");
    public static final String PLACE_INVALID_ITEM =
        MachinePlugin.getInstance().getConfig().getString("messages.place.invalidItem", "");
    public static final String PLACE_INVALID_CONFIG =
        MachinePlugin.getInstance().getConfig().getString("messages.place.invalidConfig", "");
    public static final String PLACE_TARGET_NOT_REPLACEABLE =
        MachinePlugin.getInstance().getConfig().getString("messages.place.targetNotReplaceable", "");
    public static final String PLACE_WORLD_NULL =
        MachinePlugin.getInstance().getConfig().getString("messages.place.worldNull", "");
    public static final String PLACE_DIFFERENT_TYPE_NEARBY =
        MachinePlugin.getInstance().getConfig().getString("messages.place.differentTypeNearby", "");
    public static final String PLACE_STACK_NOT_ALLOWED =
        MachinePlugin.getInstance().getConfig().getString("messages.place.stackNotAllowed", "");
    public static final String PLACE_SPAWN_DISPLAY_FAILED =
        MachinePlugin.getInstance().getConfig().getString("messages.place.spawnDisplayFailed", "");
    public static final String BREAK_SUCCESS =
        MachinePlugin.getInstance().getConfig().getString("messages.break.success", "");
    public static final String BREAK_NOT_A_MACHINE =
        MachinePlugin.getInstance().getConfig().getString("messages.break.notAMachine", "");
    public static final String BREAK_NO_PERMISSION =
        MachinePlugin.getInstance().getConfig().getString("messages.break.noPermission", "");
    public static final String OPEN_SUCCESS =
        MachinePlugin.getInstance().getConfig().getString("messages.open.success", "");
    public static final String OPEN_NO_PERMISSION =
        MachinePlugin.getInstance().getConfig().getString("messages.open.noPermission", "");
    public static final String ERROR_INTERNAL =
        MachinePlugin.getInstance().getConfig().getString("messages.error.internal", "");
    public static final String ERROR_DATABASE =
        MachinePlugin.getInstance().getConfig().getString("messages.error.database", "");
    public static final String COLLECT_EMPTY =
        MachinePlugin.getInstance().getConfig().getString("messages.collect.empty", "");
    public static final String COLLECT_SUCCESS =
        MachinePlugin.getInstance().getConfig().getString("messages.collect.success", "");
}
