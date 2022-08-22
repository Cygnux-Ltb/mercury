package io.mercury.library.ignite.learning.config;

import org.apache.ignite.IgniteException;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;

/**
 * @program: ignite_learning
 * @create: 2018-06-06 09:42
 * <p>
 * 这是用于配置Ignite生命周期的配置，可以在Ignite节点启动前，启动后，关闭前，关闭后执行相关的动作
 **/
public class IgniteLifecycleBean implements LifecycleBean {

    @Override
    public void onLifecycleEvent(LifecycleEventType lifecycleEventType) throws IgniteException {
        if (lifecycleEventType == LifecycleEventType.BEFORE_NODE_START) {
            System.out.println("Ignite node begins to start!");
        }
        if (lifecycleEventType == LifecycleEventType.AFTER_NODE_START) {
            System.out.println("Ignite node started!");
        }
        if (lifecycleEventType == LifecycleEventType.BEFORE_NODE_STOP) {
            System.out.println("Ignite node begins to stop!");
        }
        if (lifecycleEventType == LifecycleEventType.AFTER_NODE_STOP) {
            System.out.println("Ignite node stop!");
        }
    }

}
