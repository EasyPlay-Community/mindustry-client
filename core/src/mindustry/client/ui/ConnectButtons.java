package mindustry.client.ui;

import arc.Core;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Call;
import mindustry.gen.Icon;
import mindustry.gen.Iconc;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;

public class ConnectButtons {
    public static Element get() {
        Table buttons = new Table(Tex.wavepane).marginTop(6);

        buttons.button(Icon.modeSurvival,()->{Vars.netClient.disconnectQuietly(); Vars.ui.join.connect("s.easyplay.su",6567);}).height(64).width(60f);
        buttons.button(Icon.modePvp,()-> {Vars.netClient.disconnectQuietly(); Vars.ui.join.connect("s.easyplay.su",6577);}).height(64).width(60f);
        buttons.button(Icon.modeAttack,()-> {Vars.netClient.disconnectQuietly(); Vars.ui.join.connect("s.easyplay.su",6587);}).height(64).width(60f);
        buttons.button(Iconc.itemSand+"#1",()-> {Vars.netClient.disconnectQuietly(); Vars.ui.join.connect("s.easyplay.su",6686);}).height(64).width(60f);
        buttons.button(Iconc.itemSand+"#2",()-> {Vars.netClient.disconnectQuietly(); Vars.ui.join.connect("s.easyplay.su",6687);}).height(64).width(60f);
        buttons.button(Iconc.commandRally+"",()-> {Vars.netClient.disconnectQuietly(); Vars.ui.join.connect("s.easyplay.su",6676);}).height(64).width(60f);

        return buttons;
    }
}
