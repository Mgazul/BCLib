package org.betterx.bclib.api.v2.dataexchange;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.api.v2.dataexchange.handler.DataExchange;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * This is an internal class that handles a Serverside Connection to a Client-Player
 */
public class ConnectorServerside extends Connector {
    private MinecraftServer server;

    ConnectorServerside(DataExchange api) {
        super(api);
        server = null;
    }

    @Override
    public boolean onClient() {
        return false;
    }

    public void onPlayInit(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        if (this.server != null && this.server != server) {
            BCLib.LOGGER.warn("Server changed!");
        }
        this.server = server;
        for (DataHandlerDescriptor<?> desc : getDescriptors()) {
            if (desc.DIRECTION == DataHandlerDescriptor.Direction.CLIENT_TO_SERVER)
                ServerPlayNetworking.registerReceiver(
                        handler,
                        desc.IDENTIFIER,
                        desc::receiveFromClient
                );
        }
    }

    public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        for (DataHandlerDescriptor<?> desc : getDescriptors()) {
            if (desc.sendOnJoin) {
                BaseDataHandler<?> h = desc.JOIN_INSTANCE.get();
                if (h.getOriginatesOnServer()) {
                    h.sendToClient(server, handler.player);
                }
            }
        }
    }

    public void onPlayDisconnect(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        for (DataHandlerDescriptor<?> desc : getDescriptors()) {
            ServerPlayNetworking.unregisterReceiver(handler, desc.IDENTIFIER.id());
        }
    }

    public void sendToClient(BaseDataHandler<?> h) {
        if (server == null) {
            throw new RuntimeException("[internal error] Server not initialized yet!");
        }
        h.sendToClient(this.server);
    }
}
