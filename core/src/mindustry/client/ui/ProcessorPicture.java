package mindustry.client.ui;

import arc.Core;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.input.KeyCode;
import arc.scene.Element;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import mindustry.gen.Tex;

import static mindustry.Vars.*;
import static mindustry.Vars.ui;

public class ProcessorPicture  extends Table {
    public static Texture PPRegion = null;
    public ProcessorPicture(){
        background(Tex.pane);
        float margin = 5f;
        this.touchable = Touchable.disabled;

        add(new Element(){
            {
                setSize(Scl.scl(140f));
            }

            @Override
            public void act(float delta){
                setPosition(Scl.scl(margin), Scl.scl(margin));

                super.act(delta);
            }

            @Override
            public void draw(){
                if(PPRegion == null) return;
                if(!clipBegin()) return;

                Draw.rect(new TextureRegion(PPRegion), x + width / 2f, y + height / 2f, width, height);

                clipEnd();
            }
        }).size(140f);

        margin(margin);

        update(() -> {

            Element e = Core.scene.hit(Core.input.mouseX(), Core.input.mouseY(), true);
            if(e != null && e.isDescendantOf(this)){
                requestScroll();
            }else if(hasScroll()){
                Core.scene.setScrollFocus(null);
            }
        });
    }
}
