package ch.mixin.islandgenerator.islandGeneration.islandConstructor;

import ch.mixin.islandgenerator.helperClasses.Functions;
import ch.mixin.islandgenerator.islandGeneration.IslandType;
import ch.mixin.islandgenerator.islandGeneration.islandShape.IslandShape;
import ch.mixin.islandgenerator.islandGeneration.islandShape.IslandShapeGenerator;
import ch.mixin.islandgenerator.main.IslandGeneratorPlugin;
import ch.mixin.islandgenerator.metaData.IslandData;
import ch.mixin.islandgenerator.model.Coordinate3D;
import ch.mixin.namegenerator.name.NameGenerator;
import ch.mixin.namegenerator.name.TitleGenerator;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class IslandConstructor {
    private final IslandGeneratorPlugin plugin;
    private final IslandShapeGenerator islandShapeGenerator;
    private final NameGenerator nameGenerator = new NameGenerator(new Random());
    private final TitleGenerator titleGenerator = new TitleGenerator(new Random());
    private HashMap<IslandType, Double> islandTypeWeights;

    public IslandConstructor(IslandGeneratorPlugin plugin) {
        this.plugin = plugin;
        islandShapeGenerator = new IslandShapeGenerator(plugin);
        initialize();
    }

    private void initialize() {
        ConfigurationSection islandTypeSection = plugin.getConfig().getConfigurationSection("islandTypeWeights");
        islandTypeWeights = new HashMap<>();

        if (islandTypeSection == null)
            return;

        for (IslandType islandType : IslandType.values())
            islandTypeWeights.put(islandType, islandTypeSection.getDouble(islandType.toString()));
    }

    public IslandBlueprint constructIsland(World world, IslandData islandData) {
        Coordinate3D center = islandData.getIslandCenter();
        IslandConstructorPremise islandConstructorPremise = new IslandConstructorPremise(Functions.getRandomWithWeights(islandTypeWeights));

        int maximumHeight = world.getMaxHeight();
        int minimumHeight = world.getMinHeight();

        IslandShape islandShape = islandShapeGenerator.generateIslandShape(
                maximumHeight - minimumHeight
        );

        ArrayList<Coordinate3D> emptyRoofSpaces = new ArrayList<>(islandShape.getLayerTop());

        Coordinate3D lootPosition = constructLootPosition(center, emptyRoofSpaces);
        HashMap<Coordinate3D, Material> blockMap = constructBlocks(islandConstructorPremise, center, maximumHeight, minimumHeight, islandShape);
        HashMap<Coordinate3D, TreeType> treeMap = constructTrees(islandConstructorPremise, center, emptyRoofSpaces);
        ArrayList<Coordinate3D> cactusList = constructCacti(islandConstructorPremise, center, maximumHeight, minimumHeight, emptyRoofSpaces, blockMap);

        ArrayList<String> names = constructNames();
        Coordinate3D nameLocation = lootPosition.sum(0, 3, 0);
        islandData.setLootPosition(lootPosition);
        islandData.setNames(names);
        return new IslandBlueprint(world, center, islandShape.getWeightCenter().sum(center), islandShape.getWeightRadius(), lootPosition, new ArrayList<>(blockMap.entrySet()), new ArrayList<>(treeMap.entrySet()), cactusList, names, nameLocation);
    }

    private Coordinate3D constructLootPosition(Coordinate3D center, ArrayList<Coordinate3D> emptyRoofSpaces) {
        Coordinate3D lootPosition = emptyRoofSpaces.get(new Random().nextInt(emptyRoofSpaces.size()));
        emptyRoofSpaces.remove(lootPosition);
        lootPosition = lootPosition.sum(center);
        return lootPosition;
    }

    private HashMap<Coordinate3D, Material> constructBlocks(IslandConstructorPremise islandConstructorPremise, Coordinate3D center, int maximumHeight, int minimumHeight, IslandShape islandShape) {
        HashMap<Coordinate3D, Material> blockMap = new HashMap<>();

        if (islandConstructorPremise.getBlockTypesBot().size() > 0) {
            for (Coordinate3D c3d : islandShape.getLayerBot()) {
                Coordinate3D point = c3d.sum(center);

                if (point.getY() >= minimumHeight && point.getY() <= maximumHeight)
                    blockMap.put(point, Functions.getRandomWithWeights(islandConstructorPremise.getBlockTypesBot()));
            }
        }

        for (Coordinate3D c3d : islandShape.getLayerMid()) {
            Coordinate3D point = c3d.sum(center);

            if (point.getY() >= minimumHeight && point.getY() <= maximumHeight)
                blockMap.put(point, Functions.getRandomWithWeights(islandConstructorPremise.getBlockTypesMid()));
        }

        if (islandConstructorPremise.getBlockTypesTop().size() > 0) {
            for (Coordinate3D c3d : islandShape.getLayerTop()) {
                Coordinate3D point = c3d.sum(center);

                if (point.getY() >= minimumHeight && point.getY() <= maximumHeight)
                    blockMap.put(point, Functions.getRandomWithWeights(islandConstructorPremise.getBlockTypesTop()));
            }
        }

        return blockMap;
    }

    private HashMap<Coordinate3D, TreeType> constructTrees(IslandConstructorPremise islandConstructorPremise, Coordinate3D center, ArrayList<Coordinate3D> emptyRoofSpaces) {
        HashMap<Coordinate3D, TreeType> treeMap = new HashMap<>();

        for (int i = 0; i < emptyRoofSpaces.size(); i++) {
            Coordinate3D c3d = emptyRoofSpaces.get(i);

            if (new Random().nextDouble() < islandConstructorPremise.getTreeFrequency()) {
                emptyRoofSpaces.remove(c3d);
                i--;
                treeMap.put(c3d.sum(center).sum(0, 1, 0), Functions.getRandomWithWeights(islandConstructorPremise.getTreeWeights()));
            }
        }

        return treeMap;
    }

    private ArrayList<Coordinate3D> constructCacti(IslandConstructorPremise islandConstructorPremise, Coordinate3D center, int maximumHeight, int minimumHeight, ArrayList<Coordinate3D> emptyRoofSpaces, HashMap<Coordinate3D, Material> blockMap) {
        ArrayList<Coordinate3D> cactusList = new ArrayList<>();

        for (int i = 0; i < emptyRoofSpaces.size(); i++) {
            Coordinate3D c3d = emptyRoofSpaces.get(i);
            Coordinate3D c3dOffset = c3d.sum(center);
            Material material = blockMap.get(c3dOffset);

            if (material != Material.SAND && material != Material.RED_SAND)
                continue;

            if (new Random().nextDouble() < islandConstructorPremise.getCactusFrequency()) {
                emptyRoofSpaces.remove(c3d);
                i--;

                for (int height = 0; height < 3; height++) {
                    Coordinate3D point = c3dOffset.sum(0, height + 1, 0);

                    if (point.getY() < minimumHeight || point.getY() > maximumHeight)
                        break;

                    cactusList.add(point);
                    height++;
                }
            }
        }

        return cactusList;
    }

    private ArrayList<String> constructNames() {
        ArrayList<String> names = new ArrayList<>();
        names.add(nameGenerator.generateName(3, 12));
        names.add(titleGenerator.generateTitle(10, 30));
        return names;
    }
}
