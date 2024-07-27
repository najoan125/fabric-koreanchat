package com.hyfata.najoan.koreanpatch.mixin.accessor;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestor.class)
public interface ChatInputSuggestorAccessor {
    @Accessor("maxSuggestionSize")
    int getMaxSuggestionSize();

    @Accessor("pendingSuggestions")
    CompletableFuture<Suggestions> getPendingSuggestions();

    @Accessor("window")
    CommandSuggestor.SuggestionWindow getWindow();

    @Accessor("messages")
    List<OrderedText> getMessages();

    @Accessor("chatScreenSized")
    boolean isChatScreenSized();

    @Invoker("sortSuggestions")
    List<Suggestion> getSortSuggestions(Suggestions suggestions);
}