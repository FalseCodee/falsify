package falsify.falsify.module.modules.render;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static falsify.falsify.module.Module.mc;

public class ScoreboardModule extends DisplayModule<ScoreboardRenderModule> {
    private final RangeSetting padding = new RangeSetting("Padding", 0, 0, 5, 0.01);
    private final BooleanSetting numbers = new BooleanSetting("Numbers", true);
    public ScoreboardModule() {
        super("Scoreboard", "Edit the scoreboard.", new ScoreboardRenderModule(0.0, 25.0, 100, 20), Category.RENDER, -1, true, false);
        settings.add(padding);
        settings.add(numbers);

        renderModule.setModule(this);
    }

    public boolean usesNumber() {
        return numbers.getValue();
    }

    public float getPadding() {
        return padding.getValue().floatValue();
    }
}

class ScoreboardRenderModule extends RenderModule<ScoreboardModule> {

    public ScoreboardRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        Scoreboard scoreboard = mc.world.getScoreboard();
        ScoreboardObjective scoreboardObjective = null;
        Team team = scoreboard.getPlayerTeam(mc.player.getEntityName());
        ScoreboardDisplaySlot m;
        if (team != null && (m = ScoreboardDisplaySlot.fromFormatting(team.getColor())) != null) {
            scoreboardObjective = scoreboard.getObjectiveForSlot(m);
        }
        ScoreboardObjective scoreboardObjective3 = scoreboardObjective != null ? scoreboardObjective : scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (scoreboardObjective3 != null) {
            this.renderScoreboardSidebar(context, scoreboardObjective3);
        }
    }

    private void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective) {
        TextRenderer tr = mc.textRenderer;
        float padding = module.getPadding();
        boolean numbers = module.usesNumber();

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<ScoreboardPlayerScore> collection = scoreboard.getAllPlayerScores(objective);
        List list = collection.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());
        collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;

        ArrayList<Pair<ScoreboardPlayerScore, MutableText>> list2 = Lists.newArrayListWithCapacity(collection.size());
        Text text = objective.getDisplayName();
        int maxWidth = tr.getWidth(text);
        int k = tr.getWidth(": ");

        for (ScoreboardPlayerScore scoreboardPlayerScore : collection) {
            Team team = scoreboard.getPlayerTeam(scoreboardPlayerScore.getPlayerName());
            MutableText text2 = Team.decorateName(team, Text.literal(scoreboardPlayerScore.getPlayerName()));
            list2.add(Pair.of(scoreboardPlayerScore, text2));
            maxWidth = Math.max(maxWidth, tr.getWidth(text2) + k + tr.getWidth(Integer.toString(scoreboardPlayerScore.getScore())));
        }

        maxWidth += (numbers) ? 4 : -2;
        setWidth(maxWidth);
        float height = (collection.size()+1) * (tr.fontHeight+padding) + 1;
        setHeight(height);

        module.drawBackground(context, 0, 0, 0);

        for (int i = 0; i < list2.size(); i++) {
            Pair pair = list2.get(i);
            float currentHeight = height-(tr.fontHeight+padding) * (i+1);
            ScoreboardPlayerScore scoreboardPlayerScore2 = (ScoreboardPlayerScore)pair.getFirst();
            Text text3 = (Text)pair.getSecond();
            String string = "" + Formatting.RED + scoreboardPlayerScore2.getScore();

            context.drawText(tr, text3, 2, (int) currentHeight, module.getTextColor().getRGB(), false);
            if(numbers) context.drawText(tr, string, (int) (width - tr.getWidth(string)), (int) currentHeight, module.getTextColor().getRGB(), false);

            if (i != collection.size()-1) continue;
            module.drawBackgroundInternal(context, module.getBackgroundColor().brighter().brighter(), 0, 0, 0, 0, 0, maxWidth, currentHeight-1);
            context.drawText(tr, text, maxWidth / 2 - tr.getWidth(text) / 2, (int) currentHeight-(tr.fontHeight), module.getTextColor().getRGB(), false);
        }
    }
}
