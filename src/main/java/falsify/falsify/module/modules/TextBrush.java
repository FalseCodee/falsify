package falsify.falsify.module.modules;

import falsify.falsify.gui.TextBrushGUI;
import falsify.falsify.gui.TpAnnoyGUI;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.PositionImpl;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class TextBrush extends Module {

    public static String theString = "text";
    public static boolean run = false;
    ArrayList<FalseRunnable> runnables = new ArrayList<>();
    public TextBrush() {
        super("Text Brush", Category.MOVEMENT, GLFW.GLFW_KEY_EQUAL);
    }

    @Override
    public void onEvent(Event<?> e) {
        if(!run) return;
        if(e instanceof EventUpdate) {
            int x = (int) mc.player.getX();
            int y = (int) mc.player.getY()-1;
            int z = (int) mc.player.getZ();
            int k = 0;
            for(char character : theString.toCharArray()) {
                try {
                    Characters activities = Characters.valueOf(String.valueOf(character).toUpperCase());
                    for( int i = 0; i < activities.getActive().length; i++) {
                        for(int j = 0; j < activities.getActive()[i].length; j++) {
                            boolean val = activities.getActive()[i][j];
                            if(val) {
                                int finalJ = j;
                                int finalX = x;
                                int finalI = i;
                                runnables.add(new FalseRunnable() {
                                    @Override
                                    public void run() {
                                        mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI)[0], 1f));
                                        mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI)[1], 1f));
                                        while (Math.sqrt(mc.player.squaredDistanceTo(finalX + finalJ, y, z + finalI)) > 3) {
                                            mc.options.keyAttack.setPressed(false);
                                            mc.options.keyForward.setPressed(true);
                                            mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI)[0], 1f));
                                            mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI)[1], 1f));
                                        }
                                        mc.options.keyUse.setPressed(true);
                                        mc.options.keyForward.setPressed(false);
                                        new FalseRunnable() {
                                            @Override
                                            public void run() {
                                                mc.options.keyUse.setPressed(false);
                                                TextBrush.this.runNext();
                                            }
                                        }.runTaskLater(300);

                                    }
                                });
                            }
                        }
                    }
                    x += activities.getActive()[0].length + 1;
                } catch (IllegalArgumentException exception) {
                    x += 5;
                }
            }
            runNext();
            this.toggle();
        }
    }

    @Override
    public void onEnable() {
        run = false;
        mc.setScreen(new TextBrushGUI(mc.currentScreen));
    }

    private void runNext() {
        if(runnables.size() > 1) {
            runnables.remove(0);
            runnables.get(0).run();
        }
    }

    enum Characters {
        A(new boolean[][] {
                {false,true,true,true,false},
                {false,false,false,false,true},
                {false,true,true,true,true},
                {true,false,false,false,true},
                {false,true,true,true,true}
        }),
        B(new boolean[][] {
                {true,false,false,false,false},
                {true,false,false,false,false},
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,true},
                {true,true,true,true,false}
        }),
        C(new boolean[][] {
                {false,true,true,true,false},
                {true,false,false,false,true},
                {true,false,false,false,false},
                {true,false,false,false,true},
                {false,true,true,true,false}
        }),

        D(new boolean[][] {
                {false,false,false,false,true},
                {false,false,false,false,true},
                {false,true,true,false,true},
                {true,false,false,true,true},
                {true,false,false,false,true},
                {false,true,true,true,true}
        }),
        E(new boolean[][] {
                {false,true,true,true,false},
                {true,false,false,false,true},
                {true,true,true,true,true},
                {true,false,false,false,false},
                {false,true,true,true,true}
        }),
        F(new boolean[][] {
                {false,false,true,true},
                {false,true,false,false},
                {true,true,true,true},
                {false,true,false,false},
                {false,true,false,false}
        }),
        G(new boolean[][] {
                {false,true,true,true,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,true},
                {false,false,false,false,true},
                {true,true,true,true,false}
        }),
        H(new boolean[][] {
                {true,false,false,false,false},
                {true,false,false,false,false},
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true}
        }),
        I(new boolean[][] {
                {true},
                {false},
                {true},
                {true},
                {true},
                {true}
        }),
        J(new boolean[][] {
                {false,false,false,true},
                {false,false,false,false},
                {false,false,false,true},
                {false,false,false,true},
                {true,false,false,true},
                {true,false,false,true},
                {false,true,true,false}
        }),
        K(new boolean[][] {
                {true,false,false,false},
                {true,false,false,true},
                {true,false,true,false},
                {true,true,false,false},
                {true,false,true,false},
                {true,false,false,true}
        }),
        L(new boolean[][] {
                {true,false},
                {true,false},
                {true,false},
                {true,false},
                {true,false},
                {false,true}
        }),
        M(new boolean[][] {
                {true,true,false,true,false},
                {true,false,true,false,true},
                {true,false,true,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true}
        }),
        N(new boolean[][] {
                {true,true,true,true,false},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true}
        }),
        O(new boolean[][] {
                {false,true,true,true,false},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,false}
        }),
        P(new boolean[][] {
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,true},
                {true,true,true,true,true},
                {true,false,false,false,false},
                {true,false,false,false,false}
        }),
        Q(new boolean[][] {
                {false,true,true,false,true},
                {true,false,false,true,true},
                {true,false,false,false,true},
                {false,true,true,true,true},
                {false,false,false,false,true},
                {false,false,false,false,true}
        }),
        R(new boolean[][] {
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,false},
                {true,false,false,false,false},
                {true,false,false,false,false}
        }),
        S(new boolean[][] {
                {false,true,true,true,true},
                {true,false,false,false,false},
                {false,true,true,true,false},
                {false,false,false,false,true},
                {true,true,true,true,false}
        }),
        T(new boolean[][] {
                {false,true,false},
                {false,true,false},
                {true,true,true},
                {false,true,false},
                {false,true,false},
                {false,false,true}
        }),
        U(new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,true}
        }),
        V(new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,false,true,false},
                {false,false,true,false,false}
        }),
        W(new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,true,false,true},
                {true,false,true,false,true},
                {false,true,true,true,true}
        }),
        X(new boolean[][] {
                {true,false,false,false,true},
                {false,true,false,true,false},
                {false,false,true,false,false},
                {false,true,false,true,false},
                {true,false,false,false,true}
        }),
        Y(new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,true,true,true,true},
                {false,false,false,false,true},
                {true,true,true,true,false}
        }),
        Z(new boolean[][] {
                {true,true,true,true,true},
                {false,false,false,true,false},
                {false,false,true,false,false},
                {false,true,false,false,false},
                {true,true,true,true,true}
        });




        boolean[][] active;
        Characters(boolean[][] active) {
            this.active = active;
        }

        public boolean[][] getActive() {
            return active;
        }
    }
}
