package falsify.falsify.module.modules.render;

import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.*;

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
        Team team = scoreboard.getScoreHolderTeam(mc.player.getNameForScoreboard());
        if (team != null) {
            ScoreboardDisplaySlot scoreboardDisplaySlot = ScoreboardDisplaySlot.fromFormatting(team.getColor());
            if (scoreboardDisplaySlot != null) {
                scoreboardObjective = scoreboard.getObjectiveForSlot(scoreboardDisplaySlot);
            }
        }

        ScoreboardObjective scoreboardObjective2 = scoreboardObjective != null ? scoreboardObjective : scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (scoreboardObjective2 != null) {
            this.renderScoreboardSidebar(context, scoreboardObjective2);
        }
    }

    private void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective) {
        TextRenderer tr = mc.textRenderer;
        float padding = module.getPadding();
        boolean numbers = module.usesNumber();

        Scoreboard scoreboard = objective.getScoreboard();
        NumberFormat numberFormat = objective.getNumberFormatOr(StyledNumberFormat.RED);

        @Environment(EnvType.CLIENT)
        record SidebarEntry(Text name, Text score, int scoreWidth) {
        }

        SidebarEntry[] sidebarEntrys = scoreboard.getScoreboardEntries(objective)
                .stream()
                .filter(score -> !score.hidden())
                .sorted(Comparator.comparing(ScoreboardEntry::value)
                        .reversed()
                        .thenComparing(ScoreboardEntry::owner, String.CASE_INSENSITIVE_ORDER))
                .limit(15L)
                .map(scoreboardEntry -> {
                    Team team = scoreboard.getScoreHolderTeam(scoreboardEntry.owner());
                    Text textx = scoreboardEntry.name();
                    Text text2 = Team.decorateName(team, textx);
                    Text text3 = scoreboardEntry.formatted(numberFormat);
                    int ix = tr.getWidth(text3);
                    return new SidebarEntry(text2, text3, ix);
                })
                .toArray(SidebarEntry[]::new);
        Text text = objective.getDisplayName();
        int i = tr.getWidth(text);
        int maxWidth = i;
        int k = tr.getWidth(": ");

        for (SidebarEntry sidebarEntry : sidebarEntrys) {
            maxWidth = Math.max(maxWidth, tr.getWidth(sidebarEntry.name) + (sidebarEntry.scoreWidth > 0 && numbers ? k + sidebarEntry.scoreWidth : 2));
        }

        int maxWidthFinal = maxWidth;
        int entryLength = sidebarEntrys.length;
        int textHeight = (int) (entryLength * (9 + padding));
        int bottom = textHeight + 9 + 3;
        int leftBound = 0;
        int rightBound = maxWidthFinal + 2;
        int q = module.getBackgroundColor().getRGB();
        int r = module.getBackgroundColor().darker().getRGB();
        int top = 0;
        module.drawBackground(context, 0, 0, mc.getRenderTickCounter().getTickDelta(true));
        context.drawHorizontalLine(0, maxWidthFinal, 10, module.getBackgroundColor().darker().darker().getRGB());
        context.drawText(tr, text, leftBound + maxWidthFinal / 2 - i / 2, top+1, module.getTextColor().getRGB(), false);

        for (int t = 0; t < entryLength; t++) {
            SidebarEntry sidebarEntryx = sidebarEntrys[t];
            int u = (int) (bottom + (t - entryLength) * (9 + padding) + padding);
            context.drawText(tr, sidebarEntryx.name, leftBound + 2, u, module.getTextColor().getRGB(), false);
            if(numbers)
                context.drawText(tr, sidebarEntryx.score, rightBound - sidebarEntryx.scoreWidth, u, Colors.WHITE, false);
        }
        setWidth(rightBound);
        setHeight(bottom);

    }
}
