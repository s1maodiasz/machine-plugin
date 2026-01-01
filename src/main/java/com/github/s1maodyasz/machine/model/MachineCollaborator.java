package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.MachinePermissionEnum;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Builder
public final class MachineCollaborator {

	@NonNull
	@BsonProperty("machineId")
	private UUID machineId;

	@NonNull
	@BsonProperty("ownerId")
	private UUID ownerId;

	@NonNull
	@BsonProperty("permissions")
	private List<MachinePermissionEnum> permissions;
}
