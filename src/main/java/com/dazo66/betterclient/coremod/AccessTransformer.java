package com.dazo66.betterclient.coremod;

import java.io.IOException;

/**
 * @author Dazo66
 */
public class AccessTransformer extends net.minecraftforge.fml.common.asm.transformers.AccessTransformer {

    public AccessTransformer() throws IOException {
        super("betterclient_at.cfg");
    }

}
