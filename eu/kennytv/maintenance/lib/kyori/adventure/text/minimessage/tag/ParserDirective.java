/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ParserDirective
extends Tag {
    public static final Tag RESET = new ParserDirective(){

        public String toString() {
            return "ParserDirective.RESET";
        }
    };
}

