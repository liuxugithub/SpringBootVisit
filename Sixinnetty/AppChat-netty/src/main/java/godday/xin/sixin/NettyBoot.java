
package godday.xin.sixin;

import com.sun.xml.internal.ws.api.WSService;
import godday.xin.sixin.utils.WssServer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBoot implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent()==null){
            try {
                WssServer.getInstance().start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

