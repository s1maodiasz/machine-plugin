package com.github.s1maodyasz.machine.handlers;

import lombok.Builder;

public sealed interface MachinePlacementResult
    permits MachinePlacementResult.Success, MachinePlacementResult.Stacked, MachinePlacementResult.Ignore {

    record Success(String name) implements MachinePlacementResult {}
    record Stacked(String name, double amount, double actual) implements MachinePlacementResult {}
    record Ignore() implements MachinePlacementResult {}
}
