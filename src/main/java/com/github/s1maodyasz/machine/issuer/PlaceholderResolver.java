package com.github.s1maodyasz.machine.issuer;

@FunctionalInterface
interface PlaceholderResolver<Configuration, Data> {

    String resolve(Configuration configuration, Data data);
}
