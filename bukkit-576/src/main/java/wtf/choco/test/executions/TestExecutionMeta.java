package wtf.choco.test.executions;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.ItemType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.StringUtil;

import wtf.choco.test.TestPlugin;
import wtf.choco.test.util.TextUtil;

public class TestExecutionMeta implements CommandSourcedTestExecution {

    private static final Class<? extends ItemMeta> BASE_META_CLASS = Bukkit.getItemFactory().getItemMeta(ItemType.APPLE).getClass();
    private static final Map<DyeColor, ChatColor> DYE_COLOR_TO_CHAT_COLOR = new EnumMap<>(DyeColor.class);

    static {
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.BLACK, ChatColor.BLACK);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.BLUE, ChatColor.BLUE);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.BROWN, ChatColor.DARK_RED);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.CYAN, ChatColor.DARK_AQUA);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.GRAY, ChatColor.DARK_GRAY);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.GREEN, ChatColor.DARK_GREEN);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.LIGHT_BLUE, ChatColor.AQUA);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.LIGHT_GRAY, ChatColor.GRAY);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.LIME, ChatColor.GREEN);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.ORANGE, ChatColor.GOLD);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.PINK, ChatColor.LIGHT_PURPLE);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.PURPLE, ChatColor.DARK_PURPLE);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.RED, ChatColor.RED);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.WHITE, ChatColor.WHITE);
        DYE_COLOR_TO_CHAT_COLOR.put(DyeColor.YELLOW, ChatColor.YELLOW);
    }

    private static final Map<String, ItemMetaTestBundle> ITEM_METAS = new HashMap<>();

    static {
        registerMeta(ItemMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.DIAMOND_SWORD);
                ItemMeta meta = item.getItemMeta();

                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", 2.0, Operation.MULTIPLY_SCALAR_1));
                meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(new NamespacedKey(JavaPlugin.getPlugin(TestPlugin.class), "Test"), PersistentDataType.STRING, "Test");
                meta.setCustomModelData(10);
                meta.setDisplayName(ChatColor.GREEN + "Testing");
                meta.setLore(Arrays.asList(
                    ChatColor.DARK_GREEN + "This is an ItemMeta test",
                    ChatColor.DARK_GREEN + "Is it working?"
                ));
                meta.setUnbreakable(true);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                Multimap<Attribute, AttributeModifier> attributeModifiers = meta.getAttributeModifiers();
                if (attributeModifiers != null && !attributeModifiers.isEmpty()) {
                    player.sendMessage("Attributes:");
                    attributeModifiers.forEach((attribute, modifier) -> player.sendMessage(" - " + attribute.getKey() + ": " + modifier.getAmount() + " (op: " + modifier.getOperation() + ")"));
                }

                Map<Enchantment, Integer> enchantments = meta.getEnchants();
                if (!enchantments.isEmpty()) {
                    player.sendMessage("Enchantments:");
                    enchantments.forEach((enchantment, level) -> player.sendMessage(" - " + enchantment.getKey() + " level " + level));
                }

                Set<ItemFlag> itemFlags = meta.getItemFlags();
                if (!itemFlags.isEmpty()) {
                    player.sendMessage("Item flags: " + itemFlags);
                }

                if (meta.hasCustomModelData()) {
                    player.sendMessage("Custom model data: " + meta.getCustomModelData());
                }

                if (meta.hasDisplayName()) {
                    player.sendMessage("Custom name: " + meta.getDisplayName());
                }

                List<String> lore = meta.getLore();
                if (lore != null && !lore.isEmpty()) {
                    player.sendMessage("Lore:");
                    lore.forEach(line -> player.sendMessage(" - " + line));
                }

                if (meta.isUnbreakable()) {
                    player.sendMessage("Unbreakable: " + TextUtil.colouredBoolean(meta.isUnbreakable()));
                }
            }
        );

        registerMeta(BannerMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.LIME_BANNER);

                BannerMeta meta = (BannerMeta) item.getItemMeta();
                meta.addPattern(new Pattern(DyeColor.BLUE, PatternType.GLOBE));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CREEPER));
                item.setItemMeta(meta);

                return Arrays.asList(item);
            },
            (player, meta) -> {
                List<Pattern> patterns = ((BannerMeta) meta).getPatterns();
                if (!patterns.isEmpty()) {
                    player.sendMessage("Patterns:");
                    patterns.forEach(pattern -> player.sendMessage(" - " + DYE_COLOR_TO_CHAT_COLOR.get(pattern.getColor()) + pattern.getPattern()));
                }
            }
        );

        registerMeta(BlockDataMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.CHEST);
                BlockDataMeta meta = (BlockDataMeta) item.getItemMeta();

                meta.setBlockData(BlockType.CHEST.createBlockData(blockData -> {
                    org.bukkit.block.data.type.Chest chest = (org.bukkit.block.data.type.Chest) blockData;
                    chest.setWaterlogged(true);
                    chest.setFacing(BlockFace.EAST);
                    chest.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
                }));

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                player.sendMessage("Block data: " + ((BlockDataMeta) meta).getBlockData(ItemType.CHEST).getAsString());
            }
        );

        registerMeta(BlockStateMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.SHIELD);

                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                Banner bannerState = (Banner) meta.getBlockState();
                bannerState.addPattern(new Pattern(DyeColor.GREEN, PatternType.FLOWER));
                bannerState.addPattern(new Pattern(DyeColor.RED, PatternType.BRICKS));
                meta.setBlockState(bannerState);
                item.setItemMeta(meta);

                return Arrays.asList(item);
            },
            (player, meta) -> {
                player.sendMessage("Block state: " + ((BlockStateMeta) meta).getBlockState());
            }
        );

        registerMeta(BookMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.WRITTEN_BOOK);

                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.setAuthor("2008Choco");
                meta.setGeneration(Generation.TATTERED);
                meta.setTitle("The Book of Item Meta");
                meta.setPages(
                    "Once upon a time, there was an API called Bukkit. Bukkit was very old and had a beautiful Material enum.",
                    "Throughout the years, Mojang started to move to a more data-driven registry-based system that rendered the Material enum obsolete and unmaintainable.",
                    "Choco decided to nuke the Material enum and rewrite the item handling system to pull directly from Mojang's registries.",
                    "They all lived happily ever after. The End!"
                );

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                BookMeta bookMeta = (BookMeta) meta;

                if (bookMeta.hasAuthor()) {
                    player.sendMessage("Author: " + bookMeta.getAuthor());
                }

                if (bookMeta.hasGeneration()) {
                    player.sendMessage("Generation: " + bookMeta.getGeneration());
                }

                if (bookMeta.hasTitle()) {
                    player.sendMessage("Title: " + bookMeta.getTitle());
                }

                if (bookMeta.getPageCount() >= 1) {
                    player.sendMessage("Pages:");
                    bookMeta.getPages().forEach(page -> player.sendMessage(" - " + page));
                }
            }
        );

        registerMeta(CompassMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.COMPASS);

                CompassMeta meta = (CompassMeta) item.getItemMeta();
                meta.setLodestone(player.getLocation());
                meta.setLodestoneTracked(true);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                CompassMeta compassMeta = (CompassMeta) meta;

                player.sendMessage("Tracking lodestone: " + TextUtil.colouredBoolean(compassMeta.isLodestoneTracked()));

                Location lodestoneLocation = compassMeta.getLodestone();
                if (lodestoneLocation != null) {
                    player.sendMessage("Lodestone location: (" + lodestoneLocation.getBlockX() + ", " + lodestoneLocation.getBlockY() + ", " + lodestoneLocation.getBlockZ() + ") in world " + lodestoneLocation.getWorld().getName());
                }
            }
        );

        registerMeta(CrossbowMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.CROSSBOW);

                CrossbowMeta meta = (CrossbowMeta) item.getItemMeta();
                meta.addChargedProjectile(new ItemStack(ItemType.ARROW));
                meta.addChargedProjectile(new ItemStack(ItemType.FIREWORK_ROCKET));
                meta.addChargedProjectile(new ItemStack(ItemType.ARROW));

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                List<ItemStack> chargedProjectiles = ((CrossbowMeta) meta).getChargedProjectiles();
                if (!chargedProjectiles.isEmpty()) {
                    player.sendMessage("Charged projectiles:");
                    chargedProjectiles.forEach(item -> {
                        player.sendMessage(" - " + item.getType().getKey());
                    });
                }
            }
        );

        registerMeta(EnchantmentStorageMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.ENCHANTED_BOOK);

                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                meta.addStoredEnchant(Enchantment.DAMAGE_ALL, 4, false);
                meta.addStoredEnchant(Enchantment.BINDING_CURSE, 1, false);
                meta.addStoredEnchant(Enchantment.MENDING, 5, true);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                Map<Enchantment, Integer> storedEnchantments = ((EnchantmentStorageMeta) meta).getStoredEnchants();
                if (!storedEnchantments.isEmpty()) {
                    player.sendMessage("Stored enchantments:");
                    storedEnchantments.forEach((enchantment, level) -> {
                        player.sendMessage(" - " + enchantment.getKey() + " level " + level);
                    });
                }
            }
        );

        registerMeta(FireworkEffectMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.FIREWORK_STAR);

                FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
                meta.setEffect(FireworkEffect.builder()
                    .withFlicker()
                    .withTrail()
                    .with(FireworkEffect.Type.STAR)
                    .withColor(Color.fromRGB(0x03C6FC))
                    .withFade(Color.fromRGB(0x388EA6))
                    .build()
                );

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                FireworkEffect effect = ((FireworkEffectMeta) meta).getEffect();
                if (effect != null) {
                    player.sendMessage("Effect: " + effect);
                }
            }
        );

        registerMeta(FireworkMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.FIREWORK_ROCKET);

                FireworkMeta meta = (FireworkMeta) item.getItemMeta();
                meta.addEffect(FireworkEffect.builder()
                    .withFlicker()
                    .withTrail()
                    .with(FireworkEffect.Type.STAR)
                    .withColor(Color.fromRGB(0x03C6FC))
                    .withFade(Color.fromRGB(0x388EA6))
                    .build()
                );
                meta.addEffect(FireworkEffect.builder()
                    .with(FireworkEffect.Type.CREEPER)
                    .withColor(Color.fromRGB(0x2ABF24))
                    .withFade(Color.fromRGB(0x1D261C))
                    .build()
                );

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                List<FireworkEffect> effects = ((FireworkMeta) meta).getEffects();
                if (!effects.isEmpty()) {
                    player.sendMessage("Effects: ");
                    effects.forEach(effect -> player.sendMessage(" - " + effect));
                }
            }
        );

        registerMeta(KnowledgeBookMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.KNOWLEDGE_BOOK);

                KnowledgeBookMeta meta = (KnowledgeBookMeta) item.getItemMeta();
                meta.addRecipe(
                    NamespacedKey.minecraft("chest"),
                    NamespacedKey.minecraft("book")
                );

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                List<NamespacedKey> recipes = ((KnowledgeBookMeta) meta).getRecipes();
                if (!recipes.isEmpty()) {
                    player.sendMessage("Recipes:");
                    recipes.forEach(recipe -> player.sendMessage(" - " + recipe));
                }
            }
        );

        registerMeta(LeatherArmorMeta.class,
            player -> {
                LeatherArmorMeta meta = (LeatherArmorMeta) Bukkit.getItemFactory().getItemMeta(ItemType.LEATHER_CHESTPLATE);
                meta.setColor(Color.fromRGB(0xC9C91C));

                ItemStack helmet = new ItemStack(ItemType.LEATHER_HELMET);
                ItemStack chestplate = new ItemStack(ItemType.LEATHER_CHESTPLATE);
                ItemStack leggings = new ItemStack(ItemType.LEATHER_LEGGINGS);
                ItemStack boots = new ItemStack(ItemType.LEATHER_BOOTS);

                helmet.setItemMeta(meta);
                chestplate.setItemMeta(meta);
                leggings.setItemMeta(meta);
                boots.setItemMeta(meta);

                return Arrays.asList(helmet, chestplate, leggings, boots);
            },
            (player, meta) -> {
                player.sendMessage("Color: " + ((LeatherArmorMeta) meta).getColor());
            }
        );

        registerMeta(MapMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.FILLED_MAP);

                MapMeta meta = (MapMeta) item.getItemMeta();
                meta.setColor(Color.fromRGB(0xA919D1));
                meta.setScaling(true);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                MapMeta mapMeta = (MapMeta) meta;

                if (mapMeta.hasColor()) {
                    player.sendMessage("Color: " + mapMeta.getColor());
                }

                player.sendMessage("Scaling: " + TextUtil.colouredBoolean(mapMeta.isScaling()));
            }
        );

        registerMeta(PotionMeta.class,
            player -> {
                ItemStack potionItem = new ItemStack(ItemType.POTION);

                PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
                potionMeta.setBasePotionData(new PotionData(PotionType.POISON, true, false));
                potionMeta.setColor(Color.fromRGB(0xD11938));
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 500, 2, true, true, true), true);
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false, false), false);

                ItemStack splashPotionItem = new ItemStack(ItemType.SPLASH_POTION);

                PotionMeta splashPotionMeta = (PotionMeta) splashPotionItem.getItemMeta();
                splashPotionMeta.setBasePotionData(new PotionData(PotionType.POISON, false, true));
                splashPotionMeta.setColor(Color.fromRGB(0xD11938));
                splashPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 500, 2, true, true, true), true);
                splashPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false, false), false);

                potionItem.setItemMeta(potionMeta);
                splashPotionItem.setItemMeta(splashPotionMeta);
                return Arrays.asList(potionItem, splashPotionItem);
            },
            (player, meta) -> {
                PotionMeta potionMeta = (PotionMeta) meta;

                player.sendMessage("Base potion: " + potionMeta.getBasePotionData());

                if (potionMeta.hasColor()) {
                    player.sendMessage("Color: " + potionMeta.getColor());
                }

                List<PotionEffect> customEffects = potionMeta.getCustomEffects();
                if (!customEffects.isEmpty()) {
                    player.sendMessage("Custom effects:");
                    customEffects.forEach(effect -> player.sendMessage(" - " + effect.getType().getName() + " " + effect.getAmplifier() + " (" + effect.getDuration() + " ticks)"));
                }
            }
        );

        registerMeta(SkullMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.PLAYER_HEAD);

                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwningPlayer(player);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                player.sendMessage("Owner: " + ((SkullMeta) meta).getOwningPlayer().getName());
            }
        );

        registerMeta(SuspiciousStewMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.SUSPICIOUS_STEW);

                SuspiciousStewMeta meta = (SuspiciousStewMeta) item.getItemMeta();
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 500, 2, true, true, true), true);
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false, false), false);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                List<PotionEffect> customEffects = ((SuspiciousStewMeta) meta).getCustomEffects();
                if (!customEffects.isEmpty()) {
                    player.sendMessage("Custom effects:");
                    customEffects.forEach(effect -> player.sendMessage(" - " + effect.getType().getName() + " " + effect.getAmplifier() + " (" + effect.getDuration() + " ticks)"));
                }
            }
        );

        registerMeta(TropicalFishBucketMeta.class,
            player -> {
                ItemStack item = new ItemStack(ItemType.TROPICAL_FISH_BUCKET);

                TropicalFishBucketMeta meta = (TropicalFishBucketMeta) item.getItemMeta();
                meta.setBodyColor(DyeColor.PURPLE);
                meta.setPattern(TropicalFish.Pattern.BLOCKFISH);
                meta.setPatternColor(DyeColor.PINK);

                item.setItemMeta(meta);
                return Arrays.asList(item);
            },
            (player, meta) -> {
                TropicalFishBucketMeta bucketMeta = (TropicalFishBucketMeta) meta;

                player.sendMessage("Body colour: " + DYE_COLOR_TO_CHAT_COLOR.get(bucketMeta.getBodyColor()) + bucketMeta.getBodyColor());
                player.sendMessage("Pattern colour: " + DYE_COLOR_TO_CHAT_COLOR.get(bucketMeta.getPatternColor()) + bucketMeta.getPatternColor());
                player.sendMessage("Pattern: " + bucketMeta.getPattern());
            }
        );
    }

    @Override
    public void runTest(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Missing meta argument");
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this test.");
            return;
        }

        ItemMetaTestBundle testBundle = getTestBundle(args[1], String::equalsIgnoreCase);
        if (testBundle == null) {
            sender.sendMessage("Unrecognized meta type");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage("Missing action. Must either create or describe");
            return;
        }

        Player player = (Player) sender;

        if (args[2].equalsIgnoreCase("create")) {
            PlayerInventory inventory = player.getInventory();
            testBundle.creator.apply(player).forEach(inventory::addItem);
        }
        else if (args[2].equalsIgnoreCase("describe")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().isAir()) {
                player.sendMessage("You must be holding an item in your hand");
                return;
            }

            player.sendMessage("Describing item: " + item.getType().getKey());
            player.sendMessage(StringUtils.repeat("-", 44));

            ItemMeta meta = item.getItemMeta();

            if (meta.getClass() != BASE_META_CLASS) {
                ItemMetaTestBundle baseMetaBundle = ITEM_METAS.get("ItemMeta");

                if (baseMetaBundle != null) {
                    baseMetaBundle.describer.accept(player, meta);
                    player.sendMessage("--------------- SPECIFIC META --------------");
                }
            }

            testBundle.describer.accept(player, meta);
        }
        else {
            sender.sendMessage("Unknown action. Must either create or describe");
            return;
        }
    }

    @Override
    public List<String> queryTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();

            ITEM_METAS.keySet().forEach(meta -> {
                if (meta.toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(meta);
                }
            });

            return suggestions;
        }

        else if (args.length == 3) {
            if (args[1].isEmpty()) {
                return Collections.emptyList();
            }

            ItemMetaTestBundle testBundle = getTestBundle(args[1], String::startsWith);
            if (testBundle == null) {
                return Collections.emptyList();
            }

            return StringUtil.copyPartialMatches(args[2], Arrays.asList("create", "describe"), new ArrayList<>());
        }

        return Collections.emptyList();
    }

    private static void registerMeta(Class<? extends ItemMeta> metaClass, Function<Player, List<ItemStack>> creator, BiConsumer<Player, ItemMeta> describer) {
        ITEM_METAS.put(metaClass.getSimpleName(), new ItemMetaTestBundle(creator, describer));
    }

    private static ItemMetaTestBundle getTestBundle(String string, BiPredicate<String, String> comparator) {
        for (Map.Entry<String, ItemMetaTestBundle> metaEntry : ITEM_METAS.entrySet()) {
            if (comparator.test(metaEntry.getKey().toLowerCase(), string.toLowerCase())) {
                return metaEntry.getValue();
            }
        }

        return null;
    }


    private static class ItemMetaTestBundle {

        private final Function<Player, List<ItemStack>> creator;
        private final BiConsumer<Player, ItemMeta> describer;

        public ItemMetaTestBundle(Function<Player, List<ItemStack>> creator, BiConsumer<Player, ItemMeta> describer) {
            this.creator = creator;
            this.describer = describer;
        }

    }

}
