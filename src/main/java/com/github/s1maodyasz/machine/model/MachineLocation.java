package com.github.s1maodyasz.machine.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineLocation {

	@BsonProperty("worldId")
	@NonNull
	private UUID worldId;

	@BsonProperty("x")
	private long x;

	@BsonProperty("y")
	private long y;

	@BsonProperty("z")
	private long z;
}
