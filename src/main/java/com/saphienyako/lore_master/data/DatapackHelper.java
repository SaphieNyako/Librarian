package com.saphienyako.lore_master.data;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.ModList;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DatapackHelper {

    public static final Gson GSON = new GsonBuilder() //Google’s JSON serializer/deserializer, this is how we read JSON files
            .disableHtmlEscaping() //safe for HTML
            .setPrettyPrinting() //easier to read
            .create();

    public static List<ItemStack> loadStackList(ResourceManager manager, String path, String name) {

        List<ItemStack> result = new ArrayList<>();

        String targetPath = path + "/" + name + ".json";

        // Find all matching datapack resources
        Map<ResourceLocation, Resource> resources =
                manager.listResources(path, new Predicate<ResourceLocation>() {
                    @Override
                    public boolean test(ResourceLocation location) {
                        return location.getPath().equals(targetPath);
                    }
                });

        // Iterate through each resource (datapack file)
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {

            ResourceLocation id = entry.getKey();
            Resource resource = entry.getValue();

            try (Reader reader = resource.openAsReader()) {

                JsonElement root = GsonHelper.fromJson(GSON, reader, JsonElement.class);

                if (!root.isJsonArray()) {
                    continue;
                }

                JsonArray array = root.getAsJsonArray();

                for (JsonElement element : array) {

                    if (!element.isJsonObject()) {
                        continue;
                    }

                    JsonObject json = element.getAsJsonObject();

                    // Optional mod dependency check
                    if (json.has("mod")) {
                        String modid = json.get("mod").getAsString();
                        if (!ModList.get().isLoaded(modid)) {
                            continue;
                        }
                    }

                    // Convert JSON → ItemStack
                    ItemStack stack = CraftingHelper.getItemStack(json, true);

                    if (!stack.isEmpty()) {
                        result.add(stack);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException("Failed to load datapack resource: " + id, e);
            }
        }

        return result;
    }

    /*
    public static List<ItemStack> loadStackList(ResourceManager manager, String path, String name) {

        try {
            var jsonArrays = DataLoader.joinJson(
                    DataLoader.locate(manager, path + "/" + name + ".json", name), //search path
                    (id, data) -> data.getAsJsonArray()
            );

            List<ItemStack> result = new ArrayList<>();

            for (JsonArray array : jsonArrays.toList()) {
                for (JsonElement element : array) {

                    if (!element.isJsonObject())//everything in list should be json object
                        continue;

                    JsonObject json = element.getAsJsonObject(); //get the json object

                    if (json.has("mod")) { //"mod"
                        String modid = json.get("mod").getAsString();
                        if (!ModList.get().isLoaded(modid)) //is mod installed
                            continue;
                    }

                    ItemStack stack = CraftingHelper.getItemStack(json, true);  //"item" "count" "nbt"
                    if (!stack.isEmpty()) {
                        result.add(stack);
                    }
                }
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */

}
