package me.azazeldev.coinui.utility;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DelayedTask {
    private int counter;
    private final Runnable runnable;

    public DelayedTask(Runnable run, int ticks){
        counter = ticks;
        this.runnable = run;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.START) return;

        if(counter <= 0){
            MinecraftForge.EVENT_BUS.unregister(this);
            runnable.run();
        }

        counter--;
    }
}

