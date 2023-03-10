package com.mcal.worldcraft.chunk;

import androidx.annotation.NonNull;

import com.mcal.droid.rugl.Game;
import com.mcal.droid.rugl.geom.BedBlock;
import com.mcal.droid.rugl.geom.CompiledShape;
import com.mcal.droid.rugl.geom.DoorBlock;
import com.mcal.droid.rugl.geom.LadderBlock;
import com.mcal.droid.rugl.geom.ShapeBuilder;
import com.mcal.droid.rugl.geom.TexturedShape;
import com.mcal.droid.rugl.gl.GLVersion;
import com.mcal.droid.rugl.gl.VBOShape;
import com.mcal.droid.rugl.util.Colour;
import com.mcal.worldcraft.factories.BlockFactory;

import java.util.LinkedList;

public class GeometryGenerator {
    private static final Object synVBOBuilderObject;
    private static final ShapeBuilder queuedOpaqueVBOBuilder = new ShapeBuilder();
    private static final ShapeBuilder queuedTransVBOBuilder = new ShapeBuilder();
    private static final ShapeBuilder immediateOpaqueVBOBuilder = new ShapeBuilder();
    private static final ShapeBuilder immediateTransVBOBuilder = new ShapeBuilder();
    private static final GeomService geomService = new GeomService();
    private static int queueSize = 0;

    static {
        geomService.start();
        synVBOBuilderObject = new Object();
    }

    /**
     * @return The number of chunklets awaiting geometry generation
     */
    public static int getChunkletQueueSize() {
        return queueSize;
    }

    /**
     * Generates geometry for a {@link Chunklet}
     *
     * @param c
     * @param synchronous
     */
    public static void generate(final Chunklet c, final boolean synchronous) {
        Runnable r = () -> {
            synchronized (GeometryGenerator.synVBOBuilderObject) {
                ShapeBuilder opaqueVBOBuilder = synchronous ? GeometryGenerator.immediateOpaqueVBOBuilder : GeometryGenerator.queuedOpaqueVBOBuilder;
                ShapeBuilder transVBOBuilder = synchronous ? GeometryGenerator.immediateTransVBOBuilder : GeometryGenerator.queuedTransVBOBuilder;

                // not sure why this is needed, but it is
                transVBOBuilder.clear();
                opaqueVBOBuilder.clear();

                for (int xi = 0; xi < 16; xi++) {
                    for (int yi = 0; yi < 16; yi++) {
                        for (int zi = 0; zi < 16; zi++) {
                            BlockFactory.Block b = BlockFactory.getBlock(c.blockType(xi, yi, zi));
                            float light = c.light(xi, yi, zi);
                            if (b == BlockFactory.Block.Slab) {
                                // the half-blocks
                                light = c.light(xi, yi + 1, zi);
                            }
                            int colour = Colour.packFloat(light, light, light, 1.0f);
                            if (DoorBlock.isDoor(b)) {
                                DoorBlock.generateGeometry(c, opaqueVBOBuilder, xi, yi, zi, b.id, colour, c.blockData(xi, yi, zi));
                            } else if (BedBlock.isBed(b)) {
                                BedBlock.generateGeometry(c, opaqueVBOBuilder, xi, yi, zi, c.blockData(xi, yi, zi), colour);
                            } else if (LadderBlock.isLadder(b)) {
                                LadderBlock.generateGeometry(c, opaqueVBOBuilder, transVBOBuilder, xi, yi, zi, b.id, colour, c.blockData(xi, yi, zi));
                            } else if (b != null && !b.isCuboid) {
                                GeometryGenerator.addFace(c, b, xi, yi, zi, BlockFactory.Face.South, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi, zi, BlockFactory.Face.North, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi, zi, BlockFactory.Face.West, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi, zi, BlockFactory.Face.East, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi, zi, BlockFactory.Face.Bottom, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi, zi, BlockFactory.Face.Top, colour, opaqueVBOBuilder, transVBOBuilder);
                            }
                            if (b == null || !b.opaque || BedBlock.isBed(b) || DoorBlock.isDoor(b) || LadderBlock.isLadder(b)) {
                                GeometryGenerator.addFace(c, b, xi, yi, zi + 1, BlockFactory.Face.South, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi, zi - 1, BlockFactory.Face.North, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi - 1, yi, zi, BlockFactory.Face.West, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi + 1, yi, zi, BlockFactory.Face.East, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi + 1, zi, BlockFactory.Face.Bottom, colour, opaqueVBOBuilder, transVBOBuilder);
                                GeometryGenerator.addFace(c, b, xi, yi - 1, zi, BlockFactory.Face.Top, colour, opaqueVBOBuilder, transVBOBuilder);
                            }
                        }
                    }
                }
                TexturedShape s = opaqueVBOBuilder.compile();
                if (s != null) {
                    s.state = BlockFactory.state;
                    s.translate(c.x, c.y, c.z);
                }
                TexturedShape t = transVBOBuilder.compile();
                if (t != null) {
                    t.state = BlockFactory.state;
                    t.translate(c.x, c.y, c.z);
                }
                if (Game.glVersion == GLVersion.OnePointOne) {
                    VBOShape solid = null;
                    if (s != null) {
                        solid = new VBOShape(s);
                    }
                    VBOShape transparent = null;
                    if (t != null) {
                        transparent = new VBOShape(t);
                    }
                    c.geometryComplete(solid, transparent);
                } else {
                    CompiledShape solid2 = null;
                    if (s != null) {
                        solid2 = new CompiledShape(s);
                    }
                    CompiledShape transparent2 = null;
                    if (t != null) {
                        transparent2 = new CompiledShape(t);
                    }
                    c.geometryComplete(solid2, transparent2);
                }
                queueSize--;
            }
        };
        queueSize++;
        if (synchronous) {
            r.run();
        } else {
            geomService.add(r);
        }
    }

    public static void addFace(@NonNull Chunklet c, BlockFactory.Block facing, int x, int y, int z, BlockFactory.Face f, int colour, ShapeBuilder opaque, ShapeBuilder transparent) {
        byte bt = c.blockType(x, y, z);
        BlockFactory.Block b = BlockFactory.getBlock(bt);
        if (b != null) {
            if (b != facing || !b.isCuboid) {
                b.face(f, x, y, z, colour, b.opaque ? opaque : transparent);
            }
        }
    }

    public static void addTask(Runnable r) {
        geomService.add(r);
    }

    public static class GeomService implements Runnable {
        private static final long SLEEP_TIME = 10;
        private final LinkedList<Runnable> queue = new LinkedList<>();
        private boolean active;

        public void add(Runnable runnable) {
            synchronized (this.queue) {
                this.queue.offer(runnable);
            }
        }

        public Runnable poll() {
            Runnable runnable;
            synchronized (this.queue) {
                runnable = this.queue.poll();
            }
            return runnable;
        }

        @Override
        public void run() {
            while (this.active) {
                try {
                    Runnable runnable = poll();
                    if (runnable != null) {
                        runnable.run();
                    }
                    Thread.sleep(SLEEP_TIME);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    System.runFinalization();
                    Runtime.getRuntime().gc();
                    System.gc();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        public void start() {
            this.active = true;
            Thread t = new Thread(this);
            t.setPriority(5);
            t.start();
        }

        public void stop() {
            this.active = false;
        }
    }
}
