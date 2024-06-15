package org.betterx.bclib.config;

public class Configs {
    // Client and Server-Config must be the first entries. They are not part of the Auto-Sync process
    // But will be needed by other Auto-Sync Config-Files
    //@Environment(EnvType.CLIENT)
    public static final ClientConfig CLIENT_CONFIG = new ClientConfig();
    //public static final ServerConfig SERVER_CONFIG = new ServerConfig();

    //public static final GeneratorConfig GENERATOR_CONFIG = new GeneratorConfig();
    public static final MainConfig MAIN_CONFIG = new MainConfig();
    //public static final BiomesConfig BIOMES_CONFIG = new BiomesConfig();

    public static final String MAIN_PATCH_CATEGORY = "patches";
    public static final String MAIN_INFO_CATEGORY = "infos";

    public static void save() {
        MAIN_CONFIG.saveChanges();
//        GENERATOR_CONFIG.saveChanges();
//        BIOMES_CONFIG.saveChanges();
    }
}
