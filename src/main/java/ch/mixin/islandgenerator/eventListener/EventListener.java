package ch.mixin.islandgenerator.eventListener;

import ch.mixin.islandgenerator.main.IslandGeneratorPlugin;
import org.bukkit.event.Listener;

public class EventListener implements Listener {
    private final IslandGeneratorPlugin plugin;

    public EventListener(IslandGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

//    @EventHandler
//    public void manipulate(PlayerInteractEvent e) {
//        Block block = e.getClickedBlock();
//
//        if (block == null)
//            return;
//
//        if (!block.getType().equals(Material.CHEST))
//            return;
//
//        if (e.getPlayer().getGameMode() != GameMode.ADVENTURE
//                && e.getPlayer().getGameMode() != GameMode.CREATIVE
//                && e.getPlayer().getGameMode() != GameMode.SURVIVAL)
//            return;
//
//        World world = block.getWorld();
//        String worldName = world.getName();
//        MetaData metaData = plugin.getMetaData();
//        HashMap<String, WorldData> worldDataMap = metaData.getWorldDataMap();
//
//        if (!worldDataMap.containsKey(worldName))
//            return;
//
//        ArrayList<IslandData> islandDatas = worldDataMap.get(worldName).getIslandDatas();
//        Location location = block.getLocation();
//        Coordinate3D blockCoordinate = Coordinate3D.toCoordinate(location);
//
//        IslandData islandData = null;
//
//        for (IslandData id : islandDatas) {
//            if (id.isLooted())
//                continue;
//
//            if (id.getLootPosition().equals(blockCoordinate)) {
//                islandData = id;
//                break;
//            }
//        }
//
//        if (islandData == null)
//            return;
//
//        islandData.setLooted(true);
//        // TODO: 2022/12/29 宝箱处理在这里!!
//        block.setType(Material.AIR);
//        Location locationLoot = blockCoordinate.sum(0, 1, 0).toLocation(world);
//        locationLoot.add(0.5, 0.5, 0.5);
//        HashMap<Material, Integer> lootSet = plugin.getLootManager().collectLoot(plugin.getConfig().getInt("lootMultiplier"));
//
//        for (Material material : lootSet.keySet()) {
//            world.dropItem(locationLoot, new ItemStack(material, lootSet.get(material)));
//        }
//    }
}
