package net.swifthq.swiftapi.util;

public enum ActionResult {
    SUCCESS,
    CONSUME,
    PASS,
    FAIL;

    public boolean isAccepted() {
        return this == SUCCESS || this == CONSUME;
    }

    public boolean shouldSwingHand() {
        return this == SUCCESS;
    }

    public static ActionResult success(boolean shouldSwingHand) {
        return shouldSwingHand ? SUCCESS : CONSUME;
    }
}