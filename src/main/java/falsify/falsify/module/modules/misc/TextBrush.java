package falsify.falsify.module.modules.misc;

import falsify.falsify.gui.TextBrushGUI;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.MathUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class TextBrush extends Module {

    public static String theString = "text";
    public static long theDelay = 200;
    public static boolean run = false;
    final ArrayList<FalseRunnable> runnables = new ArrayList<>();
    public TextBrush() {
        super("Text Brush", Category.MOVEMENT, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!run) return;
        if(event instanceof EventUpdate) {
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
                                        mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI - activities.getOffset())[0], 1f));
                                        mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI - activities.getOffset())[1], 1f));
                                        while (Math.sqrt(mc.player.squaredDistanceTo(finalX + finalJ, y, z + finalI - activities.getOffset())) > 2) {
                                            mc.options.attackKey.setPressed(false);
                                            mc.options.forwardKey.setPressed(true);
                                            mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI - activities.getOffset())[0], 1f));
                                            mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI - activities.getOffset())[1], 1f));
                                        }
                                        mc.options.attackKey.setPressed(true);
                                        mc.options.forwardKey.setPressed(false);
                                        new FalseRunnable() {
                                            @Override
                                            public void run() {
                                                mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI - activities.getOffset())[0], 1f));
                                                mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(finalX + finalJ, y, z + finalI - activities.getOffset())[1], 1f));
                                            }
                                        }.runTaskLater(20);
                                        new FalseRunnable() {
                                            @Override
                                            public void run() {
                                                mc.options.attackKey.setPressed(false);
                                                TextBrush.this.runNext();
                                            }
                                        }.runTaskLater(theDelay);

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
        if(runnables.size() > 0) {
            runnables.get(0).cancel();
            runnables.clear();
        }
        mc.setScreen(new TextBrushGUI(mc.currentScreen));
    }

    private void runNext() {
        if(runnables.size() > 0) {
            runnables.remove(0);
            if(runnables.size() > 0) {
                runnables.get(0).run();
            }
        }
    }

    enum Characters {
        A((byte)0,new boolean[][] {
                {false,true,true,true,false},
                {false,false,false,false,true},
                {false,true,true,true,true},
                {true,false,false,false,true},
                {false,true,true,true,true}
        }),
        B((byte)1,new boolean[][] {
                {true,false,false,false,false},
                {true,false,false,false,false},
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,true},
                {true,true,true,true,false}
        }),
        C((byte)0,new boolean[][] {
                {false,true,true,true,false},
                {true,false,false,false,true},
                {true,false,false,false,false},
                {true,false,false,false,true},
                {false,true,true,true,false}
        }),

        D((byte)1,new boolean[][] {
                {false,false,false,false,true},
                {false,false,false,false,true},
                {false,true,true,false,true},
                {true,false,false,true,true},
                {true,false,false,false,true},
                {false,true,true,true,true}
        }),
        E((byte)0,new boolean[][] {
                {false,true,true,true,false},
                {true,false,false,false,true},
                {true,true,true,true,true},
                {true,false,false,false,false},
                {false,true,true,true,true}
        }),
        F((byte)1,new boolean[][] {
                {false,false,true,true},
                {false,true,false,false},
                {true,true,true,true},
                {false,true,false,false},
                {false,true,false,false},
                {false,true,false,false}
        }),
        G((byte)0,new boolean[][] {
                {false,true,true,true,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,true},
                {false,false,false,false,true},
                {true,true,true,true,false}
        }),
        H((byte)2,new boolean[][] {
                {true,false,false,false,false},
                {true,false,false,false,false},
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true}
        }),
        I((byte)1,new boolean[][] {
                {true},
                {false},
                {true},
                {true},
                {true},
                {true}
        }),
        J((byte)1,new boolean[][] {
                {false,false,false,true},
                {false,false,false,false},
                {false,false,false,true},
                {false,false,false,true},
                {true,false,false,true},
                {true,false,false,true},
                {false,true,true,false}
        }),
        K((byte)1,new boolean[][] {
                {true,false,false,false},
                {true,false,false,true},
                {true,false,true,false},
                {true,true,false,false},
                {true,false,true,false},
                {true,false,false,true}
        }),
        L((byte)1,new boolean[][] {
                {true,false},
                {true,false},
                {true,false},
                {true,false},
                {true,false},
                {false,true}
        }),
        M((byte)0,new boolean[][] {
                {true,true,false,true,false},
                {true,false,true,false,true},
                {true,false,true,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true}
        }),
        N((byte)0,new boolean[][] {
                {true,true,true,true,false},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true}
        }),
        O((byte)0,new boolean[][] {
                {false,true,true,true,false},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,false}
        }),
        P((byte)0,new boolean[][] {
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,true},
                {true,true,true,true,false},
                {true,false,false,false,false},
                {true,false,false,false,false}
        }),
        Q((byte)0,new boolean[][] {
                {false,true,true,false,true},
                {true,false,false,true,true},
                {true,false,false,false,true},
                {false,true,true,true,true},
                {false,false,false,false,true},
                {false,false,false,false,true}
        }),
        R((byte)0,new boolean[][] {
                {true,false,true,true,false},
                {true,true,false,false,true},
                {true,false,false,false,false},
                {true,false,false,false,false},
                {true,false,false,false,false}
        }),
        S((byte)0,new boolean[][] {
                {false,true,true,true,true},
                {true,false,false,false,false},
                {false,true,true,true,false},
                {false,false,false,false,true},
                {true,true,true,true,false}
        }),
        T((byte)2,new boolean[][] {
                {false,true,false},
                {false,true,false},
                {true,true,true},
                {false,true,false},
                {false,true,false},
                {false,true,false},
                {false,false,true}
        }),
        U((byte)0,new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,true}
        }),
        V((byte)0,new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,false,true,false},
                {false,false,true,false,false}
        }),
        W((byte)0,new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,true,false,true},
                {true,false,true,false,true},
                {false,true,true,true,true}
        }),
        X((byte)0,new boolean[][] {
                {true,false,false,false,true},
                {false,true,false,true,false},
                {false,false,true,false,false},
                {false,true,false,true,false},
                {true,false,false,false,true}
        }),
        Y((byte)0,new boolean[][] {
                {true,false,false,false,true},
                {true,false,false,false,true},
                {true,false,false,false,true},
                {false,true,true,true,true},
                {false,false,false,false,true},
                {true,true,true,true,false}
        }),
        Z((byte)0,new boolean[][] {
                {true,true,true,true,true},
                {false,false,false,true,false},
                {false,false,true,false,false},
                {false,true,false,false,false},
                {true,true,true,true,true}
        });




        private final boolean[][] active;
        private final byte offset;
        Characters(byte offset, boolean[][] active) {
            this.offset = offset;
            this.active = active;
        }
        public byte getOffset() {
            return offset;
        }
        public boolean[][] getActive() {
            return active;
        }
    }
}
