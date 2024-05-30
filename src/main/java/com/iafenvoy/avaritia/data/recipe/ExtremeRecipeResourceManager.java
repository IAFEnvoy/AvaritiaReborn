package com.iafenvoy.avaritia.data.recipe;

import com.google.gson.*;
import com.iafenvoy.avaritia.AvaritiaReborn;
import com.iafenvoy.avaritia.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class ExtremeRecipeResourceManager {
    public static void reload(ResourceManager manager, List<Identifier> needToRemove) {
        ExtremeCraftingShapedRecipe.RECIPES.clear();
        for (Map.Entry<Identifier, Resource> entry : manager.findResources(AvaritiaReborn.MOD_ID + "/extreme_recipes", p -> p.getPath().endsWith(".json")).entrySet()) {
            try (InputStream stream = entry.getValue().getInputStream()) {
                JsonElement element = JsonParser.parseReader(new InputStreamReader(stream));
                if (!element.isJsonObject())
                    throw new JsonSyntaxException("Extreme recipe should be a json object: " + entry.getKey());
                JsonObject json = element.getAsJsonObject();
                ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
                Map<String, Ingredient> map = RecipeUtil.readSymbols(JsonHelper.getObject(json, "key"));
                String[] strings = RecipeUtil.getPattern(JsonHelper.getArray(json, "pattern"), 9, 9);
                Ingredient[][] inputs = RecipeUtil.replacePattern(strings, map);
                if (json.has("replace")) {
                    JsonArray replace = json.get("replace").getAsJsonArray();
                    replace.forEach(x -> needToRemove.add(new Identifier(x.getAsString())));
                }
                ExtremeCraftingShapedRecipe recipe = new ExtremeCraftingShapedRecipe(entry.getKey(), output, RecipeUtil.toTable(inputs));
                ExtremeCraftingShapedRecipe.RECIPES.put(entry.getKey(), recipe);
            } catch (Exception e) {
                AvaritiaReborn.LOGGER.error("Error occurred while loading resource json " + entry.getKey().toString(), e);
            }
        }
        AvaritiaReborn.LOGGER.info(ExtremeCraftingShapedRecipe.RECIPES.size() + " extreme recipes loaded.");
    }

}
