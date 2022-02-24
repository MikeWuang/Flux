package today.flux.event;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.EntityLivingBase;

/**
 * Created by John on 2017/06/24.
 */
public class LivingUpdateEvent implements Event {
    public LivingUpdateEvent(EntityLivingBase entity){
        this.entity = entity;
    }

    
    private EntityLivingBase entity;


	public EntityLivingBase getEntity() {
		// TODO 自动生成的方法存根
		return entity;
	}
}
