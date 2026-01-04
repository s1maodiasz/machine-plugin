package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.ConsumptionMode;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import java.util.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.jetbrains.annotations.NotNull;

@Data
public class Machine {

    @BsonId
    private final UUID id = UUID.randomUUID();

    @BsonProperty("key")
    private String key;

    @BsonProperty("ownerId")
    private UUID ownerId;

    @BsonProperty("location")
    private MachineLocation location;

    @BsonProperty("collaborators")
    private Set<MachineCollaborator> collaborators = new HashSet<>();

    @BsonProperty("upgrades")
    private Map<String, Integer> upgrades = new HashMap<>();

    @BsonProperty("runtime")
    private MachineEngine engine = MachineEngine.NOT_RUNNING;

    @BsonProperty("drops")
    private double drops = 0;

    @BsonProperty("consumptionMode")
    private ConsumptionMode consumptionMode = ConsumptionMode.SPLIT;

    @BsonProperty("batteries")
    private List<BatterySlot> batteries = new ArrayList<>();

    @BsonIgnore
    public double energy() {
        return batteries.stream()
                .filter(BatterySlot::isActivated)
                .map(BatterySlot::getTotal)
                .reduce(Double::sum)
                .orElse(0D);
    }
}
