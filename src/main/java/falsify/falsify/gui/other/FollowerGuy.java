package falsify.falsify.gui.other;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

public class FollowerGuy extends Clickable {
    private Vec3d inertia = new Vec3d(0.0, 0.0, 0.0);
    public Color color = new Color(255, 255, 255);
    public double mass;

    private final String message;

    private static final String[] positives = {
            "Legacy rocks!",
            "Legacy is the best!",
            "Legacy on top!",
            "Wow this client is great!",
            "Legacy > Others",
            "Wow this client is great!",
            "#Legacy2024",
            "Give FalseCode a raise!",
            "Incredible client!",
            "Wowzers this is good!",
            "Incredible experience!",

    };
    public FollowerGuy(double x, double y) {
        super(x, y, 5, 5);
        setInertia(new Vec3d(Math.random(), Math.random(), 0));
        this.mass = Math.random() * 800;
        this.width = Math.sqrt(this.mass/Math.PI);
        this.height = Math.sqrt(this.mass/Math.PI);
        this.message = positives[(int) (Math.random() * positives.length)];
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        this.x = 100;
        this.y = 100;
        return false;
    }
    public void update(int mouseX, int mouseY, double delta, ArrayList<FollowerGuy> guys, boolean canRemove) {
        if(x < 0) {
            x = 0;
        }
        if(x+width > Falsify.mc.currentScreen.width) {
            x = Falsify.mc.currentScreen.width-width;
        }
        if(y < 0) {
            y = 0;
        }
        if(y+height > Falsify.mc.currentScreen.height) {
            y = Falsify.mc.currentScreen.height-height;
        }
        //if((mouseX-x)*(mouseX-x) + (mouseY-y)*(mouseY-y) > 5000) {
        for (int i = guys.size()-1; i >=0 ;i--) {
            if (guys.get(i) == this) continue;
            FollowerGuy guy = guys.get(i);
            Vec3d centerGuy = new Vec3d(x+width/2, y+height/2, 0);
            Vec3d centerOtherGuy = new Vec3d(guy.x+guy.width/2, guy.y+guy.height/2, 0);
            Vec3d dir = centerGuy.subtract(centerOtherGuy);
            double clippingDistance = width/2 + guy.width/2;
            double distance = dir.length();
            if(distance < clippingDistance) {
//                if(canRemove) {
                    if(mass < guy.mass){
                        x = guy.x;
                        y = guy.y;
                    }
                    addMass(guy.mass);

                    guys.remove(guy);
//                } else {
//                    Vec3d fixed = centerGuy.subtract(centerOtherGuy);
//                    x += fixed.x/2;
//                    y += fixed.y/2;
//                    guy.x -= fixed.x/2;
//                    guy.y -= fixed.y/2;
//                }
            } else reverseGravity(centerOtherGuy, guy.mass, delta);
        }
        reverseGravity(new Vec3d(0, y+height/2, 0), 1000, delta);
        reverseGravity(new Vec3d(Falsify.mc.currentScreen.width, y+height/2, 0), 1000, delta);
        reverseGravity(new Vec3d(x+width/2, 0, 0), 1000, delta);
        reverseGravity(new Vec3d(x+width/2, Falsify.mc.currentScreen.height, 0), 1000, delta);
//        reverseGravity(new Vec3d(0, 0, 0), 20000, delta);
//        reverseGravity(new Vec3d(Falsify.mc.currentScreen.width, 0, 0), 20000, delta);
//        reverseGravity(new Vec3d(0, Falsify.mc.currentScreen.height, 0), 20000, delta);
//        reverseGravity(new Vec3d(Falsify.mc.currentScreen.width, Falsify.mc.currentScreen.height, 0), 20000, delta);
        //reverseGravity(new Vec3d(x, Falsify.mc.currentScreen.height - 85, 0), 5000, delta);
        //}

        reverseGravity(new Vec3d(mouseX, mouseY, 0),10000, delta);
        //doGravity(new Vec3d(x+width/2, Falsify.mc.currentScreen.height+10, 0), 1000000, delta);

        x += inertia.getX();
        y += inertia.getY();
        inertia = inertia.multiply(0.900);
    }

    public void doGravity(Vec3d pos, double mass, double delta) {
        Vec3d dir = pos.subtract(new Vec3d(x+width/2, y+height/2, 0));
        double distance = dir.length();
        double forceMag = (0.06674*mass*this.mass)/((distance*distance)+mass/16)*delta;
        addInertia(dir.normalize().multiply(forceMag));
    }

    public void reverseGravity(Vec3d pos, double mass, double delta) {
        Vec3d dir = new Vec3d(x+width/2, y+height/2, 0).subtract(pos);
        double distance = dir.length();
        double forceMag = (0.06674*mass*this.mass)/((distance*distance)+1)*delta;
        addInertia(dir.normalize().multiply(forceMag));
    }
    public void setInertia(Vec3d inertia) {
        this.inertia = inertia;
    }

    public void addInertia(Vec3d inertia) {
        this.inertia = this.inertia.add(inertia.multiply(1/this.mass));
    }

    public void addMass(double mass) {
        this.mass += mass;
        this.width = Math.sqrt(this.mass/Math.PI);
        this.height = Math.sqrt(this.mass/Math.PI);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        drawSmoothRect(color, context.getMatrices(), (float) x, (float) y, (float) (x+width), (float) (y+height), (float) width/2.0f, new int[] {5, 5, 5, 5});
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale((float) (mass / 400), (float) (mass / 400), 1);
        Falsify.fontRenderer.drawCenteredString(context, message, 0, 0, RenderHelper.colorLerp(new Color(color.getRed(),color.getGreen(),color.getBlue(),0), new Color(color.getRed(),color.getGreen(),color.getBlue(),255), MathUtils.clamp((float) (inertia.length()), 0.0F, 1.0F)), true);
        context.getMatrices().pop();

    }
}
