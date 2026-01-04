package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.PermissionEnum;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineCollaborator {

    @NonNull
    @BsonProperty("playerId")
    private UUID playerId;

    @NonNull
    @BsonProperty("permissions")
    private List<PermissionEnum> permissions;

}
