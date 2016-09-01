package nimo.aic.ai;


import net.minecraft.world.World;
import nimo.aic.grpc.Id;
import nimo.aic.tiles.TileEntityId;

import java.util.HashMap;
import java.util.Map;

public class AI {

    private static final AI clientAI = new AI("client");
    private static final AI serverAI = new AI("server");

    public static AI server() {
        return serverAI;
    }

    public static AI client() {
        return clientAI;
    }

    public static AI forWorld(World worldObj) {
        if (worldObj == null) {
            throw new IllegalArgumentException("world must not be null.");
        }

        return worldObj.isRemote ? clientAI : serverAI;
    }

    private final Map<Id, TileEntityId> teMap = new HashMap<>();
    private final String side;

    private AI(String side) {
        this.side = side;
    }

    public void register(Id id, TileEntityId te) {
        teMap.put(id, te);
    }

    public TileEntityId get(Id id) {
        return teMap.get(id);
    }

    public void unregister(Id id) {
        teMap.remove(id);
    }
}
