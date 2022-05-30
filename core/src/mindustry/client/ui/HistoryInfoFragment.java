package mindustry.client.ui;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.NinePatchDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import mindustry.Vars;
import mindustry.client.antigrief.TileRecords;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Cicon;
import mindustry.world.Tile;

import java.util.concurrent.atomic.AtomicInteger;

public class HistoryInfoFragment extends Table {

    public HistoryInfoFragment() {
        NinePatchDrawable background = new NinePatchDrawable(Tex.wavepane);

        setBackground(background);
        Image img = new Image();
        add(img);
        Label label = new Label("");
        add(label).height(126);
        visible(() -> Core.settings.getBool("tilehud"));
        var builder = new StringBuilder();
        update(() -> {

            var record = TileRecords.INSTANCE.getHistoryResult();
            if (record.size() < 1 ) return;
            builder.setLength(0);
            for (var item : record) {
                item = item.replace(Core.bundle.get("client.built"),"[#41e89a]"+Core.bundle.get("client.built")+"[]").replace(Core.bundle.get("client.broke"),"[#f25c5c]"+Core.bundle.get("client.broke")+"[]");
                builder.append(item).append("\n");
            }
            label.setText(builder.length() == 0 ? "" : builder.substring(0, builder.length() - 1));
        });
    }
}
