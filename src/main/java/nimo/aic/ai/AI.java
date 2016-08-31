package nimo.aic.ai;


import nimo.aic.grpc.Id;
import nimo.aic.tiles.TileEntityId;

import java.util.HashMap;
import java.util.Map;

public class AI {

    private final Map<Id, TileEntityId> teMap = new HashMap<>();

    public void register(Id id, TileEntityId te) {
        teMap.put(id, te);
    }

    public TileEntityId get(Id id) {
        return teMap.get(id);
    }
}
