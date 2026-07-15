package Game.Engine.Map.Tiles;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.List;

public class BuildingSnapshot(
        private String name,
        BuildingType type,
        int id,

        Inventory inventory,
        long takenAtTick,
        Coords coords
) {}