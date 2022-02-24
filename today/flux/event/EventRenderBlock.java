package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventRenderBlock extends EventCancellable implements Event
{
    
    private Block block;
    
    private BlockPos pos;

    public EventRenderBlock(final Block block, final BlockPos pos) {
        this.block = null;
        this.pos = null;
        this.block = block;
        this.pos = pos;
    }
}
